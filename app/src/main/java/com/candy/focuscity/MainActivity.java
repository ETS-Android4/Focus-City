package com.candy.focuscity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.HapticFeedbackConstants;
import android.view.View;
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

        // Listeners Initialisation
        buttonStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStop();
            }
        });

        // Activate Seek Bar Time Select
        setTimerValues();

    }

    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {

            // Countdown Timer Start Status
            timerStatus = TimerStatus.STARTED;
            // Change Button Text
            buttonStartStop.setText("Give Up");
            // Select Countdown Time
            setTimerValues();

        } else {

            // Countdown Timer Stop Status
            timerStatus = TimerStatus.STOPPED;
            // Change Button Text
            buttonStartStop.setText("Build");

        }
    }

    private void setTimerValues() {
        seekTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int selectedTime; // Minutes
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedTime = 15 + progress * 15; // Minutes

                if (selectedTime < 60) {
                    building.setText("15");
                } else if (selectedTime < 90) {
                    building.setText("60");
                } else if (selectedTime < 120) {
                    building.setText("90");
                } else if (selectedTime == 120) {
                    building.setText("120");
                }
            textViewTime.setText(selectedTime + ":00");
            timeCountInMilliSeconds = (selectedTime * 1000 * 60) / 60; // Minutes-Seconds (Del/60)
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {};
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {};
        });
    }

}
