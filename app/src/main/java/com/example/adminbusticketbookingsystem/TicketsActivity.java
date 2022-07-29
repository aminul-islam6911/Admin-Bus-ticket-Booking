package com.example.adminbusticketbookingsystem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class TicketsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    private TextView txtDate;
    int day, month, year, dayfinal, monthfinal, yearfinal;
    String dbDate = "nothing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        listView = findViewById(R.id.AD_listView);
        txtDate = findViewById(R.id.AD_Select_date);
        Buttons();
    }

    private void Buttons() {
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                day = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH);
                year = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TicketsActivity.this,
                        TicketsActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String date_ref = arrayList.get(i);
                Intent in = new Intent(TicketsActivity.this, Ticket_time.class);
                in.putExtra("date_ref", date_ref);
                startActivity(in);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearfinal = year;
        monthfinal = month + 1;
        dayfinal = dayOfMonth;
        dbDate = (dayfinal + "-" + monthfinal + "-" + yearfinal);
        retrieve_data();
    }

    private void retrieve_data() {
        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(myArrayAdapter);

        if (dbDate.equals("nothing")) {
            Toast.makeText(TicketsActivity.this, dbDate, Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference TicketID = FirebaseDatabase.getInstance().getReference().
                    child("Tickets").child("TicketID").child(dbDate);
            TicketID.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String myChildViews = dataSnapshot.getValue(String.class);
                    arrayList.add(myChildViews);
                    myArrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}