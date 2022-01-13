package com.candy.focuscity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private long timeCountInMilliSeconds = (15 * 1 * 60000) / 60; // Minutes-Seconds for Testing

    // Timer Declaration
    private enum TimerStatus {
        STARTED,
        STOPPED
    }
    private TimerStatus timerStatus = TimerStatus.STOPPED;
    private CountDownTimer countDownTimer;

    // Views Declaration
    private SeekBar seekTime;
    private ProgressBar progressBarCircle;
    private TextView building; // TODO Change to building classes
    private TextView textViewTime;
    private Button buttonStartStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View Initialisation
        seekTime = (SeekBar) findViewById(R.id.seekBarTimeSelect);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        building = (TextView) findViewById(R.id.buildingView);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        buttonStartStop = (Button) findViewById(R.id.startStopButton);
    }

}
