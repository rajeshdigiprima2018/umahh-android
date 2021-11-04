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

import Adapter.Following_Mosque_Adapter;
import Modal.Following_Mosque_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 25-03-2019.
 */

public class Favorite_Mosque_Activity extends AppCompatActivity implements View.OnClickListener {


    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;

    RecyclerView recyclerView;
    Following_Mosque_Adapter followingMosqueAdapter;
    public ArrayList<Following_Mosque_Modal> modalList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_mosque_activity);
        modalList = new ArrayList<>();
        All_Depndency();
        ProgressBar_Function();
        Find_Element();

        Recycle_view();
      //  API();

        Following_Mosque();
        Click_Event();
    }

    private void Following_Mosque() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url+"user/getFollowingList/{user_id}",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Following list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Following List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();
                                        int j;
                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Following_Mosque_Modal List_modal = new Following_Mosque_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                                            JSONObject jsonMainNode_enner = jsonChildNode.optJSONObject("mosque_id");
                                            String avtar = jsonMainNode_enner.optString("avtar");

                                            String _id = jsonMainNode_enner.optString("_id").toString();
                                            String username = jsonMainNode_enner.optString("username").toString();
                                            String following_id = jsonMainNode_enner.optString("following_id").toString();

                                            JSONObject jsonMainNode_city = jsonMainNode_enner.optJSONObject("city");
                                            String City_id = jsonMainNode_city.optString("id").toString();
                                            String City_name = jsonMainNode_city.optString("name").toString();
                                            String City_state_id = jsonMainNode_city.optString("state_id").toString();
                                            String City_cities_id = jsonMainNode_city.optString("cities_id").toString();

                                            JSONObject jsonMainNode_state = jsonMainNode_enner.optJSONObject("state");
                                            String state_id = jsonMainNode_state.optString("id").toString();
                                            String state_name = jsonMainNode_state.optString("name").toString();
                                            String state_country_id = jsonMainNode_state.optString("country_id").toString();
                                            String state_state_id = jsonMainNode_state.optString("state_id").toString();

                                            JSONObject jsonMainNode_country = jsonMainNode_enner.optJSONObject("country");
                                            String country_id = jsonMainNode_country.optString("id").toString();
                                            String country_sortname = jsonMainNode_country.optString("sortname").toString();
                                            String country_name = jsonMainNode_country.optString("name").toString();
                                            String country_phoneCode = jsonMainNode_country.optString("phoneCode").toString();
                                            String country_country_id = jsonMainNode_country.optString("country_id").toString();



                                            List_modal.set_id(_id);
                                            List_modal.setUsername(username);
                                            List_modal.setFollowing_id(following_id);

                                            // For city details...
                                            List_modal.setCity_id(City_id);
                                            List_modal.setCity_name(City_name);
                                            List_modal.setCity_state_id(City_state_id);
                                            List_modal.setCity_cities_id(City_cities_id);

                                            // For state detail..........
                                            List_modal.setState_id(state_id);
                                            List_modal.setState_name(state_name);
                                            List_modal.setState_country_id(state_country_id);
                                            List_modal.setState_state_id(state_state_id);

                                            //For country Detail....
                                            List_modal.setCountry_id(country_id);
                                            List_modal.setCountry_sortname(country_sortname);
                                            List_modal.setCountry_name(country_name);
                                            List_modal.setCountry_phoneCode(country_phoneCode);
                                            List_modal.setCountry_country_id(country_country_id);
                                            List_modal.setAvtar(avtar);
                                            modalList.add(List_modal);
                                            hideProgressDialog();

                                        }

                                    } else {
                                        Toast.makeText(Favorite_Mosque_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                                if (modalList.size() > 0) {
                                    followingMosqueAdapter = new Following_Mosque_Adapter(Favorite_Mosque_Activity.this, modalList);
                                    recyclerView.setAdapter(followingMosqueAdapter);
                                    followingMosqueAdapter.notifyDataSetChanged();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("user_id", session.getuserinfo().get(Constant.SHARED_PREFERENCE_USER_ID_KEY));
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Favorite_Mosque_Activity.this);
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

        Intent intent = new Intent(Favorite_Mosque_Activity.this, Profile_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
/*    private void API() {
        cabList_modal.setGuest_name("Mosque");
        cabList_modal.setName("372/2 mosque RR colony");
        cabList_modal.setCabId("1");
        modalList.add(cabList_modal);
        cabList_modal.setGuest_name("Mosque");
        cabList_modal.setName("372/2 mosque RR colony");
        cabList_modal.setCabId("2");
        modalList.add(cabList_modal);
        cabList_modal.setGuest_name("Mosque");
        cabList_modal.setName("372/2 mosque RR colony");
        cabList_modal.setCabId("3");
        modalList.add(cabList_modal);
        modalList.add(cabList_modal);
        cabList_modal.setGuest_name("Mosque");
        cabList_modal.setName("372/2 mosque RR colony");
        cabList_modal.setCabId("4");
        modalList.add(cabList_modal);
        modalList.add(cabList_modal);
        cabList_modal.setGuest_name("Mosque");
        cabList_modal.setName("372/2 mosque RR colony");
        cabList_modal.setCabId("5");
        modalList.add(cabList_modal);
        favorite_mosque_adapter = new Favorite_Mosque_Adapter(this, modalList);
        recyclerView.setAdapter(favorite_mosque_adapter);
        favorite_mosque_adapter.notifyDataSetChanged();
    }*/

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
        Iv_back =(ImageView)findViewById(R.id.Iv_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Favorite_Mosque_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

//                        driver_id = Integer.parseInt((modalList.get(position).getCabId()));
//                        DriverDetailDialog(driver_id);
//                        Intent intent = new Intent(Favorite_Mosque_Activity.this, MainActivity.class);
//                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
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
