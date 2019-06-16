package org.henryschmale.milespergallontracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddMileageEventActivity extends AppCompatActivity {
    private final static String TAG = "AddMileageEventActivity";
    private int carId;
    private Calendar mainCalendar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mileage_event);

        Bundle extras = getIntent().getExtras();
        carId = extras.getInt("car_id");
        Log.d(TAG, "The car id is " + carId);

        mainCalendar = Calendar.getInstance();
        updateAfterDateTimeChanged();

        Button confirmButton = findViewById(R.id.ok_button);
        confirmButton.setOnClickListener(v -> AddMileageEventActivity.this.returnMileageEvent());


        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> AddMileageEventActivity.this.canceled());
    }

    private void returnMileageEvent() {
        Intent i = new Intent();
        String s;

        final EditText ppgText = findViewById(R.id.gal_price);
        s = ppgText.getText().toString();
        if (s.isEmpty()) {
            Toast.makeText(this, R.string.err_no_ppg, Toast.LENGTH_LONG).show();
            return;
        }
        double ppg = Double.valueOf(s);


        final EditText gallonsText = findViewById(R.id.gallons_taken);
        s = gallonsText.getText().toString();
        if (s.isEmpty()) {
            Toast.makeText(this, R.string.err_no_gallons, Toast.LENGTH_LONG).show();
        }
        double gallons = Double.valueOf(s);

        final EditText mileageText = findViewById(R.id.current_mileage);
        s = mileageText.getText().toString();
        if (s.isEmpty()) {
            Toast.makeText(this, R.string.err_no_mileage, Toast.LENGTH_LONG).show();
            return;
        }
        long mileage = Integer.valueOf(s);

        Date d = mainCalendar.getTime();

        i.putExtra("mileage", mileage);
        i.putExtra("ppg", ppg);
        i.putExtra("gallons", gallons);
        i.putExtra("date", d.getTime());

        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragement = new DatePickerFragment();
        newFragement.show(getSupportFragmentManager(), "datePicker");
    }

    private void canceled() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    protected void setHourMinute(int hourOfDay, int minute) {
        mainCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mainCalendar.set(Calendar.MINUTE, hourOfDay);

        updateAfterDateTimeChanged();
    }

    protected void setYearMonthDay(int year, int month, int day) {
        mainCalendar.set(Calendar.YEAR, year);
        mainCalendar.set(Calendar.MONTH, month);
        mainCalendar.set(Calendar.DAY_OF_MONTH, day);

        updateAfterDateTimeChanged();
    }

    private void updateAfterDateTimeChanged() {
        Date d = mainCalendar.getTime();

        Button timeButton = findViewById(R.id.set_time_button);
        java.text.DateFormat timeFormat = DateFormat.getTimeFormat(getApplicationContext());
        timeButton.setText(timeFormat.format(d));


        Button dateButton = findViewById(R.id.set_date_button);
        java.text.DateFormat dateFormat = DateFormat.getDateFormat(getApplicationContext());
        dateButton.setText(dateFormat.format(d));

        // Latest Mileage Update
        CarRepository repo = new CarRepository(getApplication());
        CarDao.MileageTuple tuple = repo.getCarDao().getLatestMileageForCar(carId);
        double lastMileage;
        if (tuple != null) {
            final TextView currentMileage = findViewById(R.id.lbl_current_mileage);
            lastMileage = tuple.mileage;
            String s = getString(R.string.lbl_current_mileage_last, lastMileage);
            currentMileage.setText(s);
        } else {
            Log.d(TAG, "Failed to get last mileage");
            lastMileage = 0;
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

            ((AddMileageEventActivity)getActivity()).setHourMinute(hourOfDay, minute);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            ((AddMileageEventActivity)getActivity()).setYearMonthDay(year, month, day);
        }
    }
}
