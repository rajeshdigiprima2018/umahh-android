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
import android.text.Html;
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

import Adapter.Association_Adapter;
import Modal.Association_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 15-04-2019.
 */

public class Association_Activity  extends AppCompatActivity implements View.OnClickListener {

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;

    RecyclerView recyclerView;
    Association_Adapter association_adapter;
    public ArrayList<Association_Modal> modalList;
  //  Association_Modal cabList_modal = new Association_Modal();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.association_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        Click_Event();
        Association_Detail();
        Recycle_view();


    }

    private void Association_Detail() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"mosque/Associates/getAll/" +getIntent().getStringExtra("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Association list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Association List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        try {


                                        JSONObject jsonObject = json.optJSONObject("data");
                                        String mosque_id = jsonObject.optString("mosque_id").toString();
                                        JSONArray jsonMainNode = jsonObject.optJSONArray("associate_user");
                                        int lengthJsonArr = jsonMainNode.length();
                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Association_Modal associationModal = new Association_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String _id = jsonChildNode.optString("_id").toString();
                                            String role = jsonChildNode.optString("role").toString();
                                            String email = jsonChildNode.optString("email").toString();
                                            String username = jsonChildNode.optString("username").toString();
                                            String first_name = jsonChildNode.optString("first_name").toString();
                                            String last_name = jsonChildNode.optString("last_name").toString();
                                            String zipCode = jsonChildNode.optString("zipCode").toString();
                                            String mosque_id_2 = jsonChildNode.optString("mosque_id").toString();
                                            String street_address = jsonChildNode.optString("street_address").toString();
                                            String avtar = jsonChildNode.optString("avtar").toString();
                                            String nameContactPerson = jsonChildNode.optString("nameContactPerson").toString();
                                            String description_service = jsonChildNode.optString("description_service").toString();


                                          //  String textarea = jsonMainNode.optString("textarea").toString();
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                String textarea_html = Html.fromHtml(description_service, Html.FROM_HTML_MODE_LEGACY).toString();
                                                System.out.println("value--1-------------------->>>  " + textarea_html);
                                                associationModal.setDescription_service(textarea_html);


                                            } else {
                                                String textarea_html = Html.fromHtml(description_service).toString();
                                                System.out.println("value---2------------------->>>  " + textarea_html);
                                                associationModal.setDescription_service(textarea_html);
                                            }
                                            associationModal.setUsername(username);
                                            associationModal.setStreet_address(street_address);
                                            associationModal.setAvtar(avtar);
                                            modalList.add(associationModal);
                                        }
                                        }catch (Exception e){

                                        }


                                    } else {
                                        Toast.makeText(Association_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }

                                    if (modalList.size() > 0) {
                                        association_adapter = new Association_Adapter(Association_Activity.this, modalList);
                                        recyclerView.setAdapter(association_adapter);
                                        association_adapter.notifyDataSetChanged();
                                    }
                                    hideProgressDialog();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Association_Activity.this);
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
    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Association_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

//                        driver_id = Integer.parseInt((modalList.get(position).getCabId()));
//                        DriverDetailDialog(driver_id);
                      /*  Intent intent = new Intent(All_Mosque_Activity.this, MainActivity.class);
                        startActivity(intent);*/

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
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


    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(this);
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
    }

    private void Click_Event() {

        Iv_back.setOnClickListener(this);

    }

    private void Find_Element() {

        Iv_back = (ImageView)findViewById(R.id.Iv_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);



    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Association_Activity.this, Mousque_detail_activity.class);
        intent.putExtra("id", getIntent().getStringExtra("id"));
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

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


