package com.example.firebase_project.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebase_project.Drawer;
import com.example.firebase_project.MainActivity;
import com.example.firebase_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registrarse extends AppCompatActivity {


    private EditText Correo, Nombre, Contraseña, Edad, Telefono, Imagen;
    private EditText Confirmarcontraseña;
    private Button btnRegistar, btnIniciar;


    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    private ProgressDialog progressDialog;


    private String nombre = "";
    private String correo = "";
    private String contraseña = "";
    private String Ccontraseña = "";
    private String edad = "";
    private String telefono = "";
    private String imagen = "";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        progressDialog = new ProgressDialog(Registrarse.this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Nombre = (EditText) findViewById(R.id.txtNombre);
        Correo = (EditText) findViewById(R.id.txtCorreo);
        Contraseña = (EditText) findViewById(R.id.txtcontraseña);
        Edad = (EditText) findViewById(R.id.txtEdad);
        Telefono = (EditText) findViewById(R.id.txtTelefono);
        Confirmarcontraseña = (EditText) findViewById(R.id.txtvcontraseña);
        Imagen= (EditText) findViewById(R.id.txtImgPerfil);



        btnRegistar = (Button) findViewById(R.id.btguardar);
        btnIniciar = (Button) findViewById(R.id.btnInicia);

        Contraseña.setOnTouchListener(this::onTouch);
        Confirmarcontraseña.setOnTouchListener(this::onTouch2);


        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registrarse.this, Login.class);
                startActivity(i);
                finish();
            }
        });

        btnRegistar.setOnClickListener((view) -> {
            registrar();
        });

    }

    //SIRVE PARA VISUALIZAR Y OCULTAR LA CONTRASEÑA
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:
                Drawable drawable = Contraseña.getCompoundDrawables()[2];
                if (drawable != null && motionEvent.getRawX() >= (Contraseña.getRight() - drawable.getBounds().width())) {
                    if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.ic_baseline_visibility_24).getConstantState())) {
                        Contraseña.setCompoundDrawablesWithIntrinsicBounds(null,
                                null, getResources().getDrawable(R.drawable.ic_baseline_visibility_off_24), null);
                        Contraseña.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    } else {
                        Contraseña.setCompoundDrawablesWithIntrinsicBounds(null,
                                null, getResources().getDrawable(R.drawable.ic_baseline_visibility_24), null);
                        Contraseña.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    return false;
                }
                break;
        }
        return false;
    }

    public boolean onTouch2(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:
                Drawable drawable = Confirmarcontraseña.getCompoundDrawables()[2];
                if (drawable != null && motionEvent.getRawX() >= (Confirmarcontraseña.getRight() - drawable.getBounds().width())) {
                    if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.ic_baseline_visibility_24).getConstantState())) {
                        Confirmarcontraseña.setCompoundDrawablesWithIntrinsicBounds(null,
                                null, getResources().getDrawable(R.drawable.ic_baseline_visibility_off_24), null);
                        Confirmarcontraseña.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    } else {
                        Confirmarcontraseña.setCompoundDrawablesWithIntrinsicBounds(null,
                                null, getResources().getDrawable(R.drawable.ic_baseline_visibility_24), null);
                        Confirmarcontraseña.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    return false;
                }
                break;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void registrar() {
        progressDialog.setTitle("Registrando");
        progressDialog.setMessage("Espere por favor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        nombre = Nombre.getText().toString();
        correo = Correo.getText().toString();
        contraseña = Contraseña.getText().toString();
        Ccontraseña = Confirmarcontraseña.getText().toString();
        edad = Edad.getText().toString();
        telefono = Telefono.getText().toString();
        imagen=Imagen.getText().toString();

        if (!nombre.isEmpty() && !correo.isEmpty() && !contraseña.isEmpty()
                && !Ccontraseña.isEmpty() && !edad.isEmpty() && !telefono.isEmpty()) {
            if (isEmailValid(correo)) {
                if (contraseña.equals(Ccontraseña)) {
                    if (contraseña.length() >= 6) {
                        mAuth.createUserWithEmailAndPassword(correo, contraseña)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {


                                            FirebaseUser user = mAuth.getCurrentUser();

                                            assert user != null;
                                            String uid = user.getUid();

                                            Map<String, Object> map = new HashMap<>();
                                            map.put("uid", uid);
                                            map.put("name", nombre);
                                            map.put("email", correo);
                                            map.put("password", contraseña);
                                            map.put("Confirm password", Ccontraseña);
                                            map.put("edad", edad);
                                            map.put("telefono", telefono);
                                            map.put("imagen", imagen);


                                            String id = mAuth.getCurrentUser().getUid();

                                            mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task2) {
                                                    if (task2.isSuccessful()) {
                                                        progressDialog.dismiss();
                                                        FirebaseUser user = mAuth.getCurrentUser();

                                                        Toast.makeText(getApplicationContext(), "Datos del  usuario correcto", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(Registrarse.this, Login.class);
                                                        startActivity(i);
                                                        finish();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getApplicationContext(), "No se puedo completar tus datos", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                }
                                            });

                                        } else {
                                            progressDialog.dismiss();

                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(getApplicationContext(), "La conexión no se completo", Toast.LENGTH_SHORT).show();
                                            //updateUI(null);
                                        }

                                    }
                                });

                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Contraseña debe tener al menos 6 carácteres", Toast.LENGTH_SHORT).show();


                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.dismiss();

                Toast.makeText(this, "Correo incorrecto", Toast.LENGTH_SHORT).show();


            }
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Para continuar llene todos los campos", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean isEmailValid(String correo) {
        String expresion = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern patter = Pattern.compile(expresion, Pattern.CASE_INSENSITIVE);
        Matcher matcher = patter.matcher(correo);
        return matcher.matches();
    }
}
