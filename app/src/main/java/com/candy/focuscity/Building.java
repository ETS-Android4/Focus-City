package com.candy.focuscity;

import android.widget.ImageView;

public class Building extends MainActivity {

    protected String buildingName;
    protected int buildingImageViewId;

    public Building(String buildingName, int buildingImageViewId) {
        this.buildingName = buildingName;
        this.buildingImageViewId = buildingImageViewId;
    }

    public void changeBuilding(int minutes, ImageView buildingImage) {
        if (buildingName.equals("Jett")) {

            if (minutes == 0) {
                buildingImageViewId = R.drawable.building_ground;
            } else if (minutes < 60) {
                buildingImageViewId = R.drawable.jett15;
            } else if (minutes < 90) {
                buildingImageViewId = R.drawable.jett60;
            } else if (minutes < 120) {
                buildingImageViewId = R.drawable.jett90;
            } else if (minutes == 120) {
                buildingImageViewId = R.drawable.jett120;
            }

            buildingImage.setImageResource(buildingImageViewId);
        }
    }
}
