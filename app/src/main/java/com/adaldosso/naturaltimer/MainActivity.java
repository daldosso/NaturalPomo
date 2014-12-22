package com.adaldosso.naturaltimer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

enum TimerType { WORK, REST }

public class MainActivity extends Activity {

    public static final int WORK_TIME = 25;
    public static final int REST_TIME = 5;
    private CountDownTimer timer;
    private MediaPlayer tick, ding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tick = MediaPlayer.create(getApplication(), R.raw.clock);
        ding = MediaPlayer.create(getApplication(), R.raw.ding);
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

    private void startTimer(int time, final TimerType timerType) {
        if (timer != null) {
            timer.cancel();
        }
        final TextView clock = (TextView) findViewById(R.id.clock);
        timer = new CountDownTimer(time * 60 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = (int) ((millisUntilFinished / 1000) / 60);
                if (timerType == TimerType.WORK) {
                    tick.start();
                }
                clock.setText(String.format("%d:%02d", minutes, seconds % 60));
            }

            public void onFinish() {
                ding.start();
                if (timerType == TimerType.WORK) {
                    startTimer(REST_TIME, TimerType.REST);
                } else {
                    startTimer(WORK_TIME, TimerType.WORK);
                }
            }
        }.start();
    }

    public void startWork(View view) {
        startTimer(WORK_TIME, TimerType.WORK);
    }

    public void startRest(View view) {
        startTimer(REST_TIME, TimerType.REST);
    }
}
