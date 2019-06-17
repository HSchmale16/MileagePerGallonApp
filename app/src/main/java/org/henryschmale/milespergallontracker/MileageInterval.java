package org.henryschmale.milespergallontracker;

import androidx.room.ColumnInfo;

import java.util.Date;

public class MileageInterval {
    @ColumnInfo(name = "id")
    int mileageEventId;

    @ColumnInfo(name = "when")
    Date when;
    @ColumnInfo(name="last_fillup")
    Date lastFillup;

    long milesTraveled;
    double mpg;
    double gallons;
    double costPerGallon;
    double costPerMile;
    @ColumnInfo(name = "car_id")
    long carId;

    MileageEvent toMileageEvent() {
        MileageEvent event = new MileageEvent();
        event.id = mileageEventId;
        event.when = when;
        event.gallons = gallons;
        event.costPerGallon = costPerGallon;
        event.carId = carId;

        return event;
    }
}