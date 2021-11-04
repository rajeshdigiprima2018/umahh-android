package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.All_Supplication_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 22-04-2019.
 */

public class All_Supplication_Adapter  extends RecyclerView.Adapter<All_Supplication_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<All_Supplication_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView Tv_msg, Tv_ser_no;
        //ImageView Iv_car;

        public MyViewHolder(View view) {
            super(view);
            Tv_msg = (TextView) view.findViewById(R.id.Tv_msg);
            Tv_ser_no= (TextView) view.findViewById(R.id.Tv_ser_no);
            //Iv_car = (ImageView) view.findViewById(R.id.Iv_car);
        }
    }

    public All_Supplication_Adapter(Context mContext, ArrayList<All_Supplication_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public All_Supplication_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.supplication_all_detail_adapter, parent, false);

        return new All_Supplication_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final All_Supplication_Adapter.MyViewHolder holder, int position) {
        All_Supplication_Modal album = flatList.get(position);
        holder.Tv_msg.setText(album.getTitle());
        holder.Tv_ser_no.setText(album.getSer_no());
    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}

