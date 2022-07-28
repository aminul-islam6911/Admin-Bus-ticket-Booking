package com.example.adminbusticketbookingsystem;

import androidx.annotation.NonNull;

public class BusNoModel {
    private String BusNo;

    public BusNoModel() {
    }

    public BusNoModel(String busNo) {
        BusNo = busNo;
    }

    public String getBusNo() {
        return BusNo;
    }

    public void setBusNo(String busNo) {
        BusNo = busNo;
    }

    @NonNull
    public String toString() {
        return BusNo;
    }
}
