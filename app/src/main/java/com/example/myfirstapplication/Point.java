package com.example.myfirstapplication;

import androidx.annotation.NonNull;

import java.util.Locale;
import java.util.Objects;

public class Point  {

    private final String pointID;
    private final double y_value;
    private final double x_value;

    public Point(String pointID, double y_value, double x_value) {
        this.pointID = pointID;
        this.y_value = y_value;
        this.x_value = x_value;
    }

    public double getY_value() {
        return y_value;
    }

    public double getX_value() {
        return x_value;
    }

    @NonNull
    @Override
    public String toString() {
        return    String.format("%13s",  pointID)
                + String.format(Locale.getDefault(),"%13.3f",  y_value)
                .replace(',', '.')
                + String.format(Locale.getDefault(),"%13.3f", x_value)
                .replace(',', '.');
    }

}
