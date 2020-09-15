package com.snaggly.ksw_soundrestorer;

import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;

public class SoundManager {
    private AudioManager am;
    private KeyEvent playEvent;
    private KeyEvent pauseEvent;
    private boolean isMusicPlaying = false;
    private boolean inLoop = true;
    private Thread checkerThread;

    public SoundManager(Context context){
        am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        playEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY);
        pauseEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE);
        checkerThread = new CheckIfMusicPlayingThread();
    }

    public boolean getCurrentPlayingState(){
        return am.isMusicActive();
    }

    public void unpause(){
        if (isMusicPlaying){
            am.dispatchMediaKeyEvent(pauseEvent);
            am.dispatchMediaKeyEvent(playEvent);
        }
    }

    public void forceunpause(){
        am.dispatchMediaKeyEvent(pauseEvent);
        am.dispatchMediaKeyEvent(playEvent);
    }

    public void startCheckingThread(){
        inLoop = true;
        checkerThread.start();
    }

    public void stopCheckingThread(){
        inLoop = false;
    }

    class CheckIfMusicPlayingThread extends Thread{
        @Override
        public void run() {
            while(inLoop){
                isMusicPlaying = am.isMusicActive();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
