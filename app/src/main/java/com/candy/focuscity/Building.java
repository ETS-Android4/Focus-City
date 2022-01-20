package com.candy.focuscity;

import android.widget.ImageView;

public class Building extends MainActivity {

    protected ImageView buildingImageView;
    protected String buildingName;

    public Building(String buildingName, ImageView buildingImage) {
        this.buildingName = buildingName;
        this.buildingImageView = buildingImage;
        buildingImageView.setImageResource(R.drawable.jett15);
    }

    public void changeBuilding(int minutes) {
        if (buildingName.equals("Jett")) {
            switch (minutes) {
                case 0:
                    buildingImageView.setImageResource(R.drawable.jett0);
                    break;
                case 15:
                    buildingImageView.setImageResource(R.drawable.jett15);
                    break;
                case 60:
                    buildingImageView.setImageResource(R.drawable.jett60);
                    break;
                case 90:
                    buildingImageView.setImageResource(R.drawable.jett90);
                    break;
                case 120:
                    buildingImageView.setImageResource(R.drawable.jett120);
                    break;
            }
        }
    }
}
