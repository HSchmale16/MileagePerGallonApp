package org.henryschmale.milespergallontracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class OpenSourceLicensesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source_licenses);

        TextView v = findViewById(R.id.open_source_content);
        v.setText(Html.fromHtml(getString(R.string.open_source_licenses)));
        v.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
