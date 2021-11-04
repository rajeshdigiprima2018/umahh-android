package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.New_Modal;
import Util.DownloadImageTask;
import app.com.ummah.R;

/**
 * Created by Dell on 16-04-2019.
 */

public class News_Adapter extends RecyclerView.Adapter<News_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<New_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView TV_Title, Tv_by;
        ImageView Image;

        public MyViewHolder(View view) {
            super(view);
            TV_Title = (TextView) view.findViewById(R.id.TV_Title);
            Tv_by = (TextView) view.findViewById(R.id.Tv_by);
            Image = (ImageView) view.findViewById(R.id.Image);
        }
    }

    public News_Adapter(Context mContext, ArrayList<New_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public News_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_adapter, parent, false);

        return new News_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final News_Adapter.MyViewHolder holder, int position) {
        New_Modal album = flatList.get(position);
        holder.TV_Title.setText(album.getTitle());
        holder.Tv_by.setText("By "+album.getByName());


        String image_url = "http://167.172.131.53:4002" + album.getUrl();
/*
        Glide.with(mContext)
                .load("http://167.172.131.53:4002" + album.getUrl())
                .centerCrop()
                .placeholder(R.drawable.book_business_big_image)
                .into(holder.Image);*/

        new DownloadImageTask(holder.Image).execute(image_url);
    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}

