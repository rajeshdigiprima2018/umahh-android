package Fragments;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
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

import Adapter.All_Supplication_Adapter;
import Adapter.Bookmark_Adapter;
import Adapter.News_Adapter;
import Modal.All_Supplication_Modal;
import Modal.Bookmark_Modal;
import Modal.New_Modal;
import Modal.Supplication_Modal;
import Util.Constant;
import Util.RecyclerItemClickListener;
import Util.SessionManager;
import app.com.ummah.All_Supplication_Activity;
import app.com.ummah.NewsActivity;
import app.com.ummah.R;


public class Fragment_bookmark extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    Bookmark_Adapter bookmark_adapter;
    public ArrayList<All_Supplication_Modal> modalList;
    SessionManager session;

    private ProgressDialog progress_dialog;

    @Nullable

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        modalList = new ArrayList<>();
        session = new SessionManager(getActivity());
        ProgressBar_Function();
        Find_element(view);
        Get_Data();
        return view;

    }

    private void ProgressBar_Function() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(getActivity());
        }
        progress_dialog.setMessage(getResources().getString(R.string.Please_wait));
        progress_dialog.setCancelable(true);
        progress_dialog.setCanceledOnTouchOutside(false);
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

    private void Get_Data() {
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url + "user/getLikeSupplication",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Supplication list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Supplication List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        JSONArray jsonMainNode = json.optJSONArray("data");
                                        int lengthJsonArr = jsonMainNode.length();

                                        for (int i = 0; i < lengthJsonArr; i++) {
                                            All_Supplication_Modal List_modal = new All_Supplication_Modal();
                                            JSONObject jsonChildNode2 = jsonMainNode.getJSONObject(i).getJSONObject("supplication_id");
                                            String supCategory_id = jsonChildNode2.optString("supCategory_id").toString();
                                            String description = jsonChildNode2.optString("description").toString();
                                            String title_aro = jsonChildNode2.optString("title_aro").toString();
                                            String description_aro = jsonChildNode2.optString("description_aro").toString();
                                            String createdAt = jsonChildNode2.optString("createdAt").toString();
                                            String title = jsonChildNode2.optString("title").toString();
                                            String deletedAt = jsonChildNode2.optString("deletedAt").toString();
                                            String updatedAt = jsonChildNode2.optString("updatedAt").toString();
                                            String supplication_id = jsonChildNode2.optString("supplication_id").toString();
                                            String id = jsonChildNode2.optString("id").toString();

                                            int ser_no = i + 1;
                                            List_modal.setSer_no(String.valueOf(ser_no));
                                            List_modal.setTitle(title);
                                            List_modal.setDescription(description);
                                            List_modal.setDescription_aro(description_aro);
                                            List_modal.setId(id);
            /*                                List_modal.setIconUrl(iconUrl);
                                            List_modal.setCreatedAt(createdAt);
                                            List_modal.setDeletedAt(deletedAt);
                                            List_modal.setUpdatedAt(updatedAt);
                                            List_modal.setCategory_id(category_id);*/
                                            modalList.add(List_modal);

                                        }


                                    } else {
                                        Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_LONG).show();
                                    }
                                }

                                if (modalList.size() > 0) {
                                    bookmark_adapter = new Bookmark_Adapter(getActivity(), modalList);
                                    recyclerView.setAdapter(bookmark_adapter);
                                    bookmark_adapter.notifyDataSetChanged();
                                }
                                hideProgressDialog();

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + session.getToken_id().get(Constant.SHARED_PREFERENCE_TOKEN_KEY));
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("user_id", session.getuserinfo().get(Constant.SHARED_PREFERENCE_USER_ID_KEY));

                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private void Find_element(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onClick(View v) {

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
}
