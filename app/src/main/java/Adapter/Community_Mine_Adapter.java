package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.List;
import java.util.Map;

import Fragments.Community_Mine_Fragment;
import Modal.Community_Modal;
import Util.Constant;
import app.com.ummah.R;

/**
 * Created by Dell on 20-05-2019.
 */

public class Community_Mine_Adapter extends RecyclerView.Adapter<Community_Mine_Adapter.MyViewHolder> {

    private Context mContext;
    private String user_id;
    private List<Community_Modal> flatList;


    public Community_Mine_Adapter(Context mContext, List<Community_Modal> pay_detail) {
        this.mContext = mContext;
        this.flatList = pay_detail;
    }

    public Community_Mine_Adapter(Context activity, ArrayList<Community_Modal> modalList, String s) {

        this.mContext = activity;
        this.flatList = modalList;
        this.user_id = s;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date;
        TextView TV_Description, Tv_like_counter;
        TextView addressTV;
        TextView AmountTV;
        ImageView Iv_favoriteImage;
        ImageView del;
        TextView Tv_username;

        public MyViewHolder(View view) {
            super(view);

            Tv_username = (TextView)view.findViewById(R.id.Tv_username);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            TV_Description = (TextView) view.findViewById(R.id.TV_Description);
            Tv_like_counter = (TextView) view.findViewById(R.id.Tv_like_counter);
            addressTV = (TextView) view.findViewById(R.id.addressTV);
            AmountTV = (TextView) view.findViewById(R.id.AmountTV);
            Iv_favoriteImage = (ImageView) view.findViewById(R.id.Iv_favoriteImage);
            del = (ImageView)view.findViewById(R.id.del);
        }
    }


    @NonNull
    @Override
    public Community_Mine_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mine_community_adapter, parent, false);

        return new Community_Mine_Adapter.MyViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(final Community_Mine_Adapter.MyViewHolder holder, int position) {
        Community_Modal album = flatList.get(position);

        try {
            holder.Tv_username.setText(album.getUsername());
        }catch (Exception e){}

        holder.TV_Description.setText(album.getDescription());
        String likeCount = album.getLikeCounter();
        String Communinty_id = album.getCommunity_id();
        String Type_Like = album.getLike_type();
        try {

            if (likeCount.equals("null")) {
                holder.Tv_like_counter.setText("10");
            } else { holder.Tv_username.setText(album.getUsername());
                holder.Tv_like_counter.setText(album.getLikeCounter());
            }
        } catch (Exception e) {}
        try {
            if (Type_Like.equals("Like")) {

                holder.Iv_favoriteImage.setImageResource(R.drawable.ic_heart_like);
            } else {
                holder.Iv_favoriteImage.setImageResource(R.drawable.ic_dislike);
            }

        } catch (Exception e) {}


        holder.Iv_favoriteImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Toast.makeText(mContext, album.getDescription(), Toast.LENGTH_LONG).show();
                holder.Iv_favoriteImage.setImageResource(R.drawable.ic_heart_like);
                Like(Communinty_id);
                return false;
            }
        });
        holder.del.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DeleteMethod(Communinty_id);
                return false;
            }
        });




    }

    private void DeleteMethod(String communinty_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, Constant.Base_url+"user/Community/delete/"+communinty_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Delete list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Delete List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        JSONObject jsonMainNode = json.optJSONObject("data");

                                        Community_Modal List_modal = new Community_Modal();
                                        String n = jsonMainNode.optString("n").toString();
                                        String ok = jsonMainNode.optString("ok").toString();
                                        String message = json.optString("message");
                                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                                        //   Iv_favoriteImage
                                        if (message.equals("User Community deleted successfully.")) {
                                            Community_Mine_Fragment.Upcomeing_list();
                                        }


                                    } else {
                                        Toast.makeText(mContext, "No Data Found.", Toast.LENGTH_LONG).show();
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
               /* map.put("community_id", communinty_id);
                map.put("user_id", user_id);*/
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }


    private void Like(String communinty_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url+"user/LikeCommunity/add",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Like Community", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Like Community", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        JSONObject jsonMainNode = json.optJSONObject("data");

                                        Community_Modal List_modal = new Community_Modal();
                                        String community_id = jsonMainNode.optString("community_id").toString();
                                        String likeCounter = jsonMainNode.optString("likeCounter").toString();
                                        String likecommunity_id = jsonMainNode.optString("likecommunity_id").toString();
                                        String message = json.optString("message");
                                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                                        //   Iv_favoriteImage
                                        if (message.equals("Like add successfully.")) {
                                            // Iv_favoriteImage.s
                                        }


                                    } else {
                                        Toast.makeText(mContext, "No Data Found.", Toast.LENGTH_LONG).show();
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
                map.put("community_id", communinty_id);
                map.put("user_id", user_id);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return flatList.size();
    }
}