package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

/**
 * Created by Abhi on 1/7/2019.
 */

public class PasswordUpadate_Activity extends AppCompatActivity implements View.OnClickListener {

    EditText Et_access_key, Et_New_pass, Et_Confirm_pass;
    Button bt_submit;
    ImageView Iv_back;
    SessionManager session;
    private ProgressDialog progress_dialog = null;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_update);
        VersionDependency();
        ProgressBar_();
        Find_Element();
        ClickEvent_Fun();

    }

    private void Find_Element() {

        Et_access_key = (EditText) findViewById(R.id.Et_access_key);
        Et_New_pass = (EditText) findViewById(R.id.Et_New_pass);
        Et_Confirm_pass = (EditText) findViewById(R.id.Et_Confirm_pass);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);


/*        Et_New_pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (Et_New_pass.getRight() - Et_New_pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (!isPasswordVisible) {
                            Log.v("isVisible", "before If");
                            isPasswordVisible = true;
                            Log.v("isVisible", "inside if");
                            Et_New_pass.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.password_icon, 0, R.drawable.password_hide, 0);
                            Et_New_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        } else {
                            Log.v("isVisible", "inside else ");
                            isPasswordVisible = false;
                            Et_New_pass.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.password_icon, 0, R.drawable.password_eye_01, 0);
                            Et_New_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                        }

                        return true;
                    }
                }
                return false;
            }
        });
        Et_Confirm_pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (Et_Confirm_pass.getRight() - Et_Confirm_pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (!isPasswordVisible) {
                            Log.v("isVisible", "before If");
                            isPasswordVisible = true;
                            Log.v("isVisible", "inside if");
                            Et_Confirm_pass.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.password_icon, 0, R.drawable.password_hide, 0);
                            Et_Confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        } else {
                            Log.v("isVisible", "inside else ");
                            isPasswordVisible = false;
                            Et_Confirm_pass.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.password_icon, 0, R.drawable.password_eye_01, 0);
                            Et_Confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                        }

                        return true;
                    }
                }
                return false;
            }
        });*/

    }

    private void ClickEvent_Fun() {
        bt_submit.setOnClickListener(this);
        Iv_back.setOnClickListener(this);
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

                Update_password();
                break;

            case R.id.Iv_back:
                onBackPressed();
                break;
        }

    }

    private void Update_password() {

        if (Et_New_pass.getText().toString().length() == 0) {
            Et_New_pass.setError("Password not entered");
            Et_New_pass.requestFocus();
        } else if (Et_Confirm_pass.getText().toString().length() == 0) {
            Et_Confirm_pass.setError("Confirm Password");
            Et_Confirm_pass.requestFocus();
        } else if (!Et_New_pass.getText().toString().equals(Et_Confirm_pass.getText().toString())) {
            Et_Confirm_pass.setError("Password not matching");
            Et_Confirm_pass.requestFocus();
        } else {

            postUsingVolley();
        }
    }

    private void postUsingVolley() {

        showProgressDialog();
        Map<String, String> map = new HashMap<String, String>();
        map.put("action", "reset_password");
        map.put("locale", "en");
        map.put("old_password", Et_access_key.getText().toString().trim());
        map.put("new_password", Et_New_pass.getText().toString().trim());
        map.put("email", getIntent().getStringExtra("email"));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constant.PasswordUpdate_url, new JSONObject(map),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject json) {

                        Log.e("Update Password", json.toString());
                        if (json != null) {

                            if (json.has("success")) {

                                String status = json.optString("success").toString();
                                if (json.optBoolean("success")) {
                                    String message = json.optString("success").toString();
                                    System.out.println("message---->>>  " + message);
                                    //   if (message.equals("successfully reset your password")) {


                                    Intent intent = new Intent(PasswordUpadate_Activity.this, Login_Activity.class);
                                    startActivity(intent);
                                    finish();
                                    hideProgressDialog();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    //   }

                                } else if (status.equals("errors")) {
                                    hideProgressDialog();
                                    String message = json.optString("message").toString();
                                    Toast.makeText(PasswordUpadate_Activity.this, message,
                                            Toast.LENGTH_LONG).show();
                                    hideProgressDialog();
                                }

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



    private void Password_Update_Api() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.PasswordUpdate_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // showProgressDialog();
                        Log.e("Update Password", response);
                        try {

                            JSONObject json = new JSONObject(response);
                            Log.e("Update Password", json.toString());
                            if (json != null) {

                                if (json.has("status")) {

                                    String status = json.optString("status").toString();
                                    if (status.equals("success")) {
                                        String message = json.optString("success").toString();
                                        System.out.println("message---->>>  " + message);
                                        //   if (message.equals("successfully reset your password")) {


                                        Intent intent = new Intent(PasswordUpadate_Activity.this, Login_Activity.class);
                                        startActivity(intent);
                                        finish();
                                        hideProgressDialog();
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                        //   }

                                    } else if (status.equals("errors")) {
                                        hideProgressDialog();
                                        String message = json.optString("message").toString();
                                        Toast.makeText(PasswordUpadate_Activity.this, message,
                                                Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Access denied by server.", Toast.LENGTH_LONG).show();
                            hideProgressDialog();
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
                map.put("action", "reset_password");
                map.put("locale", "en");
                map.put("old_password", Et_access_key.getText().toString().trim());
                map.put("new_password", Et_New_pass.getText().toString().trim());
                map.put("email", getIntent().getStringExtra("email"));
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
/*
{
  "action": "reset_password",
  "old_password": "JAaaztme",
  "new_password": "123456",
  "email": "kalpna@mailinator.com",
  "locale": "en"
}
 */
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(PasswordUpadate_Activity.this, Login_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
