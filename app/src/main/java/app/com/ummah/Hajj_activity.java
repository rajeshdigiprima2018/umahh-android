package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.Hajj_and_Umrah_Sub_Adapter;
import Modal.Hajj_and_Umrah_sub_Detail_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 25-03-2019.
 */

public class Hajj_activity extends AppCompatActivity implements View.OnClickListener {

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    LinearLayout Layout_Arequest_of;
    ImageView Iv_back;

    RecyclerView recyclerView;
    Hajj_and_Umrah_Sub_Adapter board_adapter;
    public ArrayList<Hajj_and_Umrah_sub_Detail_Modal> modalList;
    String HajjumrahCategory_id,ImageUrl,Name;
    Bundle bundle;
    ImageView Iv_Title_image;
    TextView Tv_title;
    String _id;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hajj_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        setData();
        API();
        Recycle_view();
        Click_Event();
    }

    private void setData() {


        Glide.with(this)
                .load(ImageUrl)
                .centerCrop()
                .placeholder(R.drawable.hajj)
                .into(Iv_Title_image);
        Tv_title.setText(Name);


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

    private void API() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url+"hajjumrah/getByhajUmCategory",
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
                                            Hajj_and_Umrah_sub_Detail_Modal List_modal = new Hajj_and_Umrah_sub_Detail_Modal();
                                            JSONObject jsonChildNode2 = jsonMainNode.getJSONObject(i);
                                            String hajjumrahCategory_id = jsonChildNode2.optString("hajjumrahCategory_id").toString();
                                            String title = jsonChildNode2.optString("title").toString();
                                            String description = jsonChildNode2.optString("description").toString();
                                            String title_aro = jsonChildNode2.optString("title_aro").toString();
                                            String description_aro = jsonChildNode2.optString("description_aro").toString();
                                            String createdAt = jsonChildNode2.optString("createdAt").toString();
                                            String deletedAt = jsonChildNode2.optString("deletedAt").toString();
                                            String updatedAt = jsonChildNode2.optString("updatedAt").toString();
                                            String hajjumrah_id = jsonChildNode2.optString("hajjumrah_id").toString();
                                            String id = jsonChildNode2.optString("id").toString();

                                            int ser_no = i+1;
                                            List_modal.setSer_no(String.valueOf(ser_no));

                                            List_modal.setHajjumrahCategory_id(hajjumrahCategory_id);
                                            List_modal.setTitle(title);
                                            List_modal.setCreatedAt(createdAt);
                                            List_modal.setDeletedAt(deletedAt);
                                            List_modal.setUpdatedAt(updatedAt);
                                            List_modal.setDescription(description);
                                            List_modal.setTitle_aro(title_aro);
                                            List_modal.setDescription_aro(description_aro);
                                            List_modal.setHajjumrah_id(hajjumrah_id);
                                            List_modal.setId(id);
                                            modalList.add(List_modal);

                                        }

                                        if (modalList.size() > 0) {
                                            board_adapter = new Hajj_and_Umrah_Sub_Adapter(Hajj_activity.this, modalList);
                                            recyclerView.setAdapter(board_adapter);
                                            board_adapter.notifyDataSetChanged();
                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(Hajj_activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
                map.put("hajjumrahCategory_id", HajjumrahCategory_id);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Hajj_activity.this);
        requestQueue.add(stringRequest);
    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Hajj_activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        _id = modalList.get(position).getId();
                        Intent intent = new Intent(Hajj_activity.this, Hajj_Detail_Activity.class);
                        intent.putExtra("_id", _id);
                        startActivity(intent);
                        //finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    private void Find_Element() {
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Tv_title = (TextView) findViewById(R.id.Tv_title);
        Iv_Title_image = (ImageView) findViewById(R.id.Iv_Title_image);
      //  Layout_Arequest_of = (LinearLayout) findViewById(R.id.Layout_Arequest_of);
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
        bundle = getIntent().getExtras();

        HajjumrahCategory_id = bundle.getString("HajjumrahCategory_id");
        ImageUrl = bundle.getString("ImageUrl");
        Name  = bundle.getString("Name");

        modalList = new ArrayList<>();
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
    }

    private void Click_Event() {

//        Layout_Arequest_of.setOnClickListener(this);
        Iv_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            /*case R.id.Layout_Arequest_of:
                Intent intent = new Intent(Hajj_activity.this, A_request_of_pilgrims.class);
                startActivity(intent);
                break;*/
            case R.id.Iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Hajj_activity.this, Hajj_and_Umrah_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
