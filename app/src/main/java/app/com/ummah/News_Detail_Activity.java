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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Util.ConnectionDetector;
import Util.Constant;
import Util.DownloadImageTask;
import Util.SessionManager;

/**
 * Created by Dell on 27-04-2019.
 */

public class News_Detail_Activity extends AppCompatActivity implements View.OnClickListener {


    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back, Image;
    SessionManager session;
    TextView TV_Description, TV_Title, Tv_by;
    Intent intent;
    String News_id;

    /*activity_mosque_activity*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        New_Detail();
        Click_Event();
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

    private void New_Detail() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"mosque/News/get/" + News_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("News list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("News List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        // modalList.clear();
                                        try {
                                            JSONObject jsonMainNode = json.optJSONObject("data");
                                            String mosque_id = jsonMainNode.optString("mosque_id").toString();
                                            String startDate = jsonMainNode.optString("startDate").toString();
                                            String startTime = jsonMainNode.optString("startTime").toString();
                                            String title = jsonMainNode.optString("title").toString();
                                            String byName = jsonMainNode.optString("byName").toString();
                                            String news_id = jsonMainNode.optString("news_id").toString();
                                            String id = jsonMainNode.optString("id").toString();

                                            JSONArray pictures = jsonMainNode.optJSONArray("pictures");
                                            int lengthJsonArr_pictures = pictures.length();
                                            for (int j = 0; j < lengthJsonArr_pictures; j++) {
                                                JSONObject jsonChildNode_c = pictures.getJSONObject(j);
                                                String pic_id = jsonChildNode_c.optString("_id").toString();
                                                String url = jsonChildNode_c.optString("url").toString();
                                                String image_url = "http://167.172.131.53:4002" + url;
                                                new DownloadImageTask(Image).execute(image_url);
                                            }

                                            String textarea = jsonMainNode.optString("textarea").toString();
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                String textarea_html = Html.fromHtml(textarea, Html.FROM_HTML_MODE_LEGACY).toString();
                                                System.out.println("value--1-------------------->>>  " + textarea_html);
                                                TV_Description.setText(textarea_html);
                                            } else {
                                                String textarea_html = Html.fromHtml(textarea).toString();
                                                System.out.println("value---2------------------->>>  " + textarea_html);
                                                TV_Description.setText(textarea_html);
                                            }
                                            TV_Title.setText(title);
                                            TV_Title.setText(byName);
                                            hideProgressDialog();

                                        } catch (Exception e) {
                                        }


                                    } else {
                                        Toast.makeText(News_Detail_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(News_Detail_Activity.this);
        requestQueue.add(stringRequest);
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
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
        intent = getIntent();
        News_id = intent.getStringExtra("KEY_New_id");
    }

    private void Click_Event() {

        Iv_back.setOnClickListener(this);
    }


    private void Find_Element() {
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Image = (ImageView) findViewById(R.id.Image);
        TV_Description = (TextView) findViewById(R.id.TV_Description);
        Tv_by = (TextView) findViewById(R.id.Tv_by);
        TV_Title = (TextView) findViewById(R.id.TV_Title);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(News_Detail_Activity.this, NewsActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
