package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import Adapter.All_near_Mosque_Adapter;
import Modal.All_near_Mosque_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 11-04-2019.
 */

public class All_Mosque_Activity extends AppCompatActivity implements View.OnClickListener {


    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;

    RecyclerView recyclerView;
    All_near_Mosque_Adapter all_near_mosque_adapter;
    public ArrayList<All_near_Mosque_Modal> modalList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_mosque_activity);
        modalList = new ArrayList<>();
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        Mosque_City();
        Recycle_view();
        Click_Event();
    }

    @Override
    public void onBackPressed() {

       /* Intent intent = new Intent(All_Mosque_Activity.this, MainActivity.class);
        startActivity(intent);*/
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

    private void Mosque_City() {

        showProgressDialog();
        String url = Constant.Base_url + "mosque/nearby/" + Constant.latitude + "/" + Constant.longitude + "/10000";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Mosque list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Mosque List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        // modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            All_near_Mosque_Modal modal = new All_near_Mosque_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String _id = jsonChildNode.optString("_id").toString();
                                            String username = jsonChildNode.optString("username").toString();
                                            String role = jsonChildNode.optString("role").toString();
                                            String email = jsonChildNode.optString("email").toString();
                                            String randomToken = jsonChildNode.optString("randomToken").toString();
                                            String phoneVerification = jsonChildNode.optString("phoneVerification").toString();
                                            String vefication = jsonChildNode.optString("vefication").toString();
                                            String zipCode = jsonChildNode.optString("zipCode").toString();
                                            String street_address = jsonChildNode.optString("street_address").toString();
                                            Double lng = Double.valueOf(jsonChildNode.optString("lng").toString());
                                            Double lat = Double.valueOf(jsonChildNode.optString("lat").toString());
                                            String deviceToken = jsonChildNode.optString("deviceToken").toString();
                                            String avtar = jsonChildNode.optString("avtar").toString();
                                            String businessType = jsonChildNode.optString("businessType").toString();
                                            String isSelect = jsonChildNode.optString("isSelect").toString();
                                            String isFBUser = jsonChildNode.optString("isFBUser").toString();
                                            String nameContactPerson = jsonChildNode.optString("nameContactPerson").toString();
                                            String access_token = jsonChildNode.optString("access_token").toString();
                                            String isLoggedIn = jsonChildNode.optString("isLoggedIn").toString();
                                            String mobile = jsonChildNode.optString("mobile").toString();
                                            String password = jsonChildNode.optString("password").toString();
                                            String deletedAt = jsonChildNode.optString("deletedAt").toString();
                                            String distance = jsonChildNode.optString("distance").toString();
                                            modal.set_id(_id);
                                            modal.setUsername(username);
                                            modal.setAvtar(avtar);
                                            modal.setEmail(email);
                                            modal.setMobile(mobile);
                                            modal.setStreet_address(street_address);
                                            modal.setZipCode(zipCode);
                                            modal.setDistance(distance);

                                            modalList.add(modal);
                                        }


                                    } else {
                                        Toast.makeText(All_Mosque_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }

                                    if (modalList.size() > 0) {
                                        all_near_mosque_adapter = new All_near_Mosque_Adapter(All_Mosque_Activity.this, modalList);
                                        recyclerView.setAdapter(all_near_mosque_adapter);
                                        all_near_mosque_adapter.notifyDataSetChanged();
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hideProgressDialog();

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
        RequestQueue requestQueue = Volley.newRequestQueue(All_Mosque_Activity.this);
        requestQueue.add(stringRequest);
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
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
    }

    private void Click_Event() {

        Iv_back.setOnClickListener(this);
    }

    private void Find_Element() {
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new All_Mosque_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(All_Mosque_Activity.this, Mousque_detail_activity.class);
                        intent.putExtra("id", modalList.get(position).get_id());
                        intent.putExtra("just_finish", true);
                        startActivity(intent);


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
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