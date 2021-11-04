package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.Business_Detail_Modal;
import Util.Constant;
import Util.DownloadImageTask;
import app.com.ummah.R;

/**
 * Created by Dell on 19-04-2019.
 */

public class Business_Detail_Adapter extends RecyclerView.Adapter<Business_Detail_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Business_Detail_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView TV_business_name, Tv_address;
        ImageView Iv_Image;

        public MyViewHolder(View view) {
            super(view);
            TV_business_name = (TextView) view.findViewById(R.id.TV_business_name);
            Tv_address = (TextView)view.findViewById(R.id.Tv_address);
            Iv_Image = (ImageView) view.findViewById(R.id.Iv_Image);
        }
    }

    public Business_Detail_Adapter(Context mContext, ArrayList<Business_Detail_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Business_Detail_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.business_detail_adapter, parent, false);

        return new Business_Detail_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Business_Detail_Adapter.MyViewHolder holder, int position) {
        Business_Detail_Modal album = flatList.get(position);
        holder.TV_business_name.setText(album.getUsername());
        holder.Tv_address.setText(album.getStreet_address()+", "+ album.getCity_name()+",\n"+album.getState_name()+", "+album.getCountry_name());


        System.out.print("Image_value---- >>>  " + album.getIconUrl());
        String Avtar_value = album.getIconUrl();

        if (Avtar_value.equals("null")){

            holder.Iv_Image.setImageResource(R.drawable.book_business_big_image);
        }
        else {
            String Image_url = Constant.Image_Base_url+album.getIconUrl();
            new DownloadImageTask(holder.Iv_Image).execute(Image_url);
        }
    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}

