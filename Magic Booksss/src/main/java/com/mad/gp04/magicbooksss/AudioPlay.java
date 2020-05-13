package com.mad.gp04.magicbooksss;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlay {

    public static MediaPlayer mediaPlayer;
    public static boolean isplayingAudio=false;
    public static void playAudio(Context c,int id){
        mediaPlayer = MediaPlayer.create(c,id);
        isplayingAudio=true;
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }
    public static void pauseAudio(){
        if(isplayingAudio&&mediaPlayer!=null) {
            isplayingAudio = false;
            mediaPlayer.pause();
        }
    }
    public static void resumeAudio() {
        if (!isplayingAudio&&mediaPlayer!=null) {
            isplayingAudio = true;
            mediaPlayer.start();
        }
    }
    public static boolean isPlaying(){
        return isplayingAudio;
    }
}