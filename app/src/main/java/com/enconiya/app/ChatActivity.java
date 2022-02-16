package com.enconiya.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.enconiya.app.Adapters.ChatTextAdapter;
import com.enconiya.app.Datasets.ChatDataset;
import com.enconiya.app.Datasets.ChatTextDataset;
import com.enconiya.app.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
ActivityChatBinding binding;
DatabaseReference reference,userref;
TextView title, subtitle;
ImageView icon, exit, notificationbtn, pinnedbtn;
EditText chatbox;
String name = "";
String img = "";
ImageButton sendbtn;
RecyclerView recyclerView;
ChatTextAdapter adapter;
ArrayList<ChatTextDataset> arrayList;
ArrayList<String> keys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        title = binding.titleChat;
        subtitle = binding.subtitleChat;
        icon = binding.imageChatIcon;
        exit = binding.exitFormChat;
        chatbox = binding.textfieldChat;
        sendbtn = binding.sendMessageButton;
        recyclerView = binding.recyclerChats;
        notificationbtn = binding.btnNotification;
        pinnedbtn = binding.btnPinned;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        keys = new ArrayList<>();
        Intent intent = getIntent();
        String asholkey = intent.getStringExtra("asholkey");
        reference = FirebaseDatabase.getInstance().getReference().child("chats").child(asholkey);
        userref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue(String.class);
                img = snapshot.child("img").getValue(String.class);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new ChatTextAdapter(arrayList,keys,ChatActivity.this,name,FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatDataset dataset = snapshot.getValue(ChatDataset.class);
                title.setText(dataset.getName());
                subtitle.setText(dataset.getTyping());
                Glide.with(ChatActivity.this).load(dataset.getImage()).into(icon);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                keys.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ChatTextDataset dataset = ds.getValue(ChatTextDataset.class);
                    if(!arrayList.contains(dataset)) {
                        arrayList.add(dataset);
                        keys.add(ds.getKey());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatActivity.this,MainActivity.class));
            }
        });
        chatbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()!=0){
                    reference.child("typing").setValue(name+" is typing...");
                    chatbox.setClickable(true);
                }
                else {
                    reference.child("typing").setValue("");
                    chatbox.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text  = chatbox.getText().toString();
                if(!text.isEmpty()) {
                    reference.child("chats").push().setValue(new ChatTextDataset(text, name, img, new Date().toLocaleString(),FirebaseAuth.getInstance().getCurrentUser().getUid().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            chatbox.setText("");
                            recyclerView.smoothScrollToPosition(arrayList.size());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ChatActivity.this);
                View root = LayoutInflater.from(ChatActivity.this).inflate(R.layout.successdialog,(LinearLayout) findViewById(R.id.alertdialog_container));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(root);
                TextView title = root.findViewById(R.id.alert_title);
                TextView message = root.findViewById(R.id.body_text_alerr_dialog);
                title.setText("Are you sure?");
                message.setText("Are You Sure That You want to send notification");
                dialog.show();
                root.findViewById(R.id.btn_success_alert_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        pinnedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}