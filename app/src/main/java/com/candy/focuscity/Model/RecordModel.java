package com.candy.focuscity.Model;

import android.widget.ImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RecordModel {
    private int id;
    private DateFormat date = new SimpleDateFormat("dd/MM/yyyy, hh:mm aa");
    private String dateTimeFormatted;
    private int buildingImageId;
    private int totalMinutes;

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateTimeFormatted() {
        return dateTimeFormatted;
    }

    public void setDateTimeFormatted() {
        this.dateTimeFormatted =  date.format(Calendar.getInstance().getTime());
    }

    public int getBuildingImageId() {
        return buildingImageId;
    }

    public void setBuildingImageId(int buildingImageId) {
        this.buildingImageId = buildingImageId;
    }
}
