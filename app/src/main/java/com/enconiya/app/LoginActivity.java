package com.enconiya.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.enconiya.app.Datasets.UserDataset;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;

public class LoginActivity extends AppCompatActivity {
    EditText email, pass;
    FirebaseAuth auth;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.auth_email);
        pass = (EditText) findViewById(R.id.auth_pass);
        button = findViewById(R.id.loginbtn);
        auth = FirebaseAuth.getInstance();
        button.setOnClickListener((v)->{
            String mail = email.getText().toString().trim();
            String pas = pass.getText().toString();
            if(mail.isEmpty()){
                shownegdialog("Invalid E-mail","Please Enter the Email Address");
            }
            else if(pas.isEmpty()){
                shownegdialog("Password Empty","Please Enter The Password");
            }
            else {
                auth.signInWithEmailAndPassword(mail,pas)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(authResult.getUser().getUid());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserDataset dataset = snapshot.getValue(UserDataset.class);
                                showdialog("Logged in!","Welcome "+dataset.getName()+", Press next to continue","Next","Close");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        shownegdialog(e.getLocalizedMessage(),"Please Close the dialog and try again to login");
                    }
                });
            }
        });
    }
    private void showdialog(String title, String message, String posbtntxt, String negbtntxt){
        BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(posbtntxt, R.drawable.ic_baseline_navigate_next_24, new BottomSheetMaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                    }
                })
                .setNegativeButton(negbtntxt, R.drawable.ic_baseline_close_24, new BottomSheetMaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();
        mBottomSheetDialog.show();
    }
    private void shownegdialog(String title, String message){
        BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Close", R.drawable.ic_baseline_close_24, new BottomSheetMaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();
        mBottomSheetDialog.show();
    }
}