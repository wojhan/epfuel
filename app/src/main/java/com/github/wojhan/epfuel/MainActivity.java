package com.github.wojhan.epfuel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Spinner vehicleSpinner;

    private static int ADD_NEW_REFUEL_ACTIVITY = 1;

    private RecyclerView mRecentFuelRecyclerView;
    private RecyclerView mRecentRefuelsFirstMonthRecyclerView;
    private RecyclerView mRecentRefuelsSecondMonthRecyclerView;

    private RecentFuelAdapter mRecentFuelAdapter;
    private VehicleAdapter mVehicleAdapter;
    private RecentRefuelsAdapter mRecentRefuelsFirstMonthAdapter;
    private RecentRefuelsAdapter mRecentRefuelsSecondMonthAdapter;

    private RecyclerView.LayoutManager mRecentFuelLayout;
    private RecyclerView.LayoutManager mRecentRefuelsFirstMonthLayout;
    private RecyclerView.LayoutManager mRecentRefuelsSecondMonthLayout;

    private List<Vehicle> vehicleList;
    private List<RecentRefuel> recentRefuelList;
    private List<Refuel> recentRefuelsFirstMonth;
    private List<Refuel> recentRefuelsSecondMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        vehicleList = new ArrayList<>();
        vehicleList.add(new Vehicle("Panda"));
        vehicleList.add(new Vehicle("Astra"));

        RecentRefuel r1 = new RecentRefuel();
        r1.setAvgConsumption(0.255f);
        r1.setLastConsumption(0.39f);
        r1.setLastPrice(4.73f);
        r1.setDate(new Date());
        r1.setType("benzyna");

        RecentRefuel r2 = new RecentRefuel();
        r2.setAvgConsumption(7.563f);
        r2.setLastConsumption(8.14f);
        r2.setLastPrice(2.31f);
        r2.setDate(new Date());
        r2.setType("lpg");


        recentRefuelList = new ArrayList<>();
        recentRefuelList.add(r1);
        recentRefuelList.add(r2);

        mVehicleAdapter = new VehicleAdapter(vehicleList);

        vehicleSpinner = findViewById(R.id.vehicle_spinner);
        vehicleSpinner.setAdapter(mVehicleAdapter);

        mRecentFuelLayout = new LinearLayoutManager(this);
        mRecentFuelAdapter = new RecentFuelAdapter(recentRefuelList);

        mRecentFuelRecyclerView = findViewById(R.id.main_content_fuel_recyclerview);
        mRecentFuelRecyclerView.setHasFixedSize(true);
        mRecentFuelRecyclerView.setLayoutManager(mRecentFuelLayout);
        mRecentFuelRecyclerView.setAdapter(mRecentFuelAdapter);

        Refuel r3 = new Refuel();
        r3.setAmount(19.14f);
        r3.setPriceForLiter(2.31f);
        r3.setDate(new Date());

        Refuel r4 = new Refuel();
        r4.setAmount(24.99f);
        r4.setPriceForLiter(2.31f);
        r4.setDate(new Date());

        Refuel r5 = new Refuel();
        r5.setAmount(23.94f);
        r5.setPriceForLiter(2.33f);
        r5.setDate(new Date());

        Refuel r6 = new Refuel();
        r6.setAmount(25.33f);
        r6.setPriceForLiter(2.36f);
        r6.setDate(new Date());

        Refuel r7 = new Refuel();
        r7.setAmount(25.7f);
        r7.setPriceForLiter(2.39f);
        r7.setDate(new Date());

        recentRefuelsFirstMonth = new ArrayList<>();
        recentRefuelsFirstMonth.add(r3);
        recentRefuelsFirstMonth.add(r4);
        recentRefuelsFirstMonth.add(r5);
        recentRefuelsFirstMonth.add(r6);
        recentRefuelsFirstMonth.add(r7);

        recentRefuelsSecondMonth = new ArrayList<>();
        recentRefuelsSecondMonth.addAll(recentRefuelsFirstMonth);

        mRecentRefuelsFirstMonthRecyclerView = findViewById(R.id.main_content_last_refuels_first_month_recyclerview);
        mRecentRefuelsSecondMonthRecyclerView = findViewById(R.id.main_content_last_refuels_second_month_recyclerview);

        mRecentRefuelsFirstMonthLayout = new LinearLayoutManager(this);
        mRecentRefuelsSecondMonthLayout = new LinearLayoutManager(this);

        mRecentRefuelsFirstMonthAdapter = new RecentRefuelsAdapter(recentRefuelsFirstMonth);
        mRecentRefuelsSecondMonthAdapter = new RecentRefuelsAdapter(recentRefuelsSecondMonth);

        mRecentRefuelsFirstMonthRecyclerView.setHasFixedSize(true);
        mRecentRefuelsFirstMonthRecyclerView.setLayoutManager(mRecentRefuelsFirstMonthLayout);
        mRecentRefuelsFirstMonthRecyclerView.setAdapter(mRecentRefuelsFirstMonthAdapter);

        mRecentRefuelsSecondMonthRecyclerView.setHasFixedSize(true);
        mRecentRefuelsSecondMonthRecyclerView.setLayoutManager(mRecentRefuelsSecondMonthLayout);
        mRecentRefuelsSecondMonthRecyclerView.setAdapter(mRecentRefuelsSecondMonthAdapter);
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

        if (id == R.id.drawer_add_refuel) {
            Intent intent = new Intent(this, AddRefuelActivity.class);
            startActivityForResult(intent, ADD_NEW_REFUEL_ACTIVITY);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
