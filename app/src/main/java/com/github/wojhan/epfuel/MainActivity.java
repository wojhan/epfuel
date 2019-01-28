package com.github.wojhan.epfuel;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.github.wojhan.epfuel.db.Car;
import com.github.wojhan.epfuel.db.FuelDatabase;
import com.github.wojhan.epfuel.db.Refuel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int ADD_NEW_REFUEL_ACTIVITY = 1;
    public static final int ADD_NEW_CAR_ACTIVITY = 2;

    private FuelDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = Room.databaseBuilder(getApplicationContext(),
                FuelDatabase.class, "fuel").build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MainFragment newFragment = new MainFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_content_framelayout, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_home) {
            MainFragment newFragment = new MainFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.main_content_framelayout, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }
        if (id == R.id.drawer_add_refuel) {
            Intent intent = new Intent(this, AddRefuelActivity.class);
            startActivityForResult(intent, ADD_NEW_REFUEL_ACTIVITY);
        } else if (id == R.id.drawer_refuel_list) {
            RefuelListFragment newFragment = new RefuelListFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.main_content_framelayout, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        } else if (id == R.id.drawer_cars) {
            UserCarsFragment newFragment = new UserCarsFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.main_content_framelayout, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(requestCode == ADD_NEW_CAR_ACTIVITY) {
//            if(resultCode == RESULT_OK) {
//                String name = data.getExtras().getString("name");
//                String make = data.getExtras().getString("make");
//                String model = data.getExtras().getString("model");
//                String description = data.getExtras().getString("description");
//
//                final Car car = new Car();
//                car.setName(name);
//                car.setMake(make);
//                car.setModel(model);
//                car.setDescription(description);
//                AsyncTask.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        db.carDao().insert(car);
//                    }
//                });
//            }
//        }
        if (requestCode == ADD_NEW_REFUEL_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                int carId = data.getExtras().getInt("carId");
                int counter = data.getExtras().getInt("counter");
                float fuelAmount = data.getExtras().getFloat("fuelAmount");
                float priceForLiter = data.getExtras().getFloat("priceForLiter");
                String date = data.getExtras().getString("date");

                final Refuel refuel = new Refuel();
                refuel.setAmount(fuelAmount);
                refuel.setCarId(carId);
                refuel.setCounter(counter);
                refuel.setDate(date);
                refuel.setPriceForLiter(priceForLiter);

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.fuelDao().insert(refuel);
                    }
                });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
