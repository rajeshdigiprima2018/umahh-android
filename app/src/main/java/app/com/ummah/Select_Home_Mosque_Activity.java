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
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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

import Adapter.Select_mousque_Adapter;
import Modal.Select_Home_Mosque_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.DownloadImageTask;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Abhi on 3/11/2019.
 */

public class Select_Home_Mosque_Activity extends AppCompatActivity implements View.OnClickListener {

    Button bt_Location_automatically;
    EditText Et_Phone, Et_Name_contact_person, Et_Email, Et_Zip_code;
    RecyclerView recyclerView;
    Select_mousque_Adapter selectMosque;
    public ArrayList<Select_Home_Mosque_Modal> modalList;

    String Mosque_id;
    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private ProgressDialog progress_dialog = null;
    TextView tv_Address, tv_Mosque_Name;
    Button Bt_Done;
    TextView Tv_current;
    RelativeLayout Relative_current;
    SearchView Et_Mosque_name;
    ImageView Iv_back,Iv_Image;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_home_mosque);
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        getSelect_Mosque_Detail();
        setData();
        Mosque_List();
        Recycle_view();
        Click_Event();
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
                else  if(query.length() == 0){
                    Mosque_List();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    SerchAPI(newText);
                }
                else if(newText.length() == 0){
                    Mosque_List();
                }
                return false;
            }
        });
    }

    private void SerchAPI(final String query) {
        showProgressDialog();


        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("searchmosque", query);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url+"mosque/searchMosque",
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
                                        Toast.makeText(Select_Home_Mosque_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                                if (modalList.size() > 0) {
                                    selectMosque = new Select_mousque_Adapter(Select_Home_Mosque_Activity.this, modalList);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Select_Home_Mosque_Activity.this);
        requestQueue.add(stringRequest);
    }

    private void setData() {


    }

    private void getSelect_Mosque_Detail() {

        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"user/getUserDetail/" + session.getuserinfo().get(Constant.SHARED_PREFERENCE_mosque_id_KEY),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Current Mosque", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Current Mosque", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        JSONObject jsonMainNode = json.optJSONObject("data");

                                        String _id = jsonMainNode.optString("_id").toString();
                                        String username = jsonMainNode.optString("username").toString();
                                        String role = jsonMainNode.optString("role").toString();
                                        String email = jsonMainNode.optString("email").toString();
                                        String zipCode = jsonMainNode.optString("zipCode").toString();
                                        String street_address = jsonMainNode.optString("street_address").toString();
                                        String lng = jsonMainNode.optString("lng").toString();
                                        String lat = jsonMainNode.optString("lat").toString();
                                        String avtar = jsonMainNode.optString("avtar").toString();
                                        String isSelect = jsonMainNode.optString("isSelect").toString();
                                        String nameContactPerson = jsonMainNode.optString("nameContactPerson").toString();
                                        String mobile = jsonMainNode.optString("mobile").toString();
                                        String description_service = jsonMainNode.optString("description_service").toString();


                                        JSONObject jsonMainNode_city = jsonMainNode.optJSONObject("city");
                                        String City_id = jsonMainNode_city.optString("id").toString();
                                        String City_name = jsonMainNode_city.optString("name").toString();
                                        String City_state_id = jsonMainNode_city.optString("state_id").toString();
                                        String City_cities_id = jsonMainNode_city.optString("cities_id").toString();

                                        JSONObject jsonMainNode_state = jsonMainNode.optJSONObject("state");
                                        String state_id = jsonMainNode_state.optString("id").toString();
                                        String state_name = jsonMainNode_state.optString("name").toString();
                                        String state_country_id = jsonMainNode_state.optString("country_id").toString();
                                        String state_state_id = jsonMainNode_state.optString("state_id").toString();

                                        JSONObject jsonMainNode_country = jsonMainNode.optJSONObject("country");
                                        String country_id = jsonMainNode_country.optString("id").toString();
                                        String country_sortname = jsonMainNode_country.optString("sortname").toString();
                                        String country_name = jsonMainNode_country.optString("name").toString();
                                        String country_phoneCode = jsonMainNode_country.optString("phoneCode").toString();
                                        String country_country_id = jsonMainNode_country.optString("country_id").toString();


                                        session.SetSelectMosque_Detail(description_service,_id, username, role, email, zipCode, street_address, lng, lat, avtar, isSelect, nameContactPerson, mobile, City_name, state_name, country_name);
                                        tv_Address.setText(street_address + ", " + City_name + "\n" + state_name + "" + country_name);
                                        tv_Mosque_Name.setText(username);
                                        if (avtar.equals("null")){

                                            Iv_Image.setImageResource(R.drawable.mosque_1);
                                        }
                                        else {
                                            String Image_url = Constant.Image_Base_url+avtar;
                                            new DownloadImageTask(Iv_Image).execute(Image_url);
                                        }
                                        hideProgressDialog();


                                    } else {
                                        Toast.makeText(Select_Home_Mosque_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
              /*  map.put("user_id",session.getuserinfo().get(Constant.SHARED_PREFERENCE__id_KEY) );
                map.put("isSelect", "true");
                map.put("mosque_id", session.getuserinfo().get(Constant.SHARED_PREFERENCE_mosque_id_KEY));*/
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Select_Home_Mosque_Activity.this);
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
        modalList = new ArrayList<>();
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
    }

    private void Mosque_List() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Mosque_nearby + Constant.latitude + "/" + Constant.longitude + "/10000",
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

try {
//    JSONObject jsonMainNode_city = jsonChildNode.optJSONObject("city");
//    String City_id = jsonMainNode_city.optString("id").toString();
    String City_name = jsonChildNode.optString("city").toString();
//    String City_state_id = jsonMainNode_city.optString("state_id").toString();
//    String City_cities_id = jsonMainNode_city.optString("cities_id").toString();

//    JSONObject jsonMainNode_state = jsonChildNode.optJSONObject("state");
//    String state_id = jsonMainNode_state.optString("id").toString();
    String state_name = jsonChildNode.optString("state").toString();
//    String state_country_id = jsonMainNode_state.optString("country_id").toString();
//    String state_state_id = jsonMainNode_state.optString("state_id").toString();

//    JSONObject jsonMainNode_country = jsonChildNode.optJSONObject("country");
//    String country_id = jsonMainNode_country.optString("id").toString();
//    String country_sortname = jsonMainNode_country.optString("sortname").toString();
    String country_name = jsonChildNode.optString("country").toString();
//    String country_phoneCode = jsonMainNode_country.optString("phoneCode").toString();
//    String country_country_id = jsonMainNode_country.optString("country_id").toString();


    selectHomeMosqueModal.set_id(_id);
    selectHomeMosqueModal.setUsername(username);
    selectHomeMosqueModal.setRole(role);
    selectHomeMosqueModal.setZipCode(zipCode);
    selectHomeMosqueModal.setStreet_address(street_address);
    //String re = avtar.replaceAll("\","/");
    selectHomeMosqueModal.setAvtar(avtar);
    // For city details...
//    selectHomeMosqueModal.setCity_id(City_id);
    selectHomeMosqueModal.setCity_name(City_name);
//    selectHomeMosqueModal.setCity_state_id(City_state_id);
//    selectHomeMosqueModal.setCity_cities_id(City_cities_id);

    // For state detail..........
//    selectHomeMosqueModal.setState_id(state_id);
    selectHomeMosqueModal.setState_name(state_name);
//    selectHomeMosqueModal.setState_country_id(state_country_id);
//    selectHomeMosqueModal.setState_state_id(state_state_id);

    //For country Detail....
//    selectHomeMosqueModal.setCountry_id(country_id);
//    selectHomeMosqueModal.setCountry_sortname(country_sortname);
    selectHomeMosqueModal.setCountry_name(country_name);
//    selectHomeMosqueModal.setCountry_phoneCode(country_phoneCode);
//    selectHomeMosqueModal.setCountry_country_id(country_country_id);

    modalList.add(selectHomeMosqueModal);
}
catch (Exception ee){ee.printStackTrace();}

                                        }


                                    } else {
                                        Toast.makeText(Select_Home_Mosque_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                                if (modalList.size() > 0) {
                                    selectMosque = new Select_mousque_Adapter(Select_Home_Mosque_Activity.this, modalList);
                                    recyclerView.setAdapter(selectMosque);
                                    selectMosque.notifyDataSetChanged();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Select_Home_Mosque_Activity.this);
        requestQueue.add(stringRequest);
    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Mosque_id = modalList.get(position).get_id();
                        SetMosque(Mosque_id);


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

    }


    private void SetMosque(final String mosque_id) {

      /*  final HashMap<String, String> map = new HashMap<String, String>();
        map.put("user_id",session.getuserinfo().get(Constant.SHARED_PREFERENCE__id_KEY) );
        map.put("isSelect", "true");
        map.put("mosque_id", mosque_id);*/

        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url+"mosque/firstSelected/statusChange",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Select Mosque", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Select Mosque", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        JSONObject jsonMainNode = json.optJSONObject("data");

                                        String _id = jsonMainNode.optString("_id").toString();
                                        String username = jsonMainNode.optString("username").toString();
                                        String role = jsonMainNode.optString("role").toString();
                                        String email = jsonMainNode.optString("email").toString();
                                        String zipCode = jsonMainNode.optString("zipCode").toString();
                                        String street_address = jsonMainNode.optString("street_address").toString();
                                        String lng = jsonMainNode.optString("lng").toString();
                                        String lat = jsonMainNode.optString("lat").toString();
                                        String avtar = jsonMainNode.optString("avtar").toString();
                                        String isSelect = jsonMainNode.optString("isSelect").toString();
                                        String nameContactPerson = jsonMainNode.optString("nameContactPerson").toString();
                                        String mobile = jsonMainNode.optString("mobile").toString();

//                                        JSONObject jsonMainNode_city = jsonMainNode.optJSONObject("city");
//                                        String City_id = jsonMainNode_city.optString("id").toString();
                                        String City_name = jsonMainNode.optString("city").toString();
//                                        String City_state_id = jsonMainNode_city.optString("state_id").toString();
//                                        String City_cities_id = jsonMainNode_city.optString("cities_id").toString();

//                                        JSONObject jsonMainNode_state = jsonMainNode.optJSONObject("state");
//                                        String state_id = jsonMainNode_state.optString("id").toString();
                                        String state_name = jsonMainNode.optString("state").toString();
//                                        String state_country_id = jsonMainNode_state.optString("country_id").toString();
//                                        String state_state_id = jsonMainNode_state.optString("state_id").toString();

//                                        JSONObject jsonMainNode_country = jsonMainNode.optJSONObject("country");
//                                        String country_id = jsonMainNode_country.optString("id").toString();
//                                        String country_sortname = jsonMainNode_country.optString("sortname").toString();
                                        String country_name = jsonMainNode.optString("country").toString();
//                                        String country_phoneCode = jsonMainNode_country.optString("phoneCode").toString();
//                                        String country_country_id = jsonMainNode_country.optString("country_id").toString();

                                        String description_service = jsonMainNode.optString("description_service").toString();
                                        session.SetSelectMosque_Detail(description_service, _id, username, role, email, zipCode, street_address, lng, lat, avtar, isSelect, nameContactPerson, mobile, City_name, state_name, country_name);
                                        Intent intent = new Intent(Select_Home_Mosque_Activity.this, MainActivity.class);
                                        hideProgressDialog();
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);




                                    } else {
                                        Toast.makeText(Select_Home_Mosque_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
                map.put("user_id", session.getuserinfo().get(Constant.SHARED_PREFERENCE__id_KEY));
                map.put("isSelect", "true");
                map.put("mosque_id", mosque_id);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Select_Home_Mosque_Activity.this);
        requestQueue.add(stringRequest);
    }

    private void Click_Event() {

        bt_Location_automatically.setOnClickListener(this);
        Bt_Done.setOnClickListener(this);
        Iv_back.setOnClickListener(this);

    }

    private void Find_element() {
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Et_Mosque_name = (SearchView) findViewById(R.id.Et_Mosque_name);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tv_Mosque_Name = (TextView) findViewById(R.id.tv_Mosque_Name);
        tv_Address = (TextView) findViewById(R.id.tv_descrition1);
        Tv_current = (TextView) findViewById(R.id.Tv_current);
        Relative_current = (RelativeLayout) findViewById(R.id.Relative_current);
        Iv_Image = (ImageView) findViewById(R.id.Iv_Image);

        bt_Location_automatically = (Button) findViewById(R.id.bt_Location_automatically);
        Bt_Done = (Button) findViewById(R.id.Bt_Done);
        if ((session.getuserinfo().get(Constant.SHARED_PREFERENCE_mosque_id_KEY)).equals("mosque_id") || (session.getuserinfo().get(Constant.SHARED_PREFERENCE_mosque_id_KEY)).equals(null) || (session.getuserinfo().get(Constant.SHARED_PREFERENCE_mosque_id_KEY)).equals("") || (session.getuserinfo().get(Constant.SHARED_PREFERENCE_mosque_id_KEY)).equals("null")) {

            Bt_Done.setVisibility(View.GONE);
            Tv_current.setVisibility(View.GONE);
            Relative_current.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_Location_automatically:

                Mosque_List();
                break;
            case R.id.Bt_Done:

                Intent intent = new Intent(Select_Home_Mosque_Activity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                break;
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

    @Override
    public void onBackPressed() {

        if(getIntent().getBooleanExtra("finish", false))
        {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else {
            // super.onBackPressed();
            session.logoutUser();
            Intent in = new Intent(Select_Home_Mosque_Activity.this, Login_Activity.class);
            startActivity(in);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
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
}


