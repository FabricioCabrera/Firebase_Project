package com.example.firebase_project.ui.home;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.firebase_project.CRUD_USER;
import com.example.firebase_project.Login.Login;
import com.example.firebase_project.Mapa.MapsActivity;
import com.example.firebase_project.Mapa.Miposicion;
import com.example.firebase_project.Mapa.nav_maps;
import com.example.firebase_project.Opciones.MisDatos;
import com.example.firebase_project.R;
import com.example.firebase_project.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private EditText txtLatitud;


    private TextView textCorreo, textUser, textFecha;
    private Button btnMISDATOS, btnUBICACION, btnCRUD, btnSALIR;

    private ImageView textURL;


    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        textCorreo = root.findViewById(R.id.textCorreo);
        textUser = root.findViewById(R.id.textuser);
        textFecha = root.findViewById(R.id.Fecha);
        textURL = root.findViewById(R.id.idFoto);


        btnCRUD = root.findViewById(R.id.btnCrud);
        btnMISDATOS = root.findViewById(R.id.btnMisDatos);
        btnUBICACION = root.findViewById(R.id.btnUbicacion);
        btnSALIR = root.findViewById(R.id.btnSalir);


        btnMISDATOS.setOnClickListener(this);
        btnCRUD.setOnClickListener(this);
        btnUBICACION.setOnClickListener(this);
        btnSALIR.setOnClickListener(this);



        //OBTENER DESDE LA BD LOS DATOS
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String Name = snapshot.child("name").getValue().toString();
                    String Correo = snapshot.child("email").getValue().toString();
                    String Imagen = snapshot.child("imagen").getValue().toString();

                    textUser.setText(Name);
                    textCorreo.setText(Correo);
                    textURL.setImageURI(Uri.parse(Imagen));

                    try {
                        //Si sube una imagen
                        Picasso.get().load(Imagen).placeholder(R.drawable.foto_perfil).into(textURL);
                    } catch (Exception e) {
                        //si no tiene una imagen
                        Picasso.get().load(R.drawable.foto_perfil).into(textURL);
                    }


                    //Setear la Fecha
                    Date date = new Date();
                    SimpleDateFormat fechaC = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
                    String sFecha = fechaC.format(date);
                    textFecha.setText(sFecha);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("No se puede encontrar ningún usuario" + error);

            }
        });

        return root;
    }


    @Override
    public void onClick(View v) {
        if (btnMISDATOS == v) {
            Intent i = new Intent(getContext(), MisDatos.class);
            startActivity(i);
        }
        if (btnCRUD == v) {
            Intent i = new Intent(getContext(), CRUD_USER.class);
            startActivity(i);
        }
        if (btnSALIR == v) {
            mAuth.signOut();
            Intent i = new Intent(getContext(), Login.class);
            startActivity(i);
        }

    }

}



