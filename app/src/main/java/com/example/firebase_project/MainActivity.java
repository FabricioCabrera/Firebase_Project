package com.example.firebase_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void isLogin(View view){
        Intent i= new Intent(MainActivity.this, Login.class);
        startActivity(i);

    }
    public void isRegistrar(View view){
        Intent i= new Intent(MainActivity.this, Registrarse.class);
        startActivity(i);

    }

}