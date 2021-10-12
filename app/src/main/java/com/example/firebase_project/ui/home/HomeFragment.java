package com.example.firebase_project.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.firebase_project.MapsActivity;
import com.example.firebase_project.Miposicion;
import com.example.firebase_project.R;
import com.example.firebase_project.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public  class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Button btnUb;
    private EditText txtLatitud;
    private EditText txtLongitud;
    private EditText txtAltitud;

    private TextView textCorreo, textUser;
    private Button btnMaps;


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


        btnUb = root.findViewById(R.id.buttonUbicacion);
        btnMaps = root.findViewById(R.id.buttonMap);
        txtLatitud = root.findViewById(R.id.editLati);
        txtLongitud = root.findViewById(R.id.editLong);
        txtAltitud = root.findViewById(R.id.editAltitud);


        btnUb.setOnClickListener(this);
        btnMaps.setOnClickListener(this);


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if(v==btnUb){
            miposicion();
            Toast.makeText(getContext(), "clic button", Toast.LENGTH_LONG).show();
        }
        if(v==btnMaps){
            verificar();

        }
    }

    public void miposicion(){
        int permisoLocalizacion = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int permisoCamara = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);

        if(permisoLocalizacion != PackageManager.PERMISSION_GRANTED && permisoCamara != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}, 1000);

        }
        LocationManager objLocation = null;
        LocationListener objLocListener;

        objLocation=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        objLocListener=new Miposicion();
        objLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,objLocListener);

        if(objLocation.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            txtLongitud.setText(Miposicion.longitud+"");
            txtLatitud.setText(Miposicion.latitud+"");
            txtAltitud.setText(Miposicion.altitud+"");


        }else{
            Toast.makeText(getContext()," Gps desabilitado",Toast.LENGTH_LONG).show();
        }

    }
    public void verificar(){
        if (!txtAltitud.getText().toString().equals("")|| !txtLongitud.getText().toString().equals("") || !txtLatitud.getText().toString().equals("")){
            Intent intent = new Intent(getContext(), MapsActivity.class);
            intent.putExtra("latitud", txtLatitud.getText().toString());
            intent.putExtra("longitud", txtLongitud.getText().toString());
            intent.putExtra("altitud", txtAltitud.getText().toString());
            startActivity(intent);
        }else{
            Toast.makeText(getContext(),"Los campo no estan llenos", Toast.LENGTH_LONG).show();
        }
    }
}