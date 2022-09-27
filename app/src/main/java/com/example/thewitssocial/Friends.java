package com.example.thewitssocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Friends extends AppCompatActivity {

    BottomNavigationView nav;

    FirebaseRecyclerOptions<FriendModelClass>options;
    FirebaseRecyclerAdapter<FriendModelClass,FriendMyViewHolder>adapter;

    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Friends");

        LoadFriends("");

        nav = findViewById(R.id.Bottom_nav);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.myHome:
                        Toast.makeText(Friends.this,"Home",Toast
                                .LENGTH_LONG).show();
                        break;

                    case R.id.myChat:
                        Toast.makeText(Friends.this,"chat",Toast
                                .LENGTH_LONG).show();
                        startActivity(new Intent(Friends.this,Friends.class));
                        break;

                    case R.id.myPeople:
                        Toast.makeText(Friends.this,"Find Friends",Toast
                                .LENGTH_LONG).show();
                        startActivity(new Intent(Friends.this,Findfriends.class));
                        break;

                    case R.id.mySettings:
                        startActivity(new Intent(Friends.this,Settings.class));
                        break;

                }




                return true;
            }
        });
    }

    private void LoadFriends(String s) {
        Query query = mRef.child(mUser.getUid()).orderByChild("Name").startAt(s).endAt(s+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<FriendModelClass>().setQuery(query,FriendModelClass.class).build();
        adapter = new FirebaseRecyclerAdapter<FriendModelClass, FriendMyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendMyViewHolder holder, int position, @NonNull FriendModelClass model) {
                holder.username.setText(model.getName());
            }

            @NonNull
            @Override
            public FriendMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_friend,parent,false);
                return new FriendMyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}