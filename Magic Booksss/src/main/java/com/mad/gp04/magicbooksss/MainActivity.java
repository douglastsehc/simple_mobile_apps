package com.mad.gp04.magicbooksss;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    protected static int width=0;
    protected static int height=0;
    protected static int xDpi=0;
    protected static int yDpi=0;
    private ImageButton mutebutton;
    private static MediaPlayer buttonclick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the Navigation Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getScreen();
        startplaying();
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
    protected void startplaying(){
        if(AudioPlay.mediaPlayer==null){
            Log.e("Main","Playing");
            AudioPlay.playAudio(this.getBaseContext(), R.raw.bgm);
        }
        if(AudioPlay.isPlaying()){
            Log.e("Main", "resume");
            AudioPlay.resumeAudio();
        }else{
            Log.e("Main", "Pauseing");
            AudioPlay.pauseAudio();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                System.exit(0);
                return true;
            case KeyEvent.KEYCODE_MENU:
                System.exit(0);
                return true;
            case KeyEvent.KEYCODE_HOME:
                System.exit(0);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void getScreen(){
        SharedPreferences widthprefs = this.getSharedPreferences("width", Context.MODE_PRIVATE);
        SharedPreferences heightprefs = this.getSharedPreferences("height", Context.MODE_PRIVATE);
        SharedPreferences xDpiprefs = this.getSharedPreferences("xDpi", Context.MODE_PRIVATE);
        SharedPreferences yDpiprefs = this.getSharedPreferences("yDpi", Context.MODE_PRIVATE);
            // get screen width ,height , xDpi , yDpi
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
            xDpi=(dpToPx(200));
            yDpi=(dpToPx(74));
            //save width
            SharedPreferences.Editor widtheditor = widthprefs.edit();
            widtheditor.putInt("width", width);
            widtheditor.commit();
            //save height
            SharedPreferences.Editor heighteditor = heightprefs.edit();
            heighteditor.putInt("height", height);
            heighteditor.commit();
            //save xDpi
            SharedPreferences.Editor xDpieditor = xDpiprefs.edit();
            xDpieditor.putInt("xDpi", xDpi);
            xDpieditor.commit();
            //save yDpi
            SharedPreferences.Editor yDpieditor = yDpiprefs.edit();
            yDpieditor.putInt("yDpi", yDpi);
            yDpieditor.commit();
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public void Us(View view){
        Intent intent = new Intent(this,AboutUsActivity.class);
        buttoncliksound();
        startActivity(intent);
        finish();
    }
    public void Story(View view){
        Intent intent = new Intent(this,StoryActivity.class);
        buttoncliksound();
        startActivity(intent);
        finish();
    }
    public void howtoplay(View view){
        Intent intent = new Intent(this,HowToPlayActivity.class);
        buttoncliksound();
        startActivity(intent);
        finish();
    }
    public void Game(View view){
        Intent intent = new Intent(this,GameModeActivity.class);
        buttoncliksound();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void exit(View view){
        finish();
        System.exit(0);
    }
    public void buttoncliksound(){
        if(AudioPlay.isPlaying()) {
            buttonclick = MediaPlayer.create(this, R.raw.buttonclick);
            buttonclick.start();
        }
    }
}
