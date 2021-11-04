package Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.Bookmark_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 24-03-2019.
 */

public class Gk_Question_adapter extends RecyclerView.Adapter<Gk_Question_adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Bookmark_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView Tv_Question;
        RadioButton Bt_1,Bt_2,Bt_3,Bt_4;

        ImageView Iv_car;

        public MyViewHolder(View view) {
            super(view);
            Tv_Question = (TextView) view.findViewById(R.id.Tv_Question);
            Bt_1 = (RadioButton) view.findViewById(R.id.BT_1);
            Bt_2 = (RadioButton) view.findViewById(R.id.BT_2);
            Bt_3 = (RadioButton) view.findViewById(R.id.BT_3);
            Bt_4 = (RadioButton) view.findViewById(R.id.BT_4);

        }

    }

    public Gk_Question_adapter(Context mContext, ArrayList<Bookmark_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Gk_Question_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gk_question_adapter, parent, false);

        return new Gk_Question_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Gk_Question_adapter.MyViewHolder holder, int position) {
        Bookmark_Modal album = flatList.get(position);
        holder.Tv_Question.setText(album.getGuest_name());
        holder.Tv_Question.setText(album.getGuest_name());




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

