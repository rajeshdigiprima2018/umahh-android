package Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Adapter.Sura_Adapter;
import Modal.data;
import Util.ConnectionDetector;
import Util.RecyclerItemClickListener;
import Util.SessionManager;
import app.com.ummah.Quran_activity;
import app.com.ummah.R;
import jcplayersample.Play_MainActivity;

/**
 * Created by Dell on 18-05-2019.
 */

public class Sura_Fragment extends Fragment {

    Sura_Adapter adaptor;
    // RecyclerView recyclerView;
    public static ArrayList<data> modalList = new ArrayList<>();
    private ProgressDialog progress_dialog = null;

    SessionManager session;
    Context ct;
    private ConnectionDetector cd;

    private final static String API_KEY = "surah";
    private static final String TAG = "Fragment Surah";
    public static RecyclerView recyclerView;

    //  ArrayList<data> List_Sura = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.sura_recycleview_layout, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        All_Depndency();
        ProgressBar_Function();
        findElement(rootView);

        // Sura_list();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new Sura_Adapter(Quran_activity.List_Sura, R.layout.sura_adapter, getActivity()));

       /* ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<Surah> call = apiService.getSura();
        call.enqueue(new Callback<Surah>() {
            @Override
            public void onResponse(Call<Surah> call, retrofit2.Response<Surah> response) {
                int statusCode = response.code();
                data data_ = new data();
                List_Sura = (ArrayList<data>) response.body().getData();
               // List_Sura.add(data_);
                recyclerView.setAdapter(new Sura_Adapter(List_Sura, R.layout.sura_adapter, getActivity()));


                System.out.println("Check it---------------------->>>>>>> ");
            }

            @Override
            public void onFailure(Call<Surah> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });*/


        // Click_event();
        Recycler_view(rootView);

        return rootView;
    }

    private void Recycler_view(View rootView) {


        //recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Sura_Fragment.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        String EnglishNameTranslation = Quran_activity.List_Sura.get(position).getEnglishName();
                        String NameTranslation = Quran_activity.List_Sura.get(position).getEnglishNameTranslation();
                        String Name = Quran_activity.List_Sura.get(position).getName();
                        String RevelationType = Quran_activity.List_Sura.get(position).getRevelationType();
                        String NumberOfAyahs = String.valueOf(Quran_activity.List_Sura.get(position).getNumber());
                        System.out.println("EnglishNameTranslation------------->> " + EnglishNameTranslation + "\n" + NameTranslation + "\n" + Name + "\n" + RevelationType + "\n" + NumberOfAyahs);

                        Intent intent = new Intent(getActivity(), Play_MainActivity.class);
                        intent.putExtra("EnglishNameTranslation", EnglishNameTranslation);
                        intent.putExtra("NumberOfAyahs", NumberOfAyahs);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }

/*
    private void Sura_list() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://api.alquran.cloud/v1/surah",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Surah All list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Surah all List", json.toString());
                            if (json != null) {

                                if (json.has("status")) {

                                    String success = json.optString("status");
                                    if (success.equals("OK")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Sura_Modal List_modal = new Sura_Modal();
                                            JSONObject jsonChildNode2 = jsonMainNode.getJSONObject(i);

                                            String number = jsonChildNode2.optString("number").toString();
                                            String name = jsonChildNode2.optString("name").toString();
                                            String englishName = jsonChildNode2.optString("englishName").toString();
                                            String englishNameTranslation = jsonChildNode2.optString("englishNameTranslation").toString();
                                            String numberOfAyahs = jsonChildNode2.optString("numberOfAyahs").toString();
                                            String revelationType = jsonChildNode2.optString("revelationType").toString();
                                            String Ser_no = String.valueOf(i + 1);

                                            List_modal.setSer_no(Ser_no);
                                            List_modal.setNumber(number);
                                            List_modal.setName(name);
                                            List_modal.setEnglishName(englishName);
                                            List_modal.setEnglishNameTranslation(englishNameTranslation);
                                            List_modal.setNumberOfAyahs(numberOfAyahs);
                                            List_modal.setRevelationType(revelationType);

                                            modalList.add(List_modal);

                                        }

                                      */
/*  if (modalList.size() > 0) {
                                            adaptor = new Sura_Adapter(getActivity(), modalList);
                                            recyclerView.setAdapter(adaptor);
                                            adaptor.notifyDataSetChanged();
                                        }*//*

                                        hideProgressDialog();

                                    } else {
                                        Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
*/

    private void All_Depndency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        }

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
