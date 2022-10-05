package com.example.thewitssocial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class editProfile extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    private  String userID;
    private FirebaseUser user;



    EditText nameET,surnameET, emailET,phoneET;
    String Dbname;
    String DbsurName;
    String DbeMail;
    String DbpHone;

    //profile pic uploading
    StorageReference storageRef;
    FirebaseAuth mAuth;
    ProgressDialog mloadingBar;

    CircleImageView profileImageView;
    Uri ImageUri;
    Button btnSave;


    DatabaseReference reference;

    String Etname, Etsurname, Etemail, Etphone;
    Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImageView = findViewById(R.id.profile_image);
        btnSave = findViewById(R.id.btnSave);
        mloadingBar = new ProgressDialog(this);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();
            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        storageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");

     //   reference = FirebaseDatabase.getInstance().getReference("Users");//.child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        nameET = findViewById(R.id.editTextTextPersonName);
        surnameET = findViewById(R.id.editTextTextPersonSurName);
        emailET = findViewById(R.id.editTextTextEmailAddress);
       phoneET = findViewById(R.id.editTextPhone);




        data = getIntent();
        Bundle bun = data.getExtras();

        Dbname = data.getStringExtra("name");
        DbsurName = data.getStringExtra("surname");
        DbpHone = data.getStringExtra("email");
        DbeMail = data.getStringExtra("phone");



        nameET.setText(Dbname);
        surnameET.setText(DbsurName);
        emailET.setText((DbeMail));
        phoneET.setText(DbpHone);
        //
        //DbeMail = emailET.getText().toString();
        // = phoneET.getText().toString();



       // Etname = nameET.getText().toString();
      //  Etemail = emailET.getText().toString();
      //  Etsurname = surnameET.getText().toString();
     //   Etphone = phoneET.getText().toString();


    }


    private void SaveData() {
        if(ImageUri==null){
            Toast.makeText(this, "select an image", Toast.LENGTH_SHORT).show();
        }
        else{
            mloadingBar.setTitle("adding profile picture");
            mloadingBar.setCanceledOnTouchOutside(false);
            mloadingBar.show();
            storageRef.child(userID).putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        storageRef.child(userID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("profileImage",uri.toString());

                                reference.child(userID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        //       Intent intent = new Intent()
                                        mloadingBar.dismiss();
                                        Toast.makeText(editProfile.this, "setup complete", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mloadingBar.dismiss();
                                        Toast.makeText(editProfile.this, e.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            ImageUri = data.getData();
            profileImageView.setImageURI(ImageUri);
        }
    }

    public void update(View view) {


        Etname = nameET.getText().toString();
        Etemail = emailET.getText().toString();
        Etsurname = surnameET.getText().toString();
         Etphone = phoneET.getText().toString();

        if (isNameChanged() || isSurnameChanged()) {
                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name").setValue(Etname);
                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Surname").setValue(Etsurname);
                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Email").setValue(Etphone);
                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("PhoneNo").setValue(Etemail);
                Toast.makeText(getApplicationContext(),"All changed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Nothing Changed", Toast.LENGTH_SHORT).show();
        }
    }


    public  Boolean isNameChanged(){
        if(!Dbname.equals(Etname)){
            return  true;
        }else {
            return true;
        }
    }

    public  Boolean isSurnameChanged(){
        if(!DbsurName.equals(Etsurname)){
            return  true;
        }else {
            return false;
        }
    }

}