package com.github.wojhan.epfuel;

import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.wojhan.epfuel.db.Car;
import com.github.wojhan.epfuel.db.FuelDatabase;

import java.util.List;

public class AddRefuelActivity extends AppCompatActivity {

    private Spinner mCar;
    private EditText mCounter;
    private EditText mFuelAmount;
    private EditText mPriceForLiter;
    private EditText mDate;

    private FuelDatabase db;

    private VehicleAdapter mCarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_refuel);
        setTheme(R.style.AppTheme);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_refuel_toolbar);
        setSupportActionBar(toolbar);

        mCar = findViewById(R.id.vehicle_spinner);
        mCounter = findViewById(R.id.add_refuel_counter_value);
        mFuelAmount = findViewById(R.id.add_refuel_fueled_value);
        mPriceForLiter = findViewById(R.id.add_refuel_price_for_liter_value);
        mDate = findViewById(R.id.add_refuel_date_value);

        db = Room.databaseBuilder(this,
                FuelDatabase.class, "fuel").build();

        db.carDao().getAll().observe(this, new Observer<List<Car>>() {

            @Override
            public void onChanged(@Nullable List<Car> cars) {
                mCarAdapter = new VehicleAdapter(cars);
                mCar.setAdapter(mCarAdapter);
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
                backIntent.putExtra("date", mDate.getText().toString());

                setResult(RESULT_OK, backIntent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
