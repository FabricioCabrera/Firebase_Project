package com.example.firebase_project.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebase_project.Drawer;
import com.example.firebase_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    private EditText Correo;
    private EditText Contraseña;
    private Button BtnInicia, btnCrea;


    private String correo = "";
    private String contraseña = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Correo = (EditText) findViewById(R.id.txtCorreo);
        Contraseña = (EditText) findViewById(R.id.txtCont);
        BtnInicia = (Button) findViewById(R.id.btniniciar);
        btnCrea = (Button) findViewById(R.id.btnCrear);

        btnCrea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registrarse.class);
                startActivity(i);
                finish();
            }
        });

        //Seteamos el método de visibilidad
        Contraseña.setOnTouchListener(this::onTouch);

        BtnInicia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correo = Correo.getText().toString();
                contraseña = Contraseña.getText().toString();

                if (!correo.isEmpty() && !contraseña.isEmpty()) {
                    iniciarsesión();
                } else {
                    Toast.makeText(Login.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Sirve para colocar un ícono el el EditText
        //Correo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_email_24, 0, 0, 0);
        //Contraseña.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_supervised_user_circle_24,0,0,0);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    //Método para dar visibilidad a la contraseña
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

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(this, Drawer.class);
            startActivity(i);
            //FirebaseUser currentUser = mAuth.getCurrentUser();
            finish();
            //updateUI(currentUser);
        }

    }


    public void iniciarsesión() {


        String user = Correo.getText().toString();
        String contraseña = Contraseña.getText().toString();


        if (!user.isEmpty() && !contraseña.isEmpty()) {

            if (contraseña.length() >= 6) {

                mAuth.signInWithEmailAndPassword(user, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                            //FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(Login.this, Drawer.class);

                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Usuario y contraseña incorrecta", Toast.LENGTH_SHORT);
                            //updateUI(null);
                        }

                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "Contraseña debe tener al menos 6 carácteres", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(getApplicationContext(), "Llene todos los campos para continuar", Toast.LENGTH_SHORT).show();
        }
    }

}

