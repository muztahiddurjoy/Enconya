package com.enconiya.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.enconiya.app.Adapters.ChatListAdapter;
import com.enconiya.app.Datasets.ChatListDatasetOur;
import com.enconiya.app.Datasets.UserDataset;
import com.enconiya.app.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button noticebtn;
    ActivityMainBinding binding;
    RecyclerView recyclerView;
    ArrayList<String> keys;
    ArrayList<ChatListDatasetOur> arrayList;
    DatabaseReference reference;
    ChatListAdapter chatListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        keys = new ArrayList<>();
        arrayList = new ArrayList<>();
        recyclerView = binding.recyclerChatlist;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatListAdapter = new ChatListAdapter(arrayList,keys,MainActivity.this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        noticebtn = binding.noticebtn;
        binding.actionbarMain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id =  item.getItemId();
                switch (id){
                    case R.id.user_menu:
                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                        break;
                    case R.id.tv_menu:
                        Toast.makeText(MainActivity.this, "Ad Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.add_menu:
                        Toast.makeText(MainActivity.this, "Add Selected", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        reference.child("chatlist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                keys.clear();
                for(DataSnapshot ds: snapshot.getChildren()) {
                    String ashole= ds.getValue(String.class);
                    FirebaseMessaging.getInstance().subscribeToTopic(ashole);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chats").child(ashole);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ChatListDatasetOur dataset = snapshot.getValue(ChatListDatasetOur.class);
                            if(!arrayList.contains(dataset)){
                                arrayList.add(dataset);
                                keys.add(snapshot.getKey());
                            }
                            chatListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setAdapter(chatListAdapter);
//        signout = findViewById(R.id.signout);
//        signout.setOnClickListener((v)->{
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(MainActivity.this,LoginActivity.class));
//        });
    }
}