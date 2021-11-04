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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import Adapter.Education_Adapter;
import Modal.Education_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 12-04-2019.
 */

public class Education_Activity extends AppCompatActivity implements View.OnClickListener {


    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;

    RecyclerView recyclerView;
    Education_Adapter education_adapter;
    public ArrayList<Education_Modal> modalList;
    String Education_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.education_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        Mosque_All_Education();
        Recycle_view();
        Click_Event();
    }


    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Education_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Education_id = modalList.get(position).getEducation_id();
                        // DriverDetailDialog(driver_id);
                        Intent intent = new Intent(Education_Activity.this, Education_DetailActivity.class);
                        intent.putExtra("KEY_Education", Education_id);
                        intent.putExtra("id", getIntent().getStringExtra("id"));
                        intent.putExtra("finish_onback", getIntent().getBooleanExtra("finish_onback", false));
                        startActivity(intent);
                       // finish();
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

        if(getIntent().getBooleanExtra("finish_onback", false))
        {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else {
            Intent intent = new Intent(Education_Activity.this, Mousque_detail_activity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

    private void Mosque_All_Education() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"mosque/education/getAll/" + getIntent().getStringExtra("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Education list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Education List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Education_Modal cabList_modal = new Education_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String mosque_id = jsonChildNode.optString("_id").toString();
                                            String title = jsonChildNode.optString("title").toString();
                                            String startDate = jsonChildNode.optString("startDate").toString();
                                            String endDate = jsonChildNode.optString("endDate").toString();
                                            String startTime = jsonChildNode.optString("startTime").toString();
                                            String endTime = jsonChildNode.optString("endTime").toString();
                                            String course_objective = jsonChildNode.optString("course_objective").toString();
                                            String methodology = jsonChildNode.optString("methodology").toString();
                                            String duration = jsonChildNode.optString("duration").toString();
                                            String registration_fee = jsonChildNode.optString("registration_fee").toString();
                                            String pre_requisites = jsonChildNode.optString("pre_requisites").toString();
                                            String about_instructor = jsonChildNode.optString("about_instructor").toString();
                                            String createdAt = jsonChildNode.optString("createdAt").toString();
                                            String education_id = jsonChildNode.optString("education_id").toString();
                                            String id = jsonChildNode.optString("id").toString();

                                            // Code for Start time....Start
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
                                            //Code end for Start time...

                                            // Code for End time....Start
                                            StringTokenizer tk_end = new StringTokenizer(endTime);
                                            String day_end = tk_end.nextToken();
                                            String month_end = tk_end.nextToken();
                                            String date_end = tk_end.nextToken();
                                            String year_end = tk_end.nextToken();
                                            String hour_end = tk_end.nextToken();
                                            DateFormat inputFormat_endTime = new SimpleDateFormat("hh:mm:ss");
                                            DateFormat outputFormat_endTime = new SimpleDateFormat("hh:mm a");
                                            String inputDateStr_endTime = hour_end;
                                            Date Start_endTime = null;
                                            try {
                                                Start_endTime = inputFormat_endTime.parse(inputDateStr_endTime);

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            String outputDateStr_end = outputFormat_endTime.format(Start_endTime);
                                            System.out.println("End time --->  " + outputDateStr_end);
                                            //Code end for End time...

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

                                           /* Code for end Date*/
                                            DateFormat outputFormat_end = new SimpleDateFormat("dd MMM, yyyy");
                                            String inputDateEnd = endDate;
                                            Date dateEnd = null;
                                            try {
                                                dateEnd = inputFormat.parse(inputDateEnd);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            String outputDateEnd = outputFormat_end.format(dateEnd);

                                           /* Code for Remove html*/
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                String s = Html.fromHtml(course_objective, Html.FROM_HTML_MODE_LEGACY).toString();
                                            } else {
                                                String s = Html.fromHtml(course_objective).toString();
                                            }
                                            cabList_modal.setTitle(title);
                                            cabList_modal.setMosque_id(mosque_id);
                                            cabList_modal.setStartDate(outputDateStr);
                                            cabList_modal.setEndDate(outputDateEnd);
                                            cabList_modal.setStartTime(outputDateStr_start);
                                            cabList_modal.setEndTime(outputDateStr_end);
                                            cabList_modal.setCourse_objective(course_objective);
                                            cabList_modal.setMethodology(methodology);
                                            cabList_modal.setDuration(duration);
                                            cabList_modal.setRegistration_fee(registration_fee);
                                            cabList_modal.setPre_requisites(duration);
                                            cabList_modal.setAbout_instructor(about_instructor);
                                            cabList_modal.setPre_requisites(pre_requisites);
                                            cabList_modal.setCreatedAt(createdAt);
                                            cabList_modal.setEducation_id(education_id);
                                            cabList_modal.setId(id);
                                            modalList.add(cabList_modal);
                                            hideProgressDialog();
                                        }


                                    } else {
                                        Toast.makeText(Education_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                                if (modalList.size() > 0) {
                                    education_adapter = new Education_Adapter(Education_Activity.this, modalList);
                                    recyclerView.setAdapter(education_adapter);
                                    education_adapter.notifyDataSetChanged();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Education_Activity.this);
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

        switch (v.getId()) {

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