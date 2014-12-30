package com.adaldosso.naturalpomotimer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

enum TimerType { WORK, LONG_REST, REST }

public class MainActivity extends Activity {

    public static final int WORK_TIME = 25;
    public static final int REST_TIME = 5;
    private static final int POMO_REST_NUMBER = 4;
    private static final int LONG_REST_TIME = 20;
    private CountDownTimer timer;
    private MediaPlayer tick, ding;
    private int pomoCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.adaldosso.naturalpomotimer.R.layout.activity_main);
        tick = MediaPlayer.create(getApplication(), com.adaldosso.naturalpomotimer.R.raw.clock);
        ding = MediaPlayer.create(getApplication(), com.adaldosso.naturalpomotimer.R.raw.ding);
        startWork(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.adaldosso.naturalpomotimer.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == com.adaldosso.naturalpomotimer.R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startTimer(int time, final TimerType timerType) {
        if (timer != null) {
            timer.cancel();
        }
        final TextView clock = (TextView) findViewById(R.id.clock);
        TextView status = (TextView) findViewById(R.id.status);

        switch (timerType) {
            case WORK:
                clock.setTextColor(getResources().getColor(R.color.red));
                status.setText(getResources().getString(R.string.working));
                break;
            case REST:
                clock.setTextColor(getResources().getColor(R.color.green));
                status.setText(getResources().getString(R.string.resting));
                break;
            case LONG_REST:
                clock.setTextColor(getResources().getColor(R.color.green));
                status.setText(getResources().getString(R.string.long_resting));
                break;
        }

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
                pomoCounter++;
                ding.start();
                if (timerType == TimerType.WORK) {
                    if (pomoCounter % POMO_REST_NUMBER == 0 ) {
                        startTimer(LONG_REST_TIME, TimerType.LONG_REST);
                    } else {
                        startTimer(REST_TIME, TimerType.LONG_REST);
                    }
                } else {
                    startTimer(WORK_TIME, TimerType.WORK);
                }
            }
        }.start();
    }

    public void startWork(View view) {
        startTimer(WORK_TIME, TimerType.WORK);
        Button workButton = (Button) findViewById(R.id.workButton);
        workButton.setText(getResources().getString(R.string.restart));
    }

}
