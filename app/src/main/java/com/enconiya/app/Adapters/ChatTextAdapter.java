package com.enconiya.app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.enconiya.app.Datasets.ChatTextDataset;
import com.enconiya.app.R;

import java.util.ArrayList;

public class ChatTextAdapter extends RecyclerView.Adapter{
    ArrayList<ChatTextDataset> arrayList;
    ArrayList<String> keys;
    Context context;
    String name;
    String uid;

    public ChatTextAdapter(ArrayList<ChatTextDataset> arrayList, ArrayList<String> keys, Context context, String name, String uid) {
        this.arrayList = arrayList;
        this.keys = keys;
        this.context = context;
        this.name = name;
        this.uid = uid;
    }


    @Override
    public int getItemViewType(int position) {
        if(arrayList.get(position).getUid().equals(uid)){
            return 1;
        }
        else {
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1){
            return new OwnRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.own_chat_text_adapter,parent,false));
        }
        else {
            return new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_text_adapter,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ChatTextDataset dataset = arrayList.get(position);
        if(arrayList.get(position).getUid().equals(uid)){
            OwnRecyclerViewHolder holderown = (OwnRecyclerViewHolder) holder;
            holderown.name.setText(dataset.getName());
            holderown.text.setText(dataset.getText());
            Glide.with(context).load(dataset.getImage()).into(holderown.use_dp);
            holderown.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    TooltipCompat.setTooltipText(holder.itemView,arrayList.get(position).getTimedate());
                    return true;
                }
            });
        }
        else {
            RecyclerViewHolder holder1 = (RecyclerViewHolder) holder;
            holder1.name.setText(dataset.getName());
            holder1.text.setText(dataset.getText());
            Glide.with(context).load(dataset.getImage()).into(holder1.user_dp);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name, text;
        ImageView user_dp;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username_sender);
            text = itemView.findViewById(R.id.text_message_sender);
            user_dp = itemView.findViewById(R.id.image_sender);
        }
    }
    public class OwnRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView name, text;
        ImageView use_dp;
        public OwnRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username_own);
            text = itemView.findViewById(R.id.text_message_own);
            use_dp = itemView.findViewById(R.id.image_own);
        }
    }
}
