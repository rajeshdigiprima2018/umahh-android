package Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import Modal.Question_Modal;
import app.com.ummah.R;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Question_Modal> flatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Button bt;

        public MyViewHolder(View view) {
            super(view);
            bt = (Button) view.findViewById(R.id.Bt);
        }
    }

    public QuestionAdapter(Context mContext, ArrayList<Question_Modal> flatList) {
        this.mContext = mContext;
        this.flatList = flatList;
    }

    @Override
    public QuestionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parsonnange_adapter, parent, false);

        return new QuestionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final QuestionAdapter.MyViewHolder holder, int position) {
        Question_Modal album = flatList.get(position);
        holder.bt.setText(album.getQuestion());

    }


    @Override
    public int getItemCount() {
        return flatList.size();
    }
}

