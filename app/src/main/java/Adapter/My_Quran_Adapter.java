package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.My_Quran_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 26-05-2019.
 */

public class My_Quran_Adapter  extends RecyclerView.Adapter<My_Quran_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<My_Quran_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Tv_englishNameTranslation, tv_englishName,Tv_id,Tv_Arabic_name;
        ImageView Iv_car;

        public MyViewHolder(View view) {
            super(view);
            Tv_englishNameTranslation = (TextView) view.findViewById(R.id.Tv_englishNameTranslation);
            tv_englishName = (TextView) view.findViewById(R.id.tv_englishName);
            Tv_id = (TextView) view.findViewById(R.id.Tv_id);
            Tv_Arabic_name  = (TextView) view.findViewById(R.id.Tv_Arabic_name);
         /* Iv_car = (ImageView) view.findViewById(R.id.Iv_car);
            tv_carNo_2 = (TextView) view.findViewById(R.id.tv_carNo_2);*/
        }
    }

    public My_Quran_Adapter(Context mContext, ArrayList<My_Quran_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public My_Quran_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_quran_adapter, parent, false);

        return new My_Quran_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final My_Quran_Adapter.MyViewHolder holder, int position) {
        My_Quran_Modal album = flatList.get(position);
        holder.Tv_id.setText(album.getSer_no());
        holder.tv_englishName.setText(album.getSurahs_englishName());
        holder.Tv_englishNameTranslation.setText(album.getSurahs_englishNameTranslation());
        holder.Tv_Arabic_name.setText(album.getSurahs_name());
    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}
