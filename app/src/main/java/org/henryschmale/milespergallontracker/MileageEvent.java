package org.henryschmale.milespergallontracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.sql.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity (
        tableName = "mileage_events",
        foreignKeys = @ForeignKey(
                entity = Car.class,
                parentColumns = "car_id",
                childColumns = "car_id",
                onDelete = CASCADE
        ),
        indices = {
                @Index("car_id")
        }
)
public class MileageEvent {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "car_id")
    @NonNull
    public int carId;

    @NonNull
    public long mileage;

    public double costPerGallon;

    public double gallons;

    public Date when;
}
