package org.henryschmale.milespergallontracker;

import androidx.room.ColumnInfo;

import java.util.Date;

public class MileageInterval {
    @ColumnInfo(name = "when")
    Date when;
    @ColumnInfo(name="last_fillup")
    Date lastFillup;

    long milesTraveled;
    double mpg;
    double costPerGallon;
    double costPerMile;
    @ColumnInfo(name = "car_id")
    long carId;
}