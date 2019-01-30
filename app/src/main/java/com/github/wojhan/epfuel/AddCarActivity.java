package com.github.wojhan.epfuel;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

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

    private ImageView mPhoto;

    private Bitmap bp;
    private String photoPath = "";

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
        mPhoto = findViewById(R.id.add_car_photo_image);


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
            case android.R.id.home:
                Intent backIntent = new Intent();
                setResult(RESULT_CANCELED, backIntent);
                finish();
                return true;
            case R.id.menu_add_refuel_done:
                backIntent = new Intent();
                backIntent.putExtra("name", mNameTextview.getText().toString());
                backIntent.putExtra("make", ((Make) mMakeSpinner.getSelectedItem()).getName());
                backIntent.putExtra("model", ((Model) mModelSpinner.getSelectedItem()).getName());
                backIntent.putExtra("description", mDescriptionTextView.getText().toString());
                backIntent.putExtra("firstTank", (String) mFirstFuelTypeSpinner.getSelectedItem());
                backIntent.putExtra("secondTank", mTwoTanksSwitch.isChecked() ? (String) mSecondFuelTypeSpinner.getSelectedItem() : null);
                backIntent.putExtra("photo", photoPath);
                setResult(RESULT_OK, backIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void choosePhoto(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri choosenImage = data.getData();

            if (choosenImage != null) {

                bp = decodeUri(choosenImage, 400);
                photoPath = saveToInternalStorage(bp, mNameTextview.getText().toString() + Calendar.getInstance().getTimeInMillis() + ".png");
                mPhoto.setImageBitmap(bp);
            }
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String name) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("carImages", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
