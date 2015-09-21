package com.keenant.mealdeal;

import android.location.Location;
import android.location.LocationManager;

public class LocationTracker {

    private final LocationManager manager;

    public LocationTracker(LocationManager locationManager) {
        manager = locationManager;
    }

    public Location getLastLocation() {
        return manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
}
