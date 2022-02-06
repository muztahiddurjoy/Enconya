package com.enconiya.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enconiya.app.Adapters.ChatListAdapter;
import com.enconiya.app.Datasets.ChatListAddDataset;
import com.enconiya.app.Datasets.ChatListDatasetOur;
import com.enconiya.app.Datasets.UserDataset;
import com.enconiya.app.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button noticebtn;
    ActivityMainBinding binding;
    RecyclerView recyclerView;
    ArrayList<String> keys;
    ArrayList<ChatListDatasetOur> arrayList;
    DatabaseReference reference;
    ChatListAdapter chatListAdapter;
    DatabaseReference referenceChats;
    ArrayList<String> chatlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        referenceChats = FirebaseDatabase.getInstance().getReference().child("chatsecrets");
        setContentView(binding.getRoot());
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        keys = new ArrayList<>();
        arrayList = new ArrayList<>();
        recyclerView = binding.recyclerChatlist;
        recyclerView.setHasFixedSize(true);
        chatlist = new ArrayList<>();
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
                    case R.id.add_menu:
                        openDialogAdd();
                        break;
                }
                return false;
            }

            private void openDialogAdd() {
                Dialog dialog = new Dialog(MainActivity.this);
                View root = LayoutInflater.from(MainActivity.this).inflate(R.layout.addtochatlistdialog, (LinearLayout) findViewById(R.id.adddiacontainer));
                dialog.setContentView(root);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                root.findViewById(R.id.close_addchatbtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                TextInputEditText editText = root.findViewById(R.id.chat_code);
                Button donebtn = root.findViewById(R.id.btn_code);
                TextInputLayout layout = root.findViewById(R.id.textlay_secretcode);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        layout.setErrorEnabled(false);
                        layout.setEndIconVisible(true);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                //Dialog Two
                Dialog successdia = new Dialog(MainActivity.this);
                View rootsucc = LayoutInflater.from(MainActivity.this).inflate(R.layout.successdialog,(LinearLayout) findViewById(R.id.alertdialog_container));
                successdia.setContentView(rootsucc);
                TextView title = rootsucc.findViewById(R.id.alert_title);
                TextView message = rootsucc.findViewById(R.id.body_text_alerr_dialog);
                successdia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                rootsucc.findViewById(R.id.btn_success_alert_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        successdia.dismiss();
                    }
                });
                //Dialog Two Ends
                donebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String typedcode = editText.getText().toString().trim();
                        referenceChats.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean status = false;
                                for(DataSnapshot ds: snapshot.getChildren()) {
                                    ChatListAddDataset dataset = ds.getValue(ChatListAddDataset.class);
                                    if (dataset.getSecretkey().equals(typedcode)) {
                                        status = true;
                                        if(!chatlist.contains(dataset.getMainkey())){
                                            reference.child("chatlist").push().setValue(dataset.getMainkey()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    title.setText("Success!");
                                                    message.setText("Now you are a member of the Chat Group. The Group Has been added to your chatlist");
                                                }
                                            });
                                        }
                                        else {
                                            title.setText("Failed!");
                                            message.setText("You are already a member of the Chat Group");
                                        }
                                    }
                                }
                                if(status){
                                    dialog.dismiss();
                                    successdia.show();
                                }
                                else {
                                    layout.setError("Wrong Group Code!");
                                    layout.setEndIconVisible(false);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        reference.child("chatlist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                keys.clear();
                chatlist.clear();
                for(DataSnapshot ds: snapshot.getChildren()) {
                    String ashole= ds.getValue(String.class);
                    chatlist.add(ashole);
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
        noticebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NoticeActivity.class));
            }
        });
//        signout = findViewById(R.id.signout);
//        signout.setOnClickListener((v)->{
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(MainActivity.this,LoginActivity.class));
//        });
    }
}