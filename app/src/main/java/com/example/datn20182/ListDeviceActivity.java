package com.example.datn20182;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ListDeviceActivity extends AppCompatActivity {
    LinearLayout linearQuat, linearDen;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_device);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Danh Sách Thiết Bị");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        linearQuat = findViewById(R.id.linearQuat);
        linearDen = findViewById(R.id.linearDen);
        linearDen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(ListDeviceActivity.this);
                dialog.setContentView(R.layout.dialog_device);
                final RadioButton rbLevel0, rbLevel1, rbLevel2, rbLevel3, rbLevel4;
                rbLevel0 = dialog.findViewById(R.id.RadioButtonLevel0ControlPower);
                rbLevel1 = dialog.findViewById(R.id.RadioButtonLevel1ControlPower);

                TextView text = dialog.findViewById(R.id.text);
                text.setText("Device Control");
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Device").child("Đèn bàn").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        switch (Objects.requireNonNull(dataSnapshot.getValue()).toString()) {
                            case "0": {
                                rbLevel0.setChecked(true);
                                break;
                            }
                            case "1": {
                                rbLevel1.setChecked(true);
                                break;
                            }
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                rbLevel0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child("Device").child("Đèn bàn").setValue(0);
                    }
                });
                rbLevel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child("Device").child("Đèn bàn").setValue(1);
                    }
                });
                dialog.show();
            }
        });
        linearQuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(ListDeviceActivity.this);
                dialog.setContentView(R.layout.dialog_device);
                final RadioButton rbLevel0, rbLevel1, rbLevel2, rbLevel3, rbLevel4;
                rbLevel0 = dialog.findViewById(R.id.RadioButtonLevel0ControlPower);
                rbLevel1 = dialog.findViewById(R.id.RadioButtonLevel1ControlPower);

                TextView text = dialog.findViewById(R.id.text);
                text.setText("Device Control");
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Device").child("Quạt điện").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        switch (Objects.requireNonNull(dataSnapshot.getValue()).toString()) {
                            case "0": {
                                rbLevel0.setChecked(true);
                                break;
                            }
                            case "1": {
                                rbLevel1.setChecked(true);
                                break;
                            }
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                rbLevel0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child("Device").child("Quạt điện").setValue(0);
                    }
                });
                rbLevel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child("Device").child("Quạt điện").setValue(1);
                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
