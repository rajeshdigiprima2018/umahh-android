package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import Modal.Activity_Modal;
import Util.Constant;
import Util.DownloadImageTask;
import app.com.ummah.R;

/**
 * Created by Dell on 14-04-2019.
 */

public class Activity_adapter  extends RecyclerView.Adapter<Activity_adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Activity_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView TV_textarea, TV_time;
        ImageView Iv_image;

        public MyViewHolder(View view) {
            super(view);
            TV_textarea = (TextView) view.findViewById(R.id.TV_textarea);
            TV_time = (TextView) view.findViewById(R.id.TV_time);
            Iv_image = (ImageView) view.findViewById(R.id.Iv_image);
        }
    }

    public Activity_adapter(Context mContext, ArrayList<Activity_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Activity_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_adapter, parent, false);

        return new Activity_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Activity_adapter.MyViewHolder holder, int position) {
        Activity_Modal album = flatList.get(position);
        holder.TV_textarea.setText(album.getTitle());
       // String image_url = "http://167.172.131.53:4002" + album.getUrl();


        String Avtar_value = album.getUrl();

        if (Avtar_value.equals("null")){

            holder.Iv_image.setImageResource(R.drawable.activity_image);
        }
        else {
            String Image_url = Constant.Image_Base_url+album.getUrl();
            new DownloadImageTask(holder.Iv_image).execute(Image_url);
        }
    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}

