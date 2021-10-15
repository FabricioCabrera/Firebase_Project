package com.example.firebase_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class Pantalla_de_Carga extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_carga);


        final int Duration = 2500;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Pantalla_de_Carga.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, Duration);
    }
}