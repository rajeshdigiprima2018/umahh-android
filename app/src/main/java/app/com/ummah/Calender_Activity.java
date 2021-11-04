package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import Adapter.Calendar_Adapter;
import Modal.Calendar_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 29-04-2019.
 */

public class Calender_Activity extends AppCompatActivity implements View.OnClickListener {


    SessionManager session;

    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;

    RecyclerView recyclerView;
    ImageView Iv_back;
    Calendar_Adapter education_adapter;
    public ArrayList<Calendar_Modal> modalList = new ArrayList<>();
    public ArrayList<EventDay> eventList = new ArrayList<>();

    public List<Calendar> calendarList = new ArrayList<>();

    //  CalendarView mCalendarView;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    com.applandeo.materialcalendarview.CalendarView calender;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_detail_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        Calender_Detail();
        Recycle_view();
        setData();
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

    private void Calender_Detail() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url + "mosque/Calendar/getAll/" + getIntent().getStringExtra("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Calender list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Calender List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        calendarList.clear();
                                        eventList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Calendar_Modal List_modal = new Calendar_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String calendarDate = jsonChildNode.optString("calendarDate").toString();
                                            String title = jsonChildNode.optString("title").toString();
                                            String calendarTime = jsonChildNode.optString("calendarTime").toString();
                                            String createdAt = jsonChildNode.optString("createdAt").toString();
                                            String deletedAt = jsonChildNode.optString("deletedAt").toString();
                                            String updatedAt = jsonChildNode.optString("updatedAt").toString();
                                            String mosque_id = jsonChildNode.optString("mosque_id").toString();
                                            String Calendar_id = jsonChildNode.optString("Calendar_id").toString();


                                            Calendar cal = Calendar.getInstance();
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                                            SimpleDateFormat dayf = new SimpleDateFormat("EE", Locale.ENGLISH);
                                            SimpleDateFormat monthf = new SimpleDateFormat("MMM", Locale.ENGLISH);
                                            try {
                                                cal.setTime(sdf.parse(calendarDate));
                                                EventDay eventDay = new EventDay(cal, R.drawable.circle_green);
                                                eventList.add(eventDay);
                                                calendarList.add(cal);
                                                List_modal.setHour(cal.get(Calendar.HOUR_OF_DAY) + "");
                                                List_modal.setDay_name(dayf.format(cal.getTime()));
                                                List_modal.setMonth(monthf.format(cal.getTime()));
                                                List_modal.setYear(cal.get(Calendar.YEAR) + "");
                                                List_modal.setDate(cal.get(Calendar.DATE) + "");
                                                List_modal.setCalendarDate(calendarDate);
                                                List_modal.setMosque_id(mosque_id);
                                                List_modal.setCalendarTime(calendarTime);
                                                List_modal.setCalendar_id(Calendar_id);
                                                List_modal.setTitle(title);

                                                modalList.add(List_modal);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }


//                                            String currentString = calendarDate;
//                                            String result = currentString.split("T")[0];
//                                            String[] separated = result.split("-");
////                                            separated[0]; // this will contain "Fruit"
////                                            separated[1]; // this will contain " they taste good"
//                                            separated[0] = separated[0].trim();
//                                            separated[1] = separated[1].trim();
//                                            separated[2] = separated[2].trim();
//
//                                            String selectedDate = separated[2]  + "/" + separated[1]  + "/" + separated[0] ;
//                                            System.out.println("Year--->>  "+separated[0]);
//                                            System.out.println("Month--->>  "+separated[1]);
//                                            System.out.println("Day--->>  "+separated[2]);
//                                            // Code for End time....Start
//                                            StringTokenizer tk_end = new StringTokenizer(calendarTime);
//                                            String day = tk_end.nextToken();
//                                            String month = tk_end.nextToken();
//                                            String date = tk_end.nextToken();
//                                            String year = tk_end.nextToken();
//                                            String hour = tk_end.nextToken();
//                                            System.out.println("Date---->>>  " + date);

//                                            try {
//                                                calender.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate).getTime(), true, true);
//                                            } catch (ParseException e) {
//                                                e.printStackTrace();
//                                            }

                                            calender.setEvents(eventList);
                                            //calender.setSelectedDates(calendarList);


                                        }


                                    } else {
                                        Toast.makeText(Calender_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                                if (modalList.size() > 0) {

                                    education_adapter = new Calendar_Adapter(Calender_Activity.this, modalList);
                                    recyclerView.setAdapter(education_adapter);
                                    education_adapter.notifyDataSetChanged();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Calender_Activity.this);
        requestQueue.add(stringRequest);
    }

    private void Clicklistener() {

        Iv_back.setOnClickListener(this);

        calender.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                if (eventDay != null) {


                    Calendar calc = eventDay.getCalendar();

                    for (int j = 0; j < eventList.size(); j++) {
                        try {
                            String getDate = modalList.get(j).getCalendarDate();
                            String msg = modalList.get(j).getTitle();
                            Calendar cal = eventList.get(j).getCalendar();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                            cal.setTime(sdf.parse(getDate));
                            if (cal.get(Calendar.YEAR) == calc.get(Calendar.YEAR) &&
                                    cal.get(Calendar.MONTH) == calc.get(Calendar.MONTH) &&
                                    cal.get(Calendar.DAY_OF_MONTH) == calc.get(Calendar.DAY_OF_MONTH)) {
//                    System.out.println("CalendarDate--->> " + getDate);
//                    if (selectedDate.equals(getDate)){

                                new AlertDialog.Builder(Calender_Activity.this, R.style.MyDialogTheme)
                                        // .setMessage(msg)
                                        .setMessage(Html.fromHtml("<font color='#FFFFFF'>" + msg + "</font>"))
                                        .setCancelable(false)
                                        /* .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                             public void onClick(DialogInterface dialog, int id) {

                                             }
                                         })*/
                                        .setNegativeButton("Okay", null)
                                        .show();
                                break;
                            }
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }

                    }
                }
            }
        });

    }


    private void setData() {

    }

    private void Find_element() {

        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        calender = (com.applandeo.materialcalendarview.CalendarView) findViewById(R.id.calendarView);
        // String selectedDate = "30/May/2019";

calender.setSelected(true);

//        calender.setForwardButtonImage(getResources().getDrawable(R.drawable.ic_baseline_navigate_next_24));
//        calender.setPreviousButtonImage(getResources().getDrawable(R.drawable.ic_baseline_chevron_left_24));

//        calender.setDate(Calendar.getInstance().getTime().getTime(),
//                true, true);


        // CalendarView calendarView=(CalendarView) findViewById(R.id.calendarView1);


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

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        Intent intentLogin = new Intent(Calender_Activity.this, Mousque_detail_activity.class);
        intentLogin.putExtra("id", getIntent().getStringExtra("id"));
        startActivity(intentLogin);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Calender_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        /*String New_id = modalList.get(position).getNews_id();
                        // DriverDetailDialog(driver_id);
                        Intent intent = new Intent(NewsActivity.this, News_Detail_Activity.class);
                        intent.putExtra("KEY_New_id", New_id);
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

