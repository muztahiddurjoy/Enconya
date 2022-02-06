package com.enconiya.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.enconiya.app.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {
ActivityChatBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String asholkey = intent.getStringExtra("asholkey");

    }
}