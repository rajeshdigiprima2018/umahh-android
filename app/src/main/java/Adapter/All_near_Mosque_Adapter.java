package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.All_near_Mosque_Modal;
import Util.Constant;
import Util.DownloadImageTask;
import app.com.ummah.R;

/**
 * Created by Dell on 25-03-2019.
 */

public class All_near_Mosque_Adapter  extends RecyclerView.Adapter<All_near_Mosque_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<All_near_Mosque_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView tv_Mosque_Name, Tv_address,tv_km;
        ImageView Iv_car;

        public MyViewHolder(View view) {
            super(view);
            tv_Mosque_Name = (TextView) view.findViewById(R.id.tv_Mosque_Name);
            Tv_address = (TextView) view.findViewById(R.id.Tv_address);
            tv_km = (TextView) view.findViewById(R.id.tv_km);
            Iv_car = (ImageView) view.findViewById(R.id.Iv_car);
        }
    }

    public All_near_Mosque_Adapter(Context mContext, ArrayList<All_near_Mosque_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_near_mosque_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        All_near_Mosque_Modal album = flatList.get(position);
        holder.tv_Mosque_Name.setText(album.getUsername());
        holder.Tv_address.setText(album.getStreet_address());
        String Avtar_value = album.getAvtar();

        if (Avtar_value.equals("null")){

            holder.Iv_car.setImageResource(R.drawable.mosque_1);
        }
        else {
            String Image_url = Constant.Image_Base_url+album.getAvtar();
            new DownloadImageTask(holder.Iv_car).execute(Image_url);
        }
        holder.tv_km.setText(album.getDistance()+" KM");

    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}

