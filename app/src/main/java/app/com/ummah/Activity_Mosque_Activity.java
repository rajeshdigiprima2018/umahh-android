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

import Adapter.Activity_adapter;
import Modal.Activity_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 14-04-2019.
 */

public class Activity_Mosque_Activity extends AppCompatActivity implements View.OnClickListener {


    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;
    SessionManager session;


    RecyclerView recyclerView;
    Activity_adapter education_adapter;
    public ArrayList<Activity_Modal> modalList;

    /*activity_mosque_activity*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosque_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        Mosque_Activity_Education();
        Recycle_view();
        Click_Event();
    }


    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Activity_Mosque_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String Activity_id = modalList.get(position).getActivity_id();
                        Intent intent = new Intent(Activity_Mosque_Activity.this, Activity_detail_Activity.class);
                        intent.putExtra("KEY_Activity_id", Activity_id);
                        intent.putExtra("finish_onback", getIntent().getBooleanExtra("finish_onback", false));
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    @Override
    public void onBackPressed() {

        if(getIntent().getBooleanExtra("finish_onback", false))
        {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else {

            Intent intent = new Intent(Activity_Mosque_Activity.this, Mousque_detail_activity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }


    private void Mosque_Activity_Education() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"mosque/Activities/getAll/" +getIntent().getStringExtra("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Activity list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Activity List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();
                                        int j;
                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Activity_Modal List_modal = new Activity_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String mosque_id = jsonChildNode.optString("mosque_id").toString();
                                            String startDate = jsonChildNode.optString("startDate").toString();
                                            String startTime = jsonChildNode.optString("startTime").toString();
                                            String title = jsonChildNode.optString("title").toString();
                                            String phone = jsonChildNode.optString("phone").toString();
                                            String activity_id = jsonChildNode.optString("activity_id").toString();
                                            String id = jsonChildNode.optString("id").toString();
                                            String avtar = jsonChildNode.optString("avtar").toString();

                                            JSONArray pictures = jsonChildNode.optJSONArray("pictures");
                                            int lengthJsonArr_pictures = pictures.length();

                                            for (j = 0; j < lengthJsonArr_pictures; j++) {
                                                JSONObject jsonChildNode_c = pictures.getJSONObject(j);
                                              String  pic_id = jsonChildNode_c.optString("_id").toString();
                                                String  url = jsonChildNode_c.optString("url").toString();
                                              //  System.out.println("url---->>> "+ url+ "    id--->>> "+i);
                                            }
                                            String textarea = jsonChildNode.optString("textarea").toString();
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                String textarea_html = Html.fromHtml(textarea, Html.FROM_HTML_MODE_LEGACY).toString();
                                                List_modal.setTextarea(textarea_html);
                                            } else {
                                                String textarea_html = Html.fromHtml(textarea).toString();
                                                List_modal.setTextarea(textarea_html);
                                            }
                                            List_modal.setUrl(avtar);
                                            System.out.println("url---->>> "+ avtar);
                                            List_modal.setMosque_id(mosque_id);
                                            List_modal.setStartDate(startDate);
                                            List_modal.setStartTime(startTime);
                                            List_modal.setTitle(title);
                                            List_modal.setPhone(phone);
                                            List_modal.setActivity_id(activity_id);
                                            List_modal.setId(id);
                                            modalList.add(List_modal);


                                        }

                                    } else {
                                        Toast.makeText(Activity_Mosque_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                                if (modalList.size() > 0) {
                                    education_adapter = new Activity_adapter(Activity_Mosque_Activity.this, modalList);
                                    recyclerView.setAdapter(education_adapter);
                                    education_adapter.notifyDataSetChanged();
                                    hideProgressDialog();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_Mosque_Activity.this);
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
        Iv_back =(ImageView)findViewById(R.id.Iv_back);
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
