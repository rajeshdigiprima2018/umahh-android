package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.Business_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 19-04-2019.
 */

public class Business_Adapter extends RecyclerView.Adapter<Business_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Business_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView Tv_Business_name, TV_time;
        ImageView Iv_Image;

        public MyViewHolder(View view) {
            super(view);
            Tv_Business_name = (TextView) view.findViewById(R.id.Tv_Business_name);
            Iv_Image = (ImageView) view.findViewById(R.id.Iv_Image);
        }
    }

    public Business_Adapter(Context mContext, ArrayList<Business_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Business_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.business_adapter, parent, false);

        return new Business_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Business_Adapter.MyViewHolder holder, int position) {
        Business_Modal album = flatList.get(position);
        holder.Tv_Business_name.setText(album.getName());
        System.out.print("Image_value---- >>>  " + album.getIconUrl());
        String Avtar_value = album.getIconUrl();

       /* if (Avtar_value.equals("null")){

            holder.Iv_Image.setImageResource(R.drawable.mosque_1);
        }
        else {
            String Image_url = Constant.Image_Base_url+album.getIconUrl();r
            new DownloadImageTask(holder.Iv_Image).execute(Image_url);
        }*/

        if(album.getName().equals("Book Business")){
            holder.Iv_Image.setImageResource(R.drawable.ic_book_business);


        }
        else if(album.getName().equals("Religion Clothing")){
            holder.Iv_Image.setImageResource(R.drawable.ic_religion_clothing);


        }else if (album.getName().equals("Incense Business")){
            holder.Iv_Image.setImageResource(R.drawable.ic_incense_business);
        }
    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}

