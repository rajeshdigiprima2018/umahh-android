package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Util.Constant;
import Util.SessionManager;
import Util.Utility;

/**
 * Created by Abhi on 1/5/2019.
 */

public class ForgetPassword_Activity extends AppCompatActivity implements View.OnClickListener {

    EditText Et_email;
    Button bt_submit;
    ImageView Iv_back;
    private ProgressDialog progress_dialog = null;
    SessionManager session;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        VersionDependency();
        ProgressBar_();
        Find_Element();
        ClickEvent_Fun();
    }

    private void Find_Element() {

        Et_email = (EditText) findViewById(R.id.Et_email);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
    }

    private void ClickEvent_Fun() {

        Iv_back.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
    }

    private void ProgressBar_() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(this);
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
    }

    private void SetData_Fun() {
    }

    private void VersionDependency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        session = new SessionManager(this);
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
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_submit:
                Forget_Password_check();

                break;
            case R.id.Iv_back:
                onBackPressed();
                break;
        }
    }

    private void Forget_Password_check() {

        if (!Utility.eMailValidation((Et_email.getText().toString()))) {
            String msgs = getString(R.string.text_enter_valid_email);
            Et_email.setError(msgs);
            Et_email.requestFocus();
        } else {

            postUsingVolley();
        }
    }



    private void postUsingVolley() {

        showProgressDialog();

        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("action", "forgot_password");
        map.put("email", Et_email.getText().toString());
        map.put("locale", "en");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constant.Base_url+"user/password/forgot", new JSONObject(map),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("Forgot Password ", response.toString());
                        if (response.has("success")) {
                            String success = response.optString("success").toString();
                            if (success.equals("true")) {
                                String message = response.optString("message").toString();
                                if (message.equals("User password was reset successfully and sent via Email to User.")) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ForgetPassword_Activity.this, PasswordUpadate_Activity.class);
                                    intent.putExtra("email", Et_email.getText().toString());
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    hideProgressDialog();
                                }
                            } else if (success.equals("false")) {
                                String message_readable = response.optString("message_readable").toString();
                                String error = response.optString("error").toString();
                                Toast.makeText(getApplicationContext(), message_readable, Toast.LENGTH_LONG).show();
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
    private void ForgetPass_Api() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url+"user/password/forgot",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("Forget Password", response);
                        try {

                            JSONObject json = new JSONObject(response);
                            Log.e("Forget Password", json.toString());
                            if (json != null) {

                                if (json.has("success")) {
                                    String success = json.optString("success").toString();
                                    if (success.equals("true")) {
                                        String message = json.optString("message").toString();
                                        System.out.println("message---->>>  " + message);
                                        if (message.equals("User password was reset successfully and sent via Email to User.")) {

                                            Intent intent = new Intent(ForgetPassword_Activity.this, PasswordUpadate_Activity.class);
                                            startActivity(intent);
                                            finish();
                                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                            hideProgressDialog();
                                        }

                                    } else if (success.equals("false")) {

                                        String error = json.optString("error").toString();
                                        String message_readable = json.optString("message_readable").toString();

                                        Toast.makeText(ForgetPassword_Activity.this, message_readable,
                                                Toast.LENGTH_LONG).show();
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
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("action", "forgot_password");
                map.put("email", Et_email.getText().toString());
                map.put("locale", "en");
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ForgetPassword_Activity.this, Login_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}



