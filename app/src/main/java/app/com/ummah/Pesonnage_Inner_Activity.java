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
import android.widget.ImageView;
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

import Adapter.Personnage_Inner_Adapter;
import Modal.Personnage_Enner_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

public class Pesonnage_Inner_Activity extends AppCompatActivity implements View.OnClickListener {


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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personnange_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_element();
        API();
        Recycle_view();
        Clicklistener();
    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Pesonnage_Inner_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        PersonnageUser_id = modalList.get(position).getPersonnageUser_id();
                        Personnage_Name = modalList.get(position).getName();
                        Personnage_id = modalList.get(position).getPersonnage_id();
                        // DriverDetailDialog(driver_id);
                        Intent intent = new Intent(Pesonnage_Inner_Activity.this, Parsonnange_Detail_Activity.class);
                        intent.putExtra("KEY_Personnage_id", Personnage_id);
                        intent.putExtra("KEY_Personnage_Name", Personnage_Name);
                        intent.putExtra("KEY_PersonnageUser_id", PersonnageUser_id);
                        startActivity(intent);
                        //finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"mosque/getpersonnageUsers/"+KEY_Personnage_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Personnage list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Personnage List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Personnage_Enner_Modal List_modal = new Personnage_Enner_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String title = jsonChildNode.optString("title").toString();
                                            String personnageUser_id = jsonChildNode.optString("personnageUser_id").toString();
                                            String personnage_id = jsonChildNode.optString("personnage_id").toString();
                                            List_modal.setName(title);
                                            List_modal.setPersonnageUser_id(personnageUser_id);
                                            List_modal.setPersonnage_id(personnage_id);
                                            modalList.add(List_modal);
                                        }
                                        if (modalList.size() > 0) {
                                            personnage_inner_adapter = new Personnage_Inner_Adapter(Pesonnage_Inner_Activity.this, modalList);
                                            recyclerView.setAdapter(personnage_inner_adapter);
                                            personnage_inner_adapter.notifyDataSetChanged();
                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(Pesonnage_Inner_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
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
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Pesonnage_Inner_Activity.this, Personnange_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
