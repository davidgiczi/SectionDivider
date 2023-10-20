package com.example.myfirstapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Point  {

    private String pointID;
    private double y_value;
    private double x_value;

    public Point(String pointID, double y_value, double x_value) {
        this.pointID = pointID;
        this.y_value = y_value;
        this.x_value = x_value;
    }
    public String getPointID() {
        return pointID;
    }

    public double getY_value() {
        return y_value;
    }

    public double getX_value() {
        return x_value;
    }

    @Override
    public String toString() {
        return String.format("%15s", pointID) + " "
                + String.format("%15.3f", y_value)
                .replace(',', '.') + " "
                + String.format("%14.3f", x_value)
                .replace(',', '.');
    }

}
