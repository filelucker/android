package com.example.mon.application9;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int REQUEST_LOCATION = 1;
    Button button;
    Button settimerbutton,button_show_save_location,button_save_location;
    TextView textView;
    LocationManager locationManager;
    String lattitude,longitude;
    private Handler mHandler = new Handler();
    DatabaseHelper myDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

    textView = (TextView)findViewById(R.id.text_location);
    button = (Button)findViewById(R.id.button_location);
    button_show_save_location = (Button)findViewById(R.id.button_show_save_location);
    settimerbutton = (Button)findViewById(R.id.button_updated_Location);
    button_save_location = (Button)findViewById(R.id.button_save_location);

    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    button.setOnClickListener(this);
    settimerbutton.setOnClickListener(this);
    button_show_save_location.setOnClickListener(this);
    button_save_location.setOnClickListener(this);
    myDB = new DatabaseHelper(this);


}

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button_location:
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();
                 }break;

                case R.id.button_updated_Location:

                    mToastRunnable.run();

                break;

            case R.id.button_show_save_location:

                Intent intent = new Intent(MainActivity.this, ViewListContents.class);
                startActivity(intent);

                break;

            case R.id.button_save_location:

                String newEntry = textView.getText().toString();
                if(textView.length()!= 0){
                    AddData(newEntry);
                    textView.setText("");
                }else{
                    Toast.makeText(MainActivity.this, "You must put something in the text field!", Toast.LENGTH_LONG).show();
                }


                break;
        }
    }

    public void AddData(String newEntry) {

        boolean insertData = myDB.addData(newEntry);

        if(insertData==true){
            Toast.makeText(this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
        }
    }


    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            getLocation();
            Toast.makeText(MainActivity.this, "Showing New Location", Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(this, 5000);
        }
    };


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude);

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude);


            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude);

            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
