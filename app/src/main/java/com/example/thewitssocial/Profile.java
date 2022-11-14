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

    /**
     * initilzing string elements
     */
    String Name1,Surname1, Email1, PhoneNo1;

    /**
     * initilzing buttons
     */
    private Button logout, friends_btn;

    /**
     * initilzing textviews
     */

    TextView NameTextView, SurnameTextView, EmailTextView, PhoneNoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        /**
         * hiding action bar
         */
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        /**
         *
         */
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        profileImageView = findViewById(R.id.profile_image);


        /**
         * setting Textviews with id
         */
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
/**
 * setting edit texts with respective current user data
 */
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
                /**
                 * Uloading pic if succesful toasts Profile changed l if fails toasts something went wrong
                 */
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists() && snapshot.getChildrenCount()>0){
                        if(snapshot.hasChild("profileImage")) {
                            profileImageUrlv = snapshot.child("profileImage").getValue().toString();
                            Picasso.get().load(profileImageUrlv).into(profileImageView);
                            Toast.makeText(Profile.this,"Profile Changed", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Profile.this,"Something went wrong", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    /**
     * when you click LogOut button it sends you to home page
     *
     */
    private void SendUserToLoginActivity() {
        Intent intent = new Intent(Profile.this, MainActivity.class);
        startActivity(intent);
    }


    /**
     * sending user data listed below to editProfile page in Intenyt intent
     */
    public void editProfile(View view) {

        Intent intent = new Intent(this, editProfile.class);
        intent.putExtra("name", Name1);
        intent.putExtra("surname", Surname1);
        intent.putExtra("email", Email1.toString());
        intent.putExtra("phone", PhoneNo1 );
        startActivity(intent);
    }

    /**
     * checking if retrieving data correctly
     *
     */
    public void test(View view) {
        Toast.makeText(getApplicationContext(),Email1, Toast.LENGTH_SHORT).show();
    }

    /**
     * when you click reset password button it sends you to forget password page
     *
     */
    public void reset(View view) {
        Intent intent = new Intent(this,ForgotPassword.class);
        startActivity(intent);
    }

 /*   public void openFriends(){
        Intent intent = new Intent(this,Findfriends.class);
        startActivity(intent);
    } */
}
