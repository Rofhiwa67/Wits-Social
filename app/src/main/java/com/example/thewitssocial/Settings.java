package com.example.thewitssocial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    /**
     * declaring variables we'll use
     * should make sense by reading
     */

    Button GoProfile;
    Button switcher;
    boolean nightMODE;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /**
         * hiding action bar
         */
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


         switcher = findViewById(R.id.switcher);

        /**
         * I used shareed preferences to save the theme the last time it was changed incase the app is exited
         */
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMODE =sharedPreferences.getBoolean("night", false);

        if(nightMODE){


            //switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

/**
 * When the Dark mode button is clicked it calls this method
 */
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, Homepage.class);
                if (nightMODE){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);

                }
                editor.apply();

                /**
                 * after button is clicked it takes us to homepage
                 */
                startActivity(intent);
            }
        });


        GoProfile =findViewById(R.id.Go_to_Profile);
        /**
         * This function calls an intent to take us to the forgot profile page
         *
         */
        GoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,Profile.class));
            }
        });





    }

    /**
     * This function calls an intent to take us to the forgot password page
     *
     */

    public void reset(View view) {
        Intent intent = new Intent(Settings.this,ForgotPassword.class);
        startActivity(intent);
    }
    /**
     * This function calls an intent to take us to the forgot login page
     *
     */
    public void LogOut(View view) {
        Intent intent = new Intent(Settings.this,MainActivity.class);
        startActivity(intent);

    }
}
