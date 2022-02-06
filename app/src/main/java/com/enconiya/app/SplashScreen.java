package com.enconiya.app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends Activity {
    VideoView videoView;
    FirebaseAuth auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.nee));
        videoView.start();
        videoView.setSoundEffectsEnabled(false);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setVolume(0f,0f);
            }
        });
        videoView.setMediaController(null);
        auth = FirebaseAuth.getInstance();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(auth.getCurrentUser()!=null){
                    startActivity(new Intent(SplashScreen.this,MainActivity.class),ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this).toBundle());
                }
                else {
                    startActivity(new Intent(SplashScreen.this,LoginActivity.class), ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this).toBundle());
                }
            }
        });
    }
}
