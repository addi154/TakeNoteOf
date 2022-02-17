package com.example.takenoteof;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    RelativeLayout mreturnlogin,mrecover;
    EditText mforgotmail;
    FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        mreturnlogin = findViewById(R.id.ReturnToLogin);
        mrecover = findViewById(R.id.Recover);
        mforgotmail = findViewById(R.id.forgotpassword);
        firebaseauth = FirebaseAuth.getInstance();

        mreturnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this,MainActivity.class);
                startActivity(intent);
            }
        });
        mrecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mforgotmail.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(ForgotPassword.this, "Enter Your Mail First", Toast.LENGTH_SHORT).show();
                }
                else{
                    //recover mail
                    firebaseauth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPassword.this, "Recover Email Send", Toast.LENGTH_SHORT).show();
                                finish();;
                                startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                            }
                            else{
                                Toast.makeText(ForgotPassword.this, "Email Is Wrong Or Account Does Not Exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}