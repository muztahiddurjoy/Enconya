package com.enconiya.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.enconiya.app.ChatActivity;
import com.enconiya.app.Datasets.ChatListDatasetOur;
import com.enconiya.app.R;
import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
    ArrayList<ChatListDatasetOur> arrayList;
    ArrayList<String> keys;
    Context context;

    public ChatListAdapter(ArrayList<ChatListDatasetOur> arrayList, ArrayList<String> keys, Context context) {
        this.arrayList = arrayList;
        this.keys = keys;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater.from(context).inflate(R.layout.chatlist_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        ChatListDatasetOur dataset  = arrayList.get(position);
        holder.title.setText(dataset.getName());
        Glide.with(context).load(dataset.getImage()).into(holder.icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("asholkey",keys.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_chatlist);
            icon = itemView.findViewById(R.id.icon_chatlist);
        }
    }
}
