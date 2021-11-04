package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.Mosque_Donation_Category_Adapter;
import Modal.Mosque_Donation_Category_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 15-04-2019.
 */

public class Mosque_Donation_Type_Activity extends AppCompatActivity implements View.OnClickListener {

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    RecyclerView recyclerView;
    ImageView Iv_back;

    Mosque_Donation_Category_Adapter board_adapter;
    public ArrayList<Mosque_Donation_Category_Modal> modalList;
    String dona_category_id, mosque_id, title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mosque_danation_detail);
        Mosque_Donation_Detail_Activity.Sum = 0;

        Find_Element();
        All_Depndency();
        ProgressBar_Function();
        Donation_Category();
        Recycle_view();
        Click_Event();
    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Mosque_Donation_Type_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        dona_category_id = modalList.get(position).getDona_category_id();
                        mosque_id = modalList.get(position).getMosque_id();
                        title = modalList.get(position).getTitle();
                        Intent intent = new Intent(Mosque_Donation_Type_Activity.this, Mosque_Donation_Detail_Activity.class);
                        intent.putExtra("KEY_dona_category_id", dona_category_id);
                        intent.putExtra("KEY_mosque_id", mosque_id);
                        intent.putExtra("KEY_title", title);

                        startActivity(intent);
//                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
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

    private void Donation_Category() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url +
                "donation/category/getAll/" + getIntent().getStringExtra("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Donation category list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Donation category List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Mosque_Donation_Category_Modal List_modal = new Mosque_Donation_Category_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String mosque_id = jsonChildNode.optString("mosque_id").toString();
                                            String startDate = jsonChildNode.optString("startDate").toString();
                                            String endDate = jsonChildNode.optString("endDate").toString();
                                            String title = jsonChildNode.optString("title").toString();
                                            String status = jsonChildNode.optString("status").toString();
                                            String dona_category_id = jsonChildNode.optString("dona_category_id").toString();
                                            String id = jsonChildNode.optString("id").toString();
                                            String iconUrl = jsonChildNode.optString("iconUrl").toString();

                                            List_modal.setMosque_id(mosque_id);
                                            List_modal.setStartDate(startDate);
                                            List_modal.setEndDate(endDate);
                                            List_modal.setTitle(title);
                                            List_modal.setStatus(status);
                                            List_modal.setDona_category_id(dona_category_id);
                                            List_modal.setId(id);
                                            List_modal.setIconUrl(iconUrl);
                                            modalList.add(List_modal);

                                        }

                                        if (modalList.size() > 0) {
                                            board_adapter = new Mosque_Donation_Category_Adapter(Mosque_Donation_Type_Activity.this, modalList);
                                            recyclerView.setAdapter(board_adapter);
                                            board_adapter.notifyDataSetChanged();
                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(Mosque_Donation_Type_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

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
        RequestQueue requestQueue = Volley.newRequestQueue(Mosque_Donation_Type_Activity.this);
        requestQueue.add(stringRequest);
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
        if (getIntent().getBooleanExtra("finish_onBack", false)) {

            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            Intent intent = new Intent(Mosque_Donation_Type_Activity.this, Mousque_detail_activity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));

            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    private void Click_Event() {

        Iv_back.setOnClickListener(this);
    }

    private void Find_Element() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Iv_back = (ImageView) findViewById(R.id.Iv_back);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            /*case R.id.Iv_Zakat:
                Intent intent = new Intent(Mosque_Donation_Type_Activity.this, Zakat_Al_Activity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Iv_Donation:
                Intent intent_Donation = new Intent(Mosque_Donation_Type_Activity.this, Mosque_Donation_Detail_Activity.class);
                startActivity(intent_Donation);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;*/

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
