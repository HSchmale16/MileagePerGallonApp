package org.henryschmale.milespergallontracker;

import android.app.Application;

public class CarRepository {
    private CarDao carDao;
    private CarDatabase db;

    public CarRepository(Application app) {
        db = CarDatabase.getDatabase(app);
        carDao = db.carDao();
    }

    CarDao getCarDao() {
        return carDao;
    }
}
