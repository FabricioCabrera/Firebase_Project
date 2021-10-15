package com.example.firebase_project.Editar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase_project.Login.Login;
import com.example.firebase_project.MainActivity;
import com.example.firebase_project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Cambiar_Password extends AppCompatActivity {

    private TextView txCorreoA, txCContraseñaA;

    private EditText etContraseñaA, etCContraseñaNueva;
    private Button BtnCambiarContraseña;

    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseUser user;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_password);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        txCorreoA = (TextView) findViewById(R.id.CorreoActual);
        txCContraseñaA = (TextView) findViewById(R.id.ContraseñaActual);

        etContraseñaA = (EditText) findViewById(R.id.ActualPassword);
        etCContraseñaNueva = (EditText) findViewById(R.id.NuevaPassword);

        BtnCambiarContraseña = (Button) findViewById(R.id.btnCambiarPasw);




        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String C = snapshot.child("email").getValue().toString();
                    String P = snapshot.child("password").getValue().toString();

                    txCorreoA.setText(C);
                    txCContraseñaA.setText(P);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("No se puede encontrar ningún usuario" + error);

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        //CAMBIAR CONTRASEÑA
        BtnCambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PASS_ANTERIOR = etContraseñaA.getText().toString().trim();
                String NUEVO_PASS = etCContraseñaNueva.getText().toString().trim();

                if (TextUtils.isEmpty(PASS_ANTERIOR)) {
                    Toast.makeText(Cambiar_Password.this, "El campo contraseña actual está vacío", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(NUEVO_PASS)) {
                    Toast.makeText(Cambiar_Password.this, "El campo nueva contraseña está vacío", Toast.LENGTH_SHORT).show();
                    {
                    }
                }
                if (!NUEVO_PASS.equals("") && NUEVO_PASS.length() > 6) {
                    Cambio_De_Contraseña(PASS_ANTERIOR, NUEVO_PASS);
                } else {
                    etCContraseñaNueva.setError("La contraseaña debe ser mayor a 6 carácteres");
                    etCContraseñaNueva.setFocusable(true);
                }
            }
        });

    }

    //MÉTODO PARA CAMBIAR LA CONTRASEÑA
    private void Cambio_De_Contraseña(String pass_anterior, String nuevo_pass) {
        progressDialog.show();
        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere por favor");
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), pass_anterior);
        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(nuevo_pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        String value = etCContraseñaNueva.getText().toString();
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("password", value);

                        //ACTUALIZAMOS LA NUEVA CONTRASEÑA EN LA BD
                        mDatabase.child(user.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Cambiar_Password.this, "Contraseña cambiada", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                            }
                        });

                        //lUEGO CERRAMOS SESIÓN
                        mAuth.signOut();
                        Intent i = new Intent(Cambiar_Password.this, Login.class);
                        startActivity(i);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Cambiar_Password.this, "La Contraseña actual no es la correcta", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*//PARA RETROCEDER A LA ACTIVIDAD ANTERIOR
    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }*/
}

