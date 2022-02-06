package com.enconiya.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.enconiya.app.Adapters.NoticeAdapter;
import com.enconiya.app.Datasets.NoticeDataset;
import com.enconiya.app.Datasets.ProfileDataset;
import com.enconiya.app.databinding.ActivityNoticeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {
ActivityNoticeBinding binding;
RecyclerView recyclerView;
DatabaseReference reference;
DatabaseReference userref;
ArrayList<String> targets;
ArrayList<NoticeDataset> arrayList;
ArrayList<String> keys;
NoticeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView = binding.recyclerNotice;
        targets = new ArrayList<>();
        arrayList = new ArrayList<>();
        keys = new ArrayList<>();
        adapter = new NoticeAdapter(arrayList,keys,this);
        reference = FirebaseDatabase.getInstance().getReference().child("notice");
        userref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userref.child("chatlist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    targets.add(ds.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recyclerView.removeAllViews();
                arrayList.clear();
                keys.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    NoticeDataset dataset = ds.getValue(NoticeDataset.class);
                    if(!arrayList.contains(dataset)){
                        if (targets.contains(dataset.getTarget())){
                            arrayList.add(dataset);
                            keys.add(ds.getKey());
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setAdapter(adapter);
    }
}