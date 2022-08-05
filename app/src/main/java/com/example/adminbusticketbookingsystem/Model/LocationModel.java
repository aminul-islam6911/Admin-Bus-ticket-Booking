package com.example.adminbusticketbookingsystem.Model;

import androidx.annotation.NonNull;

public class LocationModel {
    private String Place;

    public LocationModel() {
    }

    public LocationModel(String place) {
        Place = place;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    @NonNull
    public String toString() {
        return Place;
    }
}
