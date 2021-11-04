package Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import Adapter.My_Quran_Adapter;
import Modal.My_Quran_Modal;
import Util.ConnectionDetector;
import Util.RecyclerItemClickListener;
import Util.SessionManager;
import app.com.ummah.R;

/**
 * Created by Dell on 18-05-2019.
 */

public class My_quran_Fragment extends Fragment {

    My_Quran_Adapter adaptor;
    RecyclerView recyclerView;
    ArrayList<My_Quran_Modal> modalList = new ArrayList<>();
    private ProgressDialog progress_dialog = null;

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private String Tag= "My Quran fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.myquran_recycleview_layout, container, false);
        All_Depndency();
        ProgressBar_Function();
        findElement(rootView);
        Click_event();
        My_Quran_list();
        Recycler_view(rootView);
        return rootView;
    }

    private void My_Quran_list() {

        // showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://api.alquran.cloud/v1/quran/en.asad",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("MyQuran all list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("MyQuran all List", json.toString());
                            if (json != null) {

                                if (json.has("status")) {

                                    String success = json.optString("status");
                                    if (success.equals("OK")) {
                                        modalList.clear();
                                        JSONObject jsonMainNode = json.optJSONObject("data");
                                        JSONArray jsonMainNode_surahs = jsonMainNode.optJSONArray("surahs");
                                        int lengthJsonArr = jsonMainNode_surahs.length();
                                        for (int i = 0; i < lengthJsonArr; i++) {

                                            My_Quran_Modal List_modal = new My_Quran_Modal();
                                            JSONObject jsonChildNode2 = jsonMainNode_surahs.getJSONObject(i);
                                            String surahs_number = jsonChildNode2.optString("number").toString();
                                            String surahs_name = jsonChildNode2.optString("name").toString();
                                            String surahs_englishName = jsonChildNode2.optString("englishName").toString();
                                            String surahs_englishNameTranslation = jsonChildNode2.optString("englishNameTranslation").toString();
                                            String surahs_revelationType = jsonChildNode2.optString("revelationType").toString();




                                            List_modal.setSurahs_number(surahs_number);
                                            List_modal.setSurahs_name(surahs_name);
                                            List_modal.setSurahs_englishName(surahs_englishName);
                                            List_modal.setSurahs_englishNameTranslation(surahs_englishNameTranslation);
                                            List_modal.setSurahs_revelationType(surahs_revelationType);


                                            String Ser_no = String.valueOf(i + 1);
                                            List_modal.setSer_no(Ser_no);
                                            modalList.add(List_modal);

                                        }

                                        if (modalList.size() > 0) {
                                            adaptor = new My_Quran_Adapter(getActivity(), modalList);
                                            recyclerView.setAdapter(adaptor);
                                            adaptor.notifyDataSetChanged();
                                        }
                                     //   hideProgressDialog();

                                    } else {
                                        Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_LONG).show();
                                     //    hideProgressDialog();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        Log.v(Tag,"request-start--->");
    }


    private void Recycler_view(View rootView) {



        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new My_quran_Fragment.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
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


    private void All_Depndency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        }
        modalList = new ArrayList<>();
        cd = new ConnectionDetector(getActivity());
        session = new SessionManager(getActivity());
        ct = getActivity();
    }

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(getActivity());
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
    }

    private void Click_event() {
        // fab.setOnClickListener(this);
    }

    private void findElement(View rootView) {
        // fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
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

}
