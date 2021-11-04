package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Util.ConnectionDetector;
import Util.Constant;
import Util.DownloadImageTask;
import Util.SessionManager;

/**
 * Created by Dell on 20-05-2019.
 */

public class Business_inner_Detail_Activity extends AppCompatActivity implements View.OnClickListener  {


    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;
    Intent intent;
    TextView Tv_Adress,Tv_Contact,TV_Title,Tv_Business_name;
    ImageView Image;
    TextView Tv_description_service;
    Bundle bundle;
    String KEY_Business_category_id, KEY_Business_name;
    String KEY_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_inner_detail_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        BusinessDetail();
        Click_Event();
    }

    private void BusinessDetail() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"user/getUserDetail/" + KEY_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Business inner Detail", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Business inner Detail", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        // modalList.clear();
                                        JSONObject jsonMainNode = json.optJSONObject("data");
//
                                        String mobile = jsonMainNode.optString("mobile").toString();
                                        String isLoggedIn = jsonMainNode.optString("isLoggedIn").toString();
                                        String access_token = jsonMainNode.optString("access_token").toString();
                                        String nameContactPerson = jsonMainNode.optString("nameContactPerson").toString();
                                        String isFBUser = jsonMainNode.optString("isFBUser").toString();
                                        String isSelect = jsonMainNode.optString("isSelect").toString();
                                        String businessType = jsonMainNode.optString("businessType").toString();
                                        String avtar = jsonMainNode.optString("avtar").toString();
                                        String deviceToken = jsonMainNode.optString("deviceToken").toString();
                                        String lat = jsonMainNode.optString("lat").toString();
                                        String lng = jsonMainNode.optString("lng").toString();
                                        String street_address = jsonMainNode.optString("street_address").toString();
                                        String zipCode = jsonMainNode.optString("zipCode").toString();
                                        String username = jsonMainNode.optString("username").toString();


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

                                        String description_service = jsonMainNode.optString("description_service").toString();

                                        Tv_Adress.setText(street_address+", "+City_name+", "+state_name+", "+country_name+", "+zipCode);
                                        Tv_Contact.setText(mobile);
                                        TV_Title.setText(username);

                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                            String textarea_html = Html.fromHtml(description_service, Html.FROM_HTML_MODE_LEGACY).toString();
                                            System.out.println("value--1-------------------->>>  " + textarea_html);
                                            Tv_description_service.setText(textarea_html);
                                        } else {
                                            String textarea_html = Html.fromHtml(description_service).toString();
                                            System.out.println("value---2------------------->>>  " + textarea_html);
                                            Tv_description_service.setText(textarea_html);
                                        }

                                        String Avtar_value = avtar;

                                        if (Avtar_value.equals("null")){

                                            Image.setImageResource(R.drawable.mosque_1);
                                        }
                                        else {
                                            String Image_url = Constant.Image_Base_url+avtar;
                                            new DownloadImageTask(Image).execute(Image_url);
                                        }

                                        hideProgressDialog();


                                    } else {
                                        Toast.makeText(Business_inner_Detail_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                              /*  if (modalList.size() > 0) {
                                    board_adapter = new Education_Adapter(Education_Activity.this, modalList);
                                    recyclerView.setAdapter(board_adapter);
                                    board_adapter.notifyDataSetChanged();
                                }*/
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
        RequestQueue requestQueue = Volley.newRequestQueue(Business_inner_Detail_Activity.this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Business_inner_Detail_Activity.this, Business_Detatil_Activity.class);
        intent.putExtra("KEY_Business_category_id", KEY_Business_category_id);
        intent.putExtra("KEY_Business_name", KEY_Business_name);
        startActivity(intent);
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
        //  modalList = new ArrayList<>();
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
        intent = getIntent();
        KEY_id = intent.getStringExtra("KEY_id");
        bundle = getIntent().getExtras();
        KEY_Business_category_id = bundle.getString("KEY_Business_category_id");
        KEY_Business_name = bundle.getString("KEY_Business_name");
    }

    private void Click_Event() {

        Iv_back.setOnClickListener(this);
    }

    private void Find_Element() {
        Tv_Adress = (TextView)findViewById(R.id.Tv_Adress);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Tv_Contact = (TextView)findViewById(R.id.Tv_Contact);
        Tv_Business_name = (TextView)findViewById(R.id.Tv_Business_name);
        TV_Title = (TextView)findViewById(R.id.TV_Title);
        Image = (ImageView)findViewById(R.id.Image);
        Tv_description_service = (TextView)findViewById(R.id.Tv_description_service);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Iv_back:
                onBackPressed();
                break;
        }

    }
}
