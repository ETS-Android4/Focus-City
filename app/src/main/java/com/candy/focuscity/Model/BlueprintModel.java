package com.candy.focuscity.Model;

public class BlueprintModel {
    private int id;
    private int buildingImageId;
    private int totalMinutes;
    private String buildingName;

    public String getBuildingName() { return buildingName; }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

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

    public int getBuildingImageId() {
        return buildingImageId;
    }

    public void setBuildingImageId(int buildingImageId) { this.buildingImageId = buildingImageId; }
}
