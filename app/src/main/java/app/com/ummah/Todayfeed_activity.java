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

import Adapter.TodayFeed_Adapter;
import Modal.TodayFeed_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 26-03-2019.
 */

public class Todayfeed_activity extends AppCompatActivity implements View.OnClickListener {

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView IV_back;

    RecyclerView recyclerView;
    TodayFeed_Adapter board_adapter;
    public ArrayList<TodayFeed_Modal> modalList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_feed_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        API();
        Recycle_view();
        SetData();
        Clicklistener();
    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Todayfeed_activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
/*
                        HajjumrahCategory_id = modalList.get(position).getHajjumrahCategory_id();
                        Name = modalList.get(position).getName();
                        //Name = modalList.get(position).getName();
                        ImageUrl = "http://167.172.131.53:4002"+modalList.get(position).getImageUrl();
                        Intent intent = new Intent(Hajj_and_Umrah_Activity.this, Hajj_activity.class);
                        intent.putExtra("HajjumrahCategory_id", HajjumrahCategory_id);
                        intent.putExtra("ImageUrl", ImageUrl);
                        intent.putExtra("Name", Name);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);*/

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
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

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(this);
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
    }
    private void Clicklistener() {

        IV_back.setOnClickListener(this);
    }

    private void SetData() {
        
    }

    private void API() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"todaysFeed/getAll",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("todaysFeed list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("todaysFeed List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            TodayFeed_Modal List_modal = new TodayFeed_Modal();
                                            JSONObject jsonChildNode2 = jsonMainNode.getJSONObject(i);
                                            String startDate = jsonChildNode2.optString("startDate").toString();
                                            String startTime = jsonChildNode2.optString("startTime").toString();
                                            String title = jsonChildNode2.optString("title").toString();
                                            String phone = jsonChildNode2.optString("phone").toString();
                                            String activity_id = jsonChildNode2.optString("activity_id").toString();
                                            String id = jsonChildNode2.optString("id").toString();

                                            JSONObject mosque_id = jsonChildNode2.optJSONObject("mosque_id");
                                            try {
                                                String _id = mosque_id.optString("_id").toString();
                                                String username = mosque_id.optString("username").toString();
                                                String avtar = mosque_id.optString("avtar").toString();
                                                List_modal.setUsername(username);
                                                List_modal.setMosque_id(_id);
                                                List_modal.setAvtar(avtar);
                                            }catch (Exception e){}



                                            String likecommunity_id2 = jsonChildNode2.optString("likeactivity_id").toString();
                                            if (likecommunity_id2.equals("null")){
                                                List_modal.setLikeCounter("10");
                                            }
                                            else {
                                                JSONObject jsonObject = jsonChildNode2.getJSONObject("likeactivity_id");
                                                String likeCounter = jsonObject.optString("likeCounter").toString();
                                                String likecommunity_id = jsonObject.optString("likeactivity_id").toString();
                                                try {
                                                    JSONArray itemArray=jsonObject.getJSONArray("likeUser");
                                                    for (int j = 0; j < itemArray.length(); j++) {
                                                        String value=itemArray.getString(j);
                                                        Log.e("json--------->>>>>", j+"="+value);
                                                        if (session.checkEntered()) {
                                                            if ((session.getuserinfo().get(Constant.SHARED_PREFERENCE_USER_ID_KEY)).equals(value)) {
                                                                System.out.println("User Id  Like---->>>  " + value);
                                                                List_modal.setLike_type("Like");
                                                            } else {
                                                                System.out.println("User Id unlike---->>>  " + value);
                                                                List_modal.setLike_type("Unlike");
                                                            }
                                                        }

                                                    }
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }


                                                List_modal.setLikecommunity_id(likecommunity_id);
                                                List_modal.setLikeCounter(likeCounter);

                                            }


                                            JSONArray pictures = jsonChildNode2.optJSONArray("pictures");
                                            int lengthJsonArr_pictures = pictures.length();

                                            for (int j = 0; j < lengthJsonArr_pictures; j++) {
                                                JSONObject jsonChildNode_c = jsonMainNode.getJSONObject(j);
                                                String pic_id = jsonChildNode_c.optString("_id").toString();
                                                String url = jsonChildNode_c.optString("url").toString();
                                                List_modal.setPictures_id(pic_id);
                                                List_modal.setUrl(url);

                                            }

                                            String avtar_feed = jsonChildNode2.optString("avtar").toString();
                                            String textarea = jsonChildNode2.optString("textarea").toString();
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                String textarea_html = Html.fromHtml(textarea, Html.FROM_HTML_MODE_LEGACY).toString();
                                                List_modal.setTextarea(textarea_html);
                                            } else {
                                                String textarea_html = Html.fromHtml(textarea).toString();
                                                List_modal.setTextarea(textarea_html);
                                            }

                                            List_modal.setAvtar_feed(avtar_feed);
                                            List_modal.setStartDate(startDate);
                                            List_modal.setStartTime(startTime);
                                            List_modal.setTitle(title);
                                            List_modal.setPhone(phone);
                                            List_modal.setActivity_id(activity_id);
                                            List_modal.setId(id);

                                            modalList.add(List_modal);

                                        }

                                        if (modalList.size() > 0) {
                                            board_adapter = new TodayFeed_Adapter(Todayfeed_activity.this, modalList,(session.getuserinfo().get(Constant.SHARED_PREFERENCE_USER_ID_KEY)));
                                            recyclerView.setAdapter(board_adapter);
                                            board_adapter.notifyDataSetChanged();
                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(Todayfeed_activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Todayfeed_activity.this);
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

    private void Find_element() {

        IV_back = (ImageView)findViewById(R.id.IV_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.IV_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Todayfeed_activity.this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
