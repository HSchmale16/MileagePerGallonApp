package org.henryschmale.milespergallontracker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.sql.Date;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.Executors;

import static android.appwidget.AppWidgetManager.getInstance;

@Database(
        entities = {Car.class, MileageEvent.class},
        version = 1)
@TypeConverters({Converters.class})
public abstract class CarDatabase extends RoomDatabase {
    public static String TAG = "CarDatabase";

    public abstract CarDao carDao();

    private static CarDatabase INSTANCE;

    static CarDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CarDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(), CarDatabase.class, "car_database.sqlite")
                            .allowMainThreadQueries()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        Car c = new Car();
                                        String nickname = "Test Car Many Entries";
                                        c.model = "Random";
                                        c.make = "Honda";
                                        c.nickname = nickname;

                                        CarDao dao = getDatabase(context).carDao();
                                        dao.addCar(c);

                                        c = dao.getSingleCarByNickname(nickname);
                                        Log.d(TAG, Integer.toString(c.id));

                                        MileageEvent me = new MileageEvent();
                                        Random rnd = new Random();

                                        Calendar cal = Calendar.getInstance();

                                        cal.add(Calendar.DATE, -200);

                                        for (int i = 0; i < 30; ++i) {
                                            me.costPerGallon = (rnd.nextInt(200) + 225) / 100.0;
                                            me.mileage += rnd.nextInt(350) + 55;
                                            me.gallons = (rnd.nextInt(22000) + 2000) / 1000.0;

                                            cal.add(Calendar.DATE, rnd.nextInt(7) + 2);

                                            me.when = cal.getTime();

                                            dao.addMileageEvent(c, me);
                                        }

                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
