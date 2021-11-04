package Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import Adapter.Select_mousque_Adapter;
import Modal.Bookmark_Modal;
import Modal.Select_Home_Mosque_Modal;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;
import app.com.ummah.Mosque_Donation_Type_Activity;
import app.com.ummah.R;

/**
 * Created by Dell on 22-03-2019.
 */

public class Fragment_Donation extends Fragment {

    RecyclerView recyclerView;
    private ProgressDialog progress_dialog = null;
    Select_mousque_Adapter selectMosque;
    public ArrayList<Select_Home_Mosque_Modal> modalList;
    Bookmark_Modal cabList_modal = new Bookmark_Modal();
    SearchView Et_Mosque_name;
    SessionManager session;

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(getActivity());
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
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

    @Nullable

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation, container, false);
        modalList = new ArrayList<>();
        Find_element(view);
        click_event();
        ProgressBar_Function();
        Mosque_List();
        session = new SessionManager(getActivity());
        Et_Mosque_name.setIconified(false);
        Et_Mosque_name.setQueryHint("Type mosque name");
        Et_Mosque_name.clearFocus();
        Et_Mosque_name.setQueryHint(Html.fromHtml("<small><small><font color = #ffffff>" + getResources().getString(R.string.Type_Mosque_name) + "</font></small></small>"));

        Et_Mosque_name.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.length() > 2) {
                    SerchAPI(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    SerchAPI(newText);
                }
                return false;
            }
        });

        return view;
    }

    private void Mosque_List() {
        showProgressDialog();
        String url =  Constant.Mosque_nearby + Constant.latitude + "/" + Constant.longitude + "/10000";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
               url,
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
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            //   Select_Home_Mosque_Modal select_home_mosque_modal = new Select_Home_Mosque_Modal();
                                            Select_Home_Mosque_Modal selectHomeMosqueModal = new Select_Home_Mosque_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String _id = jsonChildNode.optString("_id").toString();
                                            String username = jsonChildNode.optString("username").toString();
                                            String role = jsonChildNode.optString("role").toString();
                                            String randomToken = jsonChildNode.optString("randomToken").toString();
                                            String updatedAt = jsonChildNode.optString("updatedAt").toString();
                                            String createdAt = jsonChildNode.optString("createdAt").toString();
                                            String phoneVerification = jsonChildNode.optString("phoneVerification").toString();
                                            String vefication = jsonChildNode.optString("vefication").toString();
                                            String zipCode = jsonChildNode.optString("zipCode").toString();
                                            String category = jsonChildNode.optString("category").toString();

                                            String street_address = jsonChildNode.optString("street_address").toString();
                                            String lng = jsonChildNode.optString("lng").toString();
                                            String lat = jsonChildNode.optString("lat").toString();
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
                                            String email = jsonChildNode.optString("email").toString();


//                                            JSONObject jsonMainNode_city = jsonChildNode.optJSONObject("city");
//                                            String City_id = jsonMainNode_city.optString("id").toString();
//                                            String City_name = jsonMainNode_city.optString("name").toString();
//                                            String City_state_id = jsonMainNode_city.optString("state_id").toString();
//                                            String City_cities_id = jsonMainNode_city.optString("cities_id").toString();
//
//                                            JSONObject jsonMainNode_state = jsonChildNode.optJSONObject("state");
//                                            String state_id = jsonMainNode_state.optString("id").toString();
//                                            String state_name = jsonMainNode_state.optString("name").toString();
//                                            String state_country_id = jsonMainNode_state.optString("country_id").toString();
//                                            String state_state_id = jsonMainNode_state.optString("state_id").toString();
//
//                                            JSONObject jsonMainNode_country = jsonChildNode.optJSONObject("country");
//                                            String country_id = jsonMainNode_country.optString("id").toString();
//                                            String country_sortname = jsonMainNode_country.optString("sortname").toString();
//                                            String country_name = jsonMainNode_country.optString("name").toString();
//                                            String country_phoneCode = jsonMainNode_country.optString("phoneCode").toString();
//                                            String country_country_id = jsonMainNode_country.optString("country_id").toString();


                                            selectHomeMosqueModal.set_id(_id);
                                            selectHomeMosqueModal.setUsername(username);
                                            selectHomeMosqueModal.setRole(role);
                                            selectHomeMosqueModal.setZipCode(zipCode);
                                            selectHomeMosqueModal.setStreet_address(street_address);
                                            //String re = avtar.replaceAll("\","/");
                                            selectHomeMosqueModal.setAvtar(avtar);
                                            // For city details...
//                                            selectHomeMosqueModal.setCity_id(City_id);
//                                            selectHomeMosqueModal.setCity_name(City_name);
//                                            selectHomeMosqueModal.setCity_state_id(City_state_id);
//                                            selectHomeMosqueModal.setCity_cities_id(City_cities_id);
//
//                                            // For state detail..........
//                                            selectHomeMosqueModal.setState_id(state_id);
//                                            selectHomeMosqueModal.setState_name(state_name);
//                                            selectHomeMosqueModal.setState_country_id(state_country_id);
//                                            selectHomeMosqueModal.setState_state_id(state_state_id);
//
//                                            //For country Detail....
//                                            selectHomeMosqueModal.setCountry_id(country_id);
//                                            selectHomeMosqueModal.setCountry_sortname(country_sortname);
//                                            selectHomeMosqueModal.setCountry_name(country_name);
//                                            selectHomeMosqueModal.setCountry_phoneCode(country_phoneCode);
//                                            selectHomeMosqueModal.setCountry_country_id(country_country_id);

                                            modalList.add(selectHomeMosqueModal);

                                        }


                                    } else {
                                        Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                                if (modalList.size() > 0) {
                                    selectMosque = new Select_mousque_Adapter(getActivity(), modalList);
                                    recyclerView.setAdapter(selectMosque);
                                    selectMosque.notifyDataSetChanged();
                                    hideProgressDialog();
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
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void SerchAPI(final String query) {
        showProgressDialog();


        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("searchmosque", query);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url + "mosque/searchMosque",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("searchMosque list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("searchMosque List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            //   Select_Home_Mosque_Modal select_home_mosque_modal = new Select_Home_Mosque_Modal();
                                            Select_Home_Mosque_Modal selectHomeMosqueModal = new Select_Home_Mosque_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String _id = jsonChildNode.optString("_id").toString();
                                            String username = jsonChildNode.optString("username").toString();
                                            String role = jsonChildNode.optString("role").toString();
                                            String randomToken = jsonChildNode.optString("randomToken").toString();
                                            String updatedAt = jsonChildNode.optString("updatedAt").toString();
                                            String createdAt = jsonChildNode.optString("createdAt").toString();
                                            String phoneVerification = jsonChildNode.optString("phoneVerification").toString();
                                            String vefication = jsonChildNode.optString("vefication").toString();
                                            String zipCode = jsonChildNode.optString("zipCode").toString();
                                            String category = jsonChildNode.optString("category").toString();

                                            String street_address = jsonChildNode.optString("street_address").toString();
                                            String lng = jsonChildNode.optString("lng").toString();
                                            String lat = jsonChildNode.optString("lat").toString();
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
                                            String email = jsonChildNode.optString("email").toString();


                                         /*   JSONObject jsonMainNode_city = jsonChildNode.optJSONObject("city");
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
                                            String country_country_id = jsonMainNode_country.optString("country_id").toString();*/


                                            selectHomeMosqueModal.set_id(_id);
                                            selectHomeMosqueModal.setAvtar(avtar);
                                            selectHomeMosqueModal.setUsername(username);
                                            selectHomeMosqueModal.setRole(role);
                                            selectHomeMosqueModal.setZipCode(zipCode);
                                            selectHomeMosqueModal.setStreet_address(street_address);


                                            modalList.add(selectHomeMosqueModal);

                                        }

                                        hideProgressDialog();
                                    } else {
                                        Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                                if (modalList.size() > 0) {
                                    selectMosque = new Select_mousque_Adapter(getActivity(), modalList);
                                    recyclerView.setAdapter(selectMosque);
                                    selectMosque.notifyDataSetChanged();
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
                map.put("searchmosque", query);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void SavePayment(String id, String txn, String amount) {
        showProgressDialog();


        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("mosque_id", id);
        map.put("txn_id", txn);
        map.put("amount", amount);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constant.Base_url + "payment/savePayment",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("searchMosque list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("searchMosque List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        Toast.makeText(getActivity(), "Payment success.", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_LONG).show();
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
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mosque_id", id);
                map.put("txn_id", txn);
                map.put("amount", amount);

                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private void click_event() {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

//                        Mosque_id = modalList.get(position).get_id();
//                        SetMosque(Mosque_id);
//                        SavePayment(modalList.get(position).get_id(),"3232","2323");
                        Intent association_donation = new Intent(getActivity(), Mosque_Donation_Type_Activity.class);
                        association_donation.putExtra("id", modalList.get(position).get_id());
                        association_donation.putExtra("finish_onBack", true);
                        startActivity(association_donation);


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    private void Find_element(View view) {
        Et_Mosque_name = view.findViewById(R.id.Et_Mosque_name);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


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
