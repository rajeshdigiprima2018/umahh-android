package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.Hajj_and_Umrah_sub_Detail_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 23-04-2019.
 */

public class Hajj_and_Umrah_Sub_Adapter  extends RecyclerView.Adapter<Hajj_and_Umrah_Sub_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Hajj_and_Umrah_sub_Detail_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView Tv_msg, Tv_ser_no;

        public MyViewHolder(View view) {
            super(view);
            Tv_msg = (TextView) view.findViewById(R.id.Tv_msg);
            Tv_ser_no = (TextView) view.findViewById(R.id.Tv_ser_no);
        }
    }

    public Hajj_and_Umrah_Sub_Adapter(Context mContext, ArrayList<Hajj_and_Umrah_sub_Detail_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Hajj_and_Umrah_Sub_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.supplication_all_detail_adapter, parent, false);

        return new Hajj_and_Umrah_Sub_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Hajj_and_Umrah_Sub_Adapter.MyViewHolder holder, int position) {
        Hajj_and_Umrah_sub_Detail_Modal album = flatList.get(position);
        holder.Tv_msg.setText(album.getTitle());
        holder.Tv_ser_no.setText(album.getSer_no());
        //   holder.Tv_Date.setText(album.getStartDate()+" to "+ album.getEndDate());


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


