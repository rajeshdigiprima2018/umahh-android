package Fragments;

import android.app.ProgressDialog;
import android.content.Context;
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

import Modal.Juz_Modal;
import Util.ConnectionDetector;
import Util.RecyclerItemClickListener;
import Util.SessionManager;
import app.com.ummah.R;

/**
 * Created by Dell on 18-05-2019.
 */

public class Juz_Deatail_Activity extends AppCompatActivity implements View.OnClickListener {

    Juz_Adapter adaptor;
    public static RecyclerView recyclerView;
    ArrayList<Juz_Modal> modalList = new ArrayList<>();
    private ProgressDialog progress_dialog = null;

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private static final String TAG = "Fragment Juz";
    ImageView IV_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juz_recycleview_layout);
        recyclerView =findViewById(R.id.recycler_view);
        All_Depndency();
        ProgressBar_Function();
        findElement();
        Click_event();
        Juz_list();
        Recycler_view();


    }


    private void Recycler_view() {



        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Juz_Deatail_Activity.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Juz_Deatail_Activity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(Juz_Deatail_Activity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                      /*  String Booking_date = (data.get(position).getBooking_date());
                        String Name_patho = (data.get(position).getPatholog_name());
                        String Booking_Time = (data.get(position).getBooking_time());
                        String Booking_Status = (data.get(position).getPayment_status());
                        String guid = (data.get(position).getGuid());

                        Intent intent = new Intent(getActivity(), BookingDetailActivity.class);
                        intent.putExtra("Booking_date", Booking_date);
                        intent.putExtra("Name_patho", Name_patho);
                        intent.putExtra("Booking_Time", Booking_Time);
                        intent.putExtra("Booking_Status", Booking_Status);
                        intent.putExtra("guid", guid);

                        startActivity(intent);*/


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }

    private void Juz_list() {

       showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://api.alquran.cloud/v1/juz/"+getIntent().getStringExtra("JuzIndex")+"/en.asad",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Juz All list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Juz all List", json.toString());
                            if (json != null) {

                                if (json.has("status")) {

                                    String success = json.optString("status");
                                    if (success.equals("OK")) {
                                        modalList.clear();
                                        JSONObject jsonMainNode = json.optJSONObject("data");
                                        JSONArray jsonMainNode_ayahs = jsonMainNode.optJSONArray("ayahs");
                                        int lengthJsonArr = jsonMainNode_ayahs.length();
                                        for (int i = 0; i < lengthJsonArr; i++) {

                                            Juz_Modal List_modal = new Juz_Modal();
                                            JSONObject jsonChildNode2 = jsonMainNode_ayahs.getJSONObject(i);
                                            String ayahs_number = jsonChildNode2.optString("number").toString();
                                            String ayahs_text = jsonChildNode2.optString("text").toString();
                                            String ayahs_numberInSurah = jsonChildNode2.optString("numberInSurah").toString();
                                            String ayahs_juz = jsonChildNode2.optString("juz").toString();
                                            String ayahs_manzil = jsonChildNode2.optString("manzil").toString();
                                            String ayahs_page = jsonChildNode2.optString("page").toString();
                                            String ayahs_hizbQuarter = jsonChildNode2.optString("hizbQuarter").toString();
                                            String ayahs_sajda = jsonChildNode2.optString("sajda").toString();

                                            JSONObject jsonMainNode_ayahs_surah = jsonChildNode2.optJSONObject("surah");

                                            String ayahs_surah_number = jsonMainNode_ayahs_surah.optString("number").toString();
                                            String ayahs_surah_name = jsonMainNode_ayahs_surah.optString("name").toString();
                                            String ayahs_surah_englishName = jsonMainNode_ayahs_surah.optString("englishName").toString();
                                            String ayahs_surah_englishNameTranslation = jsonMainNode_ayahs_surah.optString("englishNameTranslation").toString();
                                            String ayahs_surah_revelationType = jsonMainNode_ayahs_surah.optString("revelationType").toString();
                                            String ayahs_surah_numberOfAyahs = jsonMainNode_ayahs_surah.optString("numberOfAyahs").toString();

                                            List_modal.setAyahs_surah_number(ayahs_surah_number);
                                            List_modal.setAyahs_surah_name(ayahs_surah_name);
                                            List_modal.setAyahs_surah_englishName(ayahs_surah_englishName);
                                            List_modal.setAyahs_surah_englishNameTranslation(ayahs_surah_englishNameTranslation);
                                            List_modal.setAyahs_surah_revelationType(ayahs_surah_revelationType);
                                            List_modal.setAyahs_surah_numberOfAyahs(ayahs_surah_numberOfAyahs);


                                            String Ser_no = String.valueOf(i + 1);
                                            List_modal.setSer_no(Ser_no);
                                            List_modal.setAyahs_number(ayahs_number);
                                            List_modal.setAyahs_text(ayahs_text);
                                            List_modal.setAyahs_numberInSurah(ayahs_numberInSurah);
                                            List_modal.setAyahs_juz(ayahs_juz);
                                            List_modal.setAyahs_manzil(ayahs_manzil);
                                            List_modal.setAyahs_page(ayahs_page);
                                            List_modal.setAyahs_hizbQuarter(ayahs_hizbQuarter);
                                            List_modal.setAyahs_sajda(ayahs_sajda);
                                            modalList.add(List_modal);

                                        }

                                        if (modalList.size() > 0) {
                                            adaptor = new Juz_Adapter(Juz_Deatail_Activity.this, modalList);
                                            recyclerView.setAdapter(adaptor);
                                            adaptor.notifyDataSetChanged();
                                        }
                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(Juz_Deatail_Activity.this, "No Data Found.", Toast.LENGTH_LONG).show();
                                        hideProgressDialog();
                                    }
                                }

                            }

                        } catch (
                                JSONException e)

                        {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Juz_Deatail_Activity.this);
        requestQueue.add(stringRequest);
    }

    private void All_Depndency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Juz_Deatail_Activity.this.getWindow().setStatusBarColor(ContextCompat.getColor(Juz_Deatail_Activity.this, R.color.colorPrimaryDark));
        }
        modalList = new ArrayList<>();
        cd = new ConnectionDetector(Juz_Deatail_Activity.this);
        session = new SessionManager(Juz_Deatail_Activity.this);
        ct = Juz_Deatail_Activity.this;
    }

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(Juz_Deatail_Activity.this);
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
    }

    private void Click_event() {
        IV_back.setOnClickListener(this);
    }

    private void findElement() {
        IV_back = (ImageView) findViewById(R.id.IV_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.IV_back:
                finish();
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

    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {

        finish();
    }

}
