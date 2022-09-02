package com.example.thewitssocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;

    private  String userID;

    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        logout = (Button) findViewById(R.id.btnLogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this,MainActivity.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView NameTextView = (TextView) findViewById(R.id.textView2);
        final TextView SurnameTextView = (TextView) findViewById(R.id.textView3);
        final TextView EmailTextView = (TextView) findViewById(R.id.textView4);
        final TextView PhoneNoTextView = (TextView) findViewById(R.id.textView5);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    String Name1 = userProfile.Name;
                    String Surname1 = userProfile.Surname;
                    String Email1 = userProfile.Email;
                    String PhoneNo1 = userProfile.PhoneNo;

                    NameTextView.setText("Name :" + Name1);
                    SurnameTextView.setText("Surname :" + Surname1);
                    EmailTextView.setText("Email :" + Email1);
                    PhoneNoTextView.setText("Phone Number :" + PhoneNo1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}