package com.example.datn20182;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.datn20182.Notifications.Client;
import com.example.datn20182.Notifications.Data;
import com.example.datn20182.Notifications.MyResponse;
import com.example.datn20182.Notifications.Sender;
import com.example.datn20182.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    CircleImageView avatar;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    TextView nhietDo, doAm, anhSang, chuyenDong;
    CardView device, setupParam;
    APIService apiService;
    boolean notify = false;
    String temp;
    String humi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        avatar = findViewById(R.id.profile_image);
        username = findViewById(R.id.user_name);
        nhietDo = findViewById(R.id.txtNhietDo);
        doAm = findViewById(R.id.txtDoAm);
        anhSang = findViewById(R.id.txtAnhSang);
        chuyenDong = findViewById(R.id.txtChuyenDong);
        device = findViewById(R.id.carddevice);
        setupParam = findViewById(R.id.cardSetup);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    avatar.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(avatar);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Param").child("Nhiệt Độ").child("Nhiệt độ hiện tại").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                temp = String.valueOf(dataSnapshot.getValue());
                String newtemp = temp + (char) 0x00B0 + "C";
                nhietDo.setText(newtemp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Param").child("Độ Ẩm").child("Độ ẩm hiện tại").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                humi = String.valueOf(dataSnapshot.getValue());
                String newhumi = humi + "%";
                doAm.setText(newhumi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Param").child("Độ Ẩm").child("Ngưỡng trên").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    final int maxHumi = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                    databaseReference.child("Param").child("Độ Ẩm").child("Ngưỡng dưới").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                final int minHumi = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                                databaseReference.child("Param").child("Độ Ẩm").child("Độ ẩm hiện tại").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String humi = String.valueOf(dataSnapshot.getValue());
                                        int currentHumi = Integer.parseInt(humi);
                                        if (currentHumi > maxHumi || currentHumi < minHumi) {
                                            updateToken(FirebaseInstanceId.getInstance().getToken());
                                            notify = true;
                                            final String msg = "";
                                            if (notify) {
                                                sendNotifiaction(firebaseUser.getUid(), "Cảnh báo, Độ ẩm nằm ngoài ngưỡng", msg);
                                                notify = false;

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child("Param").child("Nhiệt Độ").child("Ngưỡng trên").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    final int maxTemp = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                    databaseReference.child("Param").child("Nhiệt Độ").child("Ngưỡng dưới").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                final int minTemp = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                                databaseReference.child("Param").child("Nhiệt Độ").child("Nhiệt độ hiện tại").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String temp = String.valueOf(dataSnapshot.getValue());
                                        int currentTemp = Integer.parseInt(temp);
                                        if (currentTemp > maxTemp || currentTemp < minTemp) {
                                            updateToken(FirebaseInstanceId.getInstance().getToken());
                                            notify = true;
                                            final String msg = "";
                                            if (notify) {
                                                sendNotifiaction(firebaseUser.getUid(), "Cảnh báo, Nhiệt độ nằm ngoài ngưỡng", msg);
                                                notify = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child("Param").child("Ánh sáng").child("Ánh sáng hiện tại").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String temp = String.valueOf(dataSnapshot.getValue());
                temp = temp + "%";
                anhSang.setText(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Param").child("Chuyển động").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String temp = String.valueOf(dataSnapshot.getValue());
                String cd = temp.equals("1") ? "Có người" : "Không";
                chuyenDong.setText(cd);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setupParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingParamActivity.class);
                startActivityForResult(intent, 11);
            }
        });
        device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, ListDeviceActivity.class), 11);
            }
        });
        databaseReference.child("Param").child("Ánh sáng").child("Ngưỡng dưới").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    final int minLight = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                    databaseReference.child("Param").child("Ánh sáng").child("Ánh sáng hiện tại").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String light = String.valueOf(dataSnapshot.getValue());
                            int curentLight = Integer.parseInt(light);
                            if (curentLight < minLight) {
                                databaseReference.child("Device").child("Đèn bàn").setValue(1);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // updateToken(FirebaseInstanceId.getInstance().getToken());
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

    private void updateToken(String token) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        databaseReference.child(firebaseUser.getUid()).setValue(token1);
    }

    private void sendNotifiaction(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        Log.d("query", query + " ");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.mipmap.ic_launcher, username + " " + message, "Cảnh báo",
                            firebaseUser.getUid());

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
