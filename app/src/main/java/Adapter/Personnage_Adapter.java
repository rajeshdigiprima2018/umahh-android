package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import Modal.Personnage_Modal;
import app.com.ummah.R;

public class Personnage_Adapter extends RecyclerView.Adapter<Personnage_Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Personnage_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Button bt;

        public MyViewHolder(View view) {
            super(view);
            bt = (Button) view.findViewById(R.id.Bt);
        }
    }

    public Personnage_Adapter(Context mContext, ArrayList<Personnage_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public Personnage_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parsonnange_adapter, parent, false);

        return new Personnage_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Personnage_Adapter.MyViewHolder holder, int position) {
        Personnage_Modal album = flatList.get(position);
        holder.bt.setText(album.getName());

    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}

