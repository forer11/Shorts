package com.shorts.shortmaker.DataClasses;

public class LocationData {
    String locationAddress;
    String locationName;
    String latitude;
    String longitude;
    String radius;

    public LocationData() {
        /* empty for FireStore */
    }

    public LocationData(String locationAddress,
                        String locationName,
                        String latitude,
                        String longitude,
                        String radius) {
        this.locationAddress = locationAddress;
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }


    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }
}
