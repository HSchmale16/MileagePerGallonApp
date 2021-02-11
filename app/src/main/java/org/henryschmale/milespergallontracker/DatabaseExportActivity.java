package org.henryschmale.milespergallontracker;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DatabaseExportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_export);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner exportFormat = findViewById(R.id.export_format);
        Spinner exportLocation = findViewById(R.id.export_location);

        EditText filename = findViewById(R.id.filename);
        setDefaultFilename(filename);

        Button exportButton = findViewById(R.id.button);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setDefaultFilename(EditText filenameText) {
        Calendar c = Calendar.getInstance();
        Date d = c.getTime();

        String name = "EXPORT_MPG_TRACKER-%s.csv";
        DateFormat df = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        name = String.format(name, df.format(d));
        name = name.replaceAll("/", "-");

        filenameText.setText(name);

    }

}
