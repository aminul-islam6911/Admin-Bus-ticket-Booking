package com.example.adminbusticketbookingsystem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminbusticketbookingsystem.Model.BusModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BusesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<BusModel, BusAdapter> firebaseRecyclerAdapter;
    private TextView start, des, busNo;
    private String stDateId, stBusNo, stStart, stDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String road = getIntent().getStringExtra("road_ref");

        DatabaseReference query = FirebaseDatabase.getInstance().getReference().child("Schedule").child(road);
        FirebaseRecyclerOptions<BusModel> options = new FirebaseRecyclerOptions.Builder<BusModel>()
                .setQuery(query, BusModel.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BusModel, BusAdapter>(options) {
            @NonNull
            @Override
            public BusAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_view, parent, false);
                return new BusAdapter(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull BusAdapter holder, @SuppressLint("RecyclerView") final int position, @NonNull BusModel model) {
                holder.txtArrivalTime.setText("Arrival Time :" + model.getArrivalTime());
                holder.txtBusNo.setText("Bus No :" + model.getBusNo());
                holder.txtBusType.setText("Bus Type :" + model.getBusType());
                holder.txtDestination.setText("Destination :" + model.getDestination());
                holder.txtStart.setText("Start :" + model.getStart());
                holder.txtStartingTime.setText("Start Time:" + model.getStartingTime());
                holder.txtTicketPrice.setText("Tk :" + model.getTicketPrice());
                holder.txtSeatNumber.setText("Seat Number :" + model.getNumberOfSeat());

                holder.btnReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DialogPlus dialogPlus = DialogPlus.newDialog(holder.btnReset.getContext())
                                .setContentHolder(new ViewHolder(R.layout.bus_update_view))
                                .setExpanded(true, 550).create();

                        View view = dialogPlus.getHolderView();
                        start = view.findViewById(R.id.bStart);
                        des = view.findViewById(R.id.bDestination);
                        busNo = view.findViewById(R.id.bBusNo);
                        EditText seat = view.findViewById(R.id.bSeatNo);
                        Button update = view.findViewById(R.id.btncUpdate);

                        start.setText(model.getStart());
                        des.setText(model.getDestination());
                        busNo.setText(model.getBusNo());
                        seat.setText(model.getNumberOfSeat());

                        stStart = start.getText().toString();
                        stDestination = des.getText().toString();
                        stBusNo = busNo.getText().toString();
                        stDateId = (stStart + " " + stDestination);

                        DatabaseReference seatUp = FirebaseDatabase.getInstance().getReference().child("Schedule")
                                .child(stDateId).child(stBusNo);
                        dialogPlus.show();

                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<String, Object> upSeats = new HashMap<>();
                                upSeats.put("NumberOfSeat", seat.getText().toString());
                                seatUp.updateChildren(upSeats).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(BusesActivity.this, "Seat Reset Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}