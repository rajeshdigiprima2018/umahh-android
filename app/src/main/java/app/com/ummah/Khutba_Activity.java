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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import Adapter.Khutba_Adapter;
import Modal.Khutba_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 16-04-2019.
 */

public class Khutba_Activity extends AppCompatActivity implements View.OnClickListener {

    ImageView Iv_back;
    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;

    RecyclerView recyclerView;
    Khutba_Adapter khutba_adapter;
    public ArrayList<Khutba_Modal> modalList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.khutba_activity);
        Find_Element();
        Click_Event();
        All_Depndency();
        ProgressBar_Function();
        Khutba_Detail();
        Recycle_view();
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
    private void Khutba_Detail() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"mosque/khutba/getAll/" + getIntent().getStringExtra("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("khutba list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("khutba List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();
                                        int j;
                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Khutba_Modal List_modal = new Khutba_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String mosque_id = jsonChildNode.optString("mosque_id").toString();
                                            String startDate = jsonChildNode.optString("startDate").toString();
                                            String startTime = jsonChildNode.optString("startTime").toString();
                                            String title = jsonChildNode.optString("title").toString();
                                            String phone = jsonChildNode.optString("phone").toString();
                                            String khutba_id = jsonChildNode.optString("activity_id").toString();
                                            String id = jsonChildNode.optString("id").toString();
                                            String speaker_name = jsonChildNode.optString("speaker_name").toString();

                                            StringTokenizer tk = new StringTokenizer(startTime);
                                            String day = tk.nextToken();
                                            String month = tk.nextToken();
                                            String date_ = tk.nextToken();
                                            String year = tk.nextToken();
                                            String hour = tk.nextToken();
                                            DateFormat inputFormat_startTime = new SimpleDateFormat("hh:mm:ss");
                                            DateFormat outputFormat_startTime = new SimpleDateFormat("hh:mm a");
                                            String inputDateStr_startTime = hour;
                                            Date Start_time = null;
                                            try {
                                                Start_time = inputFormat_startTime.parse(inputDateStr_startTime);

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            String outputDateStr_start = outputFormat_startTime.format(Start_time);
                                            System.out.println("Start time --->  " + outputDateStr_start);

                                            /*Code for start time*/
                                            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            DateFormat outputFormat = new SimpleDateFormat("dd MMM");
                                            String inputDateStr = startDate;
                                            Date date = null;
                                            try {
                                                date = inputFormat.parse(inputDateStr);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            String outputDateStr = outputFormat.format(date);

                                            List_modal.setSpeaker_name(speaker_name);
                                            List_modal.setMosque_id(mosque_id);
                                            List_modal.setStartDate(outputDateStr);
                                            List_modal.setStartTime(outputDateStr_start);
                                            List_modal.setTitle(title);
                                            List_modal.setKhutba_id(phone);
                                            List_modal.setKhutba_id(khutba_id);
                                            List_modal.setId(id);

                                            modalList.add(List_modal);
                                            hideProgressDialog();

                                        }

                                    } else {
                                        Toast.makeText(Khutba_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                                if (modalList.size() > 0) {
                                    khutba_adapter = new Khutba_Adapter(Khutba_Activity.this, modalList);
                                    recyclerView.setAdapter(khutba_adapter);
                                    khutba_adapter.notifyDataSetChanged();
                                }
                                hideProgressDialog();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Khutba_Activity.this);
        requestQueue.add(stringRequest);
    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Khutba_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

//                        driver_id = Integer.parseInt((modalList.get(position).getCabId()));
//                        DriverDetailDialog(driver_id);
                      /*  Intent intent = new Intent(All_Mosque_Activity.this, MainActivity.class);
                        startActivity(intent);*/

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
    private void Click_Event() {

        Iv_back.setOnClickListener(this);

    }

    private void Find_Element() {

        Iv_back = (ImageView)findViewById(R.id.Iv_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);



    }


    @Override
    public void onBackPressed() {

        if(getIntent().getBooleanExtra("finish_onback", false))
        {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else {

            Intent intent = new Intent(Khutba_Activity.this, Mousque_detail_activity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));

            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
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
