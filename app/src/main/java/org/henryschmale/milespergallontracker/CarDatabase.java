package org.henryschmale.milespergallontracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database( entities = {Car.class}, version = 1)
public abstract class CarDatabase extends RoomDatabase {

    public abstract CarDao carDao();

    private static CarDatabase INSTANCE;

    static CarDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CarDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(), CarDatabase.class, "car_database.sqlite")
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
