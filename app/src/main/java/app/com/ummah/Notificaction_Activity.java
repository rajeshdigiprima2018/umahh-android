package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

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

import Adapter.Notificaction_Adapter;
import Modal.Notification_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

public class Notificaction_Activity extends AppCompatActivity implements View.OnClickListener {

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;

    RecyclerView recyclerView;
    Notificaction_Adapter education_adapter;
    public ArrayList<Notification_Modal> modalList;
    String Education_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_notification_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        All_Notification_API();
        Recycle_view();
        Click_Event();
    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                       /* Education_id = modalList.get(position).getEducation_id();
                        // DriverDetailDialog(driver_id);
                        Intent intent = new Intent(Notificaction_Activity.this, Education_DetailActivity.class);
                        intent.putExtra("KEY_Education", Education_id);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);*/

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

    private void All_Notification_API() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"notification/getAll/" + session.getuserinfo().get(Constant.SHARED_PREFERENCE_USER_ID_KEY),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Notification list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Notification_Modal cabList_modal = new Notification_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            JSONObject send_id = jsonChildNode.getJSONObject("send_id");
                                            String _id = send_id.optString("_id").toString();
                                            String role = send_id.optString("role").toString();
                                            String username = send_id.optString("username").toString();

                                            String description = jsonChildNode.optString("description").toString();
                                            String createdAt = jsonChildNode.optString("createdAt").toString();
                                            String notification_id = jsonChildNode.optString("notification_id").toString();

                                            /* Code for Remove html*/
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                String s = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY).toString();
                                                cabList_modal.setDescription(s);
                                            } else {
                                                String s = Html.fromHtml(description).toString();
                                                cabList_modal.setDescription(s);
                                            }

                                            cabList_modal.set_id(_id);
                                            cabList_modal.setRole(role);
                                            cabList_modal.setUsername(username);
                                            cabList_modal.setCreatedAt(createdAt);
                                            cabList_modal.setNotification_id(notification_id);
                                            modalList.add(cabList_modal);
                                            hideProgressDialog();
                                        }


                                    } else {
                                        Toast.makeText(Notificaction_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                                if (modalList.size() > 0) {
                                    education_adapter = new Notificaction_Adapter(Notificaction_Activity.this, modalList);
                                    recyclerView.setAdapter(education_adapter);
                                    education_adapter.notifyDataSetChanged();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Notificaction_Activity.this);
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

    private void Click_Event() {

        Iv_back.setOnClickListener(this);
    }

    private void Find_Element() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()){

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

    @Override
    public void onBackPressed() {

       finish();
    }


}
