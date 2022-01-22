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
            switch (minutes) {
                case 0:
                    buildingImageViewId = R.drawable.jett0;
                    break;
                case 15:
                    buildingImageViewId = R.drawable.jett15;
                    break;
                case 60:
                    buildingImageViewId = R.drawable.jett60;
                    break;
                case 90:
                    buildingImageViewId = R.drawable.jett90;
                    break;
                case 120:
                    buildingImageViewId = R.drawable.jett120;
                    break;
            }
            buildingImage.setImageResource(buildingImageViewId);
        }
    }
}
