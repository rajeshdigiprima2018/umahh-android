package Adapter;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import Modal.Hajj_and_Umrah_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 23-04-2019.
 */

public class Hajj_and_Umrah_Adapter  extends RecyclerView.Adapter<Hajj_and_Umrah_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Hajj_and_Umrah_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView TV_textarea, TV_name;
        ImageView Iv_icon;

        public MyViewHolder(View view) {
            super(view);
            TV_name = (TextView) view.findViewById(R.id.TV_name);
            Iv_icon = (ImageView) view.findViewById(R.id.Iv_icon);
        }
    }

    public Hajj_and_Umrah_Adapter(Context mContext, ArrayList<Hajj_and_Umrah_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Hajj_and_Umrah_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hajj_and_umrah_adapter, parent, false);

        return new Hajj_and_Umrah_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Hajj_and_Umrah_Adapter.MyViewHolder holder, int position) {
        Hajj_and_Umrah_Modal album = flatList.get(position);
        holder.TV_name.setText(album.getName());
        String image_url = "http://167.172.131.53:4002"+album.getImageUrl();

        Glide
                .with(mContext)
                .load(image_url)
                .centerCrop()
                .placeholder(R.drawable.hajj)
                .into(holder.Iv_icon);

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

