package org.henryschmale.milespergallontracker;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedMpgViewModel extends AndroidViewModel {
    public static final String TAG = "SharedMpgViewModel";
    private final MutableLiveData<Car> selectedCar = new MutableLiveData<>();
    private MutableLiveData<LiveData<List<MileageEvent>>> mileageEvents;
    private MutableLiveData<LiveData<List<MileageInterval>>> mileageIntervals;
    private CarRepository repository;

    public SharedMpgViewModel(@NonNull Application application) {
        super(application);
        repository = new CarRepository(application);

        mileageEvents = new MutableLiveData<>();
        mileageIntervals = new MutableLiveData<>();
    }

    public void setSelectedCar(Car c) {
        selectedCar.setValue(c);
        mileageEvents.setValue(repository.getMileageEvents(c));
        mileageIntervals.setValue(repository.getMileageIntervals(c, "-1 year"));
    }

    public LiveData<Car> getSelectedCar() {
        return selectedCar;
    }

    public LiveData<LiveData<List<MileageEvent>>> getMileageEvents() {
        return mileageEvents;
    }

    public LiveData<LiveData<List<MileageInterval>>> getMileageIntervals() {
        return mileageIntervals;
    }
}
