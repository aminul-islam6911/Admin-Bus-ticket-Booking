package com.example.adminbusticketbookingsystem.Model;

public class BusModel {
    String ArrivalTime, BusNo, BusType, Destination, Start, StartingTime, TicketPrice, NumberOfSeat;

    public BusModel() {
    }

    public BusModel(String arrivalTime, String busNo, String busType, String destination, String start, String startingTime, String ticketPrice, String numberOfSeat) {
        ArrivalTime = arrivalTime;
        BusNo = busNo;
        BusType = busType;
        Destination = destination;
        Start = start;
        StartingTime = startingTime;
        TicketPrice = ticketPrice;
        NumberOfSeat = numberOfSeat;
    }

    public String getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    public String getBusNo() {
        return BusNo;
    }

    public void setBusNo(String busNo) {
        BusNo = busNo;
    }

    public String getBusType() {
        return BusType;
    }

    public void setBusType(String busType) {
        BusType = busType;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getStart() {
        return Start;
    }

    public void setStart(String start) {
        Start = start;
    }

    public String getStartingTime() {
        return StartingTime;
    }

    public void setStartingTime(String startingTime) {
        StartingTime = startingTime;
    }

    public String getTicketPrice() {
        return TicketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        TicketPrice = ticketPrice;
    }

    public String getNumberOfSeat() {
        return NumberOfSeat;
    }

    public void setNumberOfSeat(String numberOfSeat) {
        NumberOfSeat = numberOfSeat;
    }
}