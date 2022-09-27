package com.example.thewitssocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class message extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText msginput;
    private TextView friendname;
    private ArrayList<MessageObject> messages;
    private ImageView send;
    private String nme;
    private String email;
    private MessageAdapter messageAdapter;

    String chatid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        nme = getIntent().getStringExtra("Name");   //the person we texting
        email = getIntent().getStringExtra("Email");    //their email

        send = findViewById(R.id.imgv); //send button
        recyclerView = findViewById(R.id.messagesRec);
        msginput = findViewById(R.id.messageeditt);
        friendname = findViewById(R.id.chatwith);

        friendname.setText(nme);
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages, message.this);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("messages/"+chatid).push().setValue(new MessageObject(FirebaseAuth.getInstance().getCurrentUser().getEmail(), email,msginput.getText().toString()));

                msginput.setText("");
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);
        setChatroom();

    }

    private void setChatroom(){
        FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myname = snapshot.getValue(User.class).getName();

                if(nme.compareTo(myname)>0){
                    chatid = myname+nme;
                }
                else if(nme.compareTo(myname)==0){
                    chatid = myname+nme;
                }
                else{
                    chatid = nme+myname;
                }
                messagelistener(chatid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void messagelistener(String id){
        FirebaseDatabase.getInstance().getReference("messages"+id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(message.this, "okay lets seee ", Toast.LENGTH_SHORT).show();
                messages.clear();


                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    messages.add(dataSnapshot.getValue(MessageObject.class));
                    System.out.println(dataSnapshot.getValue().toString());
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}