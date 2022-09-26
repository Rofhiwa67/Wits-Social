package com.example.thewitssocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEdittext;
    private Button resetPasswordButton;
    ///private ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEdittext = (EditText) findViewById(R.id.EnterPasswordToReset);
        resetPasswordButton = (Button) findViewById(R.id.buttonResetPassword);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }
    private void resetPassword(){

        String email = emailEdittext.getText().toString().trim();

        if(email.isEmpty()){
            emailEdittext.setError("Email is required");
            emailEdittext.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdittext.setError("please provide valid email");
            emailEdittext.requestFocus();
            return;
        }

        //progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check your email inbox",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ForgotPassword.this,"Try Again Something wrong happened",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}