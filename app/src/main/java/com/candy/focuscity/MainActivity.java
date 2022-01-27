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
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.candy.focuscity.DatabaseHandlers.BlueprintsDatabaseHandler;
import com.candy.focuscity.DatabaseHandlers.RecordsDatabaseHandler;
import com.candy.focuscity.Model.BlueprintModel;
import com.candy.focuscity.Model.RecordModel;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.TimeUnit;

/**
 * Focus City
 * @author : Andy Jun Cheng Low
 * Part of the timer code was referenced from androidtutorialshub.com
 */
public class MainActivity extends AppCompatActivity {

    // TODO In app TimeScale Adjust
    // Constants
    private final int TIME_SCALE = 60; // Time scale control: 1 for Minutes, 60 for Seconds
    private final String CHANNEL_ID = "Focus";

    // For Timer Use
    protected static int totalBuildTime = 15;
    private long timeCountInMilliSeconds = (15 * 1 * 60000) / TIME_SCALE; // Minutes-Seconds

    // Databases and Saves Handling
    private RecordsDatabaseHandler dbRecords;
    private RecordModel record;
    private BlueprintsDatabaseHandler dbBlueprints;
    private BlueprintModel blueprint;

    // Timer Declaration
    private enum TimerStatus {
        STARTED,
        STOPPED
    }
    private TimerStatus timerStatus = TimerStatus.STOPPED;
    private CountDownTimer countDownTimer;

    // Timer Views Declaration
    private SeekBar seekTime;
    private ProgressBar progressBarCircle;
    private TextView textViewTime;

    // Building Views Declaration
    private String buildingName = "";
    private EditText buildingNameEdit;
    private TextView buildingNameText;
    private ImageView editNameButton;
    protected ImageView buildingImage;
    protected Building building;

    // Buttons Declaration
    private Button buttonStartStop;
    private Button buttonSave;

    // Drawer Declaration
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Timer Views Assignment
        seekTime = (SeekBar) findViewById(R.id.seekBarTimeSelect);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);

        // Building Views Assignment
        buildingNameEdit = (EditText) findViewById(R.id.buildingNameEdit);
        buildingNameText = (TextView) findViewById(R.id.buildingNameText);
        editNameButton = (ImageView) findViewById(R.id.editNameButton);
        buildingImage = (ImageView) findViewById(R.id.buildingImage);

        // Buttons Assignment
        buttonStartStop = (Button) findViewById(R.id.startStopButton);
        buttonSave = (Button) findViewById(R.id.saveButton);

        // Drawer Assignment
        navView = (NavigationView) findViewById(R.id.navView);

        // Fetch Databases
        dbRecords = new RecordsDatabaseHandler(getApplicationContext());
        dbRecords.openDatabase();
        dbBlueprints = new BlueprintsDatabaseHandler(getApplicationContext());
        dbBlueprints.openDatabase();

        // Create Notification Channel
        createNotificationChannel();

        // Drawer Layout
        createDrawer();

        // Show Default 15 min Building on Startup
        building = new Building("Jett", R.drawable.jett15);
        buildingImage.setImageResource(R.drawable.jett15);

        // Insert Blueprint Data if Present
        blueprintTransfer();

        // Button Listeners
        buttonStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildingName.isEmpty()) {
                    buildingName = buildingNameEdit.getText().toString();
                }
                if (!buildingName.isEmpty()) {
                    startStop();
                } else {
                    errorVibrate();
                }
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buildingName.isEmpty()) {
                    buildingName = buildingNameEdit.getText().toString();
                }
                if (!buildingName.isEmpty()) {
                    saveBlueprint();
                    buildingNameEdit.getText().clear();
                } else {
                    errorVibrate();
                }
            }
        });

        // EditText Listeners
        buildingNameEdit.setOnEditorActionListener(new android.widget.TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    buildingName = buildingNameEdit.getText().toString();
                    confirmBuildingName();
                }
                return true;
            }
        });

        editNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               resetBuildingName();
               buildingName = buildingNameEdit.getText().toString();
            }
        });

        // Activate SeekBar Time Select
        setTimerValues();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

                // TODO Add animations
                if (selectedTime < 60) {
                    popSound = MediaPlayer.create(MainActivity.this, R.raw.pope5);
                } else if (selectedTime < 90) {
                    popSound = MediaPlayer.create(MainActivity.this, R.raw.popg5);
                } else if (selectedTime < 120) {
                    popSound = MediaPlayer.create(MainActivity.this, R.raw.popc6);
                } else if (selectedTime == 120) {
                    popSound = MediaPlayer.create(MainActivity.this, R.raw.pope6);
                }
                totalBuildTime = selectedTime;
                building.changeBuilding(totalBuildTime, buildingImage);

                if (popSound != null) {
                    popSound.start();
                    popSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) { mp.release(); }
                    });
                }
                textViewTime.setText(selectedTime + ":00");
                timeCountInMilliSeconds = (selectedTime * 1000 * 60) / TIME_SCALE; // Minutes-Seconds (Del/60)
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {};
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {};
        });
    }

    /**
     * Method to set Circular Progress Bar Values
     */
    public void setProgressBarValues() {
        progressBarCircle.setMax((int) timeCountInMilliSeconds/1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds/1000);
    }

    /**
     * Wired to StartStop Button to start or stop timer
     */
    private void startStop() {
        // Button shows BUILD
        if (timerStatus == TimerStatus.STOPPED) {
            startTimer();
            // Button Shows GIVE UP
        } else {
            createWarningPopupDialog();
        }
    }

    /**
     * Button Starts the Timer
     */
    public void startTimer() {
        confirmBuildingName();
        editNameButton.setVisibility(View.GONE);
        buttonSave.setEnabled(false);

        // Record Start Time
        record = new RecordModel();
        record.setDateTimeFormatted();
        record.setBuildingName(buildingName);
        // Countdown Timer Start Status
        timerStatus = TimerStatus.STARTED;
        // Change Button Text
        buttonStartStop.setText("Give Up");
        // Make SeekBar Editable
        seekTime.setEnabled(false);
        // Start Countdown
        startCountdownTimer();
        // Initialise Progress Bar
        setProgressBarValues();
    }

    /**
     * Button Stops the Timer
     */
    public void stopTimer() {
        resetBuildingName();
        buildingNameEdit.getText().clear();
        buttonSave.setEnabled(true);

        // Add Failed Build to Records
        record.setTotalMinutes(0);
        record.setBuildingImageId(R.drawable.jett_ruins);
        dbRecords.insertRecord(record);
        // Countdown Timer Stop Status
        timerStatus = TimerStatus.STOPPED;
        // Stop Countdown Timer
        countDownTimer.cancel();
        // Change Button Text
        buttonStartStop.setText("Build");
        // Make SeekBar Editable
        seekTime.setEnabled(true);
        textViewTime.setText(totalBuildTime + ":00");
        // Shows an Unsuccessful Dialog
        createNewBuildingPopupDialog(false);
        // Reset Building to Previous Selection
        building.changeBuilding(totalBuildTime, buildingImage);
        // Initialise Progress Bar
        setProgressBarValues();
    }

    /**
     * Method to Start Countdown
     */
    private void startCountdownTimer(){
        building.changeBuilding(0, buildingImage);
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long elapsed = (timeCountInMilliSeconds - millisUntilFinished) / 1000 * TIME_SCALE; // Seconds*60 Del*60
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

                resetBuildingName();
                buttonSave.setEnabled(true);

                // Show Build Complete Notification
                createNotifications();
                // Shows a Successful Popup Dialog
                createNewBuildingPopupDialog(true);
                // Record to Database
                record.setTotalMinutes(totalBuildTime);
                record.setBuildingImageId(building.buildingImageViewId);
                dbRecords.insertRecord(record);

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
     * Create Drawer Layout
     */
    private void createDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.blueprintsPage) {
                    // Disable Blueprints Page when Timer is Running
                    if (timerStatus == TimerStatus.STOPPED) {
                        Intent buildIntent = new Intent(getApplicationContext(), BlueprintActivity.class);
                        startActivity(buildIntent);
                        drawerLayout.closeDrawers();
                    } else {
                        Toast.makeText(getApplicationContext(), "Construction Underway",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                if (id == R.id.recordsPage) {
                    Intent buildIntent = new Intent(getApplicationContext(), RecordsActivity.class);
                    startActivity(buildIntent);
                    drawerLayout.closeDrawers();
                }
                if (id == R.id.cityPage) {
                    Toast.makeText(getApplicationContext(), "Coming Soon!!",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    /**
     * Create a Popup window on Build Success
     */
    public void createNewBuildingPopupDialog(boolean success) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        ImageView popupBuildingView;
        TextView buildSuccessText;
        Button closeButton;

        final View popupView = getLayoutInflater().inflate(R.layout.building_popup_layout, null);
        popupBuildingView = (ImageView) popupView.findViewById(R.id.popupBuildingView);
        closeButton = (Button) popupView.findViewById(R.id.closeButton);
        buildSuccessText = (TextView) popupView.findViewById(R.id.buildSuccessText);

        if (!success) {
            buildSuccessText.setText("Construction Failed :(");
            popupBuildingView.setImageResource(R.drawable.jett_ruins);
        } else {
            buildSuccessText.setText("Construction Complete !!!");
            building.changeBuilding(totalBuildTime, buildingImage);
            popupBuildingView.setImageResource(building.buildingImageViewId);
        }

        dialogBuilder.setView(popupView);
        final AlertDialog dialog = dialogBuilder.create();
        // Rounded Corners
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialog.dismiss(); }
        });
    }

    /**
     * Create Warning Popup Before Destroying Building
     */
    public void createWarningPopupDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        Button giveUpButton;
        ImageButton warningCloseButton;

        final View popupView = getLayoutInflater().inflate(R.layout.warning_popup_layout, null);
        giveUpButton = (Button) popupView.findViewById(R.id.giveUpButton);
        warningCloseButton = (ImageButton) popupView.findViewById(R.id.warningCloseButton);

        dialogBuilder.setView(popupView);
        final AlertDialog dialog = dialogBuilder.create();
        // Rounded Corners
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        warningCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialog.dismiss(); }
        });

        giveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                dialog.dismiss();
            }
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

    /**
     * Displays Notification on Construction Complete
     */
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

    /**
     * Saves Common Task as a Blueprint in the Blueprint Page
     */
    private void saveBlueprint() {
        blueprint = new BlueprintModel();
        blueprint.setBuildingName(buildingName);
        blueprint.setTotalMinutes(totalBuildTime);
        blueprint.setBuildingImageId(building.buildingImageViewId);
        dbBlueprints.insertBlueprint(blueprint);
        resetBuildingName();
        Toast.makeText(getApplicationContext(), "Saved to Blueprints",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Transfers Blueprint data to Build Page
     */
    private void blueprintTransfer() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            buildingName = extras.getString("taskName");
            totalBuildTime = extras.getInt("totalMinutes");
            buildingImage.setImageResource(extras.getInt("buildingId"));
            textViewTime.setText(totalBuildTime + ":00");
            timeCountInMilliSeconds = (totalBuildTime * 1000 * 60) / TIME_SCALE;
            int progress = totalBuildTime/15;
            seekTime.setProgress(progress-1);
            confirmBuildingName();
        }
    }

    private void resetBuildingName() {
        buildingName = "";
        buildingNameText.setVisibility(View.INVISIBLE);
        buildingNameEdit.setEnabled(true);
        buildingNameEdit.setVisibility(View.VISIBLE);
        editNameButton.setVisibility(View.GONE);
    }

    private void confirmBuildingName() {
        buildingNameEdit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_dark)));
        buildingNameEdit.setHintTextColor(getResources().getColor(R.color.purple_dark));
        buildingNameEdit.setEnabled(false);
        buildingNameEdit.setVisibility(View.INVISIBLE);
        buildingNameText.setText(buildingName);
        buildingNameText.setVisibility(View.VISIBLE);
        editNameButton.setVisibility(View.VISIBLE);
    }

    private void errorVibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        buildingNameEdit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
        buildingNameEdit.setHintTextColor(getResources().getColor(R.color.colorRed));
        buildingNameEdit.startAnimation(errorShakeAnimation());
    }

    private TranslateAnimation errorShakeAnimation() {
        TranslateAnimation shake = new TranslateAnimation(0, 20, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

}
