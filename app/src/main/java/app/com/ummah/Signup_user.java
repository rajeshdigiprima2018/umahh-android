package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Util.ConnectionDetector;
import Util.Constant;
import Util.GPSTracker;
import Util.SessionManager;
import Util.Utility;


public class Signup_user extends AppCompatActivity implements View.OnClickListener {


    ImageView Iv_back;
    EditText Et_email, Et_Phone, Et_pass, Et_Conf_pass, et_user_name;
    Button bt_submit;
    private ProgressDialog progress_dialog = null;

    boolean isPasswordVisible = false;
    SessionManager session;
    LocationManager manager;
    GPSTracker gps;
    Context ct;
    private ConnectionDetector cd;
    private String token = "";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signup);
        All_Depndency();
        FindElement();
        clickEvent();
        ProgressBar_Function();

    }

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(Signup_user.this);
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
    }

    private void All_Depndency() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("MainActivity", "getInstanceId failed", task.getException());
                            return;
                        }

                        token = task.getResult().getToken();

                        Log.d("MainActivity", FirebaseInstanceId.getInstance().getToken());
                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        cd = new ConnectionDetector(getApplicationContext()); // For Internet Check.
        session = new SessionManager(getApplicationContext());
        ct = this;
    }

    private void clickEvent() {


        Iv_back.setOnClickListener(this);
        bt_submit.setOnClickListener(this);

    }

    private void FindElement() {
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Et_email = (EditText) findViewById(R.id.Et_email);
        Et_Phone = (EditText) findViewById(R.id.Et_Phone);
        Et_pass = (EditText) findViewById(R.id.Et_pass);

        Et_Conf_pass = (EditText) findViewById(R.id.Et_Conf_pass);
        bt_submit = (Button) findViewById(R.id.bt_submit);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.Iv_back:

                Intent intent = new Intent(Signup_user.this, UserType_Activity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.bt_submit:
                Register();
                break;
        }
    }

    private void Register() {

        if (et_user_name.getText().toString().length() < 2) {
            et_user_name.setError(getString(R.string.pls_enter_username));
            et_user_name.requestFocus();
        } else if (!Utility.eMailValidation((Et_email.getText().toString()))) {
            String msgs = getString(R.string.text_enter_valid_email);
            Et_email.setError(msgs);
            Et_email.requestFocus();
        } else if (Et_Phone.getText().toString().length() < 12) {
            Et_Phone.setError(getString(R.string.pls_enter_mobile_no));
            Et_Phone.requestFocus();
        } else if (Et_pass.getText().toString().length() == 0) {
            Et_pass.setError(getString(R.string.pls_enter_password));
            Et_pass.requestFocus();
        } else if (Et_Conf_pass.getText().toString().length() == 0) {
            Et_Conf_pass.setError(getString(R.string.confirm_password));
            Et_Conf_pass.requestFocus();
        } else if (!Et_pass.getText().toString().equals(Et_Conf_pass.getText().toString())) {
            Et_Conf_pass.setError(getString(R.string.password_match));
            Et_Conf_pass.requestFocus();
        } else {
            SignUp();
        }
    }


    private void SignUp() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url + "user/register",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        ;
                        Log.e("register", response);
                        try {

                            JSONObject json = new JSONObject(response);
                            Log.e("register ", json.toString());
                            if (json != null) {

                                if (json.has("success")) {
                                    String success = json.optString("success").toString();
                                   /* String imageurl = json.optString("imageurl").toString();
                                    Constant.imageurl = imageurl;
                                    session.SetProfile(imageurl);*/
                                    if (success.equals("true")) {

                                        hideProgressDialog();
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

                        if (error instanceof TimeoutError) {

                            //showSignOutAlertDialog(context, "TimeoutError");
                            Toast.makeText(getApplicationContext(), "TimeoutError", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NoConnectionError) {

                            Toast.makeText(getApplicationContext(), "NoConnectionError", Toast.LENGTH_LONG).show();

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
                }) {

         /*   @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");

                return params;
            }*/


            /*    @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/json");
                    return params;
                }*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("username", et_user_name.getText().toString().trim());
                map.put("email", Et_email.getText().toString().trim());
                map.put("password", Et_pass.getText().toString().trim());
                map.put("mobile", "+" + Et_Phone.getText().toString().trim());
                map.put("isFBUser", "false");
                map.put("role", "user");
                map.put("deviceToken", token);
                map.put("deviceType", "Android");
                return map;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       /* stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                0));*/
        RequestQueue requestQueue = Volley.newRequestQueue(Signup_user.this);
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
}
