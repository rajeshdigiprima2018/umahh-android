package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import Modal.Bookmark_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 25-03-2019.
 */

public class My_Donation_Adapter  extends RecyclerView.Adapter<My_Donation_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<JSONObject> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView tv_carname, tv_carNo, tv_carNo_2, amount,date;
        ImageView Iv_car;

        public MyViewHolder(View view) {
            super(view);
            tv_carname = (TextView) view.findViewById(R.id.tv_Mosque_Name);
            tv_carNo = (TextView) view.findViewById(R.id.tv_descrition1);
            Iv_car = (ImageView) view.findViewById(R.id.Iv_car);
            amount = view.findViewById(R.id.amount);
            date = view.findViewById(R.id.date);
        }
    }

    public My_Donation_Adapter(Context mContext, ArrayList<JSONObject> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public My_Donation_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_donation_adapter, parent, false);

        return new My_Donation_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final My_Donation_Adapter.MyViewHolder holder, int position) {
       try {
           JSONObject album = flatList.get(position);
           holder.amount.setText(album.getString("amount")+"$");
           holder.tv_carNo.setText(album.getString("txn_id"));
           holder.tv_carname.setText(album.getJSONObject("mosque_id").getString("username"));
           holder.date.setText(album.getString("createdAt").substring(0,10));
       }catch (Exception ee){ee.printStackTrace();}

        // loading album cover using Glide library
//        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
//        if(album.getLike()==0) {
//            holder.favorate.setImageResource(R.mipmap.heart);
//        }
//        else
//        {
//            holder.favorate.setImageResource(R.mipmap.like);
//        }
      /*  holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}


