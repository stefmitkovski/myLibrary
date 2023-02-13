package com.example.mylibrary;

public class Location {
    public String email;
    public Double latitude;
    public Double longitude;

    public Location() {
    }

    @Override
    public String toString() {
        return "Location{" +
                "email='" + email + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public Location(String email, Double latitude, Double longitude) {
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
