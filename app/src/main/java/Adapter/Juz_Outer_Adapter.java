package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.JuzOuter_Modal;
import app.com.ummah.R;

public class Juz_Outer_Adapter extends RecyclerView.Adapter<Juz_Outer_Adapter.MyViewHolder> {
private Context mContext;
private ArrayList<JuzOuter_Modal> flatList;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public ImageView thumbnail;
    public ImageView favorate;
    TextView Tv_englishNameTranslation, tv_englishName,Tv_id,Tv_Arabic_name,tv_Arebic_title;
    ImageView Iv_car;

    public MyViewHolder(View view) {
        super(view);
        Tv_englishNameTranslation = (TextView) view.findViewById(R.id.Tv_englishNameTranslation);
        tv_englishName = (TextView) view.findViewById(R.id.tv_englishName);
        Tv_id = (TextView) view.findViewById(R.id.Tv_id);
        Tv_Arabic_name  = (TextView) view.findViewById(R.id.Tv_Arebic_name);
        tv_Arebic_title  = (TextView) view.findViewById(R.id.tv_Arebic_title);
    }
}

    public Juz_Outer_Adapter(Context mContext, ArrayList<JuzOuter_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Juz_Outer_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.juz_adapter, parent, false);

        return new Juz_Outer_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Juz_Outer_Adapter.MyViewHolder holder, int position) {
        JuzOuter_Modal album = flatList.get(position);
        holder.Tv_id.setText(album.getIndex());
        holder.tv_englishName.setText("Juz "+album.getIndex());
        holder.Tv_englishNameTranslation.setText(album.getName());
        holder.Tv_Arabic_name.setText(album.getAerobic_name());
        holder.tv_Arebic_title.setText(album.getAerobic_title());

    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}

