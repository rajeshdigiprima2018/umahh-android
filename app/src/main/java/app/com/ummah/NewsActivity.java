package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.News_Adapter;
import Modal.New_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 16-04-2019.
 */

public class NewsActivity extends AppCompatActivity implements View.OnClickListener {


    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;
    SessionManager session;


    RecyclerView recyclerView;
    News_Adapter education_adapter;
    public ArrayList<New_Modal> modalList;


    /*activity_mosque_activity*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        Mosque_Activity_News();
        Recycle_view();
        Click_Event();
    }


    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new NewsActivity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String New_id = modalList.get(position).getNews_id();
                        // DriverDetailDialog(driver_id);
                        Intent intent = new Intent(NewsActivity.this, News_Detail_Activity.class);
                        intent.putExtra("KEY_New_id", New_id);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(NewsActivity.this, Mousque_detail_activity.class);

        intent.putExtra("id", getIntent().getStringExtra("id"));
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

    private void Mosque_Activity_News() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"mosque/News/getAll/" + getIntent().getStringExtra("id"),
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
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            New_Modal List_modal = new New_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String mosque_id = jsonChildNode.optString("mosque_id").toString();
                                            String startDate = jsonChildNode.optString("startDate").toString();
                                            String startTime = jsonChildNode.optString("startTime").toString();
                                            String title = jsonChildNode.optString("title").toString();
                                            String byName = jsonChildNode.optString("byName").toString();
                                            String news_id = jsonChildNode.optString("news_id").toString();
                                            String id = jsonChildNode.optString("id").toString();
                                            String avtar = jsonChildNode.optString("avtar").toString();

                                            JSONArray pictures = jsonChildNode.optJSONArray("pictures");
                                            int lengthJsonArr_pictures = pictures.length();

                                            for (int j = 0; j < lengthJsonArr_pictures; j++) {
                                                JSONObject jsonChildNode_c = pictures.getJSONObject(j);
                                                String pic_id = jsonChildNode_c.optString("_id").toString();
                                                String url = jsonChildNode_c.optString("url").toString();
                                                List_modal.setPic_id(pic_id);

                                            }

                                            String textarea = jsonChildNode.optString("textarea").toString();

                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                String textarea_html = Html.fromHtml(textarea, Html.FROM_HTML_MODE_LEGACY).toString();
                                                System.out.println("value--1-------------------->>>  " + textarea_html);
                                                List_modal.setTextarea(textarea_html);
                                            } else {
                                                String textarea_html = Html.fromHtml(textarea).toString();
                                                System.out.println("value---2------------------->>>  " + textarea_html);
                                                List_modal.setTextarea(textarea_html);
                                            }
                                            List_modal.setUrl(avtar);
                                            System.out.println("value---2------------------->>>  " + avtar);
                                            List_modal.setMosque_id(mosque_id);
                                            List_modal.setStartDate(startDate);
                                            List_modal.setStartTime(startTime);
                                            List_modal.setTitle(title);
                                            List_modal.setId(id);
                                            List_modal.setByName(byName);
                                            List_modal.setNews_id(news_id);
                                            modalList.add(List_modal);
                                            hideProgressDialog();

                                        }


                                    } else {
                                        Toast.makeText(NewsActivity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                                if (modalList.size() > 0) {
                                    education_adapter = new News_Adapter(NewsActivity.this, modalList);
                                    recyclerView.setAdapter(education_adapter);
                                    education_adapter.notifyDataSetChanged();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hideProgressDialog();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                               hideProgressDialog();
                        //
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewsActivity.this);
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
        modalList = new ArrayList<>();
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
    }

    private void Click_Event() {

        Iv_back.setOnClickListener(this);
    }

    private void Find_Element() {
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Iv_back:
                onBackPressed();
                break;
        }

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}