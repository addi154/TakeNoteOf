package com.example.takenoteof;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout mlogin, msignup;
    private TextView mforgotpassword;
    private EditText mloginemail,mloginpassword;
    private FirebaseAuth firebaseAuth;
    ProgressBar mprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mlogin = findViewById(R.id.logIn);
        mforgotpassword = findViewById(R.id.forgotpassword);
        msignup = findViewById(R.id.SignUp);
        mloginpassword = findViewById(R.id.loginpassword);
        mloginemail = findViewById(R.id.loginemail);

        getSupportActionBar().hide();;
        mprogressBar = findViewById(R.id.progressbarofmainactivity);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null)
        {
            finish();;
            startActivity(new Intent(MainActivity.this,Notes.class));
        }

        mforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mloginemail.getText().toString().trim();
                String password = mloginpassword.getText().toString().trim();
                if(mail.isEmpty()||password.isEmpty()){
                    Toast.makeText(MainActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else if (password.length()<7){
                    Toast.makeText(MainActivity.this, "Password Should Be Of 7 Digits", Toast.LENGTH_SHORT).show();
                }
                else{
                    //firebase
                    mprogressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                checkmailverification();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Account Does Not Exist", Toast.LENGTH_SHORT).show();
                                mprogressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }
    private void checkmailverification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()==true){
            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
            finish();;
            startActivity(new Intent(MainActivity.this,Notes.class));
        }
        else
        {
            Toast.makeText(this, "Verify Your Email First", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            mprogressBar.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onBackPressed(){

        moveTaskToBack(true);

    }
}