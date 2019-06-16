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

    @Query("SELECT * FROM car WHERE car_nickname = :nickname LIMIT 1")
    abstract Car getSingleCarByNickname(String nickname);

    @Insert
    abstract void addCar(Car car);

    @Insert
    abstract void _addMileage(MileageEvent event);

    @Query("SELECT mileage FROM mileage_events WHERE car_id = :car_id ORDER BY `when` DESC LIMIT 1")
    abstract MileageTuple getLatestMileageForCar(int car_id);


    /**
     * TODO Change since to be an actual date calculated via preferences
     * @param car_id
     * @param since sqlite time delta expression for the datetime function
     * @return
     */
    @Query("SELECT \n" +
            "    car_id,\n" +
            "    \"when\",\n" +
            "    last_fillup,\n" +
            "    mileage - last_mileage as milesTraveled,\n" +
            "    (mileage - last_mileage) / gallons as mpg,\n" +
            "    costPerGallon,\n" +
            "    gallons * costPerGallon / (mileage - last_mileage) as costPerMile\n" +
            "FROM\n" +
            "(SELECT\n" +
            "    car_id,\n" +
            "    mileage,\n" +
            "    (SELECT b.mileage FROM mileage_events as b WHERE b.\"when\" < a.\"when\" ORDER BY b.\"when\" DESC LIMIT 1) as last_mileage,\n" +
            "    (SELECT b.`when` FROM mileage_events as b WHERE b.\"when\" < a.\"when\" ORDER BY b.\"when\" DESC LIMIT 1) as last_fillup,\n" +
            "    gallons,\n" +
            "    costPerGallon,\n" +
            "    \"when\"\n" +
            "    FROM mileage_events as a WHERE last_mileage IS NOT NULL AND car_id = :car_id)\n" +
            "WHERE \"when\" >= strftime('%s', datetime('now', :since))  ORDER BY \"when\" DESC")
    abstract LiveData<List<MileageInterval>> getMileageIntervals(long car_id, String since);


    @Query("SELECT COUNT(*) FROM car")
    abstract long carCount();

    @Query("SELECT COUNT(*) FROM mileage_events")
    abstract long mileageEventCount();

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
