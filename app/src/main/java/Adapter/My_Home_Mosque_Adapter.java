package Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.Bookmark_Modal;
import Util.Constant;
import Util.DownloadImageTask;
import app.com.ummah.R;

/**
 * Created by Dell on 25-03-2019.
 */

public class My_Home_Mosque_Adapter extends RecyclerView.Adapter<My_Home_Mosque_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Bookmark_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView tv_carname, tv_carNo, tv_carNo_2;
        ImageView Iv_car;

        public MyViewHolder(View view) {
            super(view);
            tv_carname = (TextView) view.findViewById(R.id.tv_Mosque_Name);
            tv_carNo = (TextView) view.findViewById(R.id.tv_descrition1);
            Iv_car = (ImageView) view.findViewById(R.id.Iv_car);
        }
    }

    public My_Home_Mosque_Adapter(Context mContext, ArrayList<Bookmark_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public My_Home_Mosque_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_home_mosque_adapter, parent, false);

        return new My_Home_Mosque_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final My_Home_Mosque_Adapter.MyViewHolder holder, int position) {
        Bookmark_Modal album = flatList.get(position);
        holder.tv_carname.setText(album.getGuest_name());
        holder.tv_carNo.setText(album.getName());

        String avtar = album.getImage();
        if (avtar.equals("null")) {

            holder.Iv_car.setImageResource(R.drawable.mosque_1);
        } else {
            String Image_url = Constant.Image_Base_url + avtar;
            new DownloadImageTask(holder.Iv_car).execute(Image_url);
        }

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


