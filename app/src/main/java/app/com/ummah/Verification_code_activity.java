package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

import Util.Constant;
import Util.SessionManager;

/**
 * Created by Abhi on 3/11/2019.
 */

public class Verification_code_activity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Button bt_Continue;
    EditText Et_otp_1, Et_otp_2, Et_otp_3, Et_otp_4;
    Button bt_submit;
    ImageView Iv_back;
    private ProgressDialog progress_dialog = null;
    String Otp_value;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_code);
        VersionDependency();
        ProgressBar_();
        Find_element();
        Click_Event();
    }

    private void Click_Event() {

        Et_otp_1.addTextChangedListener(this);
        Et_otp_2.addTextChangedListener(this);
        Et_otp_3.addTextChangedListener(this);
        bt_Continue.addTextChangedListener(this);
        bt_Continue.setOnClickListener(this);
    }

    private void Find_element() {
        Iv_back = (ImageView)findViewById(R.id.Iv_back);
        Et_otp_1 = (EditText) findViewById(R.id.Et_otp_1);
        Et_otp_2 = (EditText) findViewById(R.id.Et_otp_2);
        Et_otp_3 = (EditText) findViewById(R.id.Et_otp_3);
        Et_otp_4 = (EditText) findViewById(R.id.Et_otp_4);
        bt_Continue = (Button) findViewById(R.id.bt_Continue);
        Iv_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_Continue:



                postUsingVolley();
                break;

            case R.id.Iv_back:
                finish();

                break;
        }
    }

    private void postUsingVolley() {

        showProgressDialog();


        final HashMap<String, String> map = new HashMap<String, String>();
        Otp_value = Et_otp_1.getText().toString().trim()+Et_otp_2.getText().toString().trim()+Et_otp_3.getText().toString().trim()+Et_otp_4.getText().toString().trim();

        //   map.put("Otp", Otp_value);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constant.Base_url+"user/sendSmsOtpCheck/"+Otp_value, new JSONObject(map),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("Verification Password ", response.toString());
                        if (response.has("success")) {
                            String success = response.optString("success").toString();
                            if (success.equals("true")) {
                                String status = response.optString("status").toString();
                                if (status.equals("200")) {
                                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Verification_code_activity.this, Login_Activity.class);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    hideProgressDialog();
                                }
                            } else if (success.equals("false")) {
                               // String message_readable = response.optString("message_readable").toString();
                                String data = response.optString("data").toString();
                                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        {
            if (editable.length() == 1) {
                if (Et_otp_1.length() == 1) {
                    Et_otp_2.requestFocus();
                }

                if (Et_otp_2.length() == 1) {
                    Et_otp_3.requestFocus();
                }
                if (Et_otp_3.length() == 1) {
                    Et_otp_4.requestFocus();
                }
            } else if (editable.length() == 0)

            {
                if (Et_otp_4.length() == 0) {
                    Et_otp_3.requestFocus();
                }
                if (Et_otp_3.length() == 0) {
                    Et_otp_2.requestFocus();
                }
                if (Et_otp_2.length() == 0) {
                    Et_otp_1.requestFocus();
                }
            }
        }
    }

    private void VersionDependency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        SessionManager session = new SessionManager(this);
    }

    private void ProgressBar_() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(this);
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




}