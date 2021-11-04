package Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import Modal.data;
import app.com.ummah.R;


/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class Sura_Adapter extends RecyclerView.Adapter<Sura_Adapter.MyViewHolder> {
    private Context mContext;
    // private ArrayList<Sura_Modal> flatList;
    List<data> data;
    private int rowLayout;

    /*   public Sura_Adapter(List<data> data, int sura_adapter, FragmentActivity activity) {


       }*/
    public Sura_Adapter(List<data> data, int sura_adapter, Context mContext) {
        this.data = data;
        this.rowLayout = sura_adapter;
        this.mContext = mContext;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView Tv_englishNameTranslation, tv_englishName, Tv_id, Tv_Arabic_name;
        ImageView Iv_car;

        public MyViewHolder(View view) {
            super(view);
            Tv_englishNameTranslation = (TextView) view.findViewById(R.id.Tv_englishNameTranslation);
            tv_englishName = (TextView) view.findViewById(R.id.tv_englishName);
            Tv_id = (TextView) view.findViewById(R.id.Tv_id);
            Tv_Arabic_name = (TextView) view.findViewById(R.id.Tv_Arabic_name);

        }
    }

  /*  public Sura_Adapter(Context mContext, ArrayList<Sura_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sura_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //Sura_Modal album = flatList.get(position);
/*        holder.Tv_id.setText(album.getSer_no());
        holder.tv_englishName.setText(album.getEnglishName());
        holder.Tv_englishNameTranslation.setText(album.getEnglishNameTranslation()+" ("+album.getNumberOfAyahs()+")");
        holder.Tv_Arabic_name.setText(album.getName());*/

        String p = (String.valueOf(position+1));
        holder.Tv_id.setText(p);
        holder.tv_englishName.setText(data.get(position).getEnglishName());
        holder.Tv_englishNameTranslation.setText(data.get(position).getEnglishNameTranslation() + " (" + data.get(position).getNumberOfAyahs() + ")");
        holder.Tv_Arabic_name.setText(data.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}
