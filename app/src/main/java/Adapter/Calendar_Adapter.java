package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.Calendar_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 29-04-2019.
 */

public class Calendar_Adapter extends RecyclerView.Adapter<Calendar_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Calendar_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Tv_tilte, Tv_day_name,Tv_date,Tv_time;

        public MyViewHolder(View view) {
            super(view);
            Tv_tilte = (TextView) view.findViewById(R.id.Tv_tilte);
            Tv_day_name = (TextView) view.findViewById(R.id.Tv_day_name);
            Tv_date = (TextView)view.findViewById(R.id.Tv_date);
            Tv_time = (TextView)view.findViewById(R.id.Tv_time);

        }
    }

    public Calendar_Adapter(Context mContext, ArrayList<Calendar_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Calendar_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_calender, parent, false);

        return new Calendar_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Calendar_Adapter.MyViewHolder holder, int position) {
        Calendar_Modal album = flatList.get(position);
        holder.Tv_tilte.setText(album.getTitle());
        holder.Tv_day_name.setText(album.getDay_name());
        holder.Tv_date.setText(album.getDate()+" "+album.getMonth()+ " "+album.getYear());
        holder.Tv_time.setText(album.getHour());
       // String image_url = "http://167.172.131.53:4002" + album.getUrl();
      /*  Glide.with(mContext)
                .load(image_url)
                .centerCrop()
                .placeholder(R.drawable.book_business_big_image)
                .into(holder.Iv_image);*/

    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}