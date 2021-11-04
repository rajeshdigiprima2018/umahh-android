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
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.Personnage_Inner_Adapter;
import Modal.Personnage_Enner_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

public class Parsonnange_Detail_Activity extends AppCompatActivity implements View.OnClickListener {


    String KEY_Personnage_id;
    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView IV_back;
    Intent intent;
    RecyclerView recyclerView;
    Personnage_Inner_Adapter personnage_inner_adapter;
    ArrayList<Personnage_Enner_Modal> modalList;
    String Personnage_id, Personnage_Name,PersonnageUser_id;
    TextView Tv_description ,TV_title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personnange_detailpage_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        API();
       // Recycle_view();
        Clicklistener();
    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Parsonnange_Detail_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//
//                        PersonnageUser_id = modalList.get(position).getPersonnageUser_id();
//                        Personnage_Name = modalList.get(position).getName();
//                        Personnage_id = modalList.get(position).getPersonnage_id();
//                        // DriverDetailDialog(driver_id);
//                        Intent intent = new Intent(Parsonnange_Detail_Activity.this, Parsonnange_Detail_Activity.class);
//                        intent.putExtra("KEY_Personnage_id", Personnage_id);
//                        intent.putExtra("KEY_Personnage_Name", Personnage_Name);
//                        intent.putExtra("PersonnageUser_id", PersonnageUser_id);
//                        startActivity(intent);
//                        finish();
//                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    private void Clicklistener() {
        IV_back.setOnClickListener(this);
    }

    private void API() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"mosque/getpersonnageUserByPersonnageUserId/"+KEY_Personnage_id+"/"+PersonnageUser_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Personnage detail list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Personnage detail List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONObject jsonMainNode = json.optJSONObject("data");
                                       // int lengthJsonArr = jsonMainNode.length();

                                        //for (int i = 0; i < lengthJsonArr; i++) {
                                          //  JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String title = jsonMainNode.optString("title").toString();
                                            String image = jsonMainNode.optString("title").toString();
                                            String textarea = jsonMainNode.optString("textarea").toString();
                                            String personnageUser_id = jsonMainNode.optString("personnageUser_id").toString();
                                            String personnage_id = jsonMainNode.optString("personnage_id").toString();
                                            TV_title.setText(title);

                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                String textarea_html = Html.fromHtml(textarea, Html.FROM_HTML_MODE_LEGACY).toString();
                                                System.out.println("value--1-------------------->>>  " + textarea_html);
                                                Tv_description.setText(textarea_html);
                                            } else {
                                                String textarea_html = Html.fromHtml(textarea).toString();
                                                System.out.println("value---2------------------->>>  " + textarea_html);
                                                Tv_description.setText(textarea_html);
                                            }
//                                            List_modal.setName(title);
////                                            List_modal.setPersonnageUser_id(personnageUser_id);
////                                            List_modal.setPersonnage_id(personnage_id);
////                                            modalList.add(List_modal);
                                    //    }
//                                        if (modalList.size() > 0) {
//                                            personnage_inner_adapter = new Personnage_Inner_Adapter(Parsonnange_Detail_Activity.this, modalList);
//                                            recyclerView.setAdapter(personnage_inner_adapter);
//                                            personnage_inner_adapter.notifyDataSetChanged();
//                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(Parsonnange_Detail_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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

    private void Find_element() {

        IV_back = (ImageView) findViewById(R.id.IV_back);
        Tv_description = (TextView) findViewById(R.id.Tv_description);
        TV_title = (TextView) findViewById(R.id.TV_title);
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
        modalList = new ArrayList<>();
        cd = new ConnectionDetector(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        ct = this;
        intent = getIntent();
        KEY_Personnage_id = intent.getStringExtra("KEY_Personnage_id");
        PersonnageUser_id = intent.getStringExtra("KEY_PersonnageUser_id");
    }

    @Override
    public void onBackPressed() {

//        Intent intent = new Intent(Parsonnange_Detail_Activity.this, Pesonnage_Inner_Activity.class);
////        startActivity(intent);
////        finish();
////        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.IV_back:
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
