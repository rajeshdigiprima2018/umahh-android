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
import android.webkit.WebView;
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
 * Created by Dell on 22-04-2019.
 */

public class Supplication_sub_id_Detail_Activity extends AppCompatActivity implements View.OnClickListener {


    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;
    TextView Tv_titel_name;
    WebView tv_descrition1, tv_description2;
    ImageView Iv_Bookmark;

    Bundle bundle;
    String KEY_Supplication_id, KEY_Business_name;
//5cb9d64a9dd5d4236b562f3b
String supCategory_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplication_sub_id_detail_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        SetData();
        API();
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

    private void API() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"supplication/get/"+KEY_Supplication_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Supplication sub detail list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Supplication sub detail List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        JSONObject jsonMainNode = json.optJSONObject("data");

                                         supCategory_id = jsonMainNode.optString("supCategory_id").toString();
                                        String title = jsonMainNode.optString("title").toString();
                                        String description = jsonMainNode.optString("description").toString();
                                        String title_aro = jsonMainNode.optString("title_aro").toString();
                                        String description_aro = jsonMainNode.optString("description_aro").toString();

                                        String createdAt = jsonMainNode.optString("createdAt").toString();
                                        String deletedAt = jsonMainNode.optString("deletedAt").toString();
                                        String updatedAt = jsonMainNode.optString("updatedAt").toString();
                                        String supplication_id = jsonMainNode.optString("supplication_id").toString();
                                        Tv_titel_name.setText(title);
//                                        tv_description2.setText(description);
//                                        tv_descrition1.setText(description_aro);
                                        tv_description2.loadData(description, "text/html; charset=utf-8", "UTF-8");
                                        tv_description2.loadData(description_aro, "text/html; charset=utf-8", "UTF-8");

                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(Supplication_sub_id_Detail_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Supplication_sub_id_Detail_Activity.this);
        requestQueue.add(stringRequest);
    }


    private void All_Depndency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        bundle = getIntent().getExtras();
        KEY_Supplication_id = bundle.getString("id");
        KEY_Business_name = bundle.getString("KEY_Business_name");
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
    }

    private void SetData() {
        // Tv_Business_name.setText(KEY_Business_name);
    }


    private void Clicklistener() {

        Iv_back.setOnClickListener(this);
        Iv_Bookmark.setOnClickListener(this);

    }

    private void Find_element() {
        Tv_titel_name = (TextView) findViewById(R.id.Tv_titel_name);
        tv_descrition1 = (WebView) findViewById(R.id.tv_descrition1);
        tv_description2 = (WebView) findViewById(R.id.tv_description2);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Iv_Bookmark = (ImageView)findViewById(R.id.Iv_Bookmark);
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

        switch (v.getId()){

            case R.id.Iv_back:
                onBackPressed();
                break;

            case R.id.Iv_Bookmark:


                BookMark();
                break;
        }
    }

    private void BookMark() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"supplication/get/"+KEY_Supplication_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("addLikeSupplication", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("addLikeSupplication", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    String message = json.optString("message");

                                    if (success.equals("true")) {
                                        JSONObject jsonMainNode = json.optJSONObject("data");

                                        try {
                                            String supplication_id = jsonMainNode.optString("supplication_id").toString();
                                            String isLiked = jsonMainNode.optString("isLiked").toString();
                                            String likeCounter = jsonMainNode.optString("likeCounter").toString();
                                            String supplicationLike_id = jsonMainNode.optString("supplicationLike_id").toString();
                                            String description_aro = jsonMainNode.optString("description_aro").toString();

                                        }catch (Exception e){}
                                        Toast.makeText(Supplication_sub_id_Detail_Activity.this, message, Toast.LENGTH_LONG).show();
                                        Iv_Bookmark.setImageResource(R.drawable.ic_bookmark_fill);
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(Supplication_sub_id_Detail_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Supplication_sub_id_Detail_Activity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Supplication_sub_id_Detail_Activity.this, Suppication_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
