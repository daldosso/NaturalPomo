package com.adaldosso.naturaltimer;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {

    private AudioManager audioManager;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startWork(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startTimer(int time) {
        if (timer != null) {
            timer.cancel();
        }
        final TextView clock = (TextView) findViewById(R.id.clock);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        final MediaPlayer mp = MediaPlayer.create(getApplication(), R.raw.clock);
        timer = new CountDownTimer(time * 60 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = (int) ((millisUntilFinished / 1000) / 60);
                mp.start();
                clock.setText(String.format("%d:%02d", minutes, seconds % 60));
//                audioManager.playSoundEffect(android.view.SoundEffectConstants.CLICK);
            }

            public void onFinish() {
                clock.setText("done!");
            }
        }.start();
    }

    public void startWork(View view) {
        startTimer(25);
    }

    public void startRest(View view) {
        startTimer(5);
    }
}
