package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import Util.SessionManager;

/**
 * Created by Dell on 03-04-2019.
 */

public class New_Request_Communnity extends AppCompatActivity implements View.OnClickListener {

    ImageView Iv_back;
    EditText Et_text;
    TextView Iv_post;
    private ProgressDialog progress_dialog;

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_request_community_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        Click_Event();

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
       /* bundle = getIntent().getExtras();

        HajjumrahCategory_id = bundle.getString("HajjumrahCategory_id");
        ImageUrl = bundle.getString("ImageUrl");
        Name  = bundle.getString("Name");*/

        //  modalList = new ArrayList<>();
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
    }

    private void Find_Element() {
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Et_text = (EditText) findViewById(R.id.Et_text);
        Iv_post = (TextView) findViewById(R.id.Iv_post);
    }

    private void Click_Event() {

        Iv_back.setOnClickListener(this);
        Iv_post.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Iv_back:

                onBackPressed();
                break;

            case R.id.Iv_post:
                API();
                break;
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

    private void API() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url+"user/Community/add",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Community add list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Community add List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    String message = json.optString("message");
                                    if (success.equals("true")) {
                                        JSONObject jsonChildNode2 = json.optJSONObject("data");

                                        String user_id = jsonChildNode2.optString("user_id").toString();
                                        String likecommunity_id = jsonChildNode2.optString("likecommunity_id").toString();
                                        String description = jsonChildNode2.optString("description").toString();
                                        String createdAt = jsonChildNode2.optString("createdAt").toString();
                                        String community_id = jsonChildNode2.optString("community_id").toString();


                                        Toast.makeText(New_Request_Communnity.this, message, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(New_Request_Communnity.this,Community_Activity.class);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(New_Request_Communnity.this, message, Toast.LENGTH_LONG).show();
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

                map.put("user_id", session.getuserinfo().get(Constant.SHARED_PREFERENCE_USER_ID_KEY));
                map.put("description", Et_text.getText().toString().trim());
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(New_Request_Communnity.this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(New_Request_Communnity.this, Community_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
