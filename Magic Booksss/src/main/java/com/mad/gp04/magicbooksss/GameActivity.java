package com.mad.gp04.magicbooksss;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    //to store current imageID of the imagebuttons
    private static int[] imagebutton_imageIDs = new int[3];
    private static int currentImageid=0;
    // to store score textview
    private static TextView textviewscore;
    // to store timer textview
    private static TextView textviewtimer;
    // to store the topic of the image
    private static ImageButton image_topic;
    // to store the 3 imagebuttons
    private static ImageButton[] imageButtons = new ImageButton[3];
    // to generat random number
    private static Random randomGenerator = new Random();
    // total number of images
    private static int totalimgNum=30;
    private static int totalfruitimgNum=7;
    // number of correct images
    private static int correctimgNum=10;
    // screen height & width
    private static int width=0;
    private static int height=0;
    private static float xDpi=0;
    private static float yDpi=0;
    // to store the score
    private static int score=0;
    private static int highestscore=0;
    // thread sleep gap
    private static int ANIMATION_SLEEP_GAP = 16 ;
    private static int DETECTION_SLEEP_GAP = 45;
    // relative movement factor
    private static double factor=0.0025;
    private static float yDistance=0;
    // to store the gaming state
    public static boolean lost=false;
    // game mode
    private static int gamemode=1;
    //countdown timer
    private CountDownTimer timer;
    //current level
    private static int currentlevel;
    // mutebutton
    private static ImageButton mutebutton;
    // to store the status of loading
    private static boolean loaded=false;
    //to play sound effect
    private static MediaPlayer soundeffect=null;
    //animation
    private static AlphaAnimation anim;
    //boolean play
    private static boolean play=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Hide the Navigation Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        resetvalue(); //reset value
        initializeScreenResoultion(); //initalize Screen Resoultion
        initalizeImageButton();
        initializeTextView(); //initalize textview
        initializeImageButtons();  //initalize imagebuttons
        initializeAnimation();
        loadhighestscore(); //load highest score()
        Music();
    }

    private void initializeAnimation() {
        anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(500);
        anim.setRepeatCount(4);
        anim.setRepeatMode(Animation.REVERSE);
    }

    protected void Music(){
        mutebutton = (ImageButton)findViewById(R.id.mutebutton);
        if(AudioPlay.isPlaying()){
            mutebutton.setImageResource(R.drawable.soundoff);
            mutebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AudioPlay.pauseAudio();
                    if(soundeffect!=null)
                        if(soundeffect.isPlaying())
                            soundeffect.stop();
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
    private void initalizeImageButton() {
        image_topic = (ImageButton)findViewById(R.id.image_topic);
        gamemode=getIntent().getIntExtra("gamemode", 0);
        play=getIntent().getBooleanExtra("play",play);
        if(!AudioPlay.isPlaying()){
            if(play){
                AudioPlay.resumeAudio();
                Music();
            }
        }
    }

    private void resetvalue(){
        score=0;
        lost=false;
        factor=0.0025;
        yDistance=(float)(height*factor);
        loaded=false;
    }
    private void loadhighestscore() {
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
    }
    private void savehighestscore(){
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
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(highestscoremode, highestscore);
        editor.commit();
    }
    protected void initializeTextView(){
        textviewscore = (TextView) findViewById(R.id.score);
        textviewtimer = (TextView) findViewById(R.id.timer);
        System.out.println("TextView initalized ");
    }
    //to initalize imagebuttons
    protected void initializeImageButtons() {
        for (int i = 0; i < imageButtons.length; i++) {
            String imagebuttonID = "imageButton" + i;
            imageButtons[i] = (ImageButton) findViewById(getResources().getIdentifier(imagebuttonID, "id", getPackageName()));
            setImage(i);
            randompostition(imageButtons[i]);
            imageButtons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    checkcorrectImageButton((ImageButton) v);
                }
            });
            addanimation(imageButtons[i]);
        }
        if(gamemode==1)
            survival();
        if(gamemode==2)
            timeattack();
        if(gamemode==3)
            relax();
        if(gamemode==4)
            learn();
        System.out.println("Gamemode = " + gamemode);
        System.out.println("ImageButtons initalized ");
    }

    private void addanimation(final ImageButton x) {
        //calculate the relative moving distance
        yDistance=(float)(height*factor);
        System.out.println(" yDistance = " + yDistance);
        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                loaded=true;
            }
        }.start();
        new Thread(new Runnable() { // create a new thread to animate, and then start it.
            @Override
            public void run(){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        Thread.sleep(ANIMATION_SLEEP_GAP);
                        x.post(new Runnable() {
                            @Override
                            public void run() {
                                x.setY(x.getY() + yDistance);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(lost)
                        Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
    // check the button collision and unclicked button
    private void survival() {
        for (int i = 0; i < imageButtons.length; i++) {
            String imagebuttonID = "imageButton" + i;
            imageButtons[i] = (ImageButton) findViewById(getResources().getIdentifier(imagebuttonID, "id", getPackageName()));
            setImage(i);
            randompostition(imageButtons[i]);
            imageButtons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    checkcorrectImageButton((ImageButton) v);
                }
            });
        }
        new Thread(new Runnable() { // create a new thread to animate, and then start it.
            @Override
            public void run(){
                int id=-1;
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        Thread.sleep(DETECTION_SLEEP_GAP);
                        for(int i=0;i<imageButtons.length;i++) {
                            if(imageButtons[i].getY()>height) {
                                if(imagebutton_imageIDs[i]<=10) {
                                    if(!lost){
                                        System.out.println("Game Over !");// correct image but didn't click
                                        imageButtons[i].post(new Runnable() {
                                            @Override
                                            public void run() {
                                                GameOver();
                                            }
                                        });
                                    }
                                }
                                else {
                                    final int tempI = i;
                                    imageButtons[i].post(new Runnable() {
                                        @Override
                                        public void run() {
                                            setImage(tempI);
                                            randompostition(imageButtons[tempI]);
                                        }
                                    });
                                    System.out.println("Collision is detected! id=" + i);
                                }
                            }
                        }
                        while((id=iscollision())>=0){
                            final int finalId = id;
                            imageButtons[id].post(new Runnable() {
                                @Override
                                public void run() {
                                    randompostition(imageButtons[finalId]);
                                }
                            });
                            Thread.sleep(100);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(lost)
                        Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
    private void timeattack() {
        factor=0.005;
        yDistance=(float)(height*factor);
        new CountDownTimer(30000, 100) {
            boolean soundeffect=false;
            boolean animation=false;
            public void onTick(long millisUntilFinished) {
                textviewtimer.setText("Time:  " + millisUntilFinished / 1000);
                if((millisUntilFinished / 1000)==4&&!soundeffect) {
                    soundeffect=true;
                    soundeffect(3);
                }
                if((millisUntilFinished / 1000)==2&&!animation) {
                    animation=true;
                    textviewtimer.startAnimation(anim);
                }
                if(lost) {
                    this.cancel();
                }
            }
            public void onFinish() {
                textviewtimer.setText("Time: 0");
                if(!lost) {
                    GameOver();
                }
            }
        }.start();
        for (int i = 0; i < imageButtons.length; i++) {
            imageButtons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    checktimeattackImageButton((ImageButton) v);
                }
            });
        }

        new Thread(new Runnable() { // create a new thread to animate, and then start it.
            @Override
            public void run(){
                int id=-1;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        Thread.sleep(DETECTION_SLEEP_GAP);
                        for(int i=0;i<imageButtons.length;i++) {
                            if(imageButtons[i].getY()>height) {
                                final int tempI = i;
                                imageButtons[i].post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setImage(tempI);
                                        randompostition(imageButtons[tempI]);
                                    }
                                });
                            }
                        }
                        while((id=iscollision())>=0){
                            final int finalId = id;
                            imageButtons[id].post(new Runnable() {
                                @Override
                                public void run() {
                                    randompostition(imageButtons[finalId]);
                                }
                            });
                            Thread.sleep(100);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(lost)
                        Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
    // check the button collision and unclicked button
    private void relax() {
        factor=0.0045;
        yDistance=(float)(height*factor);
        for(int i=0;i<imageButtons.length;i++){
            setFruitImageButton(imageButtons[i]);
        }
        GameActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                changeTopic();
            }
        });
        timer = new CountDownTimer(20000, 100) {
            boolean soundeffect=false;
            boolean animation=false;
            public void onTick(long millisUntilFinished) {
                textviewtimer.setText("Time:  " + millisUntilFinished / 1000);
                if((millisUntilFinished / 1000)==4&&!soundeffect) {
                    soundeffect = true;
                    soundeffect(3);
                }
                if((millisUntilFinished / 1000)==2&&!animation) {
                    animation=true;
                    image_topic.startAnimation(anim);
                }
                if(lost) {
                    this.cancel();
                }
            }
            public void onFinish() {
                textviewtimer.setText("Time:  0");
                changeTopic();
                try{
                    looptimer();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();
        new Thread(new Runnable() { // create a new thread to animate, and then start it.
            @Override
            public void run(){
                while (!Thread.currentThread().isInterrupted()){
                    int id=-1;
                    try {
                        Thread.sleep(DETECTION_SLEEP_GAP);
                        for(int i=0;i<imageButtons.length;i++) {
                            if(imageButtons[i].getY()>height) {
                                final int tempI = i;
                                imageButtons[i].post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setFruitImageButton(imageButtons[tempI]);
                                        randompostition(imageButtons[tempI]);
                                    }
                                });
                                System.out.println("Collision is detected! id=" + i);
                            }
                        }
                        while((id=iscollision())>=0){
                            final int finalId = id;
                            imageButtons[id].post(new Runnable() {
                                @Override
                                public void run() {
                                    randompostition(imageButtons[finalId]);
                                }
                            });
                            Thread.sleep(100);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(lost) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
    }
    private void learn() {
        factor=0.005;
        yDistance=(float)(height*factor);
        for(int i=0;i<imageButtons.length;i++){
            setLearnImageButton(imageButtons[i]);
        }
        GameActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                changeLearnTopic();
            }
        });
        timer = new CountDownTimer(15000, 100) {
            boolean soundeffect=false;
            boolean animation=false;
            public void onTick(long millisUntilFinished) {
                textviewtimer.setText("Time:  " + millisUntilFinished / 1000);
                if((millisUntilFinished / 1000)==4&&!soundeffect) {
                    soundeffect=true;
                    soundeffect(3);
                }
                if((millisUntilFinished / 1000)==2&&!animation) {
                    animation=true;
                    image_topic.startAnimation(anim);
                }
                if(lost) {
                    this.cancel();
                }
            }
            public void onFinish() {
                textviewtimer.setText("Time:  0");
                changeLearnTopic();
                try{
                    if(!lost)
                        looptimer();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();
        new Thread(new Runnable() { // create a new thread to animate, and then start it.
            @Override
            public void run(){
                while (!Thread.currentThread().isInterrupted()){
                    int id=-1;
                    try {
                        Thread.sleep(DETECTION_SLEEP_GAP);
                        for(int i=0;i<imageButtons.length;i++) {
                            if(imageButtons[i].getY()>height) {
                                final int tempI = i;
                                imageButtons[i].post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setLearnImageButton(imageButtons[tempI]);
                                        randompostition(imageButtons[tempI]);
                                    }
                                });
                                System.out.println("Collision is detected! id=" + i);
                            }
                        }
                        while((id=iscollision())>=0){
                            final int finalId = id;
                            imageButtons[id].post(new Runnable() {
                                @Override
                                public void run() {
                                    randompostition(imageButtons[finalId]);
                                }
                            });
                            Thread.sleep(100);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(lost) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() { // create a new thread to animate, and then start it.
            @Override
            public void run(){
                while (!Thread.currentThread().isInterrupted()){
                    int id=-1;
                    try {
                        Thread.sleep(DETECTION_SLEEP_GAP);
                        for(int i=0;i<imageButtons.length;i++) {
                            if(imageButtons[i].getY()>height) {
                                final int tempI = i;
                                imageButtons[i].post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setLearnImageButton(imageButtons[tempI]);
                                        randompostition(imageButtons[tempI]);
                                    }
                                });
                                System.out.println("Collision is detected! id=" + i);
                            }
                        }
                        while((id=iscollision())>=0){
                            final int finalId = id;
                            imageButtons[id].post(new Runnable() {
                                @Override
                                public void run() {
                                    randompostition(imageButtons[finalId]);
                                }
                            });
                            Thread.sleep(100);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(lost) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
    }
    private void looptimer(){
        timer.start();
    }
    private void changeTopic() {
        int ranNum= randomGenerator.nextInt(totalfruitimgNum)+1;
        String imgName = "img" + ranNum;
        int imgID = getResources().getIdentifier(imgName, "drawable", getPackageName());
        image_topic.setImageResource(imgID);
        currentImageid=ranNum;
        for(int i=0;i<imageButtons.length;i++){
            final int finalI = i;
            imageButtons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(imagebutton_imageIDs[finalI]==currentImageid){
                        plusscore();
                        setLearnImageButton((ImageButton)v);
                        randompostition((ImageButton)v);
                        System.out.println("Correct Button! imagebutton_imageIDs[i] = "+imagebutton_imageIDs[finalI]+"Current = "+currentImageid);
                    }
                    else {
                        System.out.println("Wrong Button! imagebutton_imageIDs[i] = "+imagebutton_imageIDs[finalI]+"Current = "+currentImageid);
                        GameOver();
                    }
                }
            });
        }
    }
    private void changeLearnTopic() {
        currentlevel=((currentlevel)%3)+1;
        String imgName = "level" + currentlevel;
        int imgID = getResources().getIdentifier(imgName, "drawable", getPackageName());
        image_topic.setImageResource(imgID);
        for(int i=0;i<imageButtons.length;i++){
            final int finalI = i;
            imageButtons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(imagebutton_imageIDs[finalI]>=(currentlevel*15+(-15)+1)&&imagebutton_imageIDs[finalI]<=(currentlevel*15)){
                        plusscore();
                        setLearnImageButton((ImageButton) v);
                        randompostition((ImageButton)v);
                        System.out.println("Correct Button! imagebutton_imageIDs[i] = "+imagebutton_imageIDs[finalI]+"Current = "+(currentlevel*15+(-15)+1)+"-"+(currentlevel*15));
                    }
                    else {
                        System.out.println("Wrong Button! imagebutton_imageIDs[i] = "+imagebutton_imageIDs[finalI]+"Current = "+(currentlevel*15+(-15)+1)+"-"+(currentlevel*15));
                        GameOver();
                    }
                }
            });
        }
    }

    // to initalize Screen Resoultion
    protected void initializeScreenResoultion(){
        SharedPreferences widthprefs = this.getSharedPreferences("width", Context.MODE_PRIVATE);
        SharedPreferences heightprefs = this.getSharedPreferences("height", Context.MODE_PRIVATE);
        SharedPreferences xDpiprefs = this.getSharedPreferences("xDpi", Context.MODE_PRIVATE);
        SharedPreferences yDpiprefs = this.getSharedPreferences("yDpi", Context.MODE_PRIVATE);
        width = widthprefs.getInt("width", 0); //0 is the default value
        height = heightprefs.getInt("height", 0); //0 is the default value
        xDpi = xDpiprefs.getInt("xDpi", 0); //0 is the default value
        yDpi = yDpiprefs.getInt("yDpi", 0); //0 is the default value
        System.out.println("width = " + width + " height = " + height + " xDpi = " + xDpi + " yDpi = " + yDpi);
    }
    //to set random image of the imagebuttons
    protected void setImage(int id) {
        int ranNum= randomimgid(id);
        String imgName = "img" + ranNum;
        int imgID = getResources().getIdentifier(imgName, "drawable", getPackageName());
        imageButtons[id].setImageResource(imgID);
    }
    // randomize the position of the imagebuttons
    protected void randompostition(ImageButton x) {
        if(!loaded){
            x.setX(20);
            x.setY(-((int) (2 * yDpi) + randomGenerator.nextInt((int) (4 * yDpi) - (int) (2 * yDpi) + 1)));
        }
        else{
            x.setX(20 + randomGenerator.nextInt((int) (width - xDpi)) - 20 + 1);
            x.setY(-((int) (2 * yDpi) + randomGenerator.nextInt((int) (4 * yDpi) - (int) (2 * yDpi) + 1)));
        }
    }

    // collision detection
    protected int iscollision(){
        for(int i=0;i<(imageButtons.length-1);i++) {
            for(int j=(i+1);j<imageButtons.length;j++){
                if(imageButtons[i].getY() < (imageButtons[j].getY()+yDpi) && imageButtons[i].getY() > (imageButtons[j].getY()-yDpi)){
                    return i;
                }
            }
        }
        return -1;
    }
    // to generate a imageid at least one id is correct
    protected int randomimgid(int id){
        int ranNum;
        boolean correctimage=false;
        for(int i=0;i<imagebutton_imageIDs.length;i++) {
            if(imagebutton_imageIDs[i]<=correctimgNum&&i!=id){
                correctimage=true;
                break;
            }
        }
        if(correctimage)
            ranNum=randomGenerator.nextInt(totalimgNum)+1;
        else
            ranNum=randomGenerator.nextInt(correctimgNum)+1;
        imagebutton_imageIDs[id]=ranNum;
        return ranNum;
    }
    //check whether the button clicked is correct or not
    protected void checkcorrectImageButton(ImageButton x){
        for(int i=0;i<imageButtons.length;i++){
            if(x==imageButtons[i]) {
                if(imagebutton_imageIDs[i] <=correctimgNum) {
                    plusscore();
                    setImage(i);
                    randompostition(x);
                    System.out.println("Correct Button! imagebutton_imageIDs[i] = "+imagebutton_imageIDs[i]);
                }
                else {
                    System.out.println("Wrong Button! imagebutton_imageIDs[i] = " + imagebutton_imageIDs[i]);
                    GameOver();
                }
            }
        }
    }
    protected void checktimeattackImageButton(ImageButton x){
        for(int i=0;i<imageButtons.length;i++){
            if(x==imageButtons[i]) {
                if(imagebutton_imageIDs[i] <=correctimgNum) {
                    plusscore();
                    setImage(i);
                    randompostition(x);
                    System.out.println("Correct Button! imagebutton_imageIDs[i] = "+imagebutton_imageIDs[i]);
                }
                else {
                    reducescore();
                    setImage(i);
                    randompostition(x);
                    System.out.println("Wrong Button! imagebutton_imageIDs[i] = "+imagebutton_imageIDs[i]);
                }
            }
        }
    }
    protected void setFruitImageButton(ImageButton x){
        int id=0;
        for(int i=0;i<imageButtons.length;i++){
            if(x==imageButtons[i]) {
                id = i;
                break;
            }
        }
        int ranNum= randomfruitimgid(id);
        String imgName = "fruit" + ranNum;
        int imgID = getResources().getIdentifier(imgName, "drawable", getPackageName());
        x.setImageResource(imgID);
    }
    protected void setLearnImageButton(ImageButton x){
        int id=0;
        for(int i=0;i<imageButtons.length;i++){
            if(x==imageButtons[i]) {
                id = i;
                break;
            }
        }
        int ranNum= randomlearnimgid(id);
        String imgName = "f" + ranNum;
        int imgID = getResources().getIdentifier(imgName, "drawable", getPackageName());
        x.setImageResource(imgID);
    }
    protected int randomfruitimgid(int id){
        int ranNum;
        boolean correctimage=false;
        for(int i=0;i<imagebutton_imageIDs.length;i++) {
            if(imagebutton_imageIDs[i]==currentImageid&&i!=id){
                correctimage=true;
                break;
            }
        }
        if(correctimage)
            ranNum=randomGenerator.nextInt(totalfruitimgNum)+1;
        else
            ranNum=currentImageid;
        imagebutton_imageIDs[id]=ranNum;
        return ranNum;
    }
    protected int randomlearnimgid(int id){
        int ranNum;
        boolean correctimage=false;
        for(int i=0;i<imagebutton_imageIDs.length;i++) {
            if(imagebutton_imageIDs[i]>=(currentlevel*15+(-15)+1)&&imagebutton_imageIDs[i]<=(currentlevel*15)&&i!=id){
                correctimage=true;
                break;
            }
        }
        if(correctimage)
            ranNum=randomGenerator.nextInt(45)+1;
        else
            ranNum=randomGenerator.nextInt((currentlevel*15)-(currentlevel*15+(-15)+1))+(currentlevel*15+(-15)+1);
        imagebutton_imageIDs[id]=ranNum;
        return ranNum;
    }
    // add and update the scoretext
    protected void plusscore() {
        score+=100;
        soundeffect(1);
        textviewscore.setText("Score : " + score);
        System.out.println("Score Added!");
    }
    protected void reducescore() {
        score-=200;
        soundeffect(2);
        textviewscore.setText("Score : " + score);
        System.out.println("Score reduced!");
    }
    protected void soundeffect(int sound){
        MediaPlayer temp = null;
        switch(sound){
            case 1:
                temp = MediaPlayer.create(this, R.raw.correctbutton);
                break;
            case 2:
                temp = MediaPlayer.create(this, R.raw.wrongbutton);
                break;
            case 3:
                temp = MediaPlayer.create(this, R.raw.countdown);
                break;
        }
        if(AudioPlay.isPlaying())
            temp.start();
        temp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
            }
        });
    }
    protected void GameOver()
    {
        if(score>highestscore) {
            highestscore = score;
            savehighestscore();
        }
        lost=true;
        if(soundeffect!=null) {
            if (soundeffect.isPlaying()) {
                soundeffect.stop();
            }
        }
        Intent intent = new Intent(this, GameOverActivity.class); //
        intent.putExtra("gamemode", gamemode);
        intent.putExtra("score", score);
        intent.putExtra("play",AudioPlay.isPlaying() );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lost=true;
        Thread.currentThread().interrupt();
    }
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            lost=true;
            Intent intent=new Intent(this,GameModeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;}
        if(keyCode==KeyEvent.KEYCODE_MENU) {
            lost=true;
            Intent intent=new Intent(this,GameModeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if(keyCode==KeyEvent.KEYCODE_HOME) {
            lost=true;
            if(AudioPlay.isPlaying())
            {
                AudioPlay.pauseAudio();
            }
            Intent intent=new Intent(this,GameModeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
