package Fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import net.time4j.SystemClock;
import net.time4j.calendar.HijriCalendar;
import net.time4j.format.expert.ChronoFormatter;
import net.time4j.format.expert.PatternType;

import org.json.JSONArray;
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

import Adapter.Juz_Outer_Adapter;
import Adapter.Prayer_time_Adapter;
import Modal.Juz;
import Modal.JuzOuter_Modal;
import Modal.Prayer_Time_Modal;
import Modal.Surah;
import Modal.data;
import Modal.data2;
import Util.ConnectionDetector;
import Util.Constant;
import Util.DownloadImageTask;
import Util.GPSTracker;
import Util.LocationTrack;
import Util.SessionManager;
import app.com.ummah.Activity_Mosque_Activity;
import app.com.ummah.Business_Activity;
import app.com.ummah.Community_Activity;
import app.com.ummah.Education_Activity;
import app.com.ummah.Hajj_and_Umrah_Activity;
import app.com.ummah.Khutba_Activity;
import app.com.ummah.Login_Activity;
import app.com.ummah.Mousque_detail_activity;
import app.com.ummah.MyApps;
import app.com.ummah.Organization_Activity;
import app.com.ummah.Personnange_Activity;
import app.com.ummah.Qibla_Activity;
import app.com.ummah.Quiz_Activity;
import app.com.ummah.R;
import app.com.ummah.Setting_Activity;
import app.com.ummah.Suppication_Activity;
import app.com.ummah.Quran_activity;
import app.com.ummah.Todayfeed_activity;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Belal on 18/09/16.
 */


public class Fragment_home extends Fragment implements View.OnClickListener {

    LinearLayout Lay_login_type, Lay_Today_feed, Lay_Quran, Lay_community, Lay_Setting, Lay_Qiblah, Lay_Quiz, Lay_Personnage, Lay_Supplication, Lay_Hajj, Lay_Zakkat, Lay_Business, Lay_Organise;
    Button Bt_Find_mosque;
    Button Bt_Player_time;
    ImageView Iv_Mosque_detail;
    TextView Tv_calender, Tv_mosque_name, Tv_mosque_address, location, time2;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    SessionManager session;
    LocationManager manager;
    GPSTracker gps;
    Context ct;
    LinearLayout Main_layout, Lay_out_Education, Lay_out_Activity, Lay_out_Khutba;

    private static final String TAG = "Fragment Surah";
//    public static ArrayList<data> List_Sura = new ArrayList<>();
//    public static ArrayList<data2> List_Juz = new ArrayList<>();
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    LocationTrack locationTrack;
    Juz_Outer_Adapter juz_outer_adapter;
    public static ArrayList<JuzOuter_Modal> modalList;
    public ArrayList<Prayer_Time_Modal> prayerList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Main_layout = (LinearLayout) view.findViewById(R.id.Main_layout);
        Tv_calender = (TextView) view.findViewById(R.id.Tv_calender);
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



        int t = Integer.parseInt(formattedDate2);
        if (1 == t || 2 == t || 3 == t || 4 == t) {
            Main_layout.setBackgroundResource(R.drawable.night);
        } else if (5 == t || 6 == t || 7 == t || 8 == t || 9 == t || 10 == t || 11 == t) {
            Main_layout.setBackgroundResource(R.drawable.night);
        } else if (12 == t || 13 == t || 14 == t || 15 == t || 16 == t || 17 == t) {
            Main_layout.setBackgroundResource(R.drawable.afternoon);
        } else if (18 == t || 19 == t) {
            Main_layout.setBackgroundResource(R.drawable.evening);
        } else if (20 == t || 21 == t || 22 == t || 23 == t) {
            Main_layout.setBackgroundResource(R.drawable.night);
        } else {
            Main_layout.setBackgroundResource(R.drawable.night);
        }
        All_Depndency();
        Find_element(view);
        Click_event();
        ProgressBar_Function();
        setData();
//        displayLocationSettingsRequest(ct);
//        API_Sura();
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

        if (session.checkEntered()) {
            Prayer_Time();
        } else {

            Prayer_Time_2();

        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
                                                                              @Override
                                                                              public void onReceive(Context context, Intent intent) {
                                                                                  location.setText(intent.getStringExtra("message"));
                                                                              }
                                                                          },
                new IntentFilter("LocationUpdate"));
        return view;
    }

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(getActivity());
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
    }


//    private void API_Juz() {
//        ApiInterface apiService =
//                ApiClient.getClient().create(ApiInterface.class);
//        Call<Juz> call = apiService.getJuz();
//        call.enqueue(new Callback<Juz>() {
//            @Override
//            public void onResponse(Call<Juz> call, retrofit2.Response<Juz> response) {
//                int statusCode = response.code();
//                List_Juz = (ArrayList<data2>) response.body().getData();
//                //  recyclerView.setAdapter(new Sura_Adapter(List_Sura, R.layout.juz_adapter, getActivity()));
//                System.out.println("Check it--juz->>>>>>> " + response.body().getData());
//            }
//
//            @Override
//            public void onFailure(Call<Juz> call, Throwable t) {
//                // Log error here since request failed
//                Log.e(TAG, t.toString());
//            }
//        });
//    }


    private void All_Depndency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        }
        cd = new ConnectionDetector(getActivity());
        session = new SessionManager(getActivity());
        ct = getActivity();
    }

    private void setData() {
        try {
            Tv_mosque_name.setText(session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_name_KEY));
            Tv_mosque_address.setText(session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_street_address_KEY) + ", " + session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_city_KEY) + ",\n" + session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_state_KEY) + ", " + session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_country_KEY));


            String avtar = session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_avtar_KEY);
            if (avtar.equals("null")) {

                Iv_Mosque_detail.setImageResource(R.drawable.mosque_1);
            } else {
                String Image_url = Constant.Image_Base_url + session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_avtar_KEY);
                new DownloadImageTask(Iv_Mosque_detail).execute(Image_url);
            }

        } catch (Exception e) {
        }
    }

//    private void find_current_date() {
//
//        String dateStr = "04/05/2010";
//
//        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
//        Date dateObj = null;
//        try {
//            dateObj = curFormater.parse(dateStr);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy");
//
//        String newDateStr = postFormater.format(dateObj);
//    }
//
//    private void displayLocationSettingsRequest(Context context) {
//        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
//                .addApi(LocationServices.API).build();
//        googleApiClient.connect();
//
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(10000 / 2);
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
//        builder.setAlwaysShow(true);
//
//        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(LocationSettingsResult result) {
//                final Status status = result.getStatus();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        Log.i(TAG, "All location settings are satisfied.");
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
//
//                        try {
//                            // Show the dialog by calling startResolutionForResult(), and check the result
//                            // in onActivityResult().
//                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
//                        } catch (IntentSender.SendIntentException e) {
//                            Log.i(TAG, "PendingIntent unable to execute request.");
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
//                        break;
//                }
//            }
//        });
//    }
//
//    private void Location_find() {
//
//        locationTrack = new LocationTrack(getActivity());
//        if (locationTrack.canGetLocation()) {
//
//
//            Constant.longitude = String.valueOf(locationTrack.getLongitude());
//            Constant.latitude = String.valueOf(locationTrack.getLatitude());
////
////            System.out.println("Longitude --->>  " + Constant.longitude);
////            System.out.println("latitude --->>  " + Constant.latitude);
//            // Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(Double.parseDouble(Constant.longitude)) + "\nLatitude:" + Double.toString(Double.parseDouble(Constant.latitude)), Toast.LENGTH_SHORT).show();
//        } else {
//
//            locationTrack.showSettingsAlert();
//        }
//    }

    private void Find_element(View view) {
        time2 = (TextView) view.findViewById(R.id.time2);
        location = (TextView) view.findViewById(R.id.location);
        Lay_login_type = (LinearLayout) view.findViewById(R.id.Lay_login_type);
        Tv_mosque_name = (TextView) view.findViewById(R.id.Tv_mosque_name);
        Tv_mosque_address = (TextView) view.findViewById(R.id.Tv_mosque_address);
        Iv_Mosque_detail = (ImageView) view.findViewById(R.id.Iv_Mosque_detail_Image);
        Bt_Player_time = (Button) view.findViewById(R.id.Bt_Player_time);
        Bt_Find_mosque = (Button) view.findViewById(R.id.Bt_Find_mosque);
        Lay_Today_feed = (LinearLayout) view.findViewById(R.id.Lay_Today_feed);
        Lay_Quran = (LinearLayout) view.findViewById(R.id.Lay_Quran);
        Lay_community = (LinearLayout) view.findViewById(R.id.Lay_community);
        Lay_Setting = (LinearLayout) view.findViewById(R.id.Lay_Setting);
        Lay_Qiblah = (LinearLayout) view.findViewById(R.id.Lay_Qiblah);
        Lay_Quiz = (LinearLayout) view.findViewById(R.id.Lay_Quiz);
        Lay_Personnage = (LinearLayout) view.findViewById(R.id.Lay_Personnage);
        Lay_Supplication = (LinearLayout) view.findViewById(R.id.Lay_Supplication);
        Lay_Hajj = (LinearLayout) view.findViewById(R.id.Lay_Hajj);
        Lay_Zakkat = (LinearLayout) view.findViewById(R.id.Lay_Zakkat);
        Lay_Business = (LinearLayout) view.findViewById(R.id.Lay_Business);
        Lay_Organise = (LinearLayout) view.findViewById(R.id.Lay_Organise);
        Lay_out_Education = (LinearLayout) view.findViewById(R.id.Lay_out_Education);
        Lay_out_Activity = (LinearLayout) view.findViewById(R.id.Lay_out_Activity);
        Lay_out_Khutba = (LinearLayout) view.findViewById(R.id.Lay_out_Khutba);
        if (session.checkEntered()) {


        } else {
            Lay_login_type.setVisibility(View.GONE);
        }


    }

    private void Click_event() {
        Iv_Mosque_detail.setOnClickListener(this);
        Bt_Player_time.setOnClickListener(this);
        Bt_Find_mosque.setOnClickListener(this);
        Lay_Today_feed.setOnClickListener(this);
        Lay_Quran.setOnClickListener(this);
        Lay_community.setOnClickListener(this);
        Lay_Setting.setOnClickListener(this);
        Lay_Qiblah.setOnClickListener(this);
        Lay_Quiz.setOnClickListener(this);
        Lay_Personnage.setOnClickListener(this);
        Lay_Supplication.setOnClickListener(this);
        Lay_Hajj.setOnClickListener(this);
        Lay_Zakkat.setOnClickListener(this);
        Lay_Business.setOnClickListener(this);
        Lay_Organise.setOnClickListener(this);
        Lay_out_Khutba.setOnClickListener(this);
        Lay_out_Activity.setOnClickListener(this);
        Lay_out_Education.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("            Ummah");
        // getActivity().setFeatureDrawable(getResources().getDrawable(R.drawable.umahh));
        getActivity().setTitleColor(R.color.Green);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Lay_out_Khutba:

                Intent association_khutba = new Intent(getActivity(), Khutba_Activity.class);
                association_khutba.putExtra("id", session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_id_KEY));
                association_khutba.putExtra("finish_onback", true);
                startActivity(association_khutba);
                //getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;

            case R.id.Lay_out_Activity:
                Intent Suggestion_intent = new Intent(getActivity(), Activity_Mosque_Activity.class);
                Suggestion_intent.putExtra("id", session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_id_KEY));
                Suggestion_intent.putExtra("finish_onback", true);
                startActivity(Suggestion_intent);
               // getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Lay_out_Education:


                Intent intentLogin = new Intent(getActivity(), Education_Activity.class);
                intentLogin.putExtra("id", session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_id_KEY));
                intentLogin.putExtra("finish_onback", true);
                startActivity(intentLogin);
               // getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;


            case R.id.Iv_Mosque_detail_Image:

                Intent intent = new Intent(getActivity(), Mousque_detail_activity.class);
                intent.putExtra("id", session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_id_KEY));
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Bt_Player_time:


                if (session.checkEntered()) {

                    Fragment_prayer_time fragment2 = new Fragment_prayer_time();
                    FragmentTransaction fragmentTransaction2 = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.content_frame, fragment2);
                    fragmentTransaction2.commit();

                } else {

                    Intent in = new Intent(getActivity(), Login_Activity.class);
                    startActivity(in);
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                }




                break;

            case R.id.Lay_bookmark:
                break;
            case R.id.Lay_donation:
                break;
            case R.id.Layout_prefs:
                break;
            case R.id.Lay_Today_feed:
                Intent intentToday_feed = new Intent(getActivity(), Todayfeed_activity.class);
                startActivity(intentToday_feed);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.Lay_Quran:
                Intent intent1 = new Intent(getActivity(), Quran_activity.class);
                startActivity(intent1);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.Lay_community:
                Intent intentCommunity_Activity = new Intent(getActivity(), Community_Activity.class);
                startActivity(intentCommunity_Activity);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;
            case R.id.Lay_Setting:
                Intent intent2 = new Intent(getActivity(), Setting_Activity.class);
                startActivity(intent2);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.Lay_Qiblah:
                Intent intentQibla = new Intent(getActivity(), Qibla_Activity.class);
                startActivity(intentQibla);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.Lay_Quiz:
                Intent intentQuiz = new Intent(getActivity(), Quiz_Activity.class);
                startActivity(intentQuiz);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.Lay_Personnage:

                Intent intentPersonnange = new Intent(getActivity(), Personnange_Activity.class);
                startActivity(intentPersonnange);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.Layout_Zakkat:
                break;
            case R.id.Lay_Hajj:
                Intent intentHajj = new Intent(getActivity(), Hajj_and_Umrah_Activity.class);
                startActivity(intentHajj);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.Lay_Supplication:

                Intent intent_Supplication = new Intent(getActivity(), Suppication_Activity.class);
                startActivity(intent_Supplication);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Lay_Business:
                Intent intent3 = new Intent(getActivity(), Business_Activity.class);
                startActivity(intent3);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Lay_Organise:
                Intent intent4 = new Intent(getActivity(), Organization_Activity.class);
                startActivity(intent4);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Bt_Find_mosque:
                Fragment_Map fragment_map = new Fragment_Map();
                FragmentTransaction fragmentTransaction_map = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction_map.replace(R.id.content_frame, fragment_map);
                fragmentTransaction_map.commit();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                        prayerList.clear();
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
                            Calendar  PrayerurrentDate_15 = Calendar.getInstance();
                            String[] h = hour.split(":");

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


//                            if (CurrentDate.after(PrayerurrentDate) && CurrentDate.before(PrayerurrentDate_10)) {
//                                System.out.println("AFTER==10");
//                                cabList_modal.setType(1);
//                                upcompingIndex  = i;
//                            }
//                            if (CurrentDate.after(PrayerurrentDate_15) && CurrentDate.before(PrayerurrentDate)) {
//                                System.out.println("AFTER==15");
//                                cabList_modal.setType(2);
//                                upcompingIndex = i;
//                            }
                            if (CurrentDate.after(PrayerurrentDate) && CurrentDate.before(PrayerurrentDate_10)) {
                                System.out.println("AFTER==10");
                                cabList_modal.setType(1);
                                upcompingIndex = i;
                            }
                            else if (CurrentDate.after(PrayerurrentDate_15) && CurrentDate.before(PrayerurrentDate)) {
                                System.out.println("AFTER==10");
                                cabList_modal.setType(2);
                                upcompingIndex = i;
                            }
                            else if (PrayerurrentDate.after(CurrentDate)) {
                                System.out.println("AFTER");
                                if (tempDate != null) {
                                    if (PrayerurrentDate.before(tempDate)) {
                                        upcoming = false;
                                    }
                                }
                                if (!upcoming) {
                                    upcoming = true;
                                    tempDate = PrayerurrentDate;
                                    upcompingIndex = i;
                                }
                            } else if (PrayerurrentDate.before(CurrentDate)) {
                                System.out.println("before");
                                if (tempDate != null) {
                                    if (PrayerurrentDate.before(tempDate)) {
                                        upcoming = false;
                                    }
                                }
                                if (!upcoming) {
                                    upcoming = true;
                                    tempDate = PrayerurrentDate;
                                    upcompingIndex = i;
                                }


                            }


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

                            prayerList.add(cabList_modal);
                        }


                    } else {
                        Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }
                }

                if (prayerList.size() > 0 && upcompingIndex != -1) {


                    ((TextView) getView().findViewById(R.id.prayer1)).setText(prayerList.get(upcompingIndex).getDay());
                    ((TextView) getView().findViewById(R.id.prayer2)).setText(prayerList.get(upcompingIndex).getTime());
                    ((TextView) getView().findViewById(R.id.prayer3)).setText(prayerList.get(upcompingIndex).getDay_aerobic());

                }
            }

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        hideProgressDialog();
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


}
