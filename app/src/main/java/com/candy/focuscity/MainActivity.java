package com.candy.focuscity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.HapticFeedbackConstants;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.candy.focuscity.Model.RecordModel;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.TimeUnit;

/**
 * Focus City
 * @author : Andy Jun Cheng Low
 * Part of the timer code was referenced from androidtutorialshub.com
 */
public class MainActivity extends AppCompatActivity {

    private final String CHANNEL_ID = "Focus";

    private static int totalBuildTime = 15;

    private DatabaseHandler db;
    private RecordModel record;

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
    private TextView textViewTime;
    private Button buttonStartStop;
    private ImageView buildingImage;
    private Building building;
    private long timeCountInMilliSeconds = (15 * 1 * 60000) / 60; // Minutes-Seconds for Testing

    // Drawer Layout
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View Initialisation
        seekTime = (SeekBar) findViewById(R.id.seekBarTimeSelect);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        buttonStartStop = (Button) findViewById(R.id.startStopButton);
        buildingImage = (ImageView) findViewById(R.id.buildingImage);

        navView = (NavigationView) findViewById(R.id.navView);

        // Show Default 15 min Building on Startup
        building = new Building("Jett", R.drawable.jett15);
        buildingImage.setImageResource(R.drawable.jett15);

        // Button Listener
        buttonStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStop();
            }
        });

        // Create Database to Save Data
        db = new DatabaseHandler(getApplicationContext());
        db.openDatabase();

        // Activate SeekBar Time Select
        setTimerValues();

        // Create Notification Channel
        createNotificationChannel();

        // Drawer Layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.recordsPage){
                    Intent buildIntent = new Intent(getApplicationContext(), RecordsActivity.class);
                    startActivity(buildIntent);
                    drawerLayout.closeDrawers();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Wired to StartStop Button to start or stop timer
     */
    private void startStop() {
        // Button shows BUILD
        if (timerStatus == TimerStatus.STOPPED) {

            // Record Start Time
            record = new RecordModel();
            record.setDateTimeFormatted();
            // Countdown Timer Start Status
            timerStatus = TimerStatus.STARTED;
            // Change Button Text
            buttonStartStop.setText("Give Up");
            // Make SeekBar Editable
            seekTime.setEnabled(false);
            // Start Countdown
            startCountdownTimer();
            // TODO Show Warning of Building Destruction Before Starting

        // Button Shows GIVE UP
        } else {

            // Countdown Timer Stop Status
            timerStatus = TimerStatus.STOPPED;
            // Stop Countdown Timer
            countDownTimer.cancel();
            // Change Button Text
            buttonStartStop.setText("Build");
            // Make SeekBar Editable
            seekTime.setEnabled(true);
            textViewTime.setText(totalBuildTime + ":00");
            // TODO Change to Ruins
            building.changeBuilding(totalBuildTime, buildingImage);
            // Shows an Unsuccessful Dialog
            createNewPopupDialog(false);
            // TODO Hold Button to Give up
        }
        // Initialise Progress Bar
        setProgressBarValues();
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
                MediaPlayer popSound = null;

                if (selectedTime < 60) {
                    popSound = MediaPlayer.create(MainActivity.this, R.raw.pope5);
                    totalBuildTime = 15;
                } else if (selectedTime < 90) {
                    popSound = MediaPlayer.create(MainActivity.this, R.raw.popg5);
                    totalBuildTime = 60;
                } else if (selectedTime < 120) {
                    popSound = MediaPlayer.create(MainActivity.this, R.raw.popc6);
                    totalBuildTime = 90;
                } else if (selectedTime == 120) {
                    popSound = MediaPlayer.create(MainActivity.this, R.raw.pope6);
                    totalBuildTime = 120;
                }
                building.changeBuilding(totalBuildTime, buildingImage);

                if (popSound != null) {
                    popSound.start();
                    popSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) { mp.release(); }
                    });
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
        building.changeBuilding(0, buildingImage);
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long elapsed = (timeCountInMilliSeconds - millisUntilFinished) / 1000 * 60; // Seconds*60 Del*60
                if (elapsed == 15*60) {
                    building.changeBuilding(15, buildingImage);
                } else if (elapsed == 60*60) {
                    building.changeBuilding(60, buildingImage);
                } else if (elapsed == 90*60) {
                    building.changeBuilding(90, buildingImage);
                } else if (elapsed == 120*60) {
                    building.changeBuilding(120, buildingImage);
                }
                buildingImage.setImageResource(building.buildingImageViewId);

                System.out.println(elapsed); // TODO Remove Debugging
                textViewTime.setText(msTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {

                // Show Build Complete Notification
                createNotifications();
                // Shows a Successful Popup Dialog
                createNewPopupDialog(true);

                // Record to Database
                record.setTotalMinutes(totalBuildTime);
                record.setBuildingImageId(building.buildingImageViewId);
                db.insertRecord(record);

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
                // Reset Building
                building = new Building("Jett", R.drawable.jett15);
                building.changeBuilding(totalBuildTime, buildingImage);
                // TODO Add different Buildings

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
    private void createNewPopupDialog(boolean success) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        ImageView popupBuildingView;
        TextView buildSuccessText;
        Button closeButton;

        final View popupView = getLayoutInflater().inflate(R.layout.popup, null);
        popupBuildingView = (ImageView) popupView.findViewById(R.id.popupBuildingView);
        closeButton = (Button) popupView.findViewById(R.id.closeButton);
        buildSuccessText = (TextView) popupView.findViewById(R.id.buildSuccessText);

        if (!success) {
            buildSuccessText.setText("Construction Failed :(");
        } else {
            buildSuccessText.setText("Construction Complete !!!");
        }

        building.changeBuilding(totalBuildTime, buildingImage);
        popupBuildingView.setImageResource(building.buildingImageViewId);

        dialogBuilder.setView(popupView);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialog.dismiss(); }
        });
    }

    /**
     * Create a Notification Channel for API 26+ only
     */
    private void createNotificationChannel() {
        // Create notification channel for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Build Complete Notification";
            String description = "Channel for build complete notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotifications() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Construction Complete")
                .setContentText("Well Done!")
                .setVibrate(new long[] {500,500,500,500})
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(100, builder.build());
    }

}
