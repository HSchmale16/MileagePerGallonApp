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
        new insertAsyncTask(carDao).execute(word);
    }

    public LiveData<List<Car>> getAllCars() {
        return carDao.getAllCars();
    }

    private static class insertAsyncTask extends AsyncTask<Car, Void, Void> {

        private CarDao mAsyncTaskDao;

        insertAsyncTask(CarDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Car... params) {
            mAsyncTaskDao.addCar(params[0]);
            Log.i(CarRepository.TAG, "Inserted a car");
            return null;
        }
    }
}
