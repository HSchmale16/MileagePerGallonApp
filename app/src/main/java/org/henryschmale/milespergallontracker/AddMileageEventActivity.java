package org.henryschmale.milespergallontracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddMileageEventActivity extends AppCompatActivity {
    private final static String TAG = "AddCarActivity";
    Button cancelButton;
    Button confirmButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mileage_event);

        confirmButton = findViewById(R.id.ok_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMileageEventActivity.this.returnMileageEvent();
            }
        });


        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMileageEventActivity.this.canceled();
            }
        });
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
        int mileage = Integer.valueOf(s);

        i.putExtra("mileage", mileage);
        i.putExtra("ppg", ppg);
        i.putExtra("gallons", gallons);

        setResult(Activity.RESULT_OK, i);
        finish();
    }

    private void canceled() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
