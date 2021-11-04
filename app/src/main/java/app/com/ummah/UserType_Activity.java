package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import Util.ConnectionDetector;
import Util.GPSTracker;
import Util.SessionManager;

/**
 * Created by Abhi on 2/27/2019.
 */

public class UserType_Activity extends AppCompatActivity implements View.OnClickListener {

    private Boolean isInternetPresent = false;
    // Connection detector class
    private ConnectionDetector cd;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 0;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    SessionManager session;
    LocationManager manager;
    GPSTracker gps;
    Context ct;
    private ProgressDialog progress_dialog;
    ImageView Iv_user, Iv_Business_owner, Iv_Mosque, Iv_back;
    TextView Tv_Mosque, Tv_Business_owner, Tv_user;
    Button Bt_Continue;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_type_activity);
        All_Depndency();
        Find_element();
        ProgressBar_Function();
        Clicklistener();

    }

    private void Clicklistener() {

        Bt_Continue.setOnClickListener(this);
        Iv_Business_owner.setOnClickListener(this);
        Iv_back.setOnClickListener(this);
        Iv_user.setOnClickListener(this);
        Iv_Mosque.setOnClickListener(this);

    }

    private void Find_element() {
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        Iv_user = (ImageView) findViewById(R.id.Iv_user);
        Iv_Business_owner = (ImageView) findViewById(R.id.Iv_Business_owner);
        Iv_Mosque = (ImageView) findViewById(R.id.Iv_Mosque);
        Tv_Mosque = (TextView) findViewById(R.id.Tv_Mosque);
        Tv_Business_owner = (TextView) findViewById(R.id.Tv_Business_owner);
        Tv_user = (TextView) findViewById(R.id.Tv_user);
        Bt_Continue = (Button) findViewById(R.id.Bt_Continue);

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Iv_back:
               onBackPressed();
                break;

            case R.id.Iv_user:

                Intent intent_user = new Intent(UserType_Activity.this, User_registeration_activity.class);
                startActivity(intent_user);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Iv_Business_owner:

                Intent intent_Business = new Intent(UserType_Activity.this, Create_New_Account_Activity.class);
                intent_Business.putExtra("KEY_Register_Type", "Business");
                startActivity(intent_Business);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;
            case R.id.Iv_Mosque:

                Intent intentMosque = new Intent(UserType_Activity.this, Create_New_Account_Activity.class);
                intentMosque.putExtra("KEY_Register_Type", "Mosque");
                startActivity(intentMosque);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;
            case R.id.Bt_Continue:
                Intent intentContinue = new Intent(UserType_Activity.this, User_registeration_activity.class);
                startActivity(intentContinue);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                break;


        }
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(UserType_Activity.this,WalkThroughActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
