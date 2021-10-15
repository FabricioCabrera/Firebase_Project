package com.example.firebase_project.Opciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebase_project.Editar.Cambiar_Password;
import com.example.firebase_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MisDatos extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private TextView txNombreDato, txEdadDato, txTelefonoDato, txCorreoDato,
            txContraseñaDato, txCcontraseñadato;

    private ImageView URL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        txNombreDato = findViewById(R.id.NombreDato);
        txEdadDato = findViewById(R.id.EdadDato);
        txTelefonoDato = findViewById(R.id.TelefonoDato);
        txCorreoDato = findViewById(R.id.CorreoDato);
        txContraseñaDato = findViewById(R.id.ContraseñaDato);
        URL= findViewById(R.id.ImgenDato);




        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String Name = snapshot.child("name").getValue().toString();
                    String Edad = snapshot.child("edad").getValue().toString();
                    String Telefono = snapshot.child("telefono").getValue().toString();
                    String Correo = snapshot.child("email").getValue().toString();
                    String Imagen = snapshot.child("imagen").getValue().toString();
                    String contraseña = snapshot.child("password").getValue().toString();

                    txNombreDato.setText(Name);
                    txEdadDato.setText(Edad);
                    txTelefonoDato.setText(Telefono);
                    txCorreoDato.setText(Correo);
                    txContraseñaDato.setText(contraseña);

                    URL.setImageURI(Uri.parse(Imagen));



                    try {
                        //Si sube una imagen
                        Picasso.get().load(Imagen).placeholder(R.drawable.foto_perfil).into(URL);
                    }catch (Exception e){
                        //si no tiene una imagen
                        Picasso.get().load(R.drawable.foto_perfil).into(URL);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("No se puede encontrar ningún usuario" + error);

            }
        });
    }

}

