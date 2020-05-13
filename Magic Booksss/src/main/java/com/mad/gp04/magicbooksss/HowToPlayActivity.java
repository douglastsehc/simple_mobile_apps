package com.mad.gp04.magicbooksss;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class HowToPlayActivity extends AppCompatActivity {
    private static ImageButton mutebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the Navigation Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_how_to_play);
        Music();
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
    @Override
    protected void onStart() {
        super.onStart();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {

        Intent intent=new Intent(this,MainActivity.class);
        if(keyCode==KeyEvent.KEYCODE_BACK){
            
            startActivity(intent);
            finish();
            return true;}
        if(keyCode==KeyEvent.KEYCODE_MENU) {
            
            startActivity(intent);
            finish();
            return true;
        }
        if(keyCode==KeyEvent.KEYCODE_HOME) {
            
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown (keyCode, event);
    }
}
