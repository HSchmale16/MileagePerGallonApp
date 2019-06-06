package org.henryschmale.milespergallontracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "car")
public class Car {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "car_id")
    public int id;

    @ColumnInfo(name = "car_make")
    public String make;

    @ColumnInfo(name = "car_model")
    public String model;

    @ColumnInfo(name = "car_trim")
    public String trim;

    @ColumnInfo(name = "car_nickname")
    public String nickname;

    @ColumnInfo(name = "car_year")
    public String year;

    public String getDisplayName() {
        if (this.nickname != null && !this.nickname.isEmpty()) {
            return this.nickname;
        }
        return this.year + " " + this.make + " " + this.model + " " + this.trim;
    }
}
