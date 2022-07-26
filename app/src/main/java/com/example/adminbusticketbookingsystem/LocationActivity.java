package com.example.adminbusticketbookingsystem;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adminbusticketbookingsystem.Model.IDs;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Objects;

public class LocationActivity extends AppCompatActivity {
    FloatingActionButton fabAddLocation;
    ListView listView;
    DatabaseReference dbLocation;
    FirebaseListAdapter firebaseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        fabAddLocation = findViewById(R.id.fabAddLocation);
        listView = findViewById(R.id.lvLocation);

        Query query = FirebaseDatabase.getInstance().getReference().child("Locations");
        FirebaseListOptions<IDs> options = new FirebaseListOptions.Builder<IDs>()
                .setLayout(R.layout.list_view_loc)
                .setQuery(query, IDs.class)
                .build();

        firebaseListAdapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(View v, Object model, final int position) {
                TextView textView = v.findViewById(R.id.txtLoc);

                IDs place = (IDs) model;
                textView.setText(place.getPlace());

                v.findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(LocationActivity.this);
                        dialog.setContentView(R.layout.update_location);
                        EditText edtUpLocation = dialog.findViewById(R.id.edtUpLocation);
                        Button btnUpdateLocation = dialog.findViewById(R.id.btnUpdateLocation);
                        Button btnCancel = dialog.findViewById(R.id.btnCancel);
                        ProgressDialog progressDialog = new ProgressDialog(LocationActivity.this);

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        btnUpdateLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String UpLocation = edtUpLocation.getText().toString().toUpperCase();
                                if (!UpLocation.isEmpty()) {
                                    progressDialog.setTitle("Updating");
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();
                                    dbLocation = FirebaseDatabase.getInstance().getReference().child("Locations").child(Objects.requireNonNull(getRef(position).getKey()));
                                    HashMap<String, Object> upLoc = new HashMap<>();
                                    upLoc.put("Place", UpLocation.toLowerCase());
                                    dbLocation.updateChildren(upLoc).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(LocationActivity.this, "Location updated in database", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            } else {
                                                Toast.makeText(LocationActivity.this, "Failed to update, try again", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    });
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(LocationActivity.this, "Please Update Location", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                        dialog.show();
                    }
                });

                v.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                        builder.setMessage("Are you sure you want to Delete?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseDatabase.getInstance().getReference().child("Locations").child(Objects.requireNonNull(getRef(position).getKey())).removeValue();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
            }
        };
        listView.setAdapter(firebaseListAdapter);
        addLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseListAdapter.stopListening();
    }

    private void addLocation() {
        fabAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(LocationActivity.this);
                dialog.setContentView(R.layout.add_location);
                EditText edtAdLocation = dialog.findViewById(R.id.edtAdLocation);
                Button btnAddLocation = dialog.findViewById(R.id.btnAddLocation);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                ProgressDialog progressDialog = new ProgressDialog(LocationActivity.this);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnAddLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Location = edtAdLocation.getText().toString().toUpperCase();
                        if (!Location.isEmpty()) {
                            progressDialog.setTitle("Adding");
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
                            dialog.dismiss();
                        } else {
                            Toast.makeText(LocationActivity.this, "Please Add Location", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
    }
}