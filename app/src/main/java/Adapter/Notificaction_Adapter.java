package Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Modal.Notification_Modal;
import app.com.ummah.R;

    public class Notificaction_Adapter  extends RecyclerView.Adapter<Adapter.Notificaction_Adapter.MyViewHolder> {
        private Context mContext;
        private ArrayList<Notification_Modal> flatList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView thumbnail;
            public ImageView favorate;
            TextView tv_Name, Tv_TextArea,Tv_title;

            public MyViewHolder(View view) {
                super(view);
                tv_Name = (TextView) view.findViewById(R.id.tv_Name);
                Tv_TextArea = (TextView) view.findViewById(R.id.Tv_TextArea);
                Tv_title =  (TextView) view.findViewById(R.id.Tv_title);
            }
        }

        public Notificaction_Adapter(Context mContext, ArrayList<Notification_Modal> flatList) {
            this.mContext = mContext;
            this.flatList = flatList;
        }

        @Override
        public Adapter.Notificaction_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notification_education, parent, false);

            return new Adapter.Notificaction_Adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final Adapter.Notificaction_Adapter.MyViewHolder holder, int position) {
            Notification_Modal album = flatList.get(position);
            holder.Tv_TextArea.setText(album.getDescription());
            holder.tv_Name.setText(album.getUsername());
            holder.Tv_title.setText(album.getRole());
          //  holder.Tv_title.setText(album.getTitle());


        }


        @Override
        public int getItemCount() {
            return flatList.size();
        }
    }

