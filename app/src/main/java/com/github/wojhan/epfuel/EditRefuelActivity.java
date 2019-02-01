package com.github.wojhan.epfuel;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.wojhan.epfuel.db.Car;
import com.github.wojhan.epfuel.db.FuelDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditRefuelActivity extends AppCompatActivity {

    private Spinner mCar;
    private Spinner mFuelType;
    private EditText mCounter;
    private EditText mFuelAmount;
    private EditText mPriceForLiter;
    private EditText mPrice;
    private EditText mDate;

    private TextView mGasStationName;

    private FuelDatabase db;

    private VehicleAdapter mCarAdapter;

    private SharedPreferences preferences;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();

        setContentView(R.layout.activity_add_refuel);
        setTheme(R.style.AppTheme);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_refuel_toolbar);
        setSupportActionBar(toolbar);

        mCar = findViewById(R.id.vehicle_spinner);
        mCounter = findViewById(R.id.add_refuel_counter_value);
        mFuelAmount = findViewById(R.id.add_refuel_fueled_value);
        mPriceForLiter = findViewById(R.id.add_refuel_price_for_liter_value);
        mDate = findViewById(R.id.add_refuel_date_value);
        mFuelType = findViewById(R.id.add_refuel_fuel_type);
        mPrice = findViewById(R.id.add_refuel_price_value);
        mGasStationName = findViewById(R.id.add_refuel_gas_station_location);

        myCalendar = Calendar.getInstance();


        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        mCounter.setText(String.valueOf(extras.getInt("counter")));
        mFuelAmount.setText(String.format(Locale.ENGLISH, "%.2f", extras.getFloat("fuelAmount")));
        mPriceForLiter.setText(String.format(Locale.ENGLISH, "%.2f", extras.getFloat("priceForLiter")));
        mDate.setText(sdf.format(extras.getLong("date")));
        mPrice.setText(String.format(Locale.ENGLISH, "%.2f", extras.getFloat("fuelAmount") * extras.getFloat("priceForLiter")));


        mFuelAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mPriceForLiter.getText().toString().isEmpty() && !s.toString().isEmpty()) {
                    mPrice.setText(String.format("%.2f", Float.parseFloat(s.toString()) * Float.parseFloat(mPriceForLiter.getText().toString())));
                }
            }
        });

        mPriceForLiter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mFuelAmount.getText().toString().isEmpty() && !s.toString().isEmpty()) {
                    mPrice.setText(String.format("%.2f", Float.parseFloat(s.toString()) * Float.parseFloat(mFuelAmount.getText().toString())));
                }
            }
        });

        String[] fuelType = {"Pb95", "Pb95+", "Pb98", "Pb98+", "Pb100", "LPG", "ON", "CNG"};

        preferences = getSharedPreferences("epfuel", MODE_PRIVATE);

        mFuelType.setAdapter(new FuelAdapter(Arrays.asList(fuelType)));
        mFuelType.setSelection(preferences.getInt("chosenFuelType", 0), true);
        mFuelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor preferenceEditor = preferences.edit();
                preferenceEditor.putInt("chosenFuelType", position);
                preferenceEditor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mFuelType.setSelection(extras.getInt("fuel"), true);


        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                mDate.setText(sdf.format(myCalendar.getTime()));

            }

        };


        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditRefuelActivity.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        db = Room.databaseBuilder(this,
                FuelDatabase.class, "fuel").build();

        db.carDao().getAll().observe(this, new Observer<List<Car>>() {

            @Override
            public void onChanged(@Nullable List<Car> cars) {
                mCarAdapter = new VehicleAdapter(cars);
                mCar.setAdapter(mCarAdapter);

                for (int i = 0; i < cars.size(); i++) {
                    if (cars.get(i).getId() == extras.getInt("carId")) {
                        mCar.setSelection(i + 1, true);
                        break;
                    }
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_refuel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_refuel_done) {
            Intent backIntent = new Intent();
            if (mCar.getSelectedItemPosition() > 0) {
                backIntent.putExtra("carId", ((Car) mCar.getSelectedItem()).getId());
                backIntent.putExtra("counter", Integer.parseInt(mCounter.getText().toString()));
                backIntent.putExtra("fuelAmount", Float.parseFloat(mFuelAmount.getText().toString()));
                backIntent.putExtra("priceForLiter", Float.parseFloat(mPriceForLiter.getText().toString()));
                backIntent.putExtra("date", myCalendar.getTimeInMillis());

                String fuelType = "Benzyna";

                if (mFuelType.getSelectedItemPosition() == 5) {
                    fuelType = "LPG";
                } else if (mFuelType.getSelectedItemPosition() == 6) {
                    fuelType = "Diesel";
                } else if (mFuelType.getSelectedItemPosition() == 7) {
                    fuelType = "CNG";
                }

                backIntent.putExtra("fuelType", fuelType);
                backIntent.putExtra("fuel", mFuelType.getSelectedItemPosition());

                setResult(RESULT_OK, backIntent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void chooseLocation(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String name = data.getExtras().getString("name");
                double lat = data.getExtras().getDouble("lat");
                double lng = data.getExtras().getDouble("lng");

                mGasStationName.setText(name);
            }
        }
    }

}
