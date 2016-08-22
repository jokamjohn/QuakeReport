package com.example.android.quakereport;

public class Earthquake {

    private double mMagnitude;
    private long time;
    private String primaryText;
    private String secondaryText;
    private String url;

    public Earthquake(double magnitude, String location, long time, String url) {
        mMagnitude = magnitude;
        this.time = time;
        this.url = url;

        if (location.contains("of")){
            String[] parts = location.split("(?<=of)");
            primaryText = parts[0];
            secondaryText = parts[1].trim();
        }else {
            primaryText = "Near the";
            secondaryText = location;
        }
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public long getTime() {
        return time;
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public String getUrl() {
        return url;
    }
}
