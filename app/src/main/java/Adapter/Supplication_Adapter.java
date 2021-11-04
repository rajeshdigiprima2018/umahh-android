package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.Supplication_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 21-04-2019.
 */

public class Supplication_Adapter extends RecyclerView.Adapter<Supplication_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Supplication_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public ImageView favorate;
        TextView Tv_Business_name;
        ImageView Iv_Type;

        public MyViewHolder(View view) {
            super(view);
            Tv_Business_name = (TextView) view.findViewById(R.id.Tv_Business_name);
            Iv_Type = (ImageView) view.findViewById(R.id.Iv_Type);
        }
    }

    public Supplication_Adapter(Context mContext, ArrayList<Supplication_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Supplication_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.supplication_adapter, parent, false);

        return new Supplication_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Supplication_Adapter.MyViewHolder holder, int position) {
        Supplication_Modal album = flatList.get(position);
        holder.Tv_Business_name.setText(album.getName());
        if (album.getName().equals("Morning & Evening"))
        {
            holder.Iv_Type.setImageResource(R.drawable.ic_morning_and_evevning);
        }else if(album.getName().equals("Travel")){

            holder.Iv_Type.setImageResource(R.drawable.ic_travel);

        }
        else if(album.getName().equals( "Home & Family")){
            holder.Iv_Type.setImageResource(R.drawable.ic_home_family);

        }
        else if(album.getName().equals("Joy & Distress")){
            holder.Iv_Type.setImageResource(R.drawable.ic_joy_distress);

        }
        else if(album.getName().equals("Prayer")){
            holder.Iv_Type.setImageResource(R.drawable.prayer);

        }
        else if(album.getName().equals("Praising Allah")){
            holder.Iv_Type.setImageResource(R.drawable.ic_praising_allah);

        }
        else if(album.getName().equals("Nature")){
            holder.Iv_Type.setImageResource(R.drawable.ic_nature);

        }
        else if(album.getName().equals("Hajj and Umrah")){
            holder.Iv_Type.setImageResource(R.drawable.ic_hajj_and_umrah);

        }
        else if(album.getName().equals("Good Etiquette")){
            holder.Iv_Type.setImageResource(R.drawable.ic_good_etiduetes);


        }
        else if(album.getName().equals("Food & Drink")){
            holder.Iv_Type.setImageResource(R.drawable.ic_food_and_drink);


        }else if (album.getName().equals("All")){
            holder.Iv_Type.setImageResource(R.drawable.all);
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

