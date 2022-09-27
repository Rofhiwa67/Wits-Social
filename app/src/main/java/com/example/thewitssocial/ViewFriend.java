package com.example.thewitssocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ViewFriend extends AppCompatActivity {

    DatabaseReference mUserRef,requestRef,friendRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String username;

    TextView Username;

    Button btn_request,btn_decline;
    String CurrentState = "nothing_happen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);

        String userID = getIntent().getStringExtra("userKey");
        Toast.makeText(this,""+userID,Toast.LENGTH_SHORT).show();

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        requestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        btn_request = findViewById(R.id.btn_request);
        btn_decline = findViewById(R.id.btn_decline);

        Username = findViewById(R.id.username_p);
        
        LoadUser();

        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAction(userID);
            }
        });
        checkUserExistence(userID);
    }

    private void checkUserExistence(String userID) {

        friendRef.child(mUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CurrentState = "friend";
                    btn_request.setText("start chat");
                    btn_decline.setText("unfriend");
                    btn_decline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        friendRef.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CurrentState = "friend";
                    btn_request.setText("start chat");
                    btn_decline.setText("unfriend");
                    btn_decline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(mUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState = "I_sent_pending";
                        btn_request.setText("Cancel Friend Request");
                        btn_decline.setVisibility(View.GONE);
                    }
                    if(snapshot.child("status").getValue().toString().equals("decline")){
                        CurrentState = "I_sent_decline";
                        btn_request.setText("Cancel Friend Request");
                        btn_decline.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState = "he_sent_pending";
                        btn_request.setText("Accept Friend Request");
                        btn_decline.setText("Decline Friend");
                        btn_decline.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(CurrentState.equals("nothing_happen")){
            CurrentState = "nothing_happen";
            btn_request.setText("Send Friend Request");
            btn_decline.setVisibility(View.GONE);
        }
    }

    private void PerformAction(String userID) {

        if (CurrentState.equals("nothing_happen")){
            HashMap hashMap = new HashMap();
            hashMap.put("status","pending");
            requestRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ViewFriend.this, "You have sent Friend request", Toast.LENGTH_SHORT).show();
                        btn_decline.setVisibility(View.GONE);
                        CurrentState = "I_sent_pending";
                        btn_request.setText("Cancel Friend Request");

                    }
                    else{
                        Toast.makeText(ViewFriend.this, ""+task.getException().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(CurrentState.equals("I_sent_pending")|| CurrentState.equals("I_sent_decline")){
            requestRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ViewFriend.this, "You have cancelled Friend Request", Toast.LENGTH_SHORT).show();
                        CurrentState = "nothing_happen";
                        btn_request.setText("Send Friend Request");
                        btn_decline.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(ViewFriend.this, ""+task.getException().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(CurrentState.equals("he_sent_pending")){
            requestRef.child(userID).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        HashMap hashMap = new HashMap();
                        hashMap.put("status","friend");
                        hashMap.put("Name", username);
                        friendRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    friendRef.child(userID).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(ViewFriend.this, "You added friend", Toast.LENGTH_SHORT).show();

                                            CurrentState = "friend";
                                            btn_request.setText("Start chat");
                                            btn_decline.setText("Unfriend");
                                            btn_decline.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        });
                    }

                }
            });
        }
        if(CurrentState.equals("friend")){

        }


    }

    private void LoadUser() {

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    username = snapshot.child("Name").getValue().toString();

                    Username.setText(username);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}