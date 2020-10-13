package com.shorts.shortmaker.DataClasses;

public class LocationData {
    String locationAddress;
    String locationName;
    Double latitude;
    Double longitude;
    Double radius;

    public LocationData() {
        /* empty for FireStore */
    }

    public LocationData(String locationAddress,
                        String locationName,
                        Double latitude,
                        Double longitude,
                        Double radius) {
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
