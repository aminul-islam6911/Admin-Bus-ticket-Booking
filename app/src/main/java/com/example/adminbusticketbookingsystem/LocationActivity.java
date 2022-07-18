package com.example.adminbusticketbookingsystem;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adminbusticketbookingsystem.Model.IDs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class LocationActivity extends AppCompatActivity {
    FloatingActionButton fabAddLocation;
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    DatabaseReference dbLocationRef, dbLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        fabAddLocation = findViewById(R.id.fabAddLocation);
        listView = findViewById(R.id.lvLocation);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        dbLocationRef = FirebaseDatabase.getInstance().getReference().child("Locations");
        dbLocationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = (Objects.requireNonNull(snapshot.getValue(IDs.class))).toString();
                arrayList.add(value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fabAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(LocationActivity.this);
                dialog.setContentView(R.layout.add_location);
                EditText edtLocation = dialog.findViewById(R.id.edtLocation);
                Button btnAddLocation = dialog.findViewById(R.id.btnAddLocation);
                ProgressDialog progressDialog = new ProgressDialog(LocationActivity.this);

                btnAddLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Location = edtLocation.getText().toString().toUpperCase();
                        if (!Location.isEmpty()) {
                            progressDialog.setTitle("Registering");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            dbLocation = FirebaseDatabase.getInstance().getReference().child("Locations").child(Location);
                            HashMap<String, String> location = new HashMap<>();
                            location.put("Place", Location.toLowerCase());
                            dbLocation.setValue(location).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LocationActivity.this, "Location added in database", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    } else {
                                        Toast.makeText(LocationActivity.this, "Failed to add, try again", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(LocationActivity.this, "Please fill each box", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}