package com.example.adminbusticketbookingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.adminbusticketbookingsystem.Interface.IFirebaseLoadDone;
import com.example.adminbusticketbookingsystem.Model.IDs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BusActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener, IFirebaseLoadDone{
    private Button btnStartingTime, btnArrivalTime, btnAddBus, btnRefresh;
    private EditText edtBusNo, edtTicketPrice, edtNoOfSeat;
    private TextView txtName;
    private String stName, stSpecificDay, stBusType;
    private String stStartingTime, stArrivalTime;
    private DatabaseReference admin_name, locationRef, StoringData;
    private ProgressDialog Initilizer_PD, sendingData;
    private boolean ChooseStarttime = true;
    private Spinner spinnerBusType;
    private SearchableSpinner spStartingLocation, spDestinationLocation;
    int hour, minute, hourFinal, minuteFinal;
    int day, month, year, dayFinal, monthFinal, yearFinal;

    IFirebaseLoadDone iFirebaseLoadDone;
    String stStartingLoc, stDestinationLoc;
    List<IDs> iDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        Initialize();
        busAc_NonAc();
        BusStartingTime();
        BusArrivalTime();
        FirebaseDataRetrieve();
        SpinnerGetText();
        Update();
        Confirming();
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtBusNo.setText("");
                edtNoOfSeat.setText("");
                edtTicketPrice.setText("");
                btnStartingTime.setText("Start Time");
                btnArrivalTime.setText("Arrival Time");
            }
        });
    }

    private void Update() {

    }

    private void Initialize() {
        //Button
        btnStartingTime = findViewById(R.id.btnStartingTime);
        btnArrivalTime = findViewById(R.id.btnArrivalTime);
        btnAddBus = findViewById(R.id.btnAddBus);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);

        //Edittext
        edtBusNo = findViewById(R.id.edtBusNo);
        edtTicketPrice = findViewById(R.id.edtTicketPrice);
        edtNoOfSeat = findViewById(R.id.edtNoOfSeat);

        //Searchable spinner
        spStartingLocation = findViewById(R.id.spStartingLocation);
        spDestinationLocation = findViewById(R.id.spDestinationLocation);

        //Spinner
        spinnerBusType = findViewById(R.id.spinnerBusType);

        sendingData = new ProgressDialog(this);
        sendingData.setTitle("Saving data to database");
        sendingData.setCancelable(false);
    }

    private void busAc_NonAc() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.BusType,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBusType.setAdapter(adapter);
        spinnerBusType.setOnItemSelectedListener(this);
    }

    private void BusStartingTime() {
        btnStartingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseStarttime = true;
                SelectTime();
            }
        });
    }

    private void BusArrivalTime() {
        btnArrivalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseStarttime = false;
                SelectTime();
            }
        });
    }

    private void FirebaseDataRetrieve() {
        locationRef = FirebaseDatabase.getInstance().getReference("Locations");
        iFirebaseLoadDone = this;
        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<IDs> iDs = new ArrayList<>();
                for (DataSnapshot idSnapShot : dataSnapshot.getChildren()) {
                    iDs.add(idSnapShot.getValue(IDs.class));
                }
                iFirebaseLoadDone.onFirebaseLoadSuccess(iDs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
            }
        });
    }

    private void SpinnerGetText() {
        spStartingLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IDs iD = iDs.get(position);
                stStartingLoc = iD.getPlace();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDestinationLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IDs iD = iDs.get(position);
                stDestinationLoc = iD.getPlace();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Confirming() {
        btnAddBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busAc_NonAc();
                String dbBusNo = edtBusNo.getText().toString();
                String dbNoOfSeats = edtNoOfSeat.getText().toString();
                String Ticket_price = edtTicketPrice.getText().toString();

                if (!stStartingLoc.equals(stDestinationLoc)) {
                    if (!stBusType.isEmpty() && !dbBusNo.isEmpty() && !stStartingLoc.isEmpty()
                            && !stDestinationLoc.isEmpty() && !stStartingTime.isEmpty()
                            && !stArrivalTime.isEmpty() && !dbNoOfSeats.isEmpty() && !Ticket_price.isEmpty()) {
                        sendingData.show();
                        SendBusData(stBusType, dbBusNo, stStartingLoc, stDestinationLoc, stStartingTime, stArrivalTime, dbNoOfSeats, Ticket_price);
                    } else {
                        Toast.makeText(BusActivity.this, "Please fill each box", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BusActivity.this, "Location is repeated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SendBusData(String BusType, String BusNo,
                             String StartingLoc, String DestinationLoc,
                             String StartingTime, String ArrivalTime, String NoOfSeats, String Price) {
        String DateId = (stStartingLoc + " " + stDestinationLoc);
        StoringData = FirebaseDatabase.getInstance().getReference().
                child("Buses").child(DateId).child(BusNo);
        HashMap<String, String> loc = new HashMap<>();
        loc.put("Start", stStartingLoc);
        loc.put("Destination", stDestinationLoc);
        loc.put("StartingTime", stStartingTime);
        loc.put("ArrivalTime", stArrivalTime);
        loc.put("BusType", stBusType.toLowerCase());//Converting text to lower case
        loc.put("BusNo", BusNo);
        loc.put("NumberOfSeat", NoOfSeats);
        loc.put("TicketPrice", Price);
        StoringData.setValue(loc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    sendingData.dismiss();
                    Toast.makeText(BusActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    sendingData.dismiss();
                    Toast.makeText(BusActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SelectTime() {
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(BusActivity.this,
                BusActivity.this, hour, minute,
                DateFormat.is24HourFormat(BusActivity.this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;
        if (ChooseStarttime) {
            btnStartingTime.setText("Starting at : " + hourFinal + " : " + minuteFinal);
            stStartingTime = (hourFinal + " : " + minuteFinal);
        }
        if (!ChooseStarttime) {
            btnArrivalTime.setText("Arriving at : " + hourFinal + " : " + minuteFinal);
            stArrivalTime = (hourFinal + " : " + minuteFinal);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        stBusType = parent.getItemAtPosition(position).toString();
        Toast.makeText(BusActivity.this, stBusType, Toast.LENGTH_SHORT);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onFirebaseLoadSuccess(List<IDs> LocationList) {
        iDs = LocationList;
        List<String> id_list = new ArrayList<>();
        for (IDs id : LocationList) {
            id_list.add(id.getPlace());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, id_list);
            spStartingLocation.setAdapter(adapter);
            spDestinationLocation.setAdapter(adapter);
        }
    }

    @Override
    public void onFirebaseLoadFailed(String Message) {

    }
}