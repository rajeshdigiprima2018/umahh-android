package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import Modal.Association_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 18-04-2019.
 */

public class Association_Adapter extends RecyclerView.Adapter<Association_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Association_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView Iv_user_image;
        public ImageView favorate;
        TextView Tv_user, Tv_description,Tv_Address;

        public MyViewHolder(View view) {
            super(view);
            Tv_user = (TextView) view.findViewById(R.id.Tv_user);
            Tv_description = (TextView) view.findViewById(R.id.Tv_description);
            Iv_user_image = (ImageView)view.findViewById(R.id.Iv_user_image);
            Tv_Address = view.findViewById(R.id.Tv_Address);
        }
    }

    public Association_Adapter(Context mContext, ArrayList<Association_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Association_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.association_adapter, parent, false);

        return new Association_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Association_Adapter.MyViewHolder holder, int position) {
        Association_Modal album = flatList.get(position);
        holder.Tv_user.setText(album.getUsername());
        holder.Tv_description.setText(album.getDescription_service());
        holder.Tv_Address.setText(album.getStreet_address());

        String image_url = "http://167.172.131.53:4002" + album.getAvtar();

        Glide.with(mContext)
                .load(image_url)
                .centerCrop()
                .placeholder(R.drawable.jama_association)
                .into(holder.Iv_user_image);
    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}

