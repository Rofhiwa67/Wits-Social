package com.example.thewitssocial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    private FirebaseUser user;
    private DatabaseReference reference;
    String profileImageUrlv;
    ImageView profileImageView;

    FirebaseAuth mAuth;


    private  String userID;

    String Name1,Surname1, Email1, PhoneNo1;


    private Button logout, friends_btn;



    TextView NameTextView, SurnameTextView, EmailTextView, PhoneNoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        profileImageView = findViewById(R.id.profile_image);


            NameTextView = (TextView) findViewById(R.id.textView2);
            SurnameTextView = (TextView) findViewById(R.id.textView3);
            TextView EmailTextView = (TextView) findViewById(R.id.textView4);
            TextView PhoneNoTextView = (TextView) findViewById(R.id.textView5);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                     Name1 = userProfile.Name;
                     Surname1 = userProfile.Surname;
                     Email1 = userProfile.Email;
                     PhoneNo1 = userProfile.PhoneNo;

                    NameTextView.setText( Name1);
                    SurnameTextView.setText( Surname1);
                    EmailTextView.setText( Email1);
                    PhoneNoTextView.setText( PhoneNo1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(user ==null){
            SendUserToLoginActivity();
        }
        else{
            reference.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        profileImageUrlv = snapshot.child("profileImage").getValue().toString();
                        Picasso.get().load(profileImageUrlv).into(profileImageView);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Profile.this,"Something went wrong", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void SendUserToLoginActivity() {
        Intent intent = new Intent(Profile.this, MainActivity.class);
        startActivity(intent);
    }

    public void editProfile(View view) {

        Intent intent = new Intent(this, editProfile.class);
            intent.putExtra("name", Name1);
            intent.putExtra("surname", Surname1);
            intent.putExtra("email", Email1.toString());
            intent.putExtra("phone", PhoneNo1 );
            startActivity(intent);
    }

    public void test(View view) {
        Toast.makeText(getApplicationContext(),Email1, Toast.LENGTH_SHORT).show();
    }

    public void reset(View view) {
        Intent intent = new Intent(this,ForgotPassword.class);
        startActivity(intent);
    }

 /*   public void openFriends(){
        Intent intent = new Intent(this,Findfriends.class);
        startActivity(intent);
    } */
}