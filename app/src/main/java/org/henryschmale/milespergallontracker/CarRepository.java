package org.henryschmale.milespergallontracker;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CarRepository {
    public static final String TAG = "CarRepository";
    private CarDao carDao;
    private CarDatabase db;

    public CarRepository(Application app) {
        db = CarDatabase.getDatabase(app);
        carDao = db.carDao();
    }

    public void insert (Car word) {
        new addCarTask(carDao).execute(word);
    }

    public void addCarEvent(Car c, MileageEvent e) {
        new addMileageTask(carDao).execute(c, e);
    }

    public LiveData<List<Car>> getAllCars() {
        return carDao.getAllCars();
    }

    public LiveData<List<MileageEvent>> getMileageEvents(Car c) {
        return carDao.getMileages(c.id);
    }

    public CarDao getCarDao() {
        return carDao;
    }

    public Car getCar(long carId) {
        return carDao.getSingleCar(carId);
    }


    private static class addCarTask extends AsyncTask<Car, Void, Void> {

        private CarDao mAsyncTaskDao;

        addCarTask(CarDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Car... params) {
            mAsyncTaskDao.addCar(params[0]);
            Log.i(CarRepository.TAG, "Inserted a car");
            return null;
        }
    }

    private static class addMileageTask extends AsyncTask<Object, Void, Void> {

        private CarDao mAsyncTaskDao;

        addMileageTask(CarDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Object... things) {
            mAsyncTaskDao.addMileageEvent((Car)things[0], (MileageEvent)things[1]);
            Log.i(CarRepository.TAG, "Inserted mileage event");
            return null;
        }
    }
}
