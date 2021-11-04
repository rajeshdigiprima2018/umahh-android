package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.Khutba_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 30-04-2019.
 */

public class Khutba_Adapter extends RecyclerView.Adapter<Khutba_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Khutba_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView TV_Speaker_name, TV_title, TV_time,Tv_Date;
        ImageView Iv_image;

        public MyViewHolder(View view) {
            super(view);
            TV_Speaker_name = (TextView) view.findViewById(R.id.TV_Speaker_name);
            TV_title = (TextView) view.findViewById(R.id.TV_title);
            TV_time = (TextView) view.findViewById(R.id.TV_time);
            Tv_Date = (TextView) view.findViewById(R.id.Tv_Date);
        }
    }

    public Khutba_Adapter(Context mContext, ArrayList<Khutba_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Khutba_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_khutba, parent, false);

        return new Khutba_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Khutba_Adapter.MyViewHolder holder, int position) {
        Khutba_Modal album = flatList.get(position);
        holder.TV_title.setText(album.getTitle());
        holder.TV_Speaker_name.setText("By "+album.getSpeaker_name());
        holder.TV_time.setText(album.getStartTime());
        holder.Tv_Date.setText(album.getStartDate());


    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}

