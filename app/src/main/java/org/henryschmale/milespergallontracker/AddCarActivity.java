package org.henryschmale.milespergallontracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class AddCarActivity extends AppCompatActivity {
    private final static String TAG = "AddCarActivity";
    public final static String F_MAKE = "make";
    public final static String F_MODEL = "model";
    public final static String F_TRIM = "trim";
    public final static String F_YEAR = "year";
    public final static String F_NICK = "nick";

    Spinner makeSpinner;
    EditText modelText;
    EditText trimText;
    EditText yearText;
    EditText nickText;
    Button cancelButton;
    Button confirmButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        confirmButton = findViewById(R.id.ok_button);
        confirmButton.setOnClickListener(v -> AddCarActivity.this.returnCar());

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> AddCarActivity.this.canceled());

        makeSpinner = findViewById(R.id.makeSpinner);
        modelText = findViewById(R.id.model);
        trimText = findViewById(R.id.trim);
        yearText = findViewById(R.id.year);
        nickText = findViewById(R.id.nickname);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            Log.d(TAG, "Got stuff");
            String[] things = getResources().getStringArray(R.array.car_makes);
            int pos = Arrays.binarySearch(things, b.getString(F_MAKE));
            if (pos >= 0) {
                makeSpinner.setSelection(pos);
                modelText.setText(b.getString(F_MODEL,""));
                trimText.setText(b.getString(F_TRIM, ""));
                yearText.setText(b.getString(F_YEAR, ""));
                nickText.setText(b.getString(F_NICK, ""));
            }
        }
    }

    private void returnCar() {
        Intent i = new Intent();

        String make = (String) makeSpinner.getSelectedItem();

        String model = modelText.getText().toString();
        if (model.isEmpty()) {
            Toast.makeText(this, R.string.empty_model, Toast.LENGTH_LONG).show();
            return;
        }

        String trim = trimText.getText().toString();
        String year = yearText.getText().toString();
        String nick = nickText.getText().toString();


        i.putExtra(F_MAKE, make);
        i.putExtra(F_MODEL, model);
        i.putExtra(F_TRIM, trim);
        i.putExtra(F_YEAR, year);
        i.putExtra(F_NICK, nick);

        setResult(Activity.RESULT_OK, i);
        finish();
    }

    private void canceled() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
