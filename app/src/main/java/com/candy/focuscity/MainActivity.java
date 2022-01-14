package com.candy.focuscity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Focus City
 * @author : Andy Jun Cheng Low
 * Part of the timer code was referenced from androidtutorialshub.com
 */
public class MainActivity extends AppCompatActivity {

    private long timeCountInMilliSeconds = (15 * 1 * 60000) / 60; // Minutes-Seconds for Testing
    private int totalBuildTime = 15; // TODO Move to Building class

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

        // Activate SeekBar Time Select
        setTimerValues();
    }

    /**
     * Wired to StartStop Button to start or stop timer
     */
    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {

            // Countdown Timer Start Status
            timerStatus = TimerStatus.STARTED;
            // Change Button Text
            buttonStartStop.setText("Give Up");
            // Select Countdown Time
            setTimerValues();
            // Initialise Progress Bar
            setProgressBarValues();
            // Start Countdown
            startCountdownTimer();
        } else {

            // Countdown Timer Stop Status
            timerStatus = TimerStatus.STOPPED;
            // Stop Countdown Timer
            countDownTimer.cancel();
            // Change Button Text
            buttonStartStop.setText("Build");
            // Make SeekBar Editable
            seekTime.setEnabled(true);
        }
    }

    /**
     * Select Timer Countdown Values by using SeekBar as controller
     */
    private void setTimerValues() {
        // TODO Stylize SeekBar with Gradient
        seekTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int selectedTime; // Minutes
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedTime = 15 + progress * 15; // Minutes
                seekTime.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                // TODO Add PopSounds (Building Class)
                if (selectedTime < 60) {
                    building.setText("15");
                    totalBuildTime = 15;
                } else if (selectedTime < 90) {
                    building.setText("60");
                    totalBuildTime = 60;
                } else if (selectedTime < 120) {
                    building.setText("90");
                    totalBuildTime = 90;
                } else if (selectedTime == 120) {
                    building.setText("120");
                    totalBuildTime = 120;
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

    /**
     * Method to Start Countdown
     */
    private void startCountdownTimer(){
        // TODO Change buildings to Images
        building.setText("0");
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long elapsed = (timeCountInMilliSeconds - millisUntilFinished) / 1000 * 60; // Seconds*60 Del*60
                if (elapsed == 60*60) {
                    building.setText("60");
                } else if (elapsed == 90*60) {
                    building.setText("90");
                } else if (elapsed == 120*60) {
                    building.setText("120");
                }
                System.out.println(elapsed); // TODO Remove Debugging
                textViewTime.setText(msTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                // TODO Build Notifications

                // Shows a Popup Dialog
                // TODO Implement Failed Popup
                createNewPopupDialog();
                // Reset Button Text to Build
                buttonStartStop.setText("Build");
                // Make SeekBar Editable
                seekTime.setEnabled(true);
                // Change Timer Status to STOPPED
                timerStatus = TimerStatus.STOPPED;
                // Reset Progress Bar Values
                setProgressBarValues();
                // Reset Time TextView
                textViewTime.setText(msTimeFormatter(timeCountInMilliSeconds));

            }
        };
        countDownTimer.start();
    }

    /**
     * Method to set Circular Progress Bar Values
     */

    public void setProgressBarValues() {
        progressBarCircle.setMax((int) timeCountInMilliSeconds/1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds/1000);
    }

    /**
     * Method to convert MilliSeconds to Minute Time Format
     */
    private String msTimeFormatter(long milliSeconds) {
        String ms = String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return ms;
    }

    /**
     * Create a Popup window on build success
     */
    private void createNewPopupDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        TextView popupBuildingView;
        Button closeButton;

        final View popupView = getLayoutInflater().inflate(R.layout.popup, null);
        popupBuildingView = (TextView) popupView.findViewById(R.id.popupBuildingView); // TODO Change to Image
        closeButton = (Button) popupView.findViewById(R.id.closeButton);

        popupBuildingView.setText(String.format("%d", totalBuildTime));

        dialogBuilder.setView(popupView);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialog.dismiss(); }
        });
    }

}
