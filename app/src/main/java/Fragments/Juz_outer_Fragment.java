package Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import Adapter.Juz_Outer_Adapter;
import Modal.JuzOuter_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;
import app.com.ummah.R;

public class Juz_outer_Fragment extends Fragment {

    public static RecyclerView recyclerView;
    private ProgressDialog progress_dialog = null;

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;
    private static final String TAG = "Fragment Juz";

    Juz_Outer_Adapter juz_outer_adapter;
    public static ArrayList<JuzOuter_Modal> modalList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.sura_recycleview_layout, container, false);
        recyclerView =rootView.findViewById(R.id.recycler_view);
        All_Depndency();
        ProgressBar_Function();
        findElement(rootView);
        Click_event();
        Juz();
        //Juz_list();
       /* recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new Juz_Adapter(Fragment_home.List_Juz, R.layout.juz_adapter, getActivity()));*/
        Recycler_view(rootView);
    /*    ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<Surah> call = apiService.getSura();
        call.enqueue(new Callback<Surah>() {
            @Override
            public void onResponse(Call<Surah> call, retrofit2.Response<Surah> response) {
                int statusCode = response.code();
                List<data> List_Sura = response.body().getData();
                recyclerView.setAdapter(new Sura_Adapter(List_Sura, R.layout.sura_adapter, getActivity()));
            }

            @Override
            public void onFailure(Call<Surah> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });*/
        return rootView;
    }

    private void Recycler_view(View rootView) {



        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Juz_outer_Fragment.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String Juz_id = (modalList.get(position).getJuz_id());
                        String JuzIndex = (modalList.get(position).getIndex());

                        Intent intent = new Intent(getActivity(), Juz_Deatail_Activity.class);
                        intent.putExtra("Juz_id", Juz_id);
                        intent.putExtra("JuzIndex", JuzIndex);
                        startActivity(intent);


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }

    private void Juz(){

        //showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"mosque/getjuz",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Juz list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Juz List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            JuzOuter_Modal List_modal = new JuzOuter_Modal();
                                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                            String name = jsonChildNode.optString("name").toString();
                                            String index = jsonChildNode.optString("index").toString();
                                            String juz_id = jsonChildNode.optString("juz_id").toString();
                                            String aerobic_title = jsonChildNode.optString("aerobic_title").toString();
                                            String aerobic_name = jsonChildNode.optString("aerobic_name").toString();
                                            List_modal.setName(name);
                                            List_modal.setIndex(index);
                                            List_modal.setJuz_id(juz_id);
                                            List_modal.setAerobic_title(aerobic_title);
                                            List_modal.setAerobic_name(aerobic_name);
                                            modalList.add(List_modal);
                                        }
                                        if (modalList.size() > 0) {
                                            juz_outer_adapter = new Juz_Outer_Adapter(getActivity(), modalList);
                                            recyclerView.setAdapter(juz_outer_adapter);
                                            juz_outer_adapter.notifyDataSetChanged();
                                        }
                                        //hideProgressDialog();

                                    } else {
                                        Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_LONG).show();
                                        //hideProgressDialog();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

//    private void Juz_list() {
//
//        // showProgressDialog();
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://api.alquran.cloud/v1/juz/30/en.asad",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("Juz All list", response);
//                        try {
//                            JSONObject json = new JSONObject(response);
//                            Log.e("Juz all List", json.toString());
//                            if (json != null) {
//
//                                if (json.has("status")) {
//
//                                    String success = json.optString("status");
//                                    if (success.equals("OK")) {
//                                        modalList.clear();
//                                        JSONObject jsonMainNode = json.optJSONObject("data");
//                                        JSONArray jsonMainNode_ayahs = jsonMainNode.optJSONArray("ayahs");
//                                        int lengthJsonArr = jsonMainNode_ayahs.length();
//                                        for (int i = 0; i < lengthJsonArr; i++) {
//
//                                            Juz_Modal List_modal = new Juz_Modal();
//                                            JSONObject jsonChildNode2 = jsonMainNode_ayahs.getJSONObject(i);
//                                            String ayahs_number = jsonChildNode2.optString("number").toString();
//                                            String ayahs_text = jsonChildNode2.optString("text").toString();
//                                            String ayahs_numberInSurah = jsonChildNode2.optString("numberInSurah").toString();
//                                            String ayahs_juz = jsonChildNode2.optString("juz").toString();
//                                            String ayahs_manzil = jsonChildNode2.optString("manzil").toString();
//                                            String ayahs_page = jsonChildNode2.optString("page").toString();
//                                            String ayahs_hizbQuarter = jsonChildNode2.optString("hizbQuarter").toString();
//                                            String ayahs_sajda = jsonChildNode2.optString("sajda").toString();
//
//                                            JSONObject jsonMainNode_ayahs_surah = jsonChildNode2.optJSONObject("surah");
//
//                                            String ayahs_surah_number = jsonMainNode_ayahs_surah.optString("number").toString();
//                                            String ayahs_surah_name = jsonMainNode_ayahs_surah.optString("name").toString();
//                                            String ayahs_surah_englishName = jsonMainNode_ayahs_surah.optString("englishName").toString();
//                                            String ayahs_surah_englishNameTranslation = jsonMainNode_ayahs_surah.optString("englishNameTranslation").toString();
//                                            String ayahs_surah_revelationType = jsonMainNode_ayahs_surah.optString("revelationType").toString();
//                                            String ayahs_surah_numberOfAyahs = jsonMainNode_ayahs_surah.optString("numberOfAyahs").toString();
//
//                                            List_modal.setAyahs_surah_number(ayahs_surah_number);
//                                            List_modal.setAyahs_surah_name(ayahs_surah_name);
//                                            List_modal.setAyahs_surah_englishName(ayahs_surah_englishName);
//                                            List_modal.setAyahs_surah_englishNameTranslation(ayahs_surah_englishNameTranslation);
//                                            List_modal.setAyahs_surah_revelationType(ayahs_surah_revelationType);
//                                            List_modal.setAyahs_surah_numberOfAyahs(ayahs_surah_numberOfAyahs);
//
//
//                                            String Ser_no = String.valueOf(i + 1);
//                                            List_modal.setSer_no(Ser_no);
//                                            List_modal.setAyahs_number(ayahs_number);
//                                            List_modal.setAyahs_text(ayahs_text);
//                                            List_modal.setAyahs_numberInSurah(ayahs_numberInSurah);
//                                            List_modal.setAyahs_juz(ayahs_juz);
//                                            List_modal.setAyahs_manzil(ayahs_manzil);
//                                            List_modal.setAyahs_page(ayahs_page);
//                                            List_modal.setAyahs_hizbQuarter(ayahs_hizbQuarter);
//                                            List_modal.setAyahs_sajda(ayahs_sajda);
//                                            modalList.add(List_modal);
//
//                                        }
//
//                                        if (modalList.size() > 0) {
//                                           // adaptor = new Juz_Adapter(getActivity(), modalList);
//                                           // recyclerView.setAdapter(adaptor);
//                                           // adaptor.notifyDataSetChanged();
//                                        }
//                                        // hideProgressDialog();
//
//                                    } else {
//                                        Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_LONG).show();
//                                        //  hideProgressDialog();
//                                    }
//                                }
//
//                            }
//
//                        } catch (
//                                JSONException e)
//
//                        {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener()
//
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //
//                    }
//                })
//
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                return map;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(stringRequest);
//    }

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