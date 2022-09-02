package com.example.thewitssocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName,editTextSurname,editTextPassword,
            editTextEmail,editTextPhoneNo;

    private TextView RegisterUser,WitsSocial;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        WitsSocial = (TextView) findViewById(R.id.witssocial3);
        WitsSocial.setOnClickListener(this);

        RegisterUser = (Button) findViewById(R.id.btnRegister);
        RegisterUser.setOnClickListener(this);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSurname = (EditText) findViewById(R.id.editTextSurname);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);

        //progressBar = (progressBar) findViewById(R.id.progressBar); not yet in the xml file



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.witssocial3:
                startActivity(new Intent(this,MainActivity.class));
                break;

            case R.id.btnRegister:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String Name=editTextName.getText().toString().trim();
        String Surname=editTextSurname.getText().toString().trim();
        String Email=editTextEmail.getText().toString().trim();
        String PhoneNo=editTextPhoneNo.getText().toString().trim();
        String Password=editTextPassword.getText().toString().trim();

        if(Name.isEmpty()){
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return;
        }
        if(Surname.isEmpty()){
            editTextSurname.setError("Surname is required");
            editTextSurname.requestFocus();
            return;
        }
        if(Email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editTextEmail.setError("Provide Valid Email");
            editTextEmail.requestFocus();
            return;
        }
        if(PhoneNo.isEmpty()){
            editTextPhoneNo.setError("Phone Number is required");
            editTextPhoneNo.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if(Password.length() < 8){
            editTextPassword.setError("Min Password length should be 8 characters");
            editTextPassword.requestFocus();
            return;
        }

        //progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(Name,Surname,Email,Password,PhoneNo);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(SignUp.this,"User has been Registered",Toast.LENGTH_LONG).show();
                                                //progressBar.setVisibility(View.VISIBLE);
                                            }else{
                                                Toast.makeText(SignUp.this,"Failed to Register, Try again!",Toast.LENGTH_LONG).show();
                                                //progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(SignUp.this,"Failed to Register, Try again!",Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);

                        }
                    }
                });

    }
}