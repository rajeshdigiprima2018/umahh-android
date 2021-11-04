package Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.Mosque_donation_Detail_Modal;
import app.com.ummah.R;

/**
 * Created by Dell on 27-05-2019.
 */

public class Mosque_donation_Detail_Adapter extends RecyclerView.Adapter<Mosque_donation_Detail_Adapter.MyViewHolder> {
    public Context mContext;
    public static ArrayList<Mosque_donation_Detail_Modal> flatList;
    public ChangeEdittext changeEdittext;

    public interface ChangeEdittext {
        public void onChange();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Tv_title;
        ImageView Iv_Image;
        EditText Et_Amount;

        public MyViewHolder(View view) {
            super(view);
            Tv_title = (TextView) view.findViewById(R.id.Tv_title);
            Iv_Image = (ImageView) view.findViewById(R.id.Iv_Image);
            Et_Amount = (EditText) view.findViewById(R.id.Et_Amount);
        }
    }

    public Mosque_donation_Detail_Adapter(Context mContext, ArrayList<Mosque_donation_Detail_Modal> flatList, ChangeEdittext changeEdittext) {
        this.mContext = mContext;
        this.flatList = flatList;
        this.changeEdittext = changeEdittext;
    }

    @Override
    public Mosque_donation_Detail_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mosque_donation_detail_adapter, parent, false);

        return new Mosque_donation_Detail_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Mosque_donation_Detail_Adapter.MyViewHolder holder, int position) {
        Mosque_donation_Detail_Modal album = flatList.get(position);
        holder.Tv_title.setText(album.getTitle());

        holder.Et_Amount.setText(album.getEditTextValue());
        holder.Et_Amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    changeEdittext.onChange();
                }

            }
        });
        holder.Et_Amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                flatList.get(position).setEditTextValue(holder.Et_Amount.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

     /*   holder.Et_Amount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                 Toast.makeText(mContext, "touch", Toast.LENGTH_LONG).show();


                return false;
            }
        });*/
/*
        holder.Et_Amount.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
                String ans = holder.Et_Amount.getText().toString();
                System.out.println("afterTextChanged--- >><<>> "+ans);
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                String ans = holder.Et_Amount.getText().toString();
                System.out.println("beforeTextChanged--- >><<>> "+ans);
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String ans = holder.Et_Amount.getText().toString();
                // save ans to sharedpreferences or Database
                System.out.println("Value--- >><<>> "+ans);
                flatList.get(position).setEditTextValue(holder.Et_Amount.getText().toString());
            }
        });*/




   /* @Override
    public long getItemId(int pos) {
        return pos;
    }*/

        //   System.out.print("Image_value---- >>>  " + album.getIconUrl());
  /*      String title = album.getTitle();


        if (album.getTitle().equals("Zakat Al Fitr")) {
            holder.Iv_Image.setImageResource(R.drawable.zakat);


        } else if (album.getTitle().equals("Donation")) {
            holder.Iv_Image.setImageResource(R.drawable.donation);


        }*/
    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }


    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public int getCount() {
        return flatList.size();
    }

    public Object getItem(int pos) {
        return flatList.get(pos);
    }
}




