package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.All_Supplication_Adapter;
import Modal.All_Supplication_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 22-04-2019.
 */

public class All_Supplication_Activity extends AppCompatActivity implements View.OnClickListener {


    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;
    TextView Tv_Business_name;


    RecyclerView recyclerView;
    All_Supplication_Adapter board_adapter;
    public ArrayList<All_Supplication_Modal> modalList;
    Bundle bundle;
    String KEY_Supplication_category_id, KEY_Supplication_name;
    String id;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_supplication_detail_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        SetData();
        if (KEY_Supplication_name.equals("All")) {
            API_ALl();
        } else if(!KEY_Supplication_name.equals("All")) {
            API();
        }
        Recycle_view();
        Clicklistener();


    }

    private void API() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url+"supplication/getBySupCategory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Supplication_id list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Supplication_id List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            All_Supplication_Modal List_modal = new All_Supplication_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            JSONObject jsonChildNode2 = jsonMainNode.getJSONObject(i);
                                            String supCategory_id = jsonChildNode2.optString("supCategory_id").toString();
                                            String description = jsonChildNode2.optString("description").toString();
                                            String title_aro = jsonChildNode2.optString("title_aro").toString();
                                            String description_aro = jsonChildNode2.optString("description_aro").toString();
                                            String createdAt = jsonChildNode2.optString("createdAt").toString();
                                            String title = jsonChildNode2.optString("title").toString();
                                            String deletedAt = jsonChildNode2.optString("deletedAt").toString();
                                            String updatedAt = jsonChildNode2.optString("updatedAt").toString();
                                            String supplication_id = jsonChildNode2.optString("supplication_id").toString();
                                            String id = jsonChildNode2.optString("id").toString();

                                            int ser_no = i + 1;
                                            List_modal.setSer_no(String.valueOf(ser_no));
                                            List_modal.setTitle(title);
                                            List_modal.setId(id);
            /*                                List_modal.setIconUrl(iconUrl);
                                            List_modal.setCreatedAt(createdAt);
                                            List_modal.setDeletedAt(deletedAt);
                                            List_modal.setUpdatedAt(updatedAt);
                                            List_modal.setCategory_id(category_id);*/
                                            modalList.add(List_modal);

                                        }

                                        if (modalList.size() > 0) {
                                            board_adapter = new All_Supplication_Adapter(All_Supplication_Activity.this, modalList);
                                            recyclerView.setAdapter(board_adapter);
                                            board_adapter.notifyDataSetChanged();
                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(All_Supplication_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("supCategory_id", KEY_Supplication_category_id);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(All_Supplication_Activity.this);
        requestQueue.add(stringRequest);
    }


    private void SetData() {
        Tv_Business_name.setText(KEY_Supplication_name);
    }


    private void Clicklistener() {
        Iv_back.setOnClickListener(this);
    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new All_Supplication_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        id = modalList.get(position).getId();
                        Intent intent = new Intent(All_Supplication_Activity.this, Supplication_sub_id_Detail_Activity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    private void Find_element() {
        Tv_Business_name = (TextView) findViewById(R.id.Tv_Business_name);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(this);
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
    }

    private void All_Depndency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        modalList = new ArrayList<>();
        bundle = getIntent().getExtras();
        KEY_Supplication_category_id = bundle.getString("KEY_Supplication_category_id");
        KEY_Supplication_name = bundle.getString("KEY_Supplication_name");
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
    }

    private void API_ALl() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"supplication/getAll",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Supplication list for all", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Supplication list for all", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            All_Supplication_Modal List_modal = new All_Supplication_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String supCategory_id = jsonChildNode.optString("supCategory_id").toString();
                                            String description = jsonChildNode.optString("description").toString();
                                            String title_aro = jsonChildNode.optString("title_aro").toString();
                                            String description_aro = jsonChildNode.optString("description_aro").toString();
                                            String createdAt = jsonChildNode.optString("createdAt").toString();
                                            String title = jsonChildNode.optString("title").toString();
                                            String deletedAt = jsonChildNode.optString("deletedAt").toString();
                                            String updatedAt = jsonChildNode.optString("updatedAt").toString();
                                            String supplication_id = jsonChildNode.optString("supplication_id").toString();
                                            String id = jsonChildNode.optString("id").toString();


                                            int ser_no = i + 1;
                                            List_modal.setSer_no(String.valueOf(ser_no));
                                            List_modal.setTitle(title);
                                            List_modal.setId(id);
            /*                                List_modal.setIconUrl(iconUrl);
                                            List_modal.setCreatedAt(createdAt);
                                            List_modal.setDeletedAt(deletedAt);
                                            List_modal.setUpdatedAt(updatedAt);
                                            List_modal.setCategory_id(category_id);*/
                                            modalList.add(List_modal);

                                        }

                                        if (modalList.size() > 0) {
                                            board_adapter = new All_Supplication_Adapter(All_Supplication_Activity.this, modalList);
                                            recyclerView.setAdapter(board_adapter);
                                            board_adapter.notifyDataSetChanged();
                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(All_Supplication_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
             /*   map.put("category_id", KEY_Business_category_id);
                map.put("businessType", "business");*/
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(All_Supplication_Activity.this);
        requestQueue.add(stringRequest);
    }

    public void showProgressDialog() {

        if (!progress_dialog.isShowing()) {
            progress_dialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progress_dialog.isShowing()) {
            progress_dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(All_Supplication_Activity.this, Suppication_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Iv_back:
                onBackPressed();
                break;
        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
