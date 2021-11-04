package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import Modal.Surah;
import Modal.data;
import Tabs.Pager_Quran;
import Util.ConnectionDetector;
import Util.GPSTracker;
import Util.SessionManager;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Dell on 18-03-2019.
 */

public class Quran_activity extends AppCompatActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener {
    public static ArrayList<data> List_Sura = new ArrayList<>();

    SessionManager session;
    LocationManager manager;
    GPSTracker gps;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;
    TabLayout tabLayout;
    Pager_Quran adapter;

    private ViewPager viewPager;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quran_activity);
        All_Depndency();
        ProgressBar_Function();
//        Find_element();
        API_Sura();
    }



    private void Find_element() {

        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Iv_back.setOnClickListener(this);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Sura));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Juz));
        /*   tabLayout.addTab(tabLayout.newTab().setText(R.string.My_Quran));*/
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.white)

        );
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);
        //Creating our pager adapter
        adapter = new Pager_Quran(this.getSupportFragmentManager(), tabLayout.getTabCount());
        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        //Adding onTabSelectedListener to swipe views
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
        /*tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));*/

    }


    private void API_Sura() {
        //Sura_Fragment.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        showProgressDialog();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<Surah> call = apiService.getSura();
        call.enqueue(new Callback<Surah>() {
            @Override
            public void onResponse(Call<Surah> call, retrofit2.Response<Surah> response) {
                int statusCode = response.code();
                data data_ = new data();
                try {
                    List_Sura = (ArrayList<data>) response.body().getData();
                    Find_element();
                    hideProgressDialog();
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<Surah> call, Throwable t) {
                // Log error here since request failed
//                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Iv_back:
                onBackPressed();
                break;
        }
    }


    private void All_Depndency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
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

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Quran_activity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}
