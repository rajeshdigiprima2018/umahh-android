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

import Adapter.Organization_Adapter;
import Modal.Organization_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 21-04-2019.
 */

public class Organization_Activity extends AppCompatActivity implements View.OnClickListener {


    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView IV_back;


    RecyclerView recyclerView;
    Organization_Adapter board_adapter;
    public ArrayList<Organization_Modal> modalList;
    String Business_category_id, Business_Name;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        API();
        Recycle_view();
        Clicklistener();

    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Organization_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Business_category_id = modalList.get(position).getCategory_id();
                        Business_Name = modalList.get(position).getName();
                        // DriverDetailDialog(driver_id);
                        Intent intent = new Intent(Organization_Activity.this, Organization_Detatil_Activity.class);
                        intent.putExtra("KEY_Business_category_id", Business_category_id);
                        intent.putExtra("KEY_Business_name", Business_Name);
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

    private void Clicklistener() {
        IV_back.setOnClickListener(this);
    }

    private void API() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"business/getAllCategory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Board list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Board List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Organization_Modal List_modal = new Organization_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String name = jsonChildNode.optString("name").toString();
                                            String iconUrl = jsonChildNode.optString("iconUrl").toString();
                                            String createdAt = jsonChildNode.optString("createdAt").toString();
                                            String deletedAt = jsonChildNode.optString("deletedAt").toString();
                                            String updatedAt = jsonChildNode.optString("updatedAt").toString();
                                            String category_id = jsonChildNode.optString("category_id").toString();


                                            List_modal.setName(name);
                                            List_modal.setIconUrl(iconUrl);
                                            List_modal.setCreatedAt(createdAt);
                                            List_modal.setDeletedAt(deletedAt);
                                            List_modal.setUpdatedAt(updatedAt);
                                            List_modal.setCategory_id(category_id);
                                            modalList.add(List_modal);

                                        }

                                        if (modalList.size() > 0) {
                                            board_adapter = new Organization_Adapter(Organization_Activity.this, modalList);
                                            recyclerView.setAdapter(board_adapter);
                                            board_adapter.notifyDataSetChanged();
                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(Organization_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Organization_Activity.this);
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

    private void Find_element() {

        IV_back = (ImageView) findViewById(R.id.Iv_back);
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
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Organization_Activity.this, MainActivity.class);
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
