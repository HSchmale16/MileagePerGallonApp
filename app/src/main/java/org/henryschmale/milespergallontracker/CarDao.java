package org.henryschmale.milespergallontracker;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public abstract class CarDao {
    public static class MileageTuple {
        @ColumnInfo(name = "mileage")
        double mileage;

        @Override
        public String toString() {
            return Double.toString(mileage);
        }
    }

    public static final String TAG = "CarDao";

    @Query("SELECT * FROM car ORDER BY car_make DESC, car_model DESC")
    abstract LiveData<List<Car>> getAllCars();

    @Query("SELECT * FROM mileage_events WHERE car_id = :car_id ORDER BY `when` DESC")
    abstract LiveData<List<MileageEvent>> getMileages(int car_id);

    @Insert
    abstract void addCar(Car car);

    @Insert
    abstract void _addMileage(MileageEvent event);

    @Query("SELECT mileage FROM mileage_events WHERE car_id = :car_id ORDER BY `when` DESC LIMIT 1")
    abstract MileageTuple getLatestMileageForCar(int car_id);


    void addMileageEvent(Car car, MileageEvent event) {
        event.carId = car.id;
        event.when = new Date();
        Log.d(TAG, "Latest mileage is " + getLatestMileageForCar(car.id).toString());
        _addMileage(event);
    }
}
