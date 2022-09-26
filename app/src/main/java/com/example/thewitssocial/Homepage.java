package com.example.thewitssocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Homepage extends AppCompatActivity {

    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        nav = findViewById(R.id.Bottom_nav);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.myHome:
                        Toast.makeText(Homepage.this,"Home",Toast
                                .LENGTH_LONG).show();
                        break;

                    case R.id.myChat:
                        Toast.makeText(Homepage.this,"chat",Toast
                                .LENGTH_LONG).show();
                        break;

                    case R.id.myAdd:
                        Toast.makeText(Homepage.this,"Add",Toast
                                .LENGTH_LONG).show();
                        break;

                    case R.id.mySettings:
                        startActivity(new Intent(Homepage.this,Settings.class));
                        break;

                }




                return true;
            }
        });
    }
}