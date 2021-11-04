package Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.Education_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 12-04-2019.
 */

public class Education_Adapter  extends RecyclerView.Adapter<Education_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Education_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView Tv_Date, TV_time,Tv_tilte;
        //ImageView Iv_car;

        public MyViewHolder(View view) {
            super(view);
            Tv_Date = (TextView) view.findViewById(R.id.Tv_Date);
            TV_time = (TextView) view.findViewById(R.id.TV_time);
            Tv_tilte =  (TextView) view.findViewById(R.id.Tv_tilte);
            //Iv_car = (ImageView) view.findViewById(R.id.Iv_car);
        }
    }

    public Education_Adapter(Context mContext, ArrayList<Education_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Education_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_education, parent, false);

        return new Education_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Education_Adapter.MyViewHolder holder, int position) {
        Education_Modal album = flatList.get(position);
        holder.TV_time.setText(album.getStartTime()+" - "+album.getEndTime());
        holder.Tv_Date.setText(album.getStartDate()+" to "+ album.getEndDate());
        holder.Tv_tilte.setText(album.getTitle());


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

