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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    private EditText msignupmail, msignuppassword;
    private RelativeLayout msignup , mreturntologin;
    private FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        msignupmail = findViewById(R.id.signupemail);
        msignuppassword = findViewById(R.id.signuppassword);
        msignup = findViewById(R.id.SignUp);
        mreturntologin = findViewById(R.id.ReturnToLogin);
        firebaseauth = FirebaseAuth.getInstance();
        
        mreturntologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,MainActivity.class);
                startActivity(intent);
            }
        });
        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = msignupmail.getText().toString().trim();
                String password = msignuppassword.getText().toString().trim();
                if(mail.isEmpty()||password.isEmpty()){
                    Toast.makeText(SignUp.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<7){
                    Toast.makeText(SignUp.this, "Password Should Be Of 7 Digits", Toast.LENGTH_SHORT).show();
                }
                else{
                    //register the user to firebase
                    firebaseauth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                sendVerificationEmail();
                            }
                            else{
                                Toast.makeText(SignUp.this, "Failed To Register", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }
            }
        });
                
    }

    private void sendVerificationEmail(){
        FirebaseUser firebaseuser = firebaseauth.getCurrentUser();
        if(firebaseuser != null){
            firebaseuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SignUp.this, "Verification Email is Send, Verify And Login Again ", Toast.LENGTH_SHORT).show();
                    firebaseauth.signOut();
                    finish();;
                    startActivity(new Intent(SignUp.this,MainActivity.class));
                }
            });
        }
        else {
            Toast.makeText(this, "Failed To Send Verification Email", Toast.LENGTH_SHORT).show();
        }
    }
}