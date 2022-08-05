package com.example.adminbusticketbookingsystem.Model;

import androidx.annotation.NonNull;

public class BusRouteModel {
    private String Road;

    public BusRouteModel() {
    }

    public BusRouteModel(String road) {
        Road = road;
    }

    public String getRoad() {
        return Road;
    }

    public void setRoad(String road) {
        Road = road;
    }

    @NonNull
    public String toString() {
        return Road;
    }
}
