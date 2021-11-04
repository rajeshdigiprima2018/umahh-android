package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import Modal.Question_Modal;
import app.com.ummah.R;



/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class Gk_Adapter extends RecyclerView.Adapter<Gk_Adapter.MyViewHolder>  {
    private Context mContext;
    private ArrayList<Question_Modal> flatList;
    private static final String TAG = "QuestionAdapter";
    public RadioGroup radioGroupOptions;




    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView favorate;
        TextView Tv_Question;
        ImageView Iv_car;


        public RadioButton BT_1;
        public RadioButton BT_2;
        public RadioButton BT_3;
        public RadioButton BT_4;

        public MyViewHolder(View view) {
            super(view);
            Tv_Question = (TextView) view.findViewById(R.id.Tv_Question);
//            radioGroupOptions = (RadioGroup) itemView.findViewById(R.id.radio_group_options);
            BT_1 = (RadioButton) view.findViewById(R.id.BT_1);
            BT_2 = (RadioButton) view.findViewById(R.id.BT_2);
            BT_3 = (RadioButton) view.findViewById(R.id.BT_3);
            BT_4 = (RadioButton) view.findViewById(R.id.BT_4);


        }
    }

    public Gk_Adapter(Context mContext, ArrayList<Question_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gk_question_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Question_Modal album = flatList.get(position);
        holder.BT_1.setText(album.getOption1());
        holder.BT_2.setText(album.getOption2());
        holder.BT_3.setText(album.getOption3());
        holder.BT_4.setText(album.getOption3());



        holder.BT_1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                 //   Toast.makeText(mContext, album.getOption1(), Toast.LENGTH_LONG).show();
                    holder.BT_1.setChecked(true);
                    holder.BT_2.setChecked(false);
                    holder.BT_3.setChecked(false);
                    holder.BT_4.setChecked(false);
                    if ((album.getOption1()).equals(album.getAnswer())){
                     //   Toast.makeText(mContext, album.getOption1(), Toast.LENGTH_LONG).show();
                       // General_Knowledge_Activity.Ans_correct_Sum = Ans_correct_Sum + 1;
                    }
                    else {
                        //Ans_Wrong_Sum = Ans_Wrong_Sum + 1;
                    }
                    return false;
                }
            });
        holder.BT_2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(mContext, album.getOption2(), Toast.LENGTH_LONG).show();
                holder.BT_1.setChecked(false);
                holder.BT_2.setChecked(true);
                holder.BT_3.setChecked(false);
                holder.BT_4.setChecked(false);
                if ((album.getOption2()).equals(album.getAnswer())){
                    Toast.makeText(mContext, album.getOption2(), Toast.LENGTH_LONG).show();
                   // Ans_correct_Sum = Ans_correct_Sum + 1;
                }
                else {
                   // Ans_Wrong_Sum = Ans_Wrong_Sum + 1;
                }
                return false;
            }
        });
        holder.BT_3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(mContext, album.getOption3(), Toast.LENGTH_LONG).show();
                holder.BT_1.setChecked(false);
                holder.BT_2.setChecked(false);
                holder.BT_3.setChecked(true);
                holder.BT_4.setChecked(false);
                if ((album.getOption3()).equals(album.getAnswer())){
                    Toast.makeText(mContext, album.getOption3(), Toast.LENGTH_LONG).show();
                    //Ans_correct_Sum = Ans_correct_Sum + 1;
                }
                else {
                    //Ans_Wrong_Sum = Ans_Wrong_Sum + 1;
                }
                return false;
            }
        });
        holder.BT_4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(mContext, album.getOption4(), Toast.LENGTH_LONG).show();
                holder.BT_1.setChecked(false);
                holder.BT_2.setChecked(false);
                holder.BT_3.setChecked(false);
                holder.BT_4.setChecked(true);
                if ((album.getOption4()).equals(album.getAnswer())){
                    Toast.makeText(mContext, album.getOption4(), Toast.LENGTH_LONG).show();
                    //Ans_correct_Sum = Ans_correct_Sum + 1;
                }
                else {
                   // Ans_Wrong_Sum = Ans_Wrong_Sum + 1;
                }
                return false;
            }
        });


//        holder.tv_carNo_2.setText(album.getGuest_name());


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
