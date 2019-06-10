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
import android.widget.Toast;

public class AddCarActivity extends AppCompatActivity {
    private final static String TAG = "AddCarActivity";
    Button cancelButton;
    Button confirmButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        confirmButton = findViewById(R.id.ok_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCarActivity.this.returnCar();
            }
        });


        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCarActivity.this.canceled();
            }
        });
    }

    private void returnCar() {
        Intent i = new Intent();

        final Spinner makeSpinner = findViewById(R.id.makeSpinner);
        String make = (String) makeSpinner.getSelectedItem();

        final EditText modelText = findViewById(R.id.model);
        String model = modelText.getText().toString();
        if (model.isEmpty()) {
            Toast.makeText(this, R.string.empty_model, Toast.LENGTH_LONG).show();
            return;
        }

        final EditText trimText = findViewById(R.id.trim);
        String trim = trimText.getText().toString();

        final EditText yearText = findViewById(R.id.year);
        String year = yearText.getText().toString();

        final EditText nickText = findViewById(R.id.nickname);
        String nick = nickText.getText().toString();


        i.putExtra("make", make);
        i.putExtra("model", model);
        i.putExtra("trim", trim);
        i.putExtra("year", year);
        i.putExtra("nick", nick);

        setResult(Activity.RESULT_OK, i);
        finish();
    }

    private void canceled() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
