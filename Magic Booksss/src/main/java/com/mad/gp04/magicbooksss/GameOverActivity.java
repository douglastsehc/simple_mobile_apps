package com.mad.gp04.magicbooksss;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.TextView;


public class GameOverActivity extends AppCompatActivity {
    private static int highestscore=0;
    private static int gamemode=0;
    private static MediaPlayer gameover;
    private static MediaPlayer buttonclick;
    private static boolean soundon=true;
    private static boolean play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the Navigation Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over);
        loadhighestscore();
        Music();
        gameover = MediaPlayer.create(this, R.raw.gameover);
        gameover.start();
    }
    protected void Music(){
        if(AudioPlay.isPlaying()) {
            soundon=true;
            AudioPlay.pauseAudio();
        }else
        {
            soundon=false;
        }
    }
    private void loadhighestscore() {
        gamemode=getIntent().getIntExtra("gamemode", 0);
        play=getIntent().getBooleanExtra("play",false);
        String highestscoremode=null;
        if(gamemode==1){
            highestscoremode="highestscore1";
        }
        if(gamemode==2){
            highestscoremode="highestscore2";
        }
        if(gamemode==3){
            highestscoremode="highestscore3";
        }
        if(gamemode==4){
            highestscoremode="highestscore4";
        }
        SharedPreferences prefs = this.getSharedPreferences(highestscoremode, Context.MODE_PRIVATE);
        highestscore = prefs.getInt(highestscoremode, 0); //0 is the default value
        TextView topic= (TextView)findViewById(R.id.textView_gamemode);
        TextView highest= (TextView)findViewById(R.id.textView_highestscore);
        if(gamemode==1){
            topic.setText("Survival Mode");
            topic.setTextSize(25);
            topic.setTextColor(Color.parseColor("#9a7b69"));
            highest.setText("Highest Score: " + highestscore);
        }
        if(gamemode==2){
            topic.setText("Time Attack Mode");
            topic.setTextSize(25);
            topic.setTextColor(Color.parseColor("#9a7b69"));
            highest.setText("Highest Score: " + highestscore);
        }
        if(gamemode==3){
            topic.setText("Relax Mode");
            topic.setTextSize(25);
            topic.setTextColor(Color.parseColor("#9a7b69"));
            highest.setText("Highest Score: " + highestscore);
        }
        if(gamemode==4){
            topic.setText("Learning Mode");
            topic.setTextSize(25);
            topic.setTextColor(Color.parseColor("#9a7b69"));
            highest.setText("Highest Score: " + highestscore);
        }
        TextView score= (TextView)findViewById(R.id.textView_score);
        score.setText("Score: " + getIntent().getIntExtra("score", 0));
    }

    public void restart(View v)
    {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("gamemode", gamemode);
        intent.putExtra("play", play);
        stopmusic();
        buttoncliksound();
        startActivity(intent);
        finish();
    }
    public void stopmusic()
    {
        if(gameover!=null){
            if(gameover.isPlaying())
                gameover.stop();
        }
    }
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        Intent intent=new Intent(this,GameModeActivity.class);
        intent.putExtra("play", play);
        if(keyCode==KeyEvent.KEYCODE_BACK){
            stopmusic();
            startActivity(intent);
            finish();
            return true;}
        if(keyCode==KeyEvent.KEYCODE_MENU) {
            stopmusic();
            startActivity(intent);
            finish();
            return true;
        }
        if(keyCode==KeyEvent.KEYCODE_HOME) {
            stopmusic();
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void buttoncliksound(){
        if(soundon) {
            buttonclick = MediaPlayer.create(this, R.raw.buttonclick);
            buttonclick.start();
        }
    }
}
