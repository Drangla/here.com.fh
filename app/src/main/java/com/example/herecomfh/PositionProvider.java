package com.example.herecomfh;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class PositionProvider implements LocationListener {
    private Context context;
    private PlatformLocationListener platformLocationListener;
    private LocationManager locationManager;
    private static final String TAG = MainActivity.class.getCanonicalName();
    public static final int LOCATION_UPDATE_INTERVAL_IN_MS = 100;


    public PositionProvider(Context context) {
        this.context = context;
    }

    public void startLocating(PlatformLocationListener platformLocationListener) {
        if (this.platformLocationListener != null) {
            throw new RuntimeException("Please stop locating before starting again");
        }

        if (ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Positioning permission denied");
            return;
        }

        this.platformLocationListener = platformLocationListener;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_INTERVAL_IN_MS, 1, this);
            Log.d(TAG, "locating startet gps");
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_INTERVAL_IN_MS,1,this);
            Log.d(TAG, "locating startet gps");

        } else {
            Log.d(TAG, "Positioning not possible");
            stopLocating();
        }
    }

    private void stopLocating() {
        if (locationManager == null){
            return;
        }
        locationManager.removeUpdates(this);
        platformLocationListener = null;
    }

    public interface  PlatformLocationListener {
        void onLocationUpdate(Location location);
    }



    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (platformLocationListener != null) {
            platformLocationListener.onLocationUpdate(location);
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
