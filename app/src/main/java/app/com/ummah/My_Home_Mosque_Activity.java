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

import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import Adapter.My_Home_Mosque_Adapter;
import Modal.Bookmark_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 25-03-2019.
 */

public class My_Home_Mosque_Activity extends AppCompatActivity implements View.OnClickListener {

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;


    RecyclerView recyclerView;
    My_Home_Mosque_Adapter my_home_mosque_adapter;
    public ArrayList<Bookmark_Modal> modalList;
    // public ArrayList<Home_Mosque_Modal> modalList;
    Bookmark_Modal cabList_modal = new Bookmark_Modal();
    ImageView Iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_home_mosque_activity);
        modalList = new ArrayList<>();
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        Recycle_view();
        API();
        Click_Event();
    }


    private void API() {
        cabList_modal.setGuest_name(session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_name_KEY));
        cabList_modal.setName(session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_street_address_KEY) + ", " + session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_city_KEY) + ",\n" + session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_state_KEY) + ", " + session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_country_KEY));
        cabList_modal.setCabId(session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_id_KEY));
        cabList_modal.setImage(session.getMosqueinfo().get(Constant.SHARED_PREFERENCE_Mosque_avtar_KEY));
        modalList.add(cabList_modal);
        my_home_mosque_adapter = new My_Home_Mosque_Adapter(this, modalList);
        recyclerView.setAdapter(my_home_mosque_adapter);
        my_home_mosque_adapter.notifyDataSetChanged();


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
        Iv_back.setOnClickListener(this);
        findViewById(R.id.Bt_Done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(My_Home_Mosque_Activity.this, Select_Home_Mosque_Activity.class);
                intent.putExtra("finish", true);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }

    private void Find_Element() {
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(My_Home_Mosque_Activity.this, Profile_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new My_Home_Mosque_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

//                        driver_id = Integer.parseInt((modalList.get(position).getCabId()));
//                        DriverDetailDialog(driver_id);
                        Intent intent = new Intent(My_Home_Mosque_Activity.this, MainActivity.class);
                        startActivity(intent);

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
