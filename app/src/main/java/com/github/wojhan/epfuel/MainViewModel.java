package com.github.wojhan.epfuel;

import android.arch.lifecycle.ViewModel;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;

import com.github.wojhan.epfuel.db.Car;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    public View view;

    private Spinner vehicleSpinner;

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

    public void getCars(List<Car> cars) {
        mVehicleAdapter = new VehicleAdapter(cars);
        vehicleSpinner.setAdapter(mVehicleAdapter);

    }

    public void getInformation() {
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


        vehicleSpinner = view.findViewById(R.id.vehicle_spinner);

        mRecentFuelLayout = new LinearLayoutManager(view.getContext());
        mRecentFuelAdapter = new RecentFuelAdapter(recentRefuelList);

        mRecentFuelRecyclerView = view.findViewById(R.id.main_content_fuel_recyclerview);
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

        mRecentRefuelsFirstMonthRecyclerView = view.findViewById(R.id.main_content_last_refuels_first_month_recyclerview);
        mRecentRefuelsSecondMonthRecyclerView = view.findViewById(R.id.main_content_last_refuels_second_month_recyclerview);

        mRecentRefuelsFirstMonthLayout = new LinearLayoutManager(view.getContext());
        mRecentRefuelsSecondMonthLayout = new LinearLayoutManager(view.getContext());

        mRecentRefuelsFirstMonthAdapter = new RecentRefuelsAdapter(recentRefuelsFirstMonth);
        mRecentRefuelsSecondMonthAdapter = new RecentRefuelsAdapter(recentRefuelsSecondMonth);

        mRecentRefuelsFirstMonthRecyclerView.setHasFixedSize(true);
        mRecentRefuelsFirstMonthRecyclerView.setLayoutManager(mRecentRefuelsFirstMonthLayout);
        mRecentRefuelsFirstMonthRecyclerView.setAdapter(mRecentRefuelsFirstMonthAdapter);

        mRecentRefuelsSecondMonthRecyclerView.setHasFixedSize(true);
        mRecentRefuelsSecondMonthRecyclerView.setLayoutManager(mRecentRefuelsSecondMonthLayout);
        mRecentRefuelsSecondMonthRecyclerView.setAdapter(mRecentRefuelsSecondMonthAdapter);
    }
}
