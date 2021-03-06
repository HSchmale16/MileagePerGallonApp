package org.henryschmale.milespergallontracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

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
                @Index("car_id"),
                @Index({"car_id", "when"})
        }
)
public class MileageEvent {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "car_id")
    @NonNull
    public long carId;

    @NonNull
    public long mileage;

    public double costPerGallon;

    public double gallons;

    public Date when;
}
