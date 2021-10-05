package com.example.lab4_milestone2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.os.Bundle;
import android.os.Build;
import android.widget.TextView;
import android.location.Address;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }
        };
        if (Build.VERSION.SDK_INT < 23) {
            startListening();
        } else {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                if (location != null) {
                    updateLocationInfo(location);
                }

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    String address = "Could not find address";
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);
                    if(listAddresses != null && listAddresses.size() > 0) {
                        Log.i("Place info", listAddresses.get(0).toString());
                        address = "Address: \n";
                        if(listAddresses.get(0).getSubThoroughfare() != null) {
                            address += listAddresses.get(0).getSubThoroughfare()+" ";
                        }
                        if (listAddresses.get(0).getThoroughfare() != null) {
                            address += listAddresses.get(0).getThoroughfare() + "\n";
                        }
                        if (listAddresses.get(0).getLocality() != null) {
                            address += listAddresses.get(0).getLocality() + "\n";
                        }
                        if (listAddresses.get(0).getPostalCode() != null) {
                            address += listAddresses.get(0).getPostalCode() + "\n";
                        }
                        if (listAddresses.get(0).getCountryName() != null) {
                            address += listAddresses.get(0).getCountryName() + "\n";
                        }
                    }
                    TextView addressTextView = (TextView) findViewById(R.id.address);
                    addressTextView.setText(address);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void updateLocationInfo(Location location) {
        Log.i("LocationInfo", location.toString());
        TextView latTextView = (TextView) findViewById(R.id.latitude);
        TextView lonTextView = (TextView) findViewById(R.id.longitude);
        TextView altTextView = (TextView) findViewById(R.id.altitude);
        TextView accTextView = (TextView) findViewById(R.id.accuracy);
        latTextView.setText("Latitude: "+location.getLatitude());
        lonTextView.setText("Longitude: "+location.getLongitude());
        altTextView.setText("Altitude: "+location.getAltitude());
        accTextView.setText("Accuracy: "+location.getAccuracy());

    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

}

