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
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import Adapter.Gk_Question_adapter;
import Modal.Bookmark_Modal;
import Util.ConnectionDetector;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 24-03-2019.
 */

public class GK_Question_Activity extends AppCompatActivity implements View.OnClickListener {


    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView IV_back;
    RecyclerView recyclerView;
    Gk_Question_adapter gk_question_adapter;
    public ArrayList<Bookmark_Modal> modalList;
    Bookmark_Modal cabList_modal = new Bookmark_Modal();
    Button Bt_Done;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gk_question_activity);
        modalList = new ArrayList<>();
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        API();
        SetData();
        Clicklistener();
    }


    private void SetData() {

    }

    private void API() {

        modalList = new ArrayList<>();
        cabList_modal.setGuest_name("Lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet");
        cabList_modal.setName("Al-Faatiha");
        cabList_modal.setCabId("1");
        modalList.add(cabList_modal);
        cabList_modal.setGuest_name("Lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet");
        cabList_modal.setName("Al-Faatiha");
        cabList_modal.setCabId("2");
        modalList.add(cabList_modal);
        cabList_modal.setGuest_name("Lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet");
        cabList_modal.setName("Al-Faatiha");
        cabList_modal.setCabId("3");
        modalList.add(cabList_modal);
        cabList_modal.setGuest_name("Lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet");
        cabList_modal.setName("Al-Faatiha");
        cabList_modal.setCabId("4");
        modalList.add(cabList_modal);
        cabList_modal.setGuest_name("Lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet");
        cabList_modal.setName("Al-Faatiha");
        cabList_modal.setCabId("5");
        modalList.add(cabList_modal);
        cabList_modal.setGuest_name("Lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet");
        cabList_modal.setName("Al-Faatiha");
        cabList_modal.setCabId("6");
        modalList.add(cabList_modal);
        cabList_modal.setGuest_name("Lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet");
        cabList_modal.setName("Al-Faatiha");
        cabList_modal.setCabId("7");
        modalList.add(cabList_modal);
        cabList_modal.setGuest_name("Lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet");
        cabList_modal.setName("Al-Faatiha");
        cabList_modal.setCabId("8");
        modalList.add(cabList_modal);
        cabList_modal.setGuest_name("The indian Air Force celebrated its Golden jubilee");
        cabList_modal.setName("Al-Faatiha");
        cabList_modal.setCabId("9");
        modalList.add(cabList_modal);
        gk_question_adapter = new Gk_Question_adapter(GK_Question_Activity.this, modalList);
        recyclerView.setAdapter(gk_question_adapter);
        gk_question_adapter.notifyDataSetChanged();

    }

    private void Find_element() {


        Bt_Done = (Button)findViewById(R.id.Bt_Done);
        IV_back = (ImageView) findViewById(R.id.Iv_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GK_Question_Activity.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GK_Question_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(GK_Question_Activity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );


    }

    private void Clicklistener() {

        Bt_Done.setOnClickListener(this);
        IV_back.setOnClickListener(this);

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

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Bt_Done:
                Intent intent = new Intent(GK_Question_Activity.this,Gk_result_Activity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

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
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(GK_Question_Activity.this,General_Knowledge_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
