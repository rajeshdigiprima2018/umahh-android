package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import Modal.Select_Home_Mosque_Modal;
import Util.Constant;
import Util.DownloadImageTask;
import app.com.ummah.R;


/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class Select_mousque_Adapter extends RecyclerView.Adapter<Select_mousque_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Select_Home_Mosque_Modal> flatList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        ImageView Iv_Image;

        public ImageView favorate;
        TextView tv_Mosque_Name, tv_Address, tv_carNo_2;

        public MyViewHolder(View view) {
            super(view);
            tv_Mosque_Name = (TextView) view.findViewById(R.id.tv_Mosque_Name);
            tv_Address = (TextView) view.findViewById(R.id.tv_descrition1);
            Iv_Image = (ImageView) view.findViewById(R.id.Iv_Image);

        }
    }

    public Select_mousque_Adapter(Context mContext, ArrayList<Select_Home_Mosque_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_mosque, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Select_Home_Mosque_Modal album = flatList.get(position);

        holder.tv_Mosque_Name.setText(album.getUsername());
        holder.tv_Address.setText(album.getStreet_address()+", "+album.getCity_name()+",\n"+album.getState_name()+", "+album.getZipCode());
        System.out.print("Image_value---- >>>  " + album.getAvtar());
        String Avtar_value = album.getAvtar();

        try {


        if (Avtar_value.equals("null")){

            holder.Iv_Image.setImageResource(R.drawable.mosque_1);
        }
        else {
            String Image_url = Constant.Image_Base_url+album.getAvtar();
            new DownloadImageTask(holder.Iv_Image).execute(Image_url);
        }

        }catch (Exception e){}




    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}
