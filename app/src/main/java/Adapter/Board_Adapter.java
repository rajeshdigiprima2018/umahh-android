package Adapter;

import android.content.Context;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import Modal.Board_modal;
import app.com.ummah.R;

/**
 * Created by Dell on 16-04-2019.
 */

public class Board_Adapter extends RecyclerView.Adapter<Board_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Board_modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //  public TextView title, count, overflow;
        public ImageView thumbnail;
        public ImageView IV_mosque;
        TextView tv_Name, Tv_TextArea, Tv_title;

        public MyViewHolder(View view) {
            super(view);
            tv_Name = (TextView) view.findViewById(R.id.tv_Name);
            Tv_TextArea = (TextView) view.findViewById(R.id.Tv_TextArea);
            Tv_title = (TextView) view.findViewById(R.id.Tv_title);
            IV_mosque  = (ImageView)view.findViewById(R.id.IV_mosque);
        }
    }

    public Board_Adapter(Context mContext, ArrayList<Board_modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Board_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.board_adapter, parent, false);

        return new Board_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Board_Adapter.MyViewHolder holder, int position) {
        Board_modal album = flatList.get(position);
        holder.tv_Name.setText(album.getName());
        holder.Tv_title.setText(album.getTitle());
        holder.Tv_TextArea.setText(album.getTextarea());

        String image_url = "http://167.172.131.53:4002" + album.getUrl();

        Glide.with(mContext)
                .load(image_url)
                .centerCrop()
                .placeholder(R.drawable.mosque_2)
                .into(holder.IV_mosque);



    }

    @Override
    public int getItemCount() {
        return flatList.size();
    }
}


