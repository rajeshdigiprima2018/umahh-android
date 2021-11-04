package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import java.util.HashMap;
import java.util.Map;

import Util.ConnectionDetector;
import Util.Constant;
import Util.DownloadImageTask;
import Util.GPSTracker;
import Util.SessionManager;

/**
 * Created by Dell on 18-03-2019.
 */

public class Mousque_detail_activity extends AppCompatActivity implements View.OnClickListener {

    String lng;
    String lat;
    SessionManager session;
    LocationManager manager;
    GPSTracker gps;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back, Iv_Mosque_image;
    TextView Tv_Mosque_name, Tv_Address, Tv_mobile, Tv_email;
    LinearLayout Iv_Education, Lay_Suggestion, Lay_Activity, Lay_association, Lay_news, Lay_donation, Lay_khutba, Lay_Board, Lay_Calender;
    Button Bt_Prayer_sechule, Bt_Continue, Bt_Following;
    TextView Tv_description;

    private String NAME, PLACE;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mousque_detail_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        getSelect_Mosque_Detail();
        Clicklistener();

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

    private void getSelect_Mosque_Detail() {

        showProgressDialog();
        Log.e("url", Constant.Base_url + "user/getUserDetail/" + getIntent().getStringExtra("id"));
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url + "user/getUserDetail/" + getIntent().getStringExtra("id"),
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
                                        lng = jsonMainNode.optString("lng").toString();
                                        lat = jsonMainNode.optString("lat").toString();
                                        String avtar = jsonMainNode.optString("avtar").toString();
                                        String isSelect = jsonMainNode.optString("isSelect").toString();
                                        String nameContactPerson = jsonMainNode.optString("nameContactPerson").toString();
                                        String mobile = jsonMainNode.optString("mobile").toString();
                                        String description_service = jsonMainNode.optString("description_service").toString();


//                                        JSONObject jsonMainNode_city = jsonMainNode.optJSONObject("city");
//                                        String City_id = jsonMainNode_city.optString("").toString();
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

                                        JSONArray follow = jsonMainNode.getJSONObject("following_id").getJSONArray("followUser");
                                        Bt_Following.setText("Follow");
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                            Bt_Following.setBackground(getDrawable(R.drawable.background_google));
//                                        }
                                        for (int i = 0; i < follow.length(); i++) {
                                            if (follow.getString(i).equals(session.getuserinfo().get(Constant.SHARED_PREFERENCE__id_KEY))) {
                                                Bt_Following.setText("Following");
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    Bt_Following.setBackground(getDrawable(R.drawable.background_fb));
//                                                }
                                                break;
                                            }
                                        }
                                        // session.SetSelectMosque_Detail(description_service,_id, username, role, email, zipCode, street_address, lng, lat, avtar, isSelect, nameContactPerson, mobile, City_name, state_name, country_name);
                                        Tv_Address.setText(street_address + ", " + City_name + "\n" + state_name + "" + country_name);
                                        Tv_Mosque_name.setText(username);
                                        NAME = username;
                                        PLACE = City_name + ", " + country_name;
                                        Tv_mobile.setText(mobile);
                                        Tv_email.setText(email);
                                        Tv_description.setText(Html.fromHtml(description_service));

                                        if (avtar.equals("null")) {

                                            Iv_Mosque_image.setImageResource(R.drawable.mosque_1);
                                        } else {
                                            String Image_url = Constant.Image_Base_url + avtar;
                                            new DownloadImageTask(Iv_Mosque_image).execute(Image_url);
                                        }
                                        hideProgressDialog();


                                    } else {
                                        Toast.makeText(Mousque_detail_activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Mousque_detail_activity.this);
        requestQueue.add(stringRequest);
    }

//    private void setData() {
//
//        try {
//            if ((session.getFollow_status().get(Constant.SHARED_PREFERENCE_Follow_status_KEY)).equals("Follow")){
//
//                Bt_Following.setText("Following");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Bt_Following.setBackground(getDrawable(R.drawable.background_google));
//                }
//            }else if ((session.getFollow_status().get(Constant.SHARED_PREFERENCE_Follow_status_KEY)).equals("Unfollow")){
//
//                Bt_Following.setText("Follow");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Bt_Following.setBackground(getDrawable(R.drawable.background_fb));
//                }
//            }
//        }catch (Exception e){}
//
//
//        String avtar = session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_avtar_KEY);
//        if (avtar.equals("null")){
//
//            Iv_Mosque_image.setImageResource(R.drawable.mosque_view);
//        }
//        else {
//            String Image_url = Constant.Image_Base_url+session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_avtar_KEY);
//            new DownloadImageTask(Iv_Mosque_image).execute(Image_url);
//        }
//        Tv_Mosque_name.setText(session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_name_KEY));
//        Tv_Address.setText(session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_street_address_KEY) + ", " + session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_city_KEY) + ",\n" + session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_state_KEY) + ", " + session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_country_KEY));
//        Tv_mobile.setText(session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_mobile_KEY));
//        Tv_email.setText(session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_email_KEY));
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            String textarea_html = Html.fromHtml(session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_description_service_KEY), Html.FROM_HTML_MODE_LEGACY).toString();
//            System.out.println("value--1-------------------->>>  " + textarea_html);
//            Tv_description.setText(textarea_html);
//
//        } else {
//            String textarea_html = Html.fromHtml(session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_description_service_KEY)).toString();
//            System.out.println("value---2------------------->>>  " + textarea_html);
//            Tv_description.setText(textarea_html);
//
//        }
//
//    }

    private void Clicklistener() {

        Iv_back.setOnClickListener(this);
        Iv_Education.setOnClickListener(this);
        Lay_Suggestion.setOnClickListener(this);
        Lay_Activity.setOnClickListener(this);
        Lay_association.setOnClickListener(this);
        Lay_donation.setOnClickListener(this);
        Lay_news.setOnClickListener(this);
        Lay_khutba.setOnClickListener(this);
        Lay_Board.setOnClickListener(this);
        Lay_Calender.setOnClickListener(this);
        Bt_Prayer_sechule.setOnClickListener(this);
        Bt_Continue.setOnClickListener(this);
        Bt_Following.setOnClickListener(this);

    }

    private void Find_element() {
        Tv_description = (TextView) findViewById(R.id.Tv_description);
        Tv_Mosque_name = (TextView) findViewById(R.id.Tv_Mosque_name);
        Tv_Address = (TextView) findViewById(R.id.Tv_Address);
        Tv_mobile = (TextView) findViewById(R.id.Tv_mobile);
        Tv_email = (TextView) findViewById(R.id.Tv_email);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Iv_Education = (LinearLayout) findViewById(R.id.Iv_Education);
        Lay_Suggestion = (LinearLayout) findViewById(R.id.Lay_Suggestion);
        Lay_Activity = (LinearLayout) findViewById(R.id.Lay_Activity);
        Lay_association = (LinearLayout) findViewById(R.id.Lay_association);
        Lay_news = (LinearLayout) findViewById(R.id.Lay_news);
        Lay_donation = (LinearLayout) findViewById(R.id.Lay_donation);
        Lay_khutba = (LinearLayout) findViewById(R.id.Lay_khutba);
        Lay_Board = (LinearLayout) findViewById(R.id.Lay_Board);
        Lay_Calender = (LinearLayout) findViewById(R.id.Lay_Calender);
        Bt_Prayer_sechule = (Button) findViewById(R.id.Bt_Prayer_sechule);
        Bt_Continue = (Button) findViewById(R.id.Bt_Continue);
        Iv_Mosque_image = (ImageView) findViewById(R.id.Iv_Mosque_image);
        Bt_Following = findViewById(R.id.Bt_Following);


    }

    private void All_Depndency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Iv_back:
                onBackPressed();
                break;
            case R.id.Iv_Education:

                Intent intentLogin = new Intent(Mousque_detail_activity.this, Education_Activity.class);
                intentLogin.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(intentLogin);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;
            case R.id.Lay_Suggestion:

                Intent Suggestion_intent = new Intent(Mousque_detail_activity.this, Suggestion_activity.class);
                Suggestion_intent.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(Suggestion_intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Lay_Activity:

                Intent Activity_intgent = new Intent(Mousque_detail_activity.this, Activity_Mosque_Activity.class);
                Activity_intgent.putExtra("id", getIntent().getStringExtra("id"));

                startActivity(Activity_intgent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;

            case R.id.Lay_association:

                Intent association_intgent = new Intent(Mousque_detail_activity.this, Association_Activity.class);
                association_intgent.putExtra("id", getIntent().getStringExtra("id"));

                startActivity(association_intgent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;
            case R.id.Lay_donation:

                Intent association_donation = new Intent(Mousque_detail_activity.this, Mosque_Donation_Type_Activity.class);
                association_donation.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(association_donation);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;
            case R.id.Lay_news:

                Intent association_news = new Intent(Mousque_detail_activity.this, NewsActivity.class);
                association_news.putExtra("id", getIntent().getStringExtra("id"));

                startActivity(association_news);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;

            case R.id.Lay_khutba:

                Intent association_khutba = new Intent(Mousque_detail_activity.this, Khutba_Activity.class);
                association_khutba.putExtra("id", getIntent().getStringExtra("id"));

                startActivity(association_khutba);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Lay_Board:
                Intent intent_khutba = new Intent(Mousque_detail_activity.this, Board_Activity.class);
                intent_khutba.putExtra("id", getIntent().getStringExtra("id"));

                startActivity(intent_khutba);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Lay_Calender:
                Intent intent_Calender = new Intent(Mousque_detail_activity.this, Calender_Activity.class);
                intent_Calender.putExtra("id", getIntent().getStringExtra("id"));

                startActivity(intent_Calender);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Bt_Prayer_sechule:


                Intent intent_Prayer_sechule = new Intent(Mousque_detail_activity.this, Prayer_sechule_Activity.class);
                intent_Prayer_sechule.putExtra("id", getIntent().getStringExtra("id"));
                intent_Prayer_sechule.putExtra("name", NAME);
                intent_Prayer_sechule.putExtra("place", PLACE);

                startActivity(intent_Prayer_sechule);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.Bt_Continue:
                Intent intent_map = new Intent(Mousque_detail_activity.this, MosqueMap_Activity.class);
                intent_map.putExtra("id", getIntent().getStringExtra("id"));
                intent_map.putExtra("lat", lat);
                intent_map.putExtra("lng", lng);

                startActivity(intent_map);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;

            case R.id.Bt_Following:

                Mosque_Following_API();

                break;


        }
    }

    private void Mosque_Following_API() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url + "user/addFollowing",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Follow add", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Follow add", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        JSONObject jsonMainNode = json.optJSONObject("data");


                                        String mosque_id = jsonMainNode.optString("mosque_id").toString();
                                        String isLiked = jsonMainNode.optString("isLiked").toString();
                                        String followCounter = jsonMainNode.optString("followCounter").toString();
                                        String following_id = jsonMainNode.optString("following_id").toString();


                                        try {
                                            // jsonString is a string variable that holds the JSON
                                            JSONArray itemArray = jsonMainNode.getJSONArray("followUser");
                                            session.setFollow_status("Follow");
                                            Bt_Following.setText("Follow");
//                                            Bt_Following.setBackground(getDrawable(R.drawable.background_google));

                                            for (int j = 0; j < itemArray.length(); j++) {
                                                String value = itemArray.getString(j);
                                                Log.e("json--------->>>>>", j + "=" + value);
                                                if ((session.getuserinfo().get(Constant.SHARED_PREFERENCE_USER_ID_KEY)).equals(value)) {
                                                    session.setFollow_status("Unfollow");
                                                    Bt_Following.setText("Following");
//                                                    Bt_Following.setBackground(getDrawable(R.drawable.background_fb));
                                                    break;
                                                }
                                            }
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        String message = json.optString("message");
                                        Toast.makeText(Mousque_detail_activity.this, message, Toast.LENGTH_LONG).show();
                                        //   Iv_favoriteImage
                                        if (message.equals("Follow add successfully.")) {
                                            // Iv_favoriteImage.s
                                        }


                                    } else {
                                        Toast.makeText(Mousque_detail_activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
                map.put("mosque_id", getIntent().getStringExtra("id"));
                map.put("user_id", session.getuserinfo().get(Constant.SHARED_PREFERENCE_USER_ID_KEY));
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Mousque_detail_activity.this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        //    super.onBackPressed();

        if (getIntent().getBooleanExtra("just_finish", false)) {
            finish();
        } else {
            Intent intentLogin = new Intent(Mousque_detail_activity.this, MainActivity.class);
            startActivity(intentLogin);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }
}
