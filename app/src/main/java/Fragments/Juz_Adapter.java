package Fragments;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Modal.Juz_Modal;
import Modal.data;
import app.com.ummah.R;

/**
 * Created by Dell on 20-05-2019.
 */

class Juz_Adapter  extends RecyclerView.Adapter<Juz_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Juz_Modal> flatList;
    List<data> data;
    private int rowLayout;

    public Juz_Adapter(ArrayList<data> list_juz, int juz_adapter, FragmentActivity activity) {

        this.data = list_juz;
        this.rowLayout = juz_adapter;
        this.mContext = activity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
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

    public Juz_Adapter(Context mContext, ArrayList<Juz_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }
    @Override
    public Juz_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sura_adapter, parent, false);

        return new Juz_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Juz_Adapter.MyViewHolder holder, int position) {
        Juz_Modal album = flatList.get(position);
        holder.Tv_id.setText(album.getSer_no());
        holder.tv_englishName.setText(album.getAyahs_surah_englishName());
        holder.Tv_englishNameTranslation.setText(album.getAyahs_text());
        holder.Tv_Arabic_name.setText(album.getAyahs_surah_name());
    }


  /*  @Override
    public int getItemCount() {
        return data.size();
    }*/
  @Override
  public int getItemCount() {
      return flatList.size();
  }
}
