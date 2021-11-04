package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
 * Created by Dell on 14-04-2019.
 */

public class Suggestion_activity extends AppCompatActivity implements View.OnClickListener {



    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back,Iv_Post;
    EditText Et_Msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sugestion_activity);
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
        //  modalList = new ArrayList<>();
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
       /* intent = getIntent();
        Education_id = intent.getStringExtra("KEY_Education");*/
    }

    private void Click_Event() {

        Iv_back.setOnClickListener(this);
        /*Iv_Attach.setOnClickListener(this);*/
        Iv_Post.setOnClickListener(this);
    }

    private void Find_Element() {
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        //recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
/*
        Iv_Attach  = (ImageView)findViewById(R.id.Iv_Attach);
*/
        Iv_Post  = (ImageView)findViewById(R.id.Iv_Post);
        Et_Msg = (EditText)findViewById(R.id.Et_Msg);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Iv_back:
                onBackPressed();
                break;
           /* case R.id.Iv_Attach:

                break;*/
            case R.id.Iv_Post:

                Suggestion_Post_Method();
                break;

        }
    }

    private void Suggestion_Post_Method() {

   /*     final HashMap<String, String> map = new HashMap<String, String>();
        map.put("user_id", session.getuserinfo().get(Constant.SHARED_PREFERENCE__id_KEY));
        map.put("mosque_id", session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_id_KEY));
        map.put("text_suggestion", Et_Msg.getText().toString().trim());*/


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url+"mosque/Suggestions/add",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Suggestion Add", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Suggestion Add", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        // modalList.clear();
                                        JSONObject jsonMainNode = json.optJSONObject("data");
//
                                        String _id = jsonMainNode.optString("_id").toString();
                                        String updatedAt = jsonMainNode.optString("updatedAt").toString();
                                        String deletedAt = jsonMainNode.optString("deletedAt").toString();
                                        String createdAt = jsonMainNode.optString("createdAt").toString();
                                        String status = jsonMainNode.optString("status").toString();
                                        String text_suggestion = jsonMainNode.optString("text_suggestion").toString();
                                        String mosque_id = jsonMainNode.optString("mosque_id").toString();
                                        String user_id = jsonMainNode.optString("user_id").toString();

                                        String message = json.optString("message");
                                        Toast.makeText(Suggestion_activity.this, message, Toast.LENGTH_LONG).show();
                                        onBackPressed();

                                    } else {
                                        Toast.makeText(Suggestion_activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
                map.put("mosque_id", getIntent().getStringExtra("id"));
                map.put("text_suggestion", Et_Msg.getText().toString().trim());
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

        Intent intent = new Intent(Suggestion_activity.this, Mousque_detail_activity.class);
        intent.putExtra("id", getIntent().getStringExtra("id"));
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
