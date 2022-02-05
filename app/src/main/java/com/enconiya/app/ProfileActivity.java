package com.enconiya.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.enconiya.app.Datasets.ProfileDataset;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
Button notice, watchad, logout;
TextView username,mail;
ImageView profimg;
DatabaseReference reference;
ChipGroup chipGroup;
StorageReference storage;
FloatingActionButton cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = findViewById(R.id.profilename);
        mail = findViewById(R.id.profilemail);
        profimg = findViewById(R.id.profileimg);
        logout = findViewById(R.id.logout_btn);
        chipGroup = findViewById(R.id.chipGroup);
        cardView = findViewById(R.id.edit_profile_img);
        storage = FirebaseStorage.getInstance().getReference().child("users");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chipGroup.removeAllViews();
                ProfileDataset dataset = snapshot.getValue(ProfileDataset.class);
                username.setText(dataset.getName());
                mail.setText(dataset.getEmail());
                ArrayList<String> roles = (ArrayList<String>) dataset.getRoles();
                for (int i = 0; i < roles.size(); i++) {
                    Chip chip = new Chip(ProfileActivity.this);
                    chip.setText(roles.get(i));
                    chip.setChipBackgroundColorResource(R.color.purple_700);
                    chip.setTextColor(getResources().getColor(R.color.white));
                    chipGroup.addView(chip);
                }
                Glide.with(ProfileActivity.this).load(dataset.getImg()).into(profimg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,399);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "Logged Out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==399 && resultCode== RESULT_OK && data.getData()!=null){
            showuploadDialog(data.getData());
        }
    }

    private void showuploadDialog(Uri data) {
        LinearProgressIndicator indicator;
        Dialog dialog = new Dialog(ProfileActivity.this);
        dialog.setContentView(R.layout.upload_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        storage.putFile(data).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Task<Uri> task = snapshot.getStorage().getDownloadUrl();
                while (!task.isComplete());
                Uri url = task.getResult();
                Map<String,Object> imgmap = new HashMap<>();
                imgmap.put("img",url.toString());
                reference.updateChildren(imgmap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                        Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}