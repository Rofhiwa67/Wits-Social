package com.example.thewitssocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Sendimage extends AppCompatActivity {
    String url, receiver_name,senderuid,reicUid;
    Uri imageurl;
    ImageView imgV;
    ProgressBar pb;
    FirebaseUser mUser;
    TextView warning;
    Button button;
    UploadTask uploadTask;

    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    DatabaseReference ref1, ref2, messref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private Uri uri;
    Chat chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendimage);

        chat = new Chat();
        storageReference = firebaseStorage.getInstance().getReference("Message Images");

        imgV = findViewById(R.id.iv_sendimage);
        button = findViewById(R.id.btn_sendImage);
        pb = findViewById(R.id.imageuploadprogressbar);
        warning = findViewById(R.id.Imagewarning);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        messref= FirebaseDatabase.getInstance().getReference().child("Message");

        Bundle b = getIntent().getExtras();
        if(b != null){
            url = b.getString("url");
            receiver_name = b.getString("receiver");
            reicUid = b.getString("ruid");
            senderuid = b.getString("senderUid");
        }
        else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

        Picasso.get().load(url).into(imgV);
        imageurl = Uri.parse(url);

        ref1 = database.getReference("Message").child(senderuid).child(reicUid);
        ref2 = database.getReference("Message").child(reicUid).child(senderuid);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImage();
                warning.setVisibility(View.VISIBLE);
            }
        });
    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    private void sendImage() {
         if(imageurl!= null){
             pb.setVisibility(View.VISIBLE);
             final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageurl));
             uploadTask = reference.putFile(imageurl);

             Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                 @Override
                 public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                     if(!task.isSuccessful()) {
                         throw task.getException();
                     }
                     return reference.getDownloadUrl();
                 }
             }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                 @Override
                 public void onComplete(@NonNull Task<Uri> task) {
                     if(task.isSuccessful()){
                         Uri downloadUri = task.getResult();


                         chat.setMessage(downloadUri.toString());
                         chat.setUserid(senderuid);

                         String id = ref1.push().getKey();
                         ref1.child(id).setValue(chat);

                         String id1 = ref2.push().getKey();
                         ref2.child(id1).setValue(chat);
                         pb.setVisibility(View.INVISIBLE);
                         warning.setVisibility(View.INVISIBLE);
                     }
                 }
             });

         }
         else{
             Toast.makeText(this, "Select something", Toast.LENGTH_SHORT).show();
         }

    }
}
/*<ImageView
pl.droidsonroids.gif.GifImageView
        android:id="@+id/sentImage"
                android:layout_width="200dp"
                android:layout_height="190dp"
                android:layout_gravity="right"
                android:paddingTop="8dp"
                android:visibility="gone"
                app:layout_constraintEnd_toStartOf="@+id/user2prop"
                tools:layout_editor_absoluteY="8dp" />*/