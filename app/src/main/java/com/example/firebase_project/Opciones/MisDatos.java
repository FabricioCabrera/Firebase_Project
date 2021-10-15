package com.example.firebase_project.Opciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.firebase_project.Editar.Cambiar_Password;
import com.example.firebase_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MisDatos extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private TextView txNombreDato, txApellidoDato,txCorreoDato,
            txEdadDato, txTelefonoDato,txContraseñaDato,txCcontraseñadato;

    private Button btnEditar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        txNombreDato= findViewById(R.id.NombreDato);
        txApellidoDato= findViewById(R.id.ApellidoDato);
        txCorreoDato= findViewById(R.id.CorreoDato);
        txEdadDato= findViewById(R.id.EdadDato);
        txContraseñaDato= findViewById(R.id.ContraseñaDato);

        btnEditar= findViewById(R.id.btnActualizar);


        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MisDatos.this, Cambiar_Password.class);
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
                            String A = snapshot.child("surname").getValue().toString();
                            String C = snapshot.child("email").getValue().toString();
                            String E = snapshot.child("age").getValue().toString();
                            String P= snapshot.child("password").getValue().toString();

                            txNombreDato.setText(N);
                            txApellidoDato.setText(A);
                            txCorreoDato.setText(C);
                            txEdadDato.setText(E);
                            txContraseñaDato.setText(P);

                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("No se puede encontrar ningún usuario" + error);

                    }
                });
    }

}

