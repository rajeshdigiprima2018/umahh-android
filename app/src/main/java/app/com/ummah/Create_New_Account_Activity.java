package app.com.ummah;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Modal.AllCategory_Modal;
import Modal.CityDetail_Modal;
import Modal.Country_Detail_Modal;
import Modal.State_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.SessionManager;
import Util.Utility;

/**
 * Created by Abhi on 3/11/2019.
 */

public class Create_New_Account_Activity extends AppCompatActivity implements View.OnClickListener {
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    ImageView Iv_back;
    Button bt_Sign_Up;
    EditText Et_pass, Et_Conf_pass;
    EditText Et_Mosque_name, Et_Phone, Et_Name_contact_person, Et_Email, street_address, Et_Zip_code;
    ArrayList<Country_Detail_Modal> list_country = new ArrayList<>();
    ArrayList<AllCategory_Modal> allCategory_modals = new ArrayList<>();
    private ArrayList<State_Modal> list_state = new ArrayList<>();
    private ArrayList<CityDetail_Modal> list_city = new ArrayList<>();
    EditText spinner_country, spinner_state, spinner_city;
            Spinner spinner_Category;
    String Country_Name, state_Name;
    String Country_id, State_id;
    private ProgressDialog progress_dialog = null;
    String Country, state, city, category;
    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    public RadioGroup radioGroup;
    public RadioButton Rb_Business, Rb_Org;
    static String businessType = "";
    double latitude, longitude;
    String Search_address;
    String Search_city, Search_state, Search_country;
    String Register_Type;
    Intent intent;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crete_new_account);

        Places.initialize(getApplicationContext(), "AIzaSyA53h3iZOJXc_gnyc3B6ZYGzYPcRkcH9IM");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        ProgressBar_Function();
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        Set_Visibility();
        Click_Event();
        //State_List();
        CategoryList();


    }

    private void Set_Visibility() {
        if (Register_Type.equals("Mosque")) {
            radioGroup.setVisibility(View.GONE);
            spinner_Category.setVisibility(View.GONE);

        }
    }

    private void CategoryList() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"business/getAllCategory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("AllCategory list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("AllCategory for Place", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();
                                        list_country.clear();
                                        for (int i = 0; i <= lengthJsonArr; i++) {
                                            AllCategory_Modal allCategory_modal = new AllCategory_Modal();

                                            if (i == 0) {
                                                allCategory_modal.setCategory_id("0");
                                                allCategory_modal.setName("Select Category");
                                            } else {
                                                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i - 1);
                                                // String id = jsonChildNode.optString("id").toString();
                                                String category_id = jsonChildNode.optString("category_id").toString();
                                                String name = jsonChildNode.optString("name").toString();
                                                // String phoneCode = jsonChildNode.optString("phoneCode").toString();
                                                System.out.println("Country_id --- >" + category_id + " " + "\nstate_name----->>> " + name);
                                                allCategory_modal.setCategory_id(category_id);
                                                allCategory_modal.setName(name);
                                                // stateDetailModal.setPhoneCode(phoneCode);
                                                // stateDetailModal.setId(id);
                                            }
                                            allCategory_modals.add(allCategory_modal);

                                        }

                                    } else {
                                        Toast.makeText(Create_New_Account_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Category_NameSpinnerAdapter customSpinnerAdapter = new Category_NameSpinnerAdapter(getApplicationContext(), allCategory_modals);
//                        spinner_Category.setAdapter(customSpinnerAdapter);
//                        spinner_Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                Country_Name = parent.getItemAtPosition(position).toString();
//                                // Country_id = (allCategory_modals.get(position).getId());
//                                category = allCategory_modals.get(position).getCategory_id();
//                                System.out.println("State id For Filter-- >>>> " + Country_id);
//                                if (Country_id == "0") {
//                                    System.out.println("State id For Filter-- >>>> " + Country_id);
//                                } else {
//
//                                     State_name_API(Country_id);
//                                    System.out.println("category-- >>>> " + category);
//                                }
//
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//                            }
//
//                        });

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
        RequestQueue requestQueue = Volley.newRequestQueue(Create_New_Account_Activity.this);
        requestQueue.add(stringRequest);
    }

    private void Click_Event() {

        bt_Sign_Up.setOnClickListener(this);
        Iv_back.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                }
                if (Rb_Business.isChecked()) {
                    Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                    businessType = String.valueOf(rb.getText());

                }
                if (Rb_Org.isChecked()) {
                    Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                    businessType = String.valueOf(rb.getText());
                }
            }
        });
    }

    private void Find_element() {

        spinner_Category = (Spinner) findViewById(R.id.spinner_Category);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Et_Mosque_name = (EditText) findViewById(R.id.Et_Mosque_name);
        Et_Phone = (EditText) findViewById(R.id.Et_Phone);
        Et_Name_contact_person = (EditText) findViewById(R.id.Et_Name_contact_person);
        Et_Email = (EditText) findViewById(R.id.Et_Email);
        Et_Zip_code = (EditText) findViewById(R.id.Et_Zip_code);
        bt_Sign_Up = (Button) findViewById(R.id.bt_Sign_Up);
        spinner_country = (EditText) findViewById(R.id.spinner_country);
        spinner_state = (EditText) findViewById(R.id.spinner_state);
        spinner_city = (EditText) findViewById(R.id.spinner_city);
        street_address = (EditText) findViewById(R.id.street_address);
        Et_pass = (EditText) findViewById(R.id.Et_pass);
        Et_Conf_pass = (EditText) findViewById(R.id.Et_Conf_pass);

        radioGroup = (RadioGroup) findViewById(R.id.Radio_Group_type);
        Rb_Business = (RadioButton) findViewById(R.id.Rb_Business);
        Rb_Org = (RadioButton) findViewById(R.id.Rb_Org);
        if (Register_Type.equals("Mosque")){

            Et_Mosque_name.setHint("Mosque Name");
        }
        spinner_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddress();
            }
        });
        spinner_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddress();
            }
        });
        spinner_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddress();
            }
        });
        street_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddress();
            }

        });
        Et_Zip_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddress();
            }
        });
    }

    public void OpenAddress()
    {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS_COMPONENTS, Place.Field.ADDRESS);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_Sign_Up:

              /*  Intent intent = new Intent(Create_New_Account_Activity.this, Verification_code_activity.class);
                startActivity(intent);*/

                Register();

                break;

            case R.id.Iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Create_New_Account_Activity.this, UserType_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void Register() {

        if (Et_Mosque_name.getText().toString().length() == 0) {
            Et_Mosque_name.setError("Mosque name not entered");
            Et_Mosque_name.requestFocus();
        } else if (Et_Phone.getText().toString().length() < 12) {
            Et_Phone.setError("Mobile number with country code");
            Et_Phone.requestFocus();
        } else if (Et_Name_contact_person.getText().toString().length() == 0) {
            Et_Name_contact_person.setError("Contact Person not entered");
            Et_Name_contact_person.requestFocus();
        } else if (!Utility.eMailValidation((Et_Email.getText().toString()))) {
            String msgs = getString(R.string.text_enter_valid_email);
            Et_Email.setError(msgs);
            Et_Email.requestFocus();
        } else if (street_address.getText().toString().length() == 0) {
            street_address.setError("Street Address not entered");
            street_address.requestFocus();
        } else if (Et_Zip_code.getText().toString().length() == 0) {
            Et_Zip_code.setError("Zip code not entered");
            Et_Zip_code.requestFocus();
        } else {
            Search_address = street_address + ", " + Search_city + ", " + Search_state + ", " + Search_country + ", " + Et_Zip_code;
            getLatLongFromAddress(Search_address);
            postUsingVolley();
            // SignUP();

        }
    }

    private void postUsingVolley() {

        showProgressDialog();

        final HashMap<String, String> map = new HashMap<String, String>();

        if (Register_Type.equals("Mosque")) {
            map.put("username", Et_Mosque_name.getText().toString().trim());
            map.put("nameContactPerson", Et_Name_contact_person.getText().toString().trim());
            map.put("mobile", Et_Phone.getText().toString().trim());
            map.put("isFBUser", "false");
            map.put("role", "mosque");
            map.put("email", Et_Email.getText().toString().trim());
            map.put("street_address", street_address.getText().toString());
            map.put("country", Country);
            map.put("state", state);
            map.put("city", city);
            map.put("zipCode", Et_Zip_code.getText().toString());
            map.put("lat", String.valueOf(latitude));
            map.put("lng", String.valueOf(longitude));

        } else {
            map.put("username", Et_Mosque_name.getText().toString().trim());
            map.put("nameContactPerson", Et_Name_contact_person.getText().toString().trim());
            map.put("mobile", Et_Phone.getText().toString().trim());
            map.put("isFBUser", "false");
            map.put("businessType", businessType);
            map.put("role", "business");
            map.put("email", Et_Email.getText().toString().trim());
            map.put("street_address", street_address.getText().toString());
            map.put("country", Country);
            map.put("category", category);
            map.put("state", state);
            map.put("city", city);
            map.put("zipCode", Et_Zip_code.getText().toString());
            map.put("lat", String.valueOf(latitude));
            map.put("lng", String.valueOf(longitude));
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constant.Base_url+"mosqueAndbusiness/register", new JSONObject(map),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("register ", response.toString());
                        if (response.has("success")) {
                            String success = response.optString("success").toString();
                            if (success.equals("true")) {
                                String message = response.optString("message").toString();
                                if (message.equals("Signup is completed, you can login now!")) {
                                   // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    hideProgressDialog();
                                    Show_dialog_box();


                                }
                            } else if (success.equals("false")) {
                                String message = response.optString("message").toString();
                                String error = response.optString("error").toString();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                hideProgressDialog();
                            }
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(8000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjReq);
    }

    private void Show_dialog_box() {

        new AlertDialog.Builder(this)
                .setTitle("Umahh")
                .setMessage("Thank you for registering with Umahh. We will send you further details on mail after necessary verification and approval.")
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Create_New_Account_Activity.this, Login_Activity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                })
                .show();
               /* .setNegativeButton("No", null)
                .show();*/
    }

    private void SignUP() {
        //  showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url+"mosqueAndbusiness/register",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("Register", response);
                        try {

                            JSONObject json = new JSONObject(response);
                            Log.e("Response for Register", json.toString());


                            if (json.has("success")) {
                                String success = json.optString("success").toString();
                                String message = json.optString("message").toString();

                                if (success.equals("true")) {
                                    if (message.equals("Signup is completed, you can login now!")) {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Create_New_Account_Activity.this, Login_Activity.class);
                                        //  hideProgressDialog();
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    }

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Access denied by server.", Toast.LENGTH_LONG).show();

                        }
                    }
                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            //showSignOutAlertDialog(context, "TimeoutError");
                            Toast.makeText(getApplicationContext(), "TimeoutError", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            // showSignOutAlertDialog(context, "AuthFailureError");
                            Toast.makeText(getApplicationContext(), "AuthFailureError", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {

                            //  showSignOutAlertDialog(context, "ServerError");
                            Toast.makeText(getApplicationContext(), "ServerError", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "NetworkError", Toast.LENGTH_LONG).show();
                            //showSignOutAlertDialog(context, "NetworkError");
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(), "ParseError", Toast.LENGTH_LONG).show();
                            //showSignOutAlertDialog(context, "ParseError");
                        }
                    }
                }

        )

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", Et_Mosque_name.getText().toString().trim());
                map.put("nameContactPerson", Et_Name_contact_person.getText().toString().trim());
                map.put("mobile", Et_Phone.getText().toString().trim());
                map.put("isFBUser", "false");
                map.put("businessType", businessType);
                map.put("role", "business");
                map.put("email", Et_Email.getText().toString().trim());
                map.put("street_address", street_address.getText().toString());
                map.put("country", Country);
                map.put("category", category);
                map.put("state", state);
                map.put("city", city);
                map.put("zipCode", Et_Zip_code.getText().toString());
                map.put("lat", "o");
                map.put("lng", "0");
                return map;


            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //AppController.getInstance().addToRequestQueue(request);
        //  requestQueue.add(stringRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(Create_New_Account_Activity.this);
        requestQueue.add(stringRequest);


    }

//    private void State_List() {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"user/getCountries",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("Place list", response);
//                        try {
//                            JSONObject json = new JSONObject(response);
//                            Log.e("Response for Place", json.toString());
//                            if (json != null) {
//
//                                if (json.has("success")) {
//
//                                    String success = json.optString("success");
//                                    if (success.equals("true")) {
//                                        JSONArray jsonMainNode = json.optJSONArray("countries");
//                                        int lengthJsonArr = jsonMainNode.length();
//                                        list_country.clear();
//                                        for (int i = 0; i <= lengthJsonArr; i++) {
//                                            Country_Detail_Modal stateDetailModal = new Country_Detail_Modal();
//
//                                            if (i == 0) {
//                                                stateDetailModal.setCountry_id("0");
//                                                stateDetailModal.setCountries_Name("Select country");
//                                            } else {
//                                                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i - 1);
//                                                String id = jsonChildNode.optString("id").toString();
//                                                String country_id = jsonChildNode.optString("country_id").toString();
//                                                String name = jsonChildNode.optString("name").toString();
//                                                String phoneCode = jsonChildNode.optString("phoneCode").toString();
//                                                System.out.println("Country_id --- >" + country_id + " " + "\nstate_name----->>> " + name);
//                                                stateDetailModal.setCountry_id(country_id);
//                                                stateDetailModal.setCountries_Name(name);
//                                                stateDetailModal.setPhoneCode(phoneCode);
//                                                stateDetailModal.setId(id);
//                                            }
//                                            list_country.add(stateDetailModal);
//
//                                        }
//
//                                    } else {
//                                        Toast.makeText(Create_New_Account_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        Country_NameSpinnerAdapter customSpinnerAdapter = new Country_NameSpinnerAdapter(getApplicationContext(), list_country);
//                        spinner_country.setAdapter(customSpinnerAdapter);
//                        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                try {
//
//                                    Country_Name = parent.getItemAtPosition(position).toString();
//                                    Country_id = (list_country.get(position).getId());
//                                    Country = list_country.get(position).getCountry_id();
//                                    Search_country = list_country.get(position).getCountries_Name();
//
//                                } catch (Exception e) {
//                                }
//
//
//                                System.out.println("State id For Filter-- >>>> " + Country_id);
//                                if (Country_id == "0") {
//                                    System.out.println("State id For Filter-- >>>> " + Country_id);
//                                } else {
//
//                                    State_name_API(Country_id);
//                                    System.out.println("State id For Filter-- >>>> " + Country_id);
//                                }
//
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//                            }
//
//                        });
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                return map;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(Create_New_Account_Activity.this);
//        requestQueue.add(stringRequest);
//    }

//    private void State_name_API(String Country_id) {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"user/getStates/" + Country_id,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("Place list", response);
//                        try {
//                            JSONObject json = new JSONObject(response);
//                            Log.e("Response for Place", json.toString());
//                            if (json != null) {
//
//                                if (json.has("success")) {
//
//                                    String success = json.optString("success");
//                                    if (success.equals("true")) {
//                                        JSONArray jsonMainNode = json.optJSONArray("states");
//                                        int lengthJsonArr = jsonMainNode.length();
//                                        list_state.clear();
//                                        for (int i = 0; i <= lengthJsonArr; i++) {
//                                            State_Modal state_modal = new State_Modal();
//                                            if (i == 0) {
//                                                state_modal.setState_id("0");
//                                                state_modal.setState_name("Select state");
//                                            } else {
//                                                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i - 1);
//                                                String id = jsonChildNode.optString("id").toString();
//                                                String state_id = jsonChildNode.optString("state_id").toString();
//                                                String country_id = jsonChildNode.optString("country_id").toString();
//                                                String states_name = jsonChildNode.optString("name").toString();
//                                                System.out.println("Country_id --- >" + state_id + " " + "\nstate_name----->>> " + states_name);
//                                                state_modal.setState_id(state_id);
//                                                state_modal.setState_name(states_name);
//                                                state_modal.setId(id);
//                                            }
//                                            list_state.add(state_modal);
//
//                                        }
//
//                                    } else {
//                                        Toast.makeText(Create_New_Account_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        State_NameSpinnerAdapter center_nameSpinnerAdapter = new State_NameSpinnerAdapter(getApplication(), list_state);
//                        spinner_state.setAdapter(center_nameSpinnerAdapter);
//                        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                state_Name = parent.getItemAtPosition(position).toString();
//                                State_id = (list_state.get(position).getId());
//                                state = list_state.get(position).getState_id();
//                                Search_state = list_state.get(position).getState_name();
//                                if (State_id == "Select Center") {
//                                    System.out.println("Center id For Filter-- >>>> " + State_id);
//                                } else {
//
//                                    Select_City(State_id);
//                                }
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//                            }
//                        });
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                return map;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(Create_New_Account_Activity.this);
//        requestQueue.add(stringRequest);
//    }

//    private void Select_City(String state_id) {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"user/getCities/" + state_id,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("City list", response);
//                        try {
//                            JSONObject json = new JSONObject(response);
//                            Log.e("Response for City", json.toString());
//                            if (json != null) {
//
//                                if (json.has("success")) {
//
//                                    String success = json.optString("success");
//                                    if (success.equals("true")) {
//                                        JSONArray jsonMainNode = json.optJSONArray("states");
//                                        int lengthJsonArr = jsonMainNode.length();
//                                        list_city.clear();
//                                        for (int i = 0; i <= lengthJsonArr; i++) {
//                                            CityDetail_Modal cityDetail_modal = new CityDetail_Modal();
//                                            if (i == 0) {
//                                                cityDetail_modal.setCity_id("0");
//                                                cityDetail_modal.setCity_name("Select City");
//                                            } else {
//                                                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i - 1);
//                                                String id = jsonChildNode.optString("id").toString();
//                                                String cities_id = jsonChildNode.optString("cities_id").toString();
//                                                String name = jsonChildNode.optString("name").toString();
//                                                System.out.println("Country_id --- >" + cities_id + " " + "\nstate_name----->>> " + name);
//                                                cityDetail_modal.setCity_id(cities_id);
//                                                cityDetail_modal.setCity_name(name);
//                                                cityDetail_modal.setId(id);
//                                            }
//                                            list_city.add(cityDetail_modal);
//
//                                        }
//
//                                    } else {
//                                        Toast.makeText(Create_New_Account_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        City_NameSpinnerAdapter center_nameSpinnerAdapter = new City_NameSpinnerAdapter(getApplicationContext(), list_city);
//                        spinner_city.setAdapter(center_nameSpinnerAdapter);
//                        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                state_Name = parent.getItemAtPosition(position).toString();
//                                State_id = (list_city.get(position).getId());
//                                city = list_city.get(position).getCity_id();
//                                Search_city = list_city.get(position).getCity_name();
//                                if (State_id == "Select Center") {
//                                    System.out.println("Center id For Filter-- >>>> " + State_id);
//                                } else {
//                                    // FilterMethodCenter("State");
//
//                                }
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//                            }
//                        });
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                return map;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(Create_New_Account_Activity.this);
//        requestQueue.add(stringRequest);
//    }


    public class Category_NameSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private Context activity;
        private ArrayList<AllCategory_Modal> asr;

        public Category_NameSpinnerAdapter(Context context, ArrayList<AllCategory_Modal> type_list) {
            this.asr = type_list;
            activity = context;
        }

        public int getCount() {
            return asr.size();
        }

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(Create_New_Account_Activity.this);
            txt.setPadding(12, 12, 12, 12);
            txt.setTextSize(16);
            txt.setGravity(Gravity.LEFT);
            txt.setText(asr.get(position).getName());
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(Create_New_Account_Activity.this);
            txt.setGravity(Gravity.LEFT);
            txt.setPadding(10, 10, 10, 10);
            txt.setTextSize(12);

            //  txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
            txt.setText(asr.get(i).getName());
            txt.setTextColor(Color.parseColor("#FFFFFF"));
            return txt;
        }


    }

//
//    public class Country_NameSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
//
//        private Context activity;
//        private ArrayList<Country_Detail_Modal> asr;
//
//        public Country_NameSpinnerAdapter(Context context, ArrayList<Country_Detail_Modal> type_list) {
//            this.asr = type_list;
//            activity = context;
//        }
//
//        public int getCount() {
//            return asr.size();
//        }
//
//        public Object getItem(int i) {
//
//            try {
//
//            } catch (Exception e) {
//            }
//            return asr.get(i);
//
//        }
//
//        public long getItemId(int i) {
//            return (long) i;
//        }
//
//        @Override
//        public View getDropDownView(int position, View convertView, ViewGroup parent) {
//            TextView txt = new TextView(Create_New_Account_Activity.this);
//            txt.setPadding(12, 12, 12, 12);
//            txt.setTextSize(16);
//            txt.setGravity(Gravity.LEFT);
//            txt.setText(asr.get(position).getCountries_Name());
//            txt.setTextColor(Color.parseColor("#000000"));
//            return txt;
//        }
//
//        public View getView(int i, View view, ViewGroup viewgroup) {
//            TextView txt = new TextView(Create_New_Account_Activity.this);
//            txt.setGravity(Gravity.LEFT);
//            txt.setPadding(10, 10, 10, 10);
//            txt.setTextSize(12);
//
//            //  txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
//            try {
//                txt.setText(asr.get(i).getCountries_Name());
//            } catch (Exception e) {
//            }
//
//            txt.setTextColor(Color.parseColor("#FFFFFF"));
//            return txt;
//        }
//
//
//    }
//
//    public class State_NameSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
//
//        private Context activity;
//        private ArrayList<State_Modal> asr;
//
//        public State_NameSpinnerAdapter(Application context, ArrayList<State_Modal> type_list) {
//            this.asr = type_list;
//            activity = context;
//        }
//
//        public int getCount() {
//            return asr.size();
//        }
//
//        public Object getItem(int i) {
//            return asr.get(i);
//        }
//
//        public long getItemId(int i) {
//            return (long) i;
//        }
//
//        @Override
//        public View getDropDownView(int position, View convertView, ViewGroup parent) {
//            TextView txt = new TextView(Create_New_Account_Activity.this);
//            txt.setPadding(12, 12, 12, 12);
//            txt.setTextSize(16);
//            txt.setGravity(Gravity.LEFT);
//            txt.setText(asr.get(position).getState_name());
//            txt.setTextColor(Color.parseColor("#000000"));
//            return txt;
//        }
//
//        public View getView(int i, View view, ViewGroup viewgroup) {
//            TextView txt = new TextView(Create_New_Account_Activity.this);
//            txt.setGravity(Gravity.LEFT);
//            txt.setPadding(10, 10, 10, 10);
//            txt.setTextSize(12);
//            //  txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
//            txt.setText(asr.get(i).getState_name());
//            txt.setTextColor(Color.parseColor("#FFFFFF"));
//            return txt;
//        }
//
//    }
//
//    public class City_NameSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
//
//        private Context activity;
//        private ArrayList<CityDetail_Modal> asr;
//
//        public City_NameSpinnerAdapter(Context context, ArrayList<CityDetail_Modal> type_list) {
//            this.asr = type_list;
//            activity = context;
//        }
//
//        public int getCount() {
//            return asr.size();
//        }
//
//        public Object getItem(int i) {
//            return asr.get(i);
//        }
//
//        public long getItemId(int i) {
//            return (long) i;
//        }
//
//        @Override
//        public View getDropDownView(int position, View convertView, ViewGroup parent) {
//            TextView txt = new TextView(Create_New_Account_Activity.this);
//            txt.setPadding(12, 12, 12, 12);
//            txt.setTextSize(16);
//            txt.setGravity(Gravity.LEFT);
//            txt.setText(asr.get(position).getCity_name());
//            txt.setTextColor(Color.parseColor("#000000"));
//            return txt;
//        }
//
//        public View getView(int i, View view, ViewGroup viewgroup) {
//            TextView txt = new TextView(Create_New_Account_Activity.this);
//            txt.setGravity(Gravity.LEFT);
//            txt.setPadding(10, 10, 10, 10);
//            txt.setTextSize(12);
//            //  txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
//            txt.setText(asr.get(i).getCity_name());
//            txt.setTextColor(Color.parseColor("#FFFFFF"));
//            return txt;
//        }
//
//    }

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

    private void All_Depndency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
        /* Get values from Intent */
        intent = getIntent();
        Register_Type = intent.getStringExtra("KEY_Register_Type");



    }

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(this);
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
    }

    private void getLatLongFromAddress(String address) {
        latitude = 0.0;
        longitude = 0.0;

        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                double lat = (int) (addresses.get(0).getLatitude() * 1E6);
                double lon = (int) (addresses.get(0).getLongitude() * 1E6);
                latitude = lat / 1E6;
                longitude = lon / 1E6;
                Log.d("Latitude", "" + latitude);
                Log.d("Longitude", "" + longitude);
                System.out.println("Latitude From Address  ---" + latitude);
                System.out.println("Longitude From Address ---" + longitude);
                if (latitude != 0) {
                    // Centerlatlog_Detail();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


