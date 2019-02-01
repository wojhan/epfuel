package com.github.wojhan.epfuel;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.wojhan.epfuel.db.Car;
import com.github.wojhan.epfuel.db.FuelDatabase;
import com.github.wojhan.epfuel.db.Refuel;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

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

    private TextView mRecentRefuelsFirstMonthLabel;
    private TextView mRecentRefuelsSecondMonthLabel;
    private CardView mFuelContainerCardView;
    private TextView mDescriptionTextView;

    private TextView mPaidInCurrentMonth;
    private TextView mFueledInCurrentMonth;
    private TextView mPaidInPreviousMonth;
    private TextView mFueledInPreviousMonth;

    private SharedPreferences preferences;
    private TextView mRecentRefuelsFirstMonthEmpty;
    private TextView mRecentRefuelsSecondMonthEmpty;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        preferences = getActivity().getSharedPreferences("epfuel", Context.MODE_PRIVATE);

        mViewModel.getCars().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(@Nullable List<Car> cars) {
                mVehicleAdapter = new VehicleAdapter(cars);
                vehicleSpinner.setAdapter(mVehicleAdapter);
                vehicleSpinner.setSelection(preferences.getInt("chosenCarPosition", 0), true);
            }
        });

        vehicleSpinner = getView().findViewById(R.id.vehicle_spinner);
        mRecentRefuelsFirstMonthLabel = getView().findViewById(R.id.main_content_last_refuels_first_month);
        mRecentRefuelsSecondMonthLabel = getView().findViewById(R.id.main_content_last_refuels_second_month);
        mFuelContainerCardView = getView().findViewById(R.id.main_content_fuel_empty_container);
        mDescriptionTextView = getView().findViewById(R.id.main_content_fuel_empty_description);
        mPaidInCurrentMonth = getView().findViewById(R.id.main_content_summary_first_month_price_value);
        mPaidInPreviousMonth = getView().findViewById(R.id.main_content_summary_second_month_price_value);
        mFueledInCurrentMonth = getView().findViewById(R.id.main_content_summary_first_month_fueled_value);
        mFueledInPreviousMonth = getView().findViewById(R.id.main_content_summary_second_month_fueled_value);
        mRecentRefuelsFirstMonthEmpty = getView().findViewById(R.id.main_content_last_refuels_first_empty);
        mRecentRefuelsSecondMonthEmpty = getView().findViewById(R.id.main_content_last_refuels_second_empty);

        recentRefuelList = new ArrayList<>();

        mRecentFuelLayout = new LinearLayoutManager(getView().getContext());
        mRecentFuelAdapter = new RecentFuelAdapter(recentRefuelList);

        mRecentFuelRecyclerView = getView().findViewById(R.id.main_content_fuel_recyclerview);
        mRecentFuelRecyclerView.setHasFixedSize(true);
        mRecentFuelRecyclerView.setLayoutManager(mRecentFuelLayout);
        mRecentFuelRecyclerView.setAdapter(mRecentFuelAdapter);

        recentRefuelsFirstMonth = new ArrayList<>();
        recentRefuelsSecondMonth = new ArrayList<>();

        mRecentRefuelsFirstMonthRecyclerView = getView().findViewById(R.id.main_content_last_refuels_first_month_recyclerview);
        mRecentRefuelsSecondMonthRecyclerView = getView().findViewById(R.id.main_content_last_refuels_second_month_recyclerview);

        mRecentRefuelsFirstMonthLayout = new LinearLayoutManager(getView().getContext());
        mRecentRefuelsSecondMonthLayout = new LinearLayoutManager(getView().getContext());

        mRecentRefuelsFirstMonthAdapter = new RecentRefuelsAdapter(recentRefuelsFirstMonth);
        mRecentRefuelsSecondMonthAdapter = new RecentRefuelsAdapter(recentRefuelsSecondMonth);

        mRecentRefuelsFirstMonthRecyclerView.setHasFixedSize(true);
        mRecentRefuelsFirstMonthRecyclerView.setLayoutManager(mRecentRefuelsFirstMonthLayout);
        mRecentRefuelsFirstMonthRecyclerView.setAdapter(mRecentRefuelsFirstMonthAdapter);
        mRecentRefuelsSecondMonthRecyclerView.setHasFixedSize(true);
        mRecentRefuelsSecondMonthRecyclerView.setLayoutManager(mRecentRefuelsSecondMonthLayout);
        mRecentRefuelsSecondMonthRecyclerView.setAdapter(mRecentRefuelsSecondMonthAdapter);

        vehicleSpinner.setSelection(preferences.getInt("chosenCarPosition", 0));
        vehicleSpinner.setOnItemSelectedListener(vehicleSpinnerOnItemSelectedListener);
    }

    private void sumMonth(List<Refuel> recentRefuelsInMonth, List<Refuel> refuels, boolean current) {
        recentRefuelsInMonth.clear();
        float priceSum = 0;
        float fuelSum = 0;

        for (int i = 0; i < refuels.size(); i++) {
            if (i < 5) {
                recentRefuelsInMonth.add(refuels.get(i));
            }
            priceSum += refuels.get(i).getPriceForLiter() * refuels.get(i).getAmount();
            fuelSum += refuels.get(i).getAmount();
        }

        if (current) {
            mPaidInCurrentMonth.setText(DecimalFormat.getCurrencyInstance().format((double) priceSum));
            mFueledInCurrentMonth.setText(String.format("%.2f l", fuelSum));
        } else {
            mPaidInPreviousMonth.setText(DecimalFormat.getCurrencyInstance().format((double) priceSum));
            mFueledInPreviousMonth.setText(String.format("%.2f l", fuelSum));
        }


    }

    private List<RecentRefuel> calculateLastRefuel(List<Refuel> refuels, List<RecentRefuel> recentRefuelsTmp) {
        if (recentRefuelsTmp == null) {
            recentRefuelsTmp = new ArrayList<>();
        }
        float consumptionSum = 0;
        RecentRefuel rf = new RecentRefuel();

        for (int i = 0; i < refuels.size() - 1; i++) {
            float delta = refuels.get(i).getCounter() - refuels.get(i + 1).getCounter();
            consumptionSum += refuels.get(i).getAmount() / delta * 100;
        }

        if (refuels.size() > 1) {
            rf.setAvgConsumption(consumptionSum / refuels.size() - 1);
            rf.setDate(refuels.get(0).getDate());
            rf.setLastPrice(refuels.get(0).getAmount() * refuels.get(0).getPriceForLiter());
            rf.setLastConsumption(refuels.get(0).getAmount() / (refuels.get(0).getCounter() - refuels.get(1).getCounter()) * 100);
            rf.setType(refuels.get(0).getType());

            recentRefuelsTmp.add(0, rf);
        }

        return recentRefuelsTmp;
    }

    Observer<List<Refuel>> firstTankObserver = new Observer<List<Refuel>>() {
        @Override
        public void onChanged(@Nullable List<Refuel> refuels) {
            final List<RecentRefuel> recentRefuelsTmp = calculateLastRefuel(refuels, null);

            String secondTank = ((Car) vehicleSpinner.getSelectedItem()).getSecondTank();

            if (secondTank != null) {
                mViewModel.getCarRefuelsByFuelType(((Car) vehicleSpinner.getSelectedItem()).getId(), secondTank).observe(MainFragment.this, new Observer<List<Refuel>>() {
                    @Override
                    public void onChanged(@Nullable List<Refuel> refuels) {
                        recentRefuelList.addAll(calculateLastRefuel(refuels, recentRefuelsTmp));
                        mRecentFuelAdapter.notifyDataSetChanged();

                        if (recentRefuelList.size() == 0) {
                            mDescriptionTextView.setText(getString(R.string.not_enough_records));
                        } else {
                            mFuelContainerCardView.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                recentRefuelList.addAll(recentRefuelsTmp);
                mRecentFuelAdapter.notifyDataSetChanged();

                if (recentRefuelList.size() == 0) {
                    mDescriptionTextView.setText(getString(R.string.not_enough_records));
                } else {
                    mFuelContainerCardView.setVisibility(View.GONE);
                }
            }
        }
    };

    AdapterView.OnItemSelectedListener vehicleSpinnerOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("chosenCarPosition", position);
            editor.apply();

            String[] months = {"Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"};

            final int firstMonthInt = Calendar.getInstance().get(Calendar.MONTH);
            final int firstYearInt = Calendar.getInstance().get(Calendar.YEAR);
            final int secondMonthInt = firstMonthInt - 1 < 0 ? 11 : firstMonthInt - 1;
            final int secondYearInt = firstMonthInt > 0 ? firstYearInt : firstYearInt - 1;


            String firstMonth = months[firstMonthInt];
            String secondMonth = months[secondMonthInt];

            mRecentRefuelsFirstMonthLabel.setText(firstMonth);
            mRecentRefuelsSecondMonthLabel.setText(secondMonth);

            if (position > 0) {
                mDescriptionTextView.setText(getText(R.string.loading));
                recentRefuelList.clear();
                mRecentFuelAdapter.notifyDataSetChanged();
                mFuelContainerCardView.setVisibility(View.VISIBLE);

                mViewModel.getCarRefuelsByFuelType(((Car) parent.getSelectedItem()).getId(), ((Car) parent.getSelectedItem()).getFirstTank()).observe(MainFragment.this, firstTankObserver);

                mRecentRefuelsFirstMonthEmpty.setText("");
                mRecentRefuelsSecondMonthEmpty.setText("");
                mViewModel.getCarRefuelsByMonth(((Car) parent.getSelectedItem()).getId(), firstMonthInt, firstYearInt).observe(MainFragment.this, new Observer<List<Refuel>>() {
                    @Override
                    public void onChanged(@Nullable List<Refuel> refuels) {
                        sumMonth(recentRefuelsFirstMonth, refuels, true);
                        mRecentRefuelsFirstMonthAdapter.notifyDataSetChanged();
                        if(refuels.size() == 0) {
                            mRecentRefuelsFirstMonthEmpty.setText(getString(R.string.no_records));
                        }
                    }
                });

                mViewModel.getCarRefuelsByMonth(((Car) parent.getSelectedItem()).getId(), secondMonthInt, secondYearInt).observe(MainFragment.this, new Observer<List<Refuel>>() {
                    @Override
                    public void onChanged(@Nullable List<Refuel> refuels) {
                        sumMonth(recentRefuelsSecondMonth, refuels, false);
                        mRecentRefuelsSecondMonthAdapter.notifyDataSetChanged();
                        if(refuels.size() == 0) {
                            mRecentRefuelsSecondMonthEmpty.setText(getString(R.string.no_records));
                        }
                    }
                });
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
