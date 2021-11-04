package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.Organization_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 21-04-2019.
 */

public class Organization_Adapter extends RecyclerView.Adapter<Organization_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Organization_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView Tv_Business_name, TV_time;
        ImageView Iv_image;

        public MyViewHolder(View view) {
            super(view);
            Tv_Business_name = (TextView) view.findViewById(R.id.Tv_Business_name);
            Iv_image = (ImageView) view.findViewById(R.id.Iv_image);
        }
    }

    public Organization_Adapter(Context mContext, ArrayList<Organization_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Organization_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.oganization_adapter, parent, false);

        return new Organization_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Organization_Adapter.MyViewHolder holder, int position) {
        Organization_Modal album = flatList.get(position);
        holder.Tv_Business_name.setText(album.getName());
        //   holder.Tv_Date.setText(album.getStartDate()+" to "+ album.getEndDate());

        if(album.getName().equals("Book Business")){
            holder.Iv_image.setImageResource(R.drawable.ic_book_business);


        }
        else if(album.getName().equals("Religion Clothing")){
            holder.Iv_image.setImageResource(R.drawable.ic_religion_clothing);


        }else if (album.getName().equals("Incense Business")){
            holder.Iv_image.setImageResource(R.drawable.ic_incense_business);
        }
    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}

