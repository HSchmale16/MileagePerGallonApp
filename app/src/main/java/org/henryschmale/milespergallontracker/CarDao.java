package org.henryschmale.milespergallontracker;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.sql.Date;
import java.util.List;

@Dao
public abstract class CarDao {
    public static final String TAG = "CarDao";

    public static class MileageTuple {
        @ColumnInfo(name = "mileage")
        double mileage;

        @Override
        public String toString() {
            return Double.toString(mileage);
        }
    }

    @Query("SELECT * FROM car ORDER BY car_make DESC, car_model DESC")
    abstract LiveData<List<Car>> getAllCars();

    @Query("SELECT * FROM mileage_events WHERE car_id = :car_id ORDER BY `when` DESC")
    abstract LiveData<List<MileageEvent>> getMileages(int car_id);

    @Query("SELECT * FROM car WHERE car_id = :car_id LIMIT 1")
    abstract Car getSingleCar(long car_id);

    @Insert
    abstract void addCar(Car car);

    @Insert
    abstract void _addMileage(MileageEvent event);

    @Query("SELECT mileage FROM mileage_events WHERE car_id = :car_id ORDER BY `when` DESC LIMIT 1")
    abstract MileageTuple getLatestMileageForCar(int car_id);

    @Query("SELECT \n" +
            "car_id," +
            "\"when\",\n" +
            "last_fillup,\n" +
            "mileage - last_mileage as milesTraveled,\n" +
            "(mileage - last_mileage) / gallons as mpg,\n" +
            "costPerGallon,\n" +
            "gallons * costPerGallon / (mileage - last_mileage) as costPerMile\n" +
            "FROM\n" +
            "(SELECT\n" +
            "car_id," +
            "\tmileage,\n" +
            "\t(SELECT b.mileage FROM mileage_events as b WHERE b.\"when\" < a.\"when\" ORDER BY b.\"when\" DESC LIMIT 1) as last_mileage,\n" +
            "\t(SELECT b.`when` FROM mileage_events as b WHERE b.\"when\" < a.\"when\" ORDER BY b.\"when\" DESC LIMIT 1) as last_fillup,\n" +
            "\tgallons,\n" +
            "\tcostPerGallon,\n" +
            "\t\"when\"\n" +
            "FROM mileage_events as a WHERE last_mileage IS NOT NULL)\n" +
            "WHERE car_id = :car_id AND \"when\" >= 0 AND :since LIKE '%year%' " +
            "ORDER BY \"when\" DESC")
    abstract LiveData<List<MileageInterval>> getMileageIntervals(long car_id, String since);

    void addMileageEvent(Car car, MileageEvent event) {
        event.carId = car.id;
        if (event.when == null)
            event.when = new Date(System.currentTimeMillis());

        MileageTuple latestMileage = getLatestMileageForCar(car.id);
        if (latestMileage != null)
            Log.d(TAG, "Latest mileage is " + latestMileage.toString());

        _addMileage(event);
    }
}
