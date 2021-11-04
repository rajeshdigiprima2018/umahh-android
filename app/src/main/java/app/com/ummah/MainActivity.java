package app.com.ummah;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.time4j.PlainDate;
import net.time4j.SystemClock;
import net.time4j.calendar.HijriCalendar;
import net.time4j.format.expert.ChronoFormatter;
import net.time4j.format.expert.PatternType;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Fragments.Fragment_Donation;
import Fragments.Fragment_Map;
import Fragments.Fragment_home;
import Fragments.Fragment_bookmark;
import Util.ConnectionDetector;
import Util.Constant;
import Util.SessionManager;
import butterknife.ButterKnife;


public class MainActivity extends MyBaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView mTextMessage;
    BottomNavigationView bottomNavigationView;
    LinearLayout Lay_bookmark, Lay_donation, Layout_prefs, Layout_Today_feed, Layout_Quran, Layout_Community, Layout_Setting;
    LinearLayout Layout_Qitlah, Layout_Quiz, Layout_Presonnge, Layout_Zakkat, Layout_Hajj, Layout_supplication, Layout_business, Layout_Organise;
    NavigationView navigationView;
    DrawerLayout drawer;
    LinearLayout Layout_profile;
    ImageView Iv_image_show;
    TextView Tv_Name, Tv_Email,Tv_Login;
    SessionManager session;
    Context ct;
    private ConnectionDetector cd;



    @Override
    public void UpdateLocation(Location location, String place) {
        MyApps.LOCATION = location;
        MyApps.PLACE = place;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        ButterKnife.bind(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
     All_Depndency();
        Nanigation_bar();

        Find_element();
        ClickEvent();
    }

    private void All_Depndency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
    }

    private void Nanigation_bar() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.container_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        displaySelectedScreen(R.id.nav_menu1);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //add this line to display menu1 when the activity is loaded
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        Layout_profile = (LinearLayout) headerView.findViewById(R.id.Layout_profile);
        Lay_bookmark = (LinearLayout) headerView.findViewById(R.id.Lay_bookmark);
        Lay_donation = (LinearLayout) headerView.findViewById(R.id.Lay_donation);
        Layout_prefs = (LinearLayout) headerView.findViewById(R.id.Layout_prefs);
        Layout_Today_feed = (LinearLayout) headerView.findViewById(R.id.Layout_Today_feed);
        Layout_Quran = (LinearLayout) headerView.findViewById(R.id.Layout_Quran);
        Layout_Community = (LinearLayout) headerView.findViewById(R.id.Layout_Community);
        Layout_Setting = (LinearLayout) headerView.findViewById(R.id.Layout_Setting);
        Layout_Qitlah = (LinearLayout) headerView.findViewById(R.id.Layout_Qitlah);
        Layout_Presonnge = (LinearLayout) headerView.findViewById(R.id.Layout_Presonnge);
        Layout_Quiz = (LinearLayout) headerView.findViewById(R.id.Layout_Quiz);
        Layout_Zakkat = (LinearLayout) headerView.findViewById(R.id.Layout_Zakkat);
        Layout_Hajj = (LinearLayout) headerView.findViewById(R.id.Layout_Hajj);
        Layout_supplication = (LinearLayout) headerView.findViewById(R.id.Layout_supplication);
        Layout_business = (LinearLayout) headerView.findViewById(R.id.Layout_business);
        Layout_Organise = (LinearLayout) headerView.findViewById(R.id.Layout_Organise);
        Iv_image_show = (ImageView) headerView.findViewById(R.id.Iv_image_show);
        Tv_Name = (TextView) headerView.findViewById(R.id.Tv_Name);
        Tv_Email = (TextView) headerView.findViewById(R.id.Tv_Email);
        Tv_Login = (TextView)headerView.findViewById(R.id.Tv_Login);
        if (session.checkEntered()) {
            Tv_Login.setVisibility(View.GONE);
            String url =  session.getProfile().get(Constant.SHARED_PREFERENCE_Profile_pic_KEY);
            Tv_Name.setText(session.getuserinfo().get(Constant.SHARED_PREFERENCE_NAME_KEY));
            Tv_Email.setText(session.getuserinfo().get(Constant.SHARED_PREFERENCE_EMAIL_KEY));

            if (url.length()>0) {


                // Iv_image_show.setImageDrawable(Drawable.createFromPath(session.getProfile().get(Constant.SHARED_PREFERENCE_Profile_pic_KEY)));
                //Iv_image_show.setImageURI(Uri.parse(url));
                //Glide.with(MainActivity.this).load(url).into(Iv_image_show);
                Iv_image_show.setImageBitmap(getBitmapFromURL(url));
                //Iv_image_show.setImageURI(Uri.parse(url));
              //  Iv_image_show.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));

            } else {
                // Iv_image_show.setImageDrawable(Drawable.createFromPath(session.getProfile().get(Constant.SHARED_PREFERENCE_Profile_pic_KEY)));
                Iv_image_show.setImageResource(R.drawable.imgpsh_fullsize_anim);
            }
        } else {
            // Iv_image_show.setImageDrawable(Drawable.createFromPath(session.getProfile().get(Constant.SHARED_PREFERENCE_Profile_pic_KEY)));
            Iv_image_show.setImageResource(R.drawable.imgpsh_fullsize_anim);
            Tv_Email.setVisibility(View.GONE);
            Tv_Name.setVisibility(View.GONE);
            Tv_Login.setVisibility(View.VISIBLE);
        }


    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    private void Find_element() {

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

    }

    private void ClickEvent() {
        // Click event on Navigation bottom bar.

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Lay_donation.setOnClickListener(this);
        Layout_prefs.setOnClickListener(this);
        Layout_Today_feed.setOnClickListener(this);
        Layout_Quran.setOnClickListener(this);
        Layout_Community.setOnClickListener(this);
        Layout_Setting.setOnClickListener(this);
        Layout_Qitlah.setOnClickListener(this);
        Layout_Presonnge.setOnClickListener(this);
        Layout_Quiz.setOnClickListener(this);
        Layout_Zakkat.setOnClickListener(this);
        Layout_Hajj.setOnClickListener(this);
        Layout_supplication.setOnClickListener(this);
        Layout_business.setOnClickListener(this);
        Layout_Organise.setOnClickListener(this);

        Lay_bookmark.setOnClickListener(this);
        Layout_profile.setOnClickListener(this);

    }

    //   All Bottom bar fragment.
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:


                    Fragment_home fragment_home = new Fragment_home();
                    FragmentTransaction fragmentTransaction_home = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction_home.replace(R.id.content_frame, fragment_home);
                    fragmentTransaction_home.commit();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    return true;
                case R.id.navigation_Bookmark:
                    if (session.checkEntered()) {
                        Fragment_bookmark fragment = new Fragment_bookmark();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.commit();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                    } else {
                        LoginMSG();
                    }


                    return true;
                case R.id.navigation_Map:

                    Fragment_Map fragment_map = new Fragment_Map();
                    FragmentTransaction fragmentTransaction_map = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction_map.replace(R.id.content_frame, fragment_map);
                    fragmentTransaction_map.commit();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    return true;
                case R.id.navigation_Donation:
                    if (session.checkEntered()) {
                        Fragment_Donation fragment_donation = new Fragment_Donation();
                        FragmentTransaction fragmentTransaction_donation = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction_donation.replace(R.id.content_frame, fragment_donation);
                        fragmentTransaction_donation.commit();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        LoginMSG();
                    }
                    return true;
                case R.id.navigation_3:
                    Intent intentSetting = new Intent(MainActivity.this, Setting_Activity.class);
                    startActivity(intentSetting);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    return true;
            }
            return false;
        }
    };

    private void LoginMSG() {
        new AlertDialog.Builder(this)
                .setMessage("Please login the app")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MainActivity.this, WalkThroughActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_menu1:
                fragment = new Fragment_home();
                break;
            case R.id.nav_menu2:
                fragment = new Fragment_bookmark();
                break;
            case R.id.nav_menu3:
                fragment = new Fragment_bookmark();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.container_main);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent  intent = new Intent(MainActivity.this, Notificaction_Activity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.container_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //  super.onBackPressed();

            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onClick(View v) {

        //creating fragment object
        //    Fragment fragment = null;

        switch (v.getId()) {

            case R.id.Lay_bookmark:

                if (session.checkEntered()) {

                    Fragment_bookmark fragment_home = new Fragment_bookmark();
                    FragmentTransaction fragmentTransaction_home = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction_home.replace(R.id.content_frame, fragment_home);
                    fragmentTransaction_home.commit();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    LoginMSG();
                }
                break;

            case R.id.Lay_donation:


                if (session.checkEntered()) {
                    Fragment_Donation fragment_donation = new Fragment_Donation();
                    FragmentTransaction fragmentTransaction_donation = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction_donation.replace(R.id.content_frame, fragment_donation);
                    fragmentTransaction_donation.commit();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    LoginMSG();
                }
                break;

            case R.id.Layout_profile:
                if (session.checkEntered()) {
                    Intent intent = new Intent(MainActivity.this, Profile_Activity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;
                }
                else {

                    Intent intent = new Intent(MainActivity.this, Login_Activity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;
                }

            case R.id.Layout_Setting:
                Intent intentSetting = new Intent(MainActivity.this, Setting_Activity.class);
                startActivity(intentSetting);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Layout_Quiz:
                Intent intentQuiz = new Intent(MainActivity.this, Quiz_Activity.class);
                startActivity(intentQuiz);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Layout_Today_feed:
                Intent intentToday_feed = new Intent(MainActivity.this, Todayfeed_activity.class);
                startActivity(intentToday_feed);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Layout_Quran:
                Intent Quran = new Intent(MainActivity.this, Quran_activity.class);
                startActivity(Quran);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Layout_Community:
                Intent Quran_Community = new Intent(MainActivity.this, Community_Activity.class);
                startActivity(Quran_Community);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Layout_Hajj:
                Intent Hajj_and_Umrah = new Intent(MainActivity.this, Hajj_and_Umrah_Activity.class);
                startActivity(Hajj_and_Umrah);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Layout_supplication:
                Intent supplication = new Intent(MainActivity.this, Suppication_Activity.class);
                startActivity(supplication);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Layout_business:
                Intent business = new Intent(MainActivity.this, Business_Activity.class);
                startActivity(business);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Layout_Qitlah:
                Intent Qitlah = new Intent(MainActivity.this, Qibla_Activity.class);
                startActivity(Qitlah);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;


        }


        //replacing the fragment
      /*  if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }*/

    }
}
