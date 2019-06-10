package org.henryschmale.milespergallontracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener  {


    final public String TAG = "MainActivity";
    final private static int ADD_CAR_REQUEST_CODE = 1;
    final private static int ADD_MILEAGE_EVENT_RQUEST_CODE = 2;

    Spinner carSpinner;
    CarRepository repository;
    FloatingActionButton fabAddMileageEvent;
    ViewPager viewPager;
    private TabsPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private SharedMpgViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewModel = ViewModelProviders.of(this).get(SharedMpgViewModel.class);


        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_dashboard));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_history_table));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_graphs));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        viewPager = findViewById(R.id.pager);
        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // Other stuff
        repository = new CarRepository(getApplication());
        fabAddMileageEvent = findViewById(R.id.floatingActionButton);
        fabAddMileageEvent.setEnabled(false);

        fabAddMileageEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddMileageEventActivity.class);
                Car c = (Car)carSpinner.getSelectedItem();
                i.putExtra("car_id", c.id);
                startActivityForResult(i, ADD_MILEAGE_EVENT_RQUEST_CODE);
            }
        });

        carSpinner = findViewById(R.id.car_spinner);
        carSpinner.setOnItemSelectedListener(this);
        repository.getAllCars().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(List<Car> cars) {
                if (cars.size() == 0) {
                    Toast.makeText(MainActivity.this, R.string.no_cars_available, Toast.LENGTH_LONG).show();
                } else {
                    fabAddMileageEvent.setEnabled(true);
                }
                ArrayAdapter<Car> carArrayAdapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_spinner_item, cars);
                carSpinner.setAdapter(carArrayAdapter);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_add_car:
                i = new Intent(this, AddCarActivity.class);
                startActivityForResult(i, ADD_CAR_REQUEST_CODE);
                return true;
            case R.id.action_donate:
                break;
            case R.id.action_open_source_licenses:
                i = new Intent(this, OpenSourceLicensesActivity.class);
                startActivity(i);
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Car selected = (Car) carSpinner.getSelectedItem();
        Log.d(TAG, "The selected car is " + selected.toString());
        viewModel.setSelectedCar(selected);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.i(TAG, "Nothing is selected");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ADD_CAR_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Car c = new Car();
                    c.make = data.getStringExtra("make");
                    c.nickname = data.getStringExtra("nick");
                    c.model = data.getStringExtra("model");
                    c.year = data.getStringExtra("year");
                    c.trim = data.getStringExtra("trim");

                    new CarRepository(getApplication()).insert(c);

                } else if (requestCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, R.string.no_car_added, Toast.LENGTH_LONG).show();
                }
                break;
            case ADD_MILEAGE_EVENT_RQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    MileageEvent event = new MileageEvent();
                    Bundle b = data.getExtras();
                    if (b != null) {
                        event.costPerGallon = b.getDouble("ppg");
                        event.gallons = b.getDouble("gallons");
                        event.mileage = b.getLong("mileage");

                        repository.addCarEvent((Car)carSpinner.getSelectedItem(), event);
                    } else {
                        Log.e(TAG, "Returned bundle was null");
                    }
                } else {
                    Toast.makeText(this, R.string.no_mileage_added, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}

