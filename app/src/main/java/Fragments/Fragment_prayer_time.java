package Fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import net.time4j.SystemClock;
import net.time4j.calendar.HijriCalendar;
import net.time4j.format.expert.ChronoFormatter;
import net.time4j.format.expert.PatternType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import Adapter.Prayer_time_Adapter;
import Modal.Prayer_Time_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;
import app.com.ummah.MyApps;
import app.com.ummah.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Dell on 17-03-2019.
 */

public class Fragment_prayer_time extends Fragment {


    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;
    TextView location, Tv_calender, time2;
    RecyclerView recyclerView;
    Prayer_time_Adapter prayer_time_adapter;
    public ArrayList<Prayer_Time_Modal> modalList;

    @Nullable

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_prayer_time, container, false);

        All_Depndency();
        ProgressBar_Function();
        Find_Element(view);
        if (session.checkEntered()) {
            Prayer_Time();
        } else {

            Prayer_Time_2();

        }

        HijriCalendar hijriDate =
                SystemClock.inLocalView().today().transform(
                        HijriCalendar.class,
                        HijriCalendar.VARIANT_UMALQURA
                );
        ChronoFormatter<HijriCalendar> hf =
                ChronoFormatter.ofPattern(
                        "dd MMM yyyy", // mmm as given by you would be in minutes in CLDR-standard
                        PatternType.CLDR,
                        Locale.ROOT,
                        HijriCalendar.family()
                );
        System.out.println("Result: " + hf.format(hijriDate));
        time2.setText(hf.format(hijriDate));
        location.setText(MyApps.PLACE);
        // Juz();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
                                                                              @Override
                                                                              public void onReceive(Context context, Intent intent) {
                                                                                  location.setText(intent.getStringExtra("message"));
                                                                              }
                                                                          },
                new IntentFilter("LocationUpdate"));

        Recycle_view();
        return view;

    }

    private void Prayer_Time_2() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url + "mosque/prayer/getAll/5cc2dc2dbc568504a7926ab9",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Prayer_Time list", response);
                        Apireponse(response);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void Find_Element(View view) {
        time2 = view.findViewById(R.id.time2);
        Tv_calender = view.findViewById(R.id.Tv_calender);
        location = view.findViewById(R.id.location);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        String formattedDate2 = null;
        String formattedDate3 = null;
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("DDD:dd:mm");
        SimpleDateFormat outFormat = new SimpleDateFormat("HH");
        try {
            formattedDate2 = outFormat.format(c);
            System.out.println("Current time => " + formattedDate2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd YYYY");
        System.out.println("Date-->> " + dateFormat.format(cal.getTime()));
        Tv_calender.setText(dateFormat.format(cal.getTime()));

    }


    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(getActivity());
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
    }

    private void All_Depndency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        modalList = new ArrayList<>();
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = getActivity();
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

    private boolean upcoming = false;

    private void Apireponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            Log.e("Prayer_Time List", json.toString());
            if (json != null) {
                int upcompingIndex = -1;

                if (json.has("success")) {

                    String success = json.optString("success");
                    if (success.equals("true")) {
                        modalList.clear();
                        JSONArray jsonMainNode = json.optJSONArray("data");
                        int lengthJsonArr = jsonMainNode.length();
                        Calendar tempDate = null;
                        for (int i = 0; i < lengthJsonArr; i++) {
                            Prayer_Time_Modal cabList_modal = new Prayer_Time_Modal();
                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                            String day = jsonChildNode.optString("day").toString();
                            String day_aerobic = jsonChildNode.optString("day_aerobic").toString();
                            String time = jsonChildNode.optString("time").toString();
                            String status = jsonChildNode.optString("status").toString();
                            String createdAt = jsonChildNode.optString("createdAt").toString();
                            String deletedAt = jsonChildNode.optString("deletedAt").toString();
                            String updatedAt = jsonChildNode.optString("updatedAt").toString();
                            String prayer_id = jsonChildNode.optString("prayer_id").toString();
                            String mosque_id = jsonChildNode.optString("mosque_id").toString();

                            // Code for Start time....Start
                            StringTokenizer tk = new StringTokenizer(time);
                            String day1 = tk.nextToken();

                            String month = tk.nextToken();
                            String date_ = tk.nextToken();
                            String year = tk.nextToken();
                            String hour = tk.nextToken();


                            DateFormat cdateformate = new SimpleDateFormat("hh:mm:ss");
                            Calendar CurrentDate = Calendar.getInstance();
                            Calendar PrayerurrentDate = Calendar.getInstance();
                            Calendar PrayerurrentDate_10 = Calendar.getInstance();
                            Calendar PrayerurrentDate_15 = Calendar.getInstance();
                            String[] h = hour.split(":");
//
//                            if (day1.toUpperCase().equals("SUN")) {
//                                PrayerurrentDate.set(Calendar.DAY_OF_WEEK, 1);
//                            } else if (day1.toUpperCase().equals("MON")) {
//                                PrayerurrentDate.set(Calendar.DAY_OF_WEEK, 2);
//                            } else if (day1.toUpperCase().equals("TUE")) {
//                                PrayerurrentDate.set(Calendar.DAY_OF_WEEK, 3);
//                            } else if (day1.toUpperCase().equals("WED")) {
//                                PrayerurrentDate.set(Calendar.DAY_OF_WEEK, 4);
//                            } else if (day1.toUpperCase().equals("THU")) {
//                                PrayerurrentDate.set(Calendar.DAY_OF_WEEK, 5);
//                            } else if (day1.toUpperCase().equals("FRI")) {
//                                PrayerurrentDate.set(Calendar.DAY_OF_WEEK, 6);
//                            } else if (day1.toUpperCase().equals("SAT")) {
//                                PrayerurrentDate.set(Calendar.DAY_OF_WEEK, 7);
//                            }


                            PrayerurrentDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(h[0]));
                            PrayerurrentDate.set(Calendar.MINUTE, Integer.parseInt(h[1]));
                            PrayerurrentDate.set(Calendar.SECOND, Integer.parseInt(h[2]));
                            PrayerurrentDate_10.setTime(PrayerurrentDate.getTime());
                            PrayerurrentDate_10.add(Calendar.MINUTE, 10);

                            PrayerurrentDate_15.setTime(PrayerurrentDate.getTime());
                            PrayerurrentDate_15.add(Calendar.MINUTE, -15);


                            System.out.println("current hours.." +  CurrentDate.get(Calendar.HOUR_OF_DAY) + ":" + CurrentDate.get(Calendar.MINUTE));
                            System.out.println("prayer hours.." +  PrayerurrentDate.get(Calendar.HOUR_OF_DAY) + ":" + PrayerurrentDate.get(Calendar.MINUTE));
                            //System.out.println("prayer hours.." + PrayerurrentDate.get(Calendar.YEAR) + ":" + PrayerurrentDate.get(Calendar.MONTH) + ":" + PrayerurrentDate.get(Calendar.HOUR_OF_DAY) + ":" + PrayerurrentDate.get(Calendar.MINUTE));


                            if (CurrentDate.after(PrayerurrentDate) && CurrentDate.before(PrayerurrentDate_10)) {
                                System.out.println("AFTER==10");
                                cabList_modal.setType(1);
                            }
                            if (CurrentDate.after(PrayerurrentDate_15) && CurrentDate.before(PrayerurrentDate)) {
                                System.out.println("AFTER==15");
                                cabList_modal.setType(2);
                            }
//
//
//
//
//                            else if (PrayerurrentDate.after(CurrentDate)) {
//                                System.out.println("AFTER");
//                                if (tempDate != null) {
//                                    if (PrayerurrentDate.before(tempDate)) {
//                                        upcoming = false;
//                                    }
//                                }
//                                if (!upcoming) {
//                                    upcoming = true;
//                                    tempDate = PrayerurrentDate;
//                                    upcompingIndex = i;
//                                }
//                            } else if (PrayerurrentDate.before(CurrentDate)) {
//                                System.out.println("before");
//                                if (tempDate != null) {
//                                    if (PrayerurrentDate.before(tempDate)) {
//                                        upcoming = false;
//                                    }
//                                }
//                                if (!upcoming) {
//                                    upcoming = true;
//                                    tempDate = PrayerurrentDate;
//                                    upcompingIndex = i;
//                                }
//
//                            }


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
                            cabList_modal.setMosque_id(mosque_id);
                            cabList_modal.setTime(outputDateStr_start);
                            cabList_modal.setDay_aerobic(day_aerobic);
                            cabList_modal.setDay(day);
                            cabList_modal.setPrayer_id(prayer_id);

                            modalList.add(cabList_modal);
                            hideProgressDialog();
                        }


                    } else {
                        Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }
                }

                if (modalList.size() > 0) {
                    if (upcompingIndex != -1) {
                        modalList.get(upcompingIndex).setType(1);
                    }
                    prayer_time_adapter = new Prayer_time_Adapter(getActivity(), modalList);
                    recyclerView.setAdapter(prayer_time_adapter);
                    prayer_time_adapter.notifyDataSetChanged();
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    private void Prayer_Time() {
        upcoming = false;
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url + "mosque/prayer/getAll/" + session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_id_KEY),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Prayer_Time list", response);
                        Apireponse(response);
hideProgressDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    hideProgressDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Fragment_prayer_time.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        /*Education_id = modalList.get(position).getEducation_id();
                        // DriverDetailDialog(driver_id);
                        Intent intent = new Intent(Education_Activity.this, Education_DetailActivity.class);
                        intent.putExtra("KEY_Education", Education_id);
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
}
