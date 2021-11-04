package Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import Modal.Prayer_Time_Modal;
import app.com.ummah.R;


/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class Prayer_time_Adapter extends RecyclerView.Adapter<Prayer_time_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Prayer_Time_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView tv_carname, TV_time, tv_carNo_2, name;
        ImageView Iv_car;
        LinearLayout back;

        public MyViewHolder(View view) {
            super(view);
            tv_carname = (TextView) view.findViewById(R.id.tv_Mosque_Name);
            TV_time = (TextView) view.findViewById(R.id.TV_time);
            back = view.findViewById(R.id.back);
            name = view.findViewById(R.id.name);
            Iv_car = view.findViewById(R.id.icon);
     /*       Iv_car = (ImageView) view.findViewById(R.id.Iv_car);
            tv_carNo_2 = (TextView) view.findViewById(R.id.tv_carNo_2);*/
        }
    }

    public Prayer_time_Adapter(Context mContext, ArrayList<Prayer_Time_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prayer_ads, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Prayer_Time_Modal album = flatList.get(position);
        holder.name.setText(album.getDay());
        holder.tv_carname.setText(album.getDay_aerobic());

        holder.TV_time.setText(album.getTime());
        if (album.getType() == 1) {
            holder.name.setTextColor(Color.WHITE);
            holder.tv_carname.setTextColor(Color.WHITE);
            holder.TV_time.setTextColor(Color.WHITE);

            holder.back.setBackgroundColor(mContext.getResources().getColor(R.color.Green));
        } else if (album.getType() == 2) {
            holder.name.setTextColor(Color.WHITE);
            holder.tv_carname.setTextColor(Color.WHITE);
            holder.TV_time.setTextColor(Color.WHITE);
            holder.back.setBackgroundColor(mContext.getResources().getColor(R.color.SystemYellow));
        } else {
            holder.back.setBackgroundResource(R.drawable.recycler_bg);
            holder.name.setTextColor(mContext.getResources().getColor(R.color.Green));
            holder.tv_carname.setTextColor(mContext.getResources().getColor(R.color.Green));
            holder.TV_time.setTextColor(mContext.getResources().getColor(R.color.Green));
        }
     /*   holder.tv_Address.setText(album.getGuest_name());
   holder.name.setTextColor(Color.WHITE);
            holder.tv_carname.setTextColor(Color.WHITE);
        holder.tv_carNo_2.setText(album.getGuest_name());*/


        // loading album cover using Glide library
//        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
//        if(album.getLike()==0) {
//            holder.favorate.setImageResource(R.mipmap.heart);
//        }
//        else
//        {
//            holder.favorate.setImageResource(R.mipmap.like);
//        }
      /*  holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}
