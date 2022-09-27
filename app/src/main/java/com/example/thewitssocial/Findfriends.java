package com.example.thewitssocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Findfriends extends AppCompatActivity {

    BottomNavigationView nav;

    FirebaseRecyclerOptions<User>options;
    FirebaseRecyclerAdapter<User,FindfriendsViewHolder>adapter;
    Toolbar toolbar;

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    RecyclerView recyclerView;

   // private Button friend_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findfriends);

      //  toolbar = findViewById(R.id.)

        recyclerView =findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

     /*   friend_list =(Button) findViewById(R.id.friend_list);
        friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFriendList();
            }
        }); */


        LoadUsers("");

        nav = findViewById(R.id.Bottom_nav);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.myHome:
                        Toast.makeText(Findfriends.this,"Home",Toast
                                .LENGTH_LONG).show();
                        break;

                    case R.id.myChat:
                        Toast.makeText(Findfriends.this,"chat",Toast
                                .LENGTH_LONG).show();
                        startActivity(new Intent(Findfriends.this,Friends.class));
                        break;

                    case R.id.myPeople:
                        Toast.makeText(Findfriends.this,"Find Friends",Toast
                                .LENGTH_LONG).show();
                        startActivity(new Intent(Findfriends.this,Findfriends.class));
                        break;

                    case R.id.mySettings:
                        startActivity(new Intent(Findfriends.this,Settings.class));
                        break;

                }




                return true;
            }
        });
    }

    private void LoadUsers(String s) {
        Query query = mUserRef.orderByChild("Name").startAt(s).endAt(s+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
        adapter = new FirebaseRecyclerAdapter<User, FindfriendsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindfriendsViewHolder holder, int position, @NonNull User model) {
                holder.username.setText(model.getName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Findfriends.this, ViewFriend.class);
                        intent.putExtra("userKey", getRef(position).getKey().toString());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FindfriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_find_friend,parent,false);

                return new FindfriendsViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
  /*  public void openFriendList(){
        Intent intent = new Intent(this,Friends.class);
        startActivity(intent);
    } */
}