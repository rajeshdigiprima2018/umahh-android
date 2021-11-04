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
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import Util.ConnectionDetector;
import Util.Constant;
import Util.SessionManager;

/**
 * Created by Dell on 14-04-2019.
 */

public class Education_DetailActivity extends AppCompatActivity implements View.OnClickListener {

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;
    /* Get values from Intent */
    Intent intent;
    String Education_id;
    TextView Tv_Course_Objective, Tv_Duration, Tv_Methodology, Tv_Registration, Tv_Pre_requisites, Tv_About_the_Instructor, TV_Address, Tv_Mobile;

    TextView Tv_Date, TV_time,Tv_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.education_detail_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        Mosque_All_Education();
        Click_Event();
    }


    @Override
    public void onBackPressed() {
//        if(getIntent().getBooleanExtra("finish_onback", false))
//        {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

//        }
//        else {
//            Intent intent = new Intent(Education_DetailActivity.this, Education_Activity.class);
//            intent.putExtra("id", getIntent().getStringExtra("id"));
//            startActivity(intent);
//            finish();
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//        }
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"mosque/education/get/" + Education_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Education Detail", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Education Detail", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        // modalList.clear();
                                        JSONObject jsonMainNode = json.optJSONObject("data");
//
                                        String mosque_id = jsonMainNode.optString("mosque_id").toString();
                                        String startDate = jsonMainNode.optString("startDate").toString();
                                        String endDate = jsonMainNode.optString("endDate").toString();
                                        String startTime = jsonMainNode.optString("startTime").toString();
                                        String endTime = jsonMainNode.optString("endTime").toString();
                                        String course_objective = jsonMainNode.optString("course_objective").toString();
                                        String methodology = jsonMainNode.optString("methodology").toString();
                                        String duration = jsonMainNode.optString("duration").toString();
                                        String registration_fee = jsonMainNode.optString("registration_fee").toString();
                                        String pre_requisites = jsonMainNode.optString("pre_requisites").toString();
                                        String about_instructor = jsonMainNode.optString("about_instructor").toString();
                                        String createdAt = jsonMainNode.optString("createdAt").toString();
                                        String education_id = jsonMainNode.optString("education_id").toString();
                                        String id = jsonMainNode.optString("id").toString();
                                        String mobile = jsonMainNode.optString("mobile").toString();
                                        String address = jsonMainNode.optString("address").toString();
                                        String title = jsonMainNode.optString("title").toString();


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
                                        System.out.println("outputDateStr---->>>  " + outputDateStr);
                                        /////
                                        DateFormat outputFormat_end = new SimpleDateFormat("dd MMM, yyyy");
                                        String inputDateEnd = endDate;
                                        Date dateEnd = null;
                                        try {
                                            dateEnd = inputFormat.parse(inputDateEnd);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        String outputDateEnd = outputFormat_end.format(dateEnd);
                                        System.out.println("outputDateEnd99999---->>>  " + outputDateEnd);

                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                            String course_objective_html = Html.fromHtml(course_objective, Html.FROM_HTML_MODE_LEGACY).toString();
                                            String methodology_html = Html.fromHtml(methodology, Html.FROM_HTML_MODE_LEGACY).toString();
                                            String registration_fee_html = Html.fromHtml(registration_fee, Html.FROM_HTML_MODE_LEGACY).toString();
                                            String pre_requisites_html = Html.fromHtml(pre_requisites, Html.FROM_HTML_MODE_LEGACY).toString();
                                            String about_instructor_html = Html.fromHtml(about_instructor, Html.FROM_HTML_MODE_LEGACY).toString();
                                            String duration_html = Html.fromHtml(duration, Html.FROM_HTML_MODE_LEGACY).toString();

                                            System.out.println("value--1-------------------->>>  " + course_objective_html);

                                            Tv_Course_Objective.setText(course_objective_html);
                                            Tv_Duration.setText(duration_html);
                                            Tv_Methodology.setText(methodology_html);
                                            Tv_Registration.setText(registration_fee_html);
                                            Tv_Pre_requisites.setText(pre_requisites_html);
                                            Tv_About_the_Instructor.setText(about_instructor_html);
                                        } else {
                                            String course_objective_html = Html.fromHtml(course_objective).toString();
                                            System.out.println("value---2------------------->>>  " + course_objective_html);
                                            Tv_Course_Objective.setText(course_objective_html);
                                        }
                                        Tv_title.setText(title);
                                        TV_Address.setText(address);
                                        Tv_Mobile.setText(mobile);
                                        Tv_Date.setText(outputDateStr + "  to  " + outputDateEnd);
                                        TV_time.setText(outputDateStr_start + "  -  " + outputDateStr_end);
                                        hideProgressDialog();


                                    } else {
                                        Toast.makeText(Education_DetailActivity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                             /*   if (modalList.size() > 0) {
                                    board_adapter = new Education_Adapter(Education_Activity.this, modalList);
                                    recyclerView.setAdapter(board_adapter);
                                    board_adapter.notifyDataSetChanged();
                                }*/
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
        RequestQueue requestQueue = Volley.newRequestQueue(Education_DetailActivity.this);
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
        //  modalList = new ArrayList<>();
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
        intent = getIntent();
        Education_id = intent.getStringExtra("KEY_Education");
    }

    private void Click_Event() {

        Iv_back.setOnClickListener(this);
    }

    private void Find_Element() {
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Tv_Course_Objective = (TextView) findViewById(R.id.Tv_Course_Objective);
        Tv_Duration = (TextView) findViewById(R.id.Tv_Duration);
        Tv_Methodology = (TextView) findViewById(R.id.Tv_Methodology);
        Tv_Registration = (TextView) findViewById(R.id.Tv_Registration);
        Tv_Pre_requisites = (TextView) findViewById(R.id.Tv_Pre_requisites);
        Tv_About_the_Instructor = (TextView) findViewById(R.id.Tv_About_the_Instructor);
        TV_Address = (TextView) findViewById(R.id.TV_Address);
        Tv_Mobile = (TextView) findViewById(R.id.Tv_Mobile);
        Tv_Date = (TextView) findViewById(R.id.Tv_Date);
        TV_time = (TextView) findViewById(R.id.TV_time);
        Tv_title = (TextView) findViewById(R.id.Tv_title);
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
