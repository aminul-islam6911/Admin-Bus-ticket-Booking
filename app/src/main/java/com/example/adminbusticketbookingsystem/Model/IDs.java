package com.example.adminbusticketbookingsystem.Model;

import androidx.annotation.NonNull;

public class IDs {
    private String Place;

    public IDs() {
    }

    public IDs(String place) {
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
