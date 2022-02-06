package com.enconiya.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.enconiya.app.Datasets.NoticeDataset;
import com.enconiya.app.R;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {
    ArrayList<NoticeDataset> arrayList;
    ArrayList<String> keys;
    Context context;

    public NoticeAdapter(ArrayList<NoticeDataset> arrayList, ArrayList<String> keys, Context context) {
        this.arrayList = arrayList;
        this.keys = keys;
        this.context = context;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoticeViewHolder(LayoutInflater.from(context).inflate(R.layout.notice_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        NoticeDataset dataset = arrayList.get(position);
        holder.title.setText(dataset.getTitle());
        holder.desc.setText(dataset.getDesc());
        holder.date.setText(dataset.getDate());
        Glide.with(context).load(dataset.getImg()).into(holder.bannerimage);
        holder.linkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(dataset.getLink()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerimage;
        TextView title, desc, date;
        ImageButton linkbtn;
        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerimage = itemView.findViewById(R.id.image_notice);
            title = itemView.findViewById(R.id.notice_title);
            desc = itemView.findViewById(R.id.notice_decription);
            date = itemView.findViewById(R.id.time_date_notice);
            linkbtn = itemView.findViewById(R.id.notice_open_btn);
        }
    }
}
