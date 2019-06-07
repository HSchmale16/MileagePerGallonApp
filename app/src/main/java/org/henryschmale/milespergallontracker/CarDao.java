package org.henryschmale.milespergallontracker;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CarDao {
    @Query("SELECT * FROM car ORDER BY car_make DESC, car_model DESC")
    LiveData<List<Car>> getAllCars();

    @Insert
    void addCar(Car car);
}
