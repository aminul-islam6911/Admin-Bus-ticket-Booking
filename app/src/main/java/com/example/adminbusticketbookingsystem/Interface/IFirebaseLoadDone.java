package com.example.adminbusticketbookingsystem.Interface;

import com.example.adminbusticketbookingsystem.Model.LocationModel;

import java.util.List;

public interface IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<LocationModel> LocationList);

    void onFirebaseLoadFailed(String Message);
}