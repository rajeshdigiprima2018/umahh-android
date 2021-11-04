package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.Mosque_Donation_Category_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 24-05-2019.
 */

public class Mosque_Donation_Category_Adapter extends RecyclerView.Adapter<Mosque_Donation_Category_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Mosque_Donation_Category_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Tv_title;
        ImageView Iv_Image;

        public MyViewHolder(View view) {
            super(view);
            Tv_title = (TextView) view.findViewById(R.id.Tv_title);
            Iv_Image = (ImageView) view.findViewById(R.id.Iv_Image);
        }
    }

    public Mosque_Donation_Category_Adapter(Context mContext, ArrayList<Mosque_Donation_Category_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Mosque_Donation_Category_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mosque_donation_categery_adapter, parent, false);

        return new Mosque_Donation_Category_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Mosque_Donation_Category_Adapter.MyViewHolder holder, int position) {
        Mosque_Donation_Category_Modal album = flatList.get(position);
        holder.Tv_title.setText(album.getTitle());
        System.out.print("Image_value---- >>>  " + album.getIconUrl());
        String title = album.getTitle();


        if (album.getTitle().equals("Zakat Al Fitr")) {
            holder.Iv_Image.setBackgroundResource(R.drawable.zakat);


        } else if (album.getTitle().equals("Donation")) {
            holder.Iv_Image.setBackgroundResource(R.drawable.donation);


        }
    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}



