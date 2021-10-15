package com.example.firebase_project.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.firebase_project.Opciones.MisDatos;
import com.example.firebase_project.R;
import com.example.firebase_project.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private EditText txtLatitud;


    private TextView textCorreo, textUser, textFecha, textApellido;
    private Button btnMISDATOS, btnCREAR, btnPUBLICACIONES;


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
        textApellido= root.findViewById(R.id.textApellidos);

        btnMISDATOS = root.findViewById(R.id.btnMisDatos);
        btnCREAR = root.findViewById(R.id.btnCrear);

        btnMISDATOS.setOnClickListener(this);
        btnCREAR = root.findViewById(R.id.btnCrear);


        String id = mAuth.getCurrentUser().getUid();

        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String N = snapshot.child("name").getValue().toString();
                    String A = snapshot.child("surname").getValue().toString();
                    String C = snapshot.child("email").getValue().toString();

                    textUser.setText(N);
                    textApellido.setText(A);
                    textCorreo.setText(C);

                    // textImg.setText(D);

                    //Fecha
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

    }

}



