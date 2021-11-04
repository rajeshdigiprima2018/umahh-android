package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import Adapter.Personnage_Adapter;
import Modal.Personnage_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 20-03-2019.
 */

public class Quiz_Activity extends AppCompatActivity implements View.OnClickListener {

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView IV_back;
    Button Bt_Gk;

    RecyclerView recyclerView;
    Personnage_Adapter personnage_adapter;
    ArrayList<Personnage_Modal> modalList;
    String Personnage_id,Personnage_Name;
    TextView TV_title;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personnange_activity);
        TV_title = findViewById(R.id.TV_title);
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        API();
        Clicklistener();
        Recycle_view();
        TV_title.setText("Quiz");

    }
    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Quiz_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Personnage_id = modalList.get(position).getPersonnage_id();
                        Personnage_Name = modalList.get(position).getName();
                        // DriverDetailDialog(driver_id);
                        Intent intent = new Intent(Quiz_Activity.this, General_Knowledge_Activity.class);
                        intent.putExtra("KEY_id", Personnage_id);
                        intent.putExtra("KEY_Title", Personnage_Name);
                        startActivity(intent);
                        finish();
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


    private void API() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"quiz/Category/getAll",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    Log.e("quiz list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("quiz List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Personnage_Modal List_modal = new Personnage_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String title = jsonChildNode.optString("title").toString();

                                            String quiz_category_id = jsonChildNode.optString("quiz_category_id").toString();


                                            List_modal.setName(title);
                                            List_modal.setPersonnage_id(quiz_category_id);
                                            modalList.add(List_modal);

                                        }

                                        if (modalList.size() > 0) {
                                            personnage_adapter = new Personnage_Adapter(Quiz_Activity.this, modalList);
                                            recyclerView.setAdapter(personnage_adapter);
                                            personnage_adapter.notifyDataSetChanged();
                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(Quiz_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void Find_element() {


        IV_back = (ImageView)findViewById(R.id.IV_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void Clicklistener() {

//        Bt_Gk.setOnClickListener(this);
        IV_back.setOnClickListener(this);
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
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.IV_back:

                onBackPressed();

                break;

//            case R.id.Bt_Gk:
//
//
//                Intent intent = new Intent(Quiz_Activity.this,MainActivity.class);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                break;
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Quiz_Activity.this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
