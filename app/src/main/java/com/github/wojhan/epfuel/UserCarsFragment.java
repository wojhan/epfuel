package com.github.wojhan.epfuel;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.wojhan.epfuel.db.Car;
import com.github.wojhan.epfuel.db.FuelDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class UserCarsFragment extends Fragment {

    private UserCarsViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private UserCarAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int mCurrentPos;

    private List<Car> mCarList;

    public static UserCarsFragment newInstance() {
        return new UserCarsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_cars_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserCarsViewModel.class);
        // TODO: Use the ViewModel

        mCarList = new ArrayList<>();

        mRecyclerView = getView().findViewById(R.id.user_cars_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new UserCarAdapter(mCarList);

        mAdapter.setOnCreateContextMenuListener(new UserCarAdapter.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v) {
                MenuInflater menuInflater = getActivity().getMenuInflater();
                menuInflater.inflate(R.menu.context_menu, menu);
            }
        });

        mAdapter.setOnLongClickListener(new UserCarAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                mCurrentPos = position;
            }
        });


        mRecyclerView.setAdapter(mAdapter);

        mViewModel.getAllCars().observe(this, carObserver);

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCarActivity.class);
                startActivityForResult(intent, MainActivity.ADD_NEW_CAR_ACTIVITY);
            }
        });
    }

    Observer<List<Car>> carObserver = new Observer<List<Car>>() {
        @Override
        public void onChanged(@Nullable List<Car> cars) {
            if (!mCarList.isEmpty()) {
                mCarList.clear();
            }
            mCarList.addAll(cars);
            mAdapter.notifyDataSetChanged();
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.ADD_NEW_CAR_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String name = data.getExtras().getString("name");
                String make = data.getExtras().getString("make");
                String model = data.getExtras().getString("model");
                String description = data.getExtras().getString("description");
                String firstTank = data.getExtras().getString("firstTank");
                String secondTank = null;
                String photo = data.getExtras().getString("photo");
                if (data.getExtras().get("secondTank") != null) {
                    secondTank = (String) data.getExtras().getString("secondTank");
                }

                Car car = new Car();
                car.setName(name);
                car.setMake(make);
                car.setModel(model);
                car.setDescription(description);
                car.setFirstTank(firstTank);
                car.setSecondTank(secondTank);
                car.setImage(photo);
                mViewModel.insertCar(car);
            } else if (resultCode == RESULT_CANCELED) {

            }
        }

        if (requestCode == MainActivity.EDIT_CAR_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String name = data.getExtras().getString("name");
                String make = data.getExtras().getString("make");
                String model = data.getExtras().getString("model");
                String description = data.getExtras().getString("description");
                String firstTank = data.getExtras().getString("firstTank");
                String secondTank = null;
                String photo = data.getExtras().getString("photo");
                if (data.getExtras().get("secondTank") != null) {
                    secondTank = (String) data.getExtras().getString("secondTank");
                }

                Car car = mCarList.get(mCurrentPos);
                car.setName(name);
                car.setMake(make);
                car.setModel(model);
                car.setDescription(description);
                car.setFirstTank(firstTank);
                car.setSecondTank(secondTank);
                car.setImage(photo);
                mViewModel.updateCar(car);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = mCurrentPos;
        } catch (Exception e) {
            Log.d("epfuelapp", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.context_menu_edit:
                Intent intent = new Intent(getActivity(), EditCarActivity.class);
                intent.putExtra("name", mCarList.get(position).getName());
                intent.putExtra("make", mCarList.get(position).getMake());
                intent.putExtra("model", mCarList.get(position).getModel());
                intent.putExtra("description", mCarList.get(position).getDescription());
                intent.putExtra("firstTank", mCarList.get(position).getFirstTank());
                intent.putExtra("secondTank", mCarList.get(position).getSecondTank());
                intent.putExtra("photo", mCarList.get(position).getImage());
                startActivityForResult(intent, MainActivity.EDIT_CAR_ACTIVITY);
                break;
            case R.id.context_menu_remove:
                mViewModel.deleteCar(mCarList.get(position));
                mAdapter.notifyItemRemoved(position);
                break;
        }
        return super.onContextItemSelected(item);
    }
}
