package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import Util.ConnectionDetector;
import Util.SessionManager;

/**
 * Created by Dell on 25-03-2019.
 */

public class Umrah_activity extends AppCompatActivity implements View.OnClickListener {

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    LinearLayout Layout_Arequest_of;
    ImageView Iv_back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.umrah_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        Click_Event();
    }

    private void Find_Element() {

        Layout_Arequest_of = (LinearLayout)findViewById(R.id.Layout_Arequest_of);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);

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
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
    }

    private void Click_Event() {

        Layout_Arequest_of.setOnClickListener(this);
        Iv_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Layout_Arequest_of:
                Intent intent = new Intent(Umrah_activity.this,A_request_of_pilgrims.class);
                startActivity(intent);
                break;

            case R.id.Iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Umrah_activity.this, Hajj_and_Umrah_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
