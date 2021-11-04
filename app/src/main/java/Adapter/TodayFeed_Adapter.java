package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import java.util.Map;

import Modal.TodayFeed_Modal;
import Util.Constant;
import Util.DownloadImageTask;
import app.com.ummah.R;

/**
 * Created by Dell on 26-04-2019.
 */

public class TodayFeed_Adapter extends RecyclerView.Adapter<TodayFeed_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<TodayFeed_Modal> flatList;
    private String User_id;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView Tv_Descrition, Tv_mosque_name,Tv_like_counter,Tv_mosque_title,time;
        ImageView Iv_icon,IV_mosquea,Iv_like;

        public MyViewHolder(View view) {
            super(view);
            Tv_Descrition = (TextView) view.findViewById(R.id.Tv_Descrition);

            time = (TextView) view.findViewById(R.id.time);
            Tv_mosque_name = (TextView) view.findViewById(R.id.Tv_mosque_name);
            Iv_icon = (ImageView) view.findViewById(R.id.Iv_icon);
            IV_mosquea = (ImageView) view.findViewById(R.id.IV_mosque);
            Iv_like = (ImageView) view.findViewById(R.id.Iv_like);
            Tv_like_counter  = (TextView) view.findViewById(R.id.Tv_like_counter);
            Tv_mosque_title = (TextView) view.findViewById(R.id.Tv_mosque_title);
        }
    }

    public TodayFeed_Adapter(Context mContext, ArrayList<TodayFeed_Modal> flatList,String user_id) {
        this.mContext = mContext;
        this.flatList = flatList;
        this.User_id = user_id;
    }

    @Override
    public TodayFeed_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_today_feed, parent, false);

        return new TodayFeed_Adapter.MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final TodayFeed_Adapter.MyViewHolder holder, int position) {
        TodayFeed_Modal album = flatList.get(position);
        holder.Tv_Descrition.setText(album.getTextarea());
        holder.Tv_mosque_name.setText(album.getUsername());
        holder.Tv_mosque_title.setText(album.getTitle());
        String likeCount = album.getLikeCounter();
        String Activity_id = album.getActivity_id();
        String Type_Like = album.getLike_type();


        holder.time.setText(album.getStartTime().substring(0,16));
       // Code for Today_feed detail image......
        String Avtar_value = album.getAvtar_feed();
        if (Avtar_value == null|| Avtar_value.equals("null")){

            holder.Iv_icon.setImageResource(R.drawable.activity_image);
        }
        else {
            String Image_url = Constant.Image_Base_url+album.getAvtar_feed();
            new DownloadImageTask(holder.Iv_icon).execute(Image_url);
        }
        //Code for Mosque small icon,......
        String Avtar_mosquea = album.getAvtar();
        if ( Avtar_mosquea== null || Avtar_mosquea.equals("null") ){

            holder.IV_mosquea.setImageResource(R.drawable.activity_image);
        }
        else {
            String Image_url = Constant.Image_Base_url+album.getAvtar();
            new DownloadImageTask(holder.IV_mosquea).execute(Image_url);
        }

        // Code For Like Counter....
        try {
            if (likeCount.equals("null")) {
                holder.Tv_like_counter.setText("0");
            } else {
                holder.Tv_like_counter.setText(album.getLikeCounter());
            }
        } catch (Exception e) {}

        //Code For like event...
        try {
            if (Type_Like.equals("Like")) {

                holder.Iv_like.setImageResource(R.drawable.ic_like);
            } else {
                holder.Iv_like.setImageResource(R.drawable.dislike);
            }

        } catch (Exception e) {}

        holder.Iv_like.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Toast.makeText(mContext, album.getDescription(), Toast.LENGTH_LONG).show();
                holder.Iv_like.setImageResource(R.drawable.ic_like);
                Like(Activity_id);
                return false;
            }
        });



    }
    private void Like(String Activity_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Base_url+"todaysFeed/LikeActivity/add",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Like list", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("Like List", json.toString());
                            if (json != null) {

                                if (json.has("success")) {

                                    String success = json.optString("success");
                                    if (success.equals("true")) {
                                        JSONObject jsonMainNode = json.optJSONObject("data");


                                        String likeCounter = jsonMainNode.optString("likeCounter").toString();
                                        String likeactivity_id = jsonMainNode.optString("likeactivity_id").toString();
                                        String message = json.optString("message");
                                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

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
                map.put("activity_id", Activity_id);
                map.put("user_id", User_id);
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

