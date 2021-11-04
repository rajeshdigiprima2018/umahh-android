package app.com.ummah;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Tabs.Pager_Community;
import Util.SessionManager;

public class Community_Activity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    private ViewPager viewPager;
    ImageView Iv_back;
    TextView toolbarTitle;
    Toolbar toolbar;
    FloatingActionButton fab;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity);
        Find_element();
        Click_Event();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.All_community));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Mine));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.white)
        );
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);
        //Creating our pager adapter
        Pager_Community adapter = new Pager_Community(this.getSupportFragmentManager(), tabLayout.getTabCount());
        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);

    }

    private void Find_element() {
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
    }

    private void Click_Event() {
        Iv_back.setOnClickListener(this);
        session = new SessionManager(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 1) {
            if (session.checkEntered()) {
                viewPager.setCurrentItem(tab.getPosition());

            } else {
                Intent in = new Intent(Community_Activity.this, Login_Activity.class);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        } else {
            viewPager.setCurrentItem(tab.getPosition());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Iv_back:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Community_Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
