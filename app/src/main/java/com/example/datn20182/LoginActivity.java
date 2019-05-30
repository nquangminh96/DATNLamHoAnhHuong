package com.example.datn20182;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.datn20182.Notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {
    EditText edt_pass, edt_email;
    Button createacc;
    Button btn_login;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    AlertDialog.Builder mBuilderWait;
    AlertDialog mDialogWait;
    TextView txt_reset_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edt_pass = findViewById(R.id.password);
        edt_email = findViewById(R.id.email);
        btn_login = findViewById(R.id.login);
        createacc = findViewById(R.id.createAccount);
        txt_reset_pass = findViewById(R.id.forgot_password);
        mBuilderWait = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View viewPleaseWait = inflater.inflate(R.layout.dialog_please_wait, null);
        mBuilderWait.setView(viewPleaseWait);
        mDialogWait = mBuilderWait.create();
        auth = FirebaseAuth.getInstance();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogWait.show();
                String email = edt_email.getText().toString();
                String pass = edt_pass.getText().toString();
                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivityForResult(intent,11);
                            mDialogWait.dismiss();
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String userID = firebaseUser.getUid();
//                            updateToken(userID, FirebaseInstanceId.getInstance().getToken());
                            finish();
                        }
                    }
                });

            }
        });
        createacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent , 11);
            }
        });
        txt_reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivityForResult(intent,11);
            }
        });
    }
    private void updateToken(String userId , String token) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        databaseReference.child(userId).setValue(token1);
    }
}
