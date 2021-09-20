package com.example.firebase_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.rpc.context.AttributeContext;

public class Registrarse extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private EditText Nombre, Contraseña;
    private EditText Confirmarcontraseña;
    private Button btnGuardar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        mAuth = FirebaseAuth.getInstance();
        Nombre = findViewById(R.id.txtNombre);
        Contraseña=  findViewById(R.id.txtcontraseña);
        Confirmarcontraseña=  findViewById(R.id.txtvcontraseña);
        btnGuardar= (Button) findViewById(R.id.btguardar);

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void registrar(View view){

        if (Contraseña.getText().toString().equals(Confirmarcontraseña.getText().toString())){
            mAuth.createUserWithEmailAndPassword(Nombre.getText().toString(), Contraseña.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getApplicationContext(), "Bienvenido usuario correcto", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);


                                // updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getApplicationContext(), "La conexión no se completo", Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                        }
                    });

        }else{
            Toast.makeText(this, "No Hay llenado los campos correctos", Toast.LENGTH_SHORT).show();

        }


    }

}