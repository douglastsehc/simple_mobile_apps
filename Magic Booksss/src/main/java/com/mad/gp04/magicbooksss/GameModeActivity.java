package com.mad.gp04.magicbooksss;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class GameModeActivity extends AppCompatActivity {
    private static ImageButton mutebutton;
    private static MediaPlayer buttonclick;
    private static boolean play=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the Navigation Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_mode);
        Music();
        play=getIntent().getBooleanExtra("play",play);
        if(!AudioPlay.isPlaying()){
            if(play){
                AudioPlay.resumeAudio();
                Music();
            }
        }
        play=getIntent().getBooleanExtra("play",play);
    }
    protected void Music(){
        mutebutton = (ImageButton)findViewById(R.id.mutebutton);
        if(AudioPlay.isPlaying()){
            mutebutton.setImageResource(R.drawable.soundoff);
            mutebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AudioPlay.pauseAudio();
                    Music();
                }
            });
        }
        else {
            mutebutton.setImageResource(R.drawable.soundon);
            mutebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AudioPlay.resumeAudio();
                    Music();
                }
            });
        }
    }
    public void survival(View view){
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("gamemode", 1);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        buttoncliksound();
        startActivity(intent);
        finish();
    }

    public void timeattack(View view){
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("gamemode", 2);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        buttoncliksound();
        startActivity(intent);
        finish();
    }

    public void relax(View view){
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("gamemode", 3);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        buttoncliksound();
        startActivity(intent);
        finish();
    }
    public void learn(View view){
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("gamemode", 4);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        buttoncliksound();
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        Intent intent=new Intent(this,MainActivity.class);
        if(keyCode==KeyEvent.KEYCODE_BACK){
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        if(keyCode==KeyEvent.KEYCODE_MENU) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        if(keyCode==KeyEvent.KEYCODE_HOME) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void buttoncliksound(){
        if(AudioPlay.isPlaying()) {
            buttonclick = MediaPlayer.create(this, R.raw.buttonclick);
            buttonclick.start();
        }
    }
}
