package com.github.wojhan.epfuel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddCarActivity extends AppCompatActivity {

    private ArrayList<Make> mMakelist;
    private ArrayList<Model> mModelList;

    private MakeAdapter mMakeAdapter;
    private ModelAdapter mModelAdapter;

    private TextView mNameTextview;
    private Spinner mMakeSpinner;
    private Spinner mModelSpinner;
    private TextView mDescriptionTextView;
    private Switch mTwoTanksSwitch;
    private Spinner mFirstFuelTypeSpinner;
    private Spinner mSecondFuelTypeSpinner;
    private TextView mFirstFuelIcon;
    private TextView mSecondFuelIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMakeSpinner = findViewById(R.id.add_car_basic_brand);
        mModelSpinner = findViewById(R.id.add_car_basic_model);
        mNameTextview = findViewById(R.id.add_car_basic_name);
        mDescriptionTextView = findViewById(R.id.add_car_basic_description);
        mTwoTanksSwitch = findViewById(R.id.add_car_fuel_type_two_tanks_value);
        mFirstFuelTypeSpinner = findViewById(R.id.add_car_fuel_type_first_value);
        mSecondFuelTypeSpinner = findViewById(R.id.add_car_fuel_type_second_value);
        mFirstFuelIcon = findViewById(R.id.add_car_fuel_type_first_icon);
        mSecondFuelIcon = findViewById(R.id.add_car_fuel_type_second_icon);

        mTwoTanksSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSecondFuelTypeSpinner.setVisibility(View.VISIBLE);
                    mSecondFuelIcon.setVisibility(View.VISIBLE);
                } else {
                    mSecondFuelTypeSpinner.setVisibility(View.GONE);
                    mSecondFuelIcon.setVisibility(View.GONE);
                }
            }
        });

        String[] firstTank = {"Benzyna", "Diesel"};
        String[] secondTank = {"LPG", "CNG"};

        ArrayAdapter<String> firstSpinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        firstTank);
        firstSpinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        mFirstFuelTypeSpinner.setAdapter(firstSpinnerArrayAdapter);

        ArrayAdapter<String> secondSpinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        secondTank);
        secondSpinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        mSecondFuelTypeSpinner.setAdapter(secondSpinnerArrayAdapter);


        if (!mTwoTanksSwitch.isActivated()) {
            mSecondFuelTypeSpinner.setVisibility(View.GONE);
            mSecondFuelIcon.setVisibility(View.GONE);
        }

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CarApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final CarApiService service = retrofit.create(CarApiService.class);
        final Call<MakeList> makeCall = service.getMakes();

        makeCall.enqueue(new Callback<MakeList>() {
            @Override
            public void onResponse(Call<MakeList> call, Response<MakeList> response) {
                MakeList makeList = response.body();

                Make[] test = makeList.getMakes();

                mMakelist = new ArrayList<>(Arrays.asList(makeList.getMakes()));
                mMakeAdapter = new MakeAdapter(mMakelist);
                mMakeSpinner.setAdapter(mMakeAdapter);
            }

            @Override
            public void onFailure(Call<MakeList> call, Throwable t) {
                Log.d("addCarActivity", t.getMessage());
            }
        });

        Call<ModelList> modelCall = service.getModels("opel");
        modelCall.enqueue(new Callback<ModelList>() {
            @Override
            public void onResponse(Call<ModelList> call, Response<ModelList> response) {
                ModelList modelList = response.body();

                mModelList = new ArrayList<>(Arrays.asList(modelList.getModels()));
                mModelAdapter = new ModelAdapter(mModelList);
                mModelSpinner.setAdapter(mModelAdapter);
            }

            @Override
            public void onFailure(Call<ModelList> call, Throwable t) {

            }
        });

        mMakeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mMakeAdapter != null && mMakelist.size() > 0) {
                    Make make = (Make) mMakeSpinner.getSelectedItem();
                    Call<ModelList> modelCall = service.getModels(make.getId());
                    modelCall.enqueue(new Callback<ModelList>() {
                        @Override
                        public void onResponse(Call<ModelList> call, Response<ModelList> response) {
                            ModelList modelList = response.body();

                            mModelList = new ArrayList<>(Arrays.asList(modelList.getModels()));
                            mModelAdapter = new ModelAdapter(mModelList);
                            mModelSpinner.setAdapter(mModelAdapter);
                        }

                        @Override
                        public void onFailure(Call<ModelList> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_refuel, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_refuel_done:
                Intent backIntent = new Intent();
                backIntent.putExtra("name", mNameTextview.getText().toString());
                backIntent.putExtra("make", ((Make) mMakeSpinner.getSelectedItem()).getName());
                backIntent.putExtra("model", ((Model) mModelSpinner.getSelectedItem()).getName());
                backIntent.putExtra("description", mDescriptionTextView.getText().toString());
                backIntent.putExtra("firstTank", (String) mFirstFuelTypeSpinner.getSelectedItem());
                backIntent.putExtra("secondTank", mTwoTanksSwitch.isChecked() ? (String) mSecondFuelTypeSpinner.getSelectedItem() : null);
                setResult(RESULT_OK, backIntent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
