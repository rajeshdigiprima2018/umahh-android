package app.com.ummah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.List;
import java.util.Map;

import Adapter.Board_Adapter;
import Modal.Board_modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;

/**
 * Created by Dell on 16-04-2019.
 */

public class Board_Activity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    Spinner Spinner_map;
    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ProgressDialog progress_dialog;
    ImageView Iv_back;

    RecyclerView recyclerView;
    Board_Adapter board_adapter;
    public ArrayList<Board_modal> modalList;

    String Education_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);
        All_Depndency();
        ProgressBar_Function();
        Find_Element();
        Mosque_All_Education();
        Recycle_view();
        Click_Event();
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
    private void Mosque_All_Education() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"mosque/Board/getAll/" + getIntent().getStringExtra("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Board list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Board List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Board_modal List_modal = new Board_modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String mosque_id = jsonChildNode.optString("_id").toString();
                                            String name = jsonChildNode.optString("name").toString();
                                            String title = jsonChildNode.optString("title").toString();
                                            String textarea = jsonChildNode.optString("textarea").toString();
                                            String Board_id = jsonChildNode.optString("Board_id").toString();
                                            String id = jsonChildNode.optString("id").toString();


                                            JSONArray pictures = jsonChildNode.optJSONArray("pictures");
                                            int lengthJsonArr_pictures = pictures.length();

                                            for (int j = 0; j < lengthJsonArr_pictures; j++) {
                                                JSONObject jsonChildNode_c = pictures.getJSONObject(j);
                                                String pic_id = jsonChildNode_c.optString("_id").toString();
                                                String url = jsonChildNode_c.optString("url").toString();
                                                List_modal.setPic_id(pic_id);
                                                List_modal.setUrl(url);
                                            }
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                String textarea_html = Html.fromHtml(textarea, Html.FROM_HTML_MODE_LEGACY).toString();
                                                System.out.println("value--1-------------------->>>  " + textarea_html);
                                                List_modal.setTextarea(textarea_html);
                                            } else {
                                                String textarea_html = Html.fromHtml(textarea).toString();
                                                System.out.println("value---2------------------->>>  " + textarea_html);
                                                List_modal.setTextarea(textarea_html);
                                            }
                                            List_modal.setMosque_id(mosque_id);
                                            List_modal.setName(name);
                                            List_modal.setTitle(title);
                                            List_modal.setBoard_id(Board_id);
                                            List_modal.setId(id);
                                            modalList.add(List_modal);

                                        }

                                        if (modalList.size() > 0) {
                                            board_adapter = new Board_Adapter(Board_Activity.this, modalList);
                                            recyclerView.setAdapter(board_adapter);
                                            board_adapter.notifyDataSetChanged();
                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(Board_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Board_Activity.this);
        requestQueue.add(stringRequest);
    }

    private void Recycle_view() {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Board_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                     /*   Education_id = modalList.get(position).getEducation_id();
                        // DriverDetailDialog(driver_id);
                        Intent intent = new Intent(Board_Activity.this, Education_DetailActivity.class);
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
    }


    private void Click_Event() {

        Iv_back.setOnClickListener(this);

    }

    private void Find_Element() {

        Spinner_map = (Spinner) findViewById(R.id.Spinner_map);
        Iv_back = (ImageView)findViewById(R.id.Iv_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        Spinner_map.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("IMAM");
        categories.add("Ist IMAM");
        categories.add("2nd IMAM");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        }

        // attaching data adapter to spinner
        Spinner_map.setAdapter(dataAdapter);

    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Board_Activity.this, Mousque_detail_activity.class);
        intent.putExtra("id", getIntent().getStringExtra("id"));
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Iv_back:
                onBackPressed();
                break;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();
        ((TextView) view).setTextColor(getResources().getColorStateList(R.color.white));
        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColorStateList(R.color.white));
        // Set the text color of drop down items
        ((TextView) view).setTextColor(Color.WHITE);
        //  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

       /* if (item.equals("All")) {
            Filte_Method("all");
        } else if (item.equals("Mosque")) {
            Filte_Method("mosque");
        } else if (item.equals("Business/Organization")) {

            Filte_Method("business");
        }*/

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
