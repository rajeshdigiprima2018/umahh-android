package Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
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

import Adapter.Community_Mine_Adapter;
import Modal.Community_Modal;
import Util.ConnectionDetector;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;
import app.com.ummah.New_Request_Communnity;
import app.com.ummah.R;

/**
 * Created by Abhi on 9/7/2018.
 */

public class Community_Mine_Fragment extends Fragment implements View.OnClickListener {

    static Community_Mine_Adapter adaptor;
    static RecyclerView recyclerView;
    public static ArrayList<Community_Modal> modalList = new ArrayList<>();
    private static ProgressDialog progress_dialog = null;
    FloatingActionButton fab;


    static SessionManager session;
    static Context ct;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.recycleview_layout, container, false);
        All_Depndency();
        ProgressBar_Function();
        findElement(rootView);
        Click_event();
        Upcomeing_list();
        return rootView;
    }

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(getActivity());
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
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

    public static void Upcomeing_list() {

//        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Base_url+"user/Community/getAll/" + session.getuserinfo().get(Constant.SHARED_PREFERENCE_USER_ID_KEY),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Community mine list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Community mine List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        modalList.clear();
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            Community_Modal List_modal = new Community_Modal();
                                            JSONObject jsonChildNode2 = jsonMainNode.getJSONObject(i);
                                            String description = jsonChildNode2.optString("description").toString();
                                            String createdAt = jsonChildNode2.optString("createdAt").toString();
                                            String deletedAt = jsonChildNode2.optString("deletedAt").toString();
                                            String updatedAt = jsonChildNode2.optString("updatedAt").toString();
                                            String community_id = jsonChildNode2.optString("community_id").toString();
                                            try {
                                              //  String user_id = jsonChildNode2.optString("user_id").toString();
                                                JSONObject jsonObject_user_id = jsonChildNode2.getJSONObject("user_id");
                                                String _id = jsonObject_user_id.optString("_id").toString();
                                                String username = jsonObject_user_id.optString("username").toString();
                                                List_modal.setUsername(username);
                                            } catch (Exception e) {
                                            }

                                            String likecommunity_id2 = jsonChildNode2.optString("likecommunity_id").toString();
                                            if (likecommunity_id2.equals("null")) {
                                                List_modal.setLikeCounter("10");
                                            } else {
                                                JSONObject jsonObject = jsonChildNode2.getJSONObject("likecommunity_id");
                                                String likeCounter = jsonObject.optString("likeCounter").toString();
                                                String likecommunity_id = jsonObject.optString("likecommunity_id").toString();
                                                try {
                                                    // jsonString is a string variable that holds the JSON
                                                    JSONArray itemArray = jsonObject.getJSONArray("likeUser");
                                                    for (int j = 0; j < itemArray.length(); j++) {
                                                        String value = itemArray.getString(j);
                                                        Log.e("json--------->>>>>", j + "=" + value);
                                                        if ((session.getuserinfo().get(Constant.SHARED_PREFERENCE_USER_ID_KEY)).equals(value)) {
                                                            System.out.println("User Id  Like---->>>  " + value);
                                                            List_modal.setLike_type("Like");
                                                        } else {
                                                            System.out.println("User Id unlike---->>>  " + value);
                                                            List_modal.setLike_type("Unlike");
                                                        }
                                                    }
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                List_modal.setLikecommunity_id(likecommunity_id);
                                                List_modal.setLikeCounter(likeCounter);

                                            }

                                            List_modal.setDescription(description);
                                            List_modal.setCommunity_id(community_id);
                                            modalList.add(List_modal);

                                        }

                                        if (modalList.size() > 0) {
                                            adaptor = new Community_Mine_Adapter(ct, modalList, (session.getuserinfo().get(Constant.SHARED_PREFERENCE_USER_ID_KEY)));
                                            recyclerView.setAdapter(adaptor);
                                            adaptor.notifyDataSetChanged();
                                        }
                                        //   hideProgressDialog();

                                    } else {
                                        Toast.makeText(ct, "No Data Found.", Toast.LENGTH_LONG).show();
                                        //    hideProgressDialog();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ct);
        requestQueue.add(stringRequest);
    }

    private void Click_event() {
        fab.setOnClickListener(this);
    }

    private void findElement(View rootView) {


        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Community_Mine_Fragment.GridSpacingItemDecoration(1, dpToPx(10), true));
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.fab:
                Intent intent = new Intent(getActivity(), New_Request_Communnity.class);
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

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


    public static void showProgressDialog() {

        if (!progress_dialog.isShowing()) {
            progress_dialog.show();
        }
    }

    public static void hideProgressDialog() {
        if (progress_dialog.isShowing()) {
            progress_dialog.dismiss();
        }
    }

}