package com.example.datn20182;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingParamActivity extends AppCompatActivity {
    TextView txt_tempMax, txt_tempMin, txt_humiMax, txt_humiMin , txt_lightMax , txt_lightMin;
    EditText edt_tempMax, edt_tempMin, edt_humiMax, edt_humiMin , edt_lightMax , edt_lightMin;
    Button btn_commitTemp, btn_commitHumi , btn_commitLight;
    Toolbar toolbar;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_param);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cài đặt thông số");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt_tempMax = findViewById(R.id.txtTempMax);
        txt_tempMin = findViewById(R.id.txtTempMin);
        txt_humiMax = findViewById(R.id.txtHumiMax);
        txt_humiMin = findViewById(R.id.txtHumiMin);
        edt_humiMax = findViewById(R.id.edtHumiMax);
        edt_humiMin = findViewById(R.id.edtHumiMin);
        edt_tempMax = findViewById(R.id.edtTempMax);
        edt_tempMin = findViewById(R.id.edtTempMin);
        btn_commitHumi = findViewById(R.id.btnCommitHumi);
        btn_commitTemp = findViewById(R.id.btnCommitTemp);
       // txt_lightMax = findViewById(R.id.txtLightMax);
        txt_lightMin = findViewById(R.id.txtLightMin);
       // edt_lightMax = findViewById(R.id.edtLightMax);
        edt_lightMin = findViewById(R.id.edtLightMin);
        btn_commitLight = findViewById(R.id.btnCommitLight);
        databaseReference.child("Param").child("Nhiệt Độ").child("Ngưỡng dưới").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String minTemp = String.valueOf(dataSnapshot.getValue());
                minTemp = minTemp + (char) 0x00B0 + "C";
                txt_tempMin.setText("Min: " + minTemp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Param").child("Nhiệt Độ").child("Ngưỡng trên").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String maxTemp = String.valueOf(dataSnapshot.getValue());
                maxTemp = maxTemp + (char) 0x00B0 + "C";
                txt_tempMax.setText("Max: " + maxTemp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Param").child("Độ Ẩm").child("Ngưỡng dưới").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String minHumi = String.valueOf(dataSnapshot.getValue());
                minHumi = minHumi + "%";
                txt_humiMin.setText("Min: " + minHumi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Param").child("Độ Ẩm").child("Ngưỡng trên").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String maxHumi = String.valueOf(dataSnapshot.getValue());
                maxHumi = maxHumi + "%";
                txt_humiMax.setText("Max: " + maxHumi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Param").child("Ánh sáng").child("Ngưỡng dưới").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String minLight = String.valueOf(dataSnapshot.getValue());
                minLight = minLight + "%";
                txt_lightMin.setText("Min: " + minLight);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_commitTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_tempMin.getText().toString().length() == 0 && edt_tempMax.getText().toString().length() == 0) {
                    Toast.makeText(SettingParamActivity.this, "Nhập thông số", Toast.LENGTH_SHORT).show();
                } else {
                    if (edt_tempMax.getText().toString().length() == 0 && edt_tempMin.getText().toString().length() != 0) {
                        int newMinTemp = Integer.parseInt(edt_tempMin.getText().toString());
                        databaseReference.child("Param").child("Nhiệt Độ").child("Ngưỡng dưới").setValue(newMinTemp);
                    }
                    if (edt_tempMax.getText().toString().length() != 0 && edt_tempMin.getText().toString().length() == 0) {
                        int newMaxTemp = Integer.parseInt(edt_tempMax.getText().toString());
                        databaseReference.child("Param").child("Nhiệt Độ").child("Ngưỡng trên").setValue(newMaxTemp);
                    }
                    if (edt_tempMax.getText().toString().length() != 0 && edt_tempMin.getText().toString().length() != 0) {
                        int newMaxTemp = Integer.parseInt(edt_tempMax.getText().toString());
                        int newMinTemp = Integer.parseInt(edt_tempMin.getText().toString());
                        databaseReference.child("Param").child("Nhiệt Độ").child("Ngưỡng trên").setValue(newMaxTemp);
                        databaseReference.child("Param").child("Nhiệt Độ").child("Ngưỡng dưới").setValue(newMinTemp);
                    }
                }

            }
        });
        btn_commitHumi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_humiMin.getText().toString().length() == 0 && edt_humiMax.getText().toString().length() == 0) {
                    Toast.makeText(SettingParamActivity.this, "Nhập thông số", Toast.LENGTH_SHORT).show();
                } else {
                    if (edt_humiMax.getText().toString().length() == 0 && edt_humiMin.getText().toString().length() != 0) {
                        int newMinTemp = Integer.parseInt(edt_humiMin.getText().toString());
                        databaseReference.child("Param").child("Độ Ẩm").child("Ngưỡng dưới").setValue(newMinTemp);
                    }
                    if (edt_humiMax.getText().toString().length() != 0 && edt_humiMin.getText().toString().length() == 0) {
                        int newMaxTemp = Integer.parseInt(edt_humiMax.getText().toString());
                        databaseReference.child("Param").child("Độ Ẩm").child("Ngưỡng trên").setValue(newMaxTemp);
                    }
                    if (edt_humiMax.getText().toString().length() != 0 && edt_humiMin.getText().toString().length() != 0) {
                        int newMaxTemp = Integer.parseInt(edt_humiMax.getText().toString());
                        int newMinTemp = Integer.parseInt(edt_humiMin.getText().toString());
                        databaseReference.child("Param").child("Độ Ẩm").child("Ngưỡng trên").setValue(newMaxTemp);
                        databaseReference.child("Param").child("Độ Ẩm").child("Ngưỡng dưới").setValue(newMinTemp);
                    }
                }

            }
        });
        btn_commitLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_lightMin.getText().toString().length() == 0 ) {
                    Toast.makeText(SettingParamActivity.this, "Nhập thông số", Toast.LENGTH_SHORT).show();
                } else {
                    if (edt_lightMin.getText().toString().length() != 0) {
                        int newMinLight = Integer.parseInt(edt_lightMin.getText().toString());
                        databaseReference.child("Param").child("Ánh Sáng").child("Ngưỡng dưới").setValue(newMinLight);

                    }
                }

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
