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

import Adapter.My_Donation_Adapter;
import Modal.Bookmark_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 25-03-2019.
 */

public class My_Donation_Activity  extends AppCompatActivity{

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;

    RecyclerView recyclerView;
    My_Donation_Adapter my_donation_adapter;
    public ArrayList<JSONObject> modalList;
    Bookmark_Modal cabList_modal = new Bookmark_Modal();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_donation_activity);
        modalList = new ArrayList<>();
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        API();
        Recycle_view();
        Click_Event();

        findViewById(R.id.Iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
       finish();
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

        my_donation_adapter = new My_Donation_Adapter(this, modalList);
        recyclerView.setAdapter(my_donation_adapter);
        my_donation_adapter.notifyDataSetChanged();

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+
                "payment/getPaymentByUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Donation category list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Donation category List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        JSONArray data = json.optJSONArray("data");

                                        for(int i=0;i<data.length();i++)
                                        {
                                           modalList.add(data.getJSONObject(i));
                                        }

                                        my_donation_adapter.notifyDataSetChanged();
/*

 {"code":200,"success":true,"data":[{"_id":"5f194c037613d73f6ba38389","isDeleted":null,"status":0,
 "amount":445,"updatedAt":"2020-07-19T19:33:27.888Z","deletedAt":null,"createdAt":"2020-07-19T19:33:27.888Z",
 "txn_id":"PAY-3MW769249D518453HL4MUX3Q","user_id":{"_id":"5ccdef3b44eb836c647d32bd","email":"test@gmail.com",
 "username":"Test"},"mosque_id":"5cc2dc2dbc568504a7926ab9","__v":0},{"_id":"5eaa8b5fb231430c14fcb506",
 "isDeleted":null,"status":0,"amount":242,"updatedAt":"2020-03-25T11:03:00.733Z","deletedAt":null,"
 createdAt":"2020-03-25T11:03:00.733Z","txn_id":"PAY-1GF84786GW728012LL2VIWSA","user_id":
 {"_id":"5ccdef3b44eb836c647d32bd","email":"test@gmail.com","username":"Test"}
 ,"mosque_id":"5cc2dc2dbc568504a7926ab9","__v":0}],"message":"List Donation successfully."}
 */
                                    } else {
                                        Toast.makeText(My_Donation_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }
                                hideProgressDialog();

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + session.getToken_id().get(Constant.SHARED_PREFERENCE_TOKEN_KEY));
                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("count","20");
                map.put("page","1");
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(My_Donation_Activity.this);
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
    }

    private void Find_Element() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new My_Donation_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

//                        driver_id = Integer.parseInt((modalList.get(position).getCabId()));
//                        DriverDetailDialog(driver_id);
                        Intent intent = new Intent(My_Donation_Activity.this, MainActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
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
