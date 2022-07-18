package com.example.adminbusticketbookingsystem.Interface;

import com.example.adminbusticketbookingsystem.Model.IDs;

import java.util.List;

public interface IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<IDs> LocationList);

    void onFirebaseLoadFailed(String Message);
}