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

import Adapter.Business_Detail_Adapter;
import Modal.Business_Detail_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 19-04-2019.
 */

public class Business_Detatil_Activity extends AppCompatActivity implements View.OnClickListener {


    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;
    TextView Tv_Business_name;


    RecyclerView recyclerView;
    Business_Detail_Adapter board_adapter;
    public ArrayList<Business_Detail_Modal> modalList;
    Bundle bundle;
    String KEY_Business_category_id, KEY_Business_name;
    String KEY_id;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_detail_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        SetData();
        API();
        Recycle_view();
        Clicklistener();


    }

    private void SetData() {
        Tv_Business_name.setText(KEY_Business_name);
    }


    private void Clicklistener() {
        Iv_back.setOnClickListener(this);
    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Business_Detatil_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        KEY_id = modalList.get(position).get_id();
                        Intent intent = new Intent(Business_Detatil_Activity.this, Business_inner_Detail_Activity.class);
                        intent.putExtra("KEY_id", KEY_id);
                        intent.putExtra("KEY_Business_category_id", KEY_Business_category_id);
                        intent.putExtra("KEY_Business_name", KEY_Business_name);
                        startActivity(intent);
                       // finish();
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
        KEY_Business_category_id = bundle.getString("KEY_Business_category_id");
        KEY_Business_name = bundle.getString("KEY_Business_name");

        Constant.KEY_Business_category_id =KEY_Business_category_id;
        Constant.KEY_Business_name =KEY_Business_name;
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
    }

    private void API() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url+"business/getByCategoryBusinessList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("CategoryBusinessList list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("CategoryBusinessList List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Business_Detail_Modal List_modal = new Business_Detail_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String _id = jsonChildNode.optString("_id").toString();
                                            String username = jsonChildNode.optString("username").toString();
                                            String role = jsonChildNode.optString("role").toString();
                                            String email = jsonChildNode.optString("email").toString();
                                            String zipCode = jsonChildNode.optString("zipCode").toString();

                                            String city_name = jsonChildNode.optString("name").toString();
                                            String cities_id = jsonChildNode.optString("cities_id").toString();
                                            String avtar = jsonChildNode.optString("avtar").toString();
                                            String street_address = jsonChildNode.optString("street_address").toString();

                                            JSONObject jsonMainNode_city = jsonChildNode.optJSONObject("city");
                                            String City_id = jsonMainNode_city.optString("id").toString();
                                            String City_name = jsonMainNode_city.optString("name").toString();
                                            String City_state_id = jsonMainNode_city.optString("state_id").toString();
                                            String City_cities_id = jsonMainNode_city.optString("cities_id").toString();

                                            JSONObject jsonMainNode_state = jsonChildNode.optJSONObject("state");
                                            String state_id = jsonMainNode_state.optString("id").toString();
                                            String state_name = jsonMainNode_state.optString("name").toString();
                                            String state_country_id = jsonMainNode_state.optString("country_id").toString();
                                            String state_state_id = jsonMainNode_state.optString("state_id").toString();

                                            JSONObject jsonMainNode_country = jsonChildNode.optJSONObject("country");
                                            String country_id = jsonMainNode_country.optString("id").toString();
                                            String country_sortname = jsonMainNode_country.optString("sortname").toString();
                                            String country_name = jsonMainNode_country.optString("name").toString();
                                            String country_phoneCode = jsonMainNode_country.optString("phoneCode").toString();
                                            String country_country_id = jsonMainNode_country.optString("country_id").toString();

                                            List_modal.set_id(_id);
                                            List_modal.setCity_name(City_name);
                                            List_modal.setState_name(state_name);
                                            List_modal.setCountry_name(country_name);
                                            List_modal.setUsername(username);
                                            List_modal.setIconUrl(avtar);
                                            List_modal.setStreet_address(street_address);
                                            List_modal.setZipCode(zipCode);
                                            modalList.add(List_modal);

                                        }

                                        if (modalList.size() > 0) {
                                            board_adapter = new Business_Detail_Adapter(Business_Detatil_Activity.this, modalList);
                                            recyclerView.setAdapter(board_adapter);
                                            board_adapter.notifyDataSetChanged();
                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(Business_Detatil_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
                map.put("category_id", KEY_Business_category_id);
                map.put("businessType", "business");
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Business_Detatil_Activity.this);
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

        Intent intent = new Intent(Business_Detatil_Activity.this, Business_Activity.class);
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
