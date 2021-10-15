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

    private Button btnEditar;


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

        btnEditar = findViewById(R.id.btnActualizar);


        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MisDatos.this, Cambiar_Password.class);
                startActivity(i);
                finish();
            }
        });


        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String N = snapshot.child("name").getValue().toString();
                    String E = snapshot.child("edad").getValue().toString();
                    String T = snapshot.child("telefono").getValue().toString();
                    String C = snapshot.child("email").getValue().toString();
                    String I = snapshot.child("imagen").getValue().toString();
                    String P = snapshot.child("password").getValue().toString();

                    txNombreDato.setText(N);
                    txEdadDato.setText(E);
                    txTelefonoDato.setText(T);
                    txCorreoDato.setText(C);
                    txContraseñaDato.setText(P);

                    URL.setImageURI(Uri.parse(I));



                    try {
                        //Si sube una imagen
                        Picasso.get().load(I).placeholder(R.drawable.foto_perfil).into(URL);
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

