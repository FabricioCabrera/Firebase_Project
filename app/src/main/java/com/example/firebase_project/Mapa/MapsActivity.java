package com.example.firebase_project.Mapa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.firebase_project.R;
import com.example.firebase_project.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.jetbrains.annotations.NotNull;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String longitud, latitud;
    SharedPreferences preferences;
    private Button btnFavorito, btneliminar;
    private Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btneliminar=  (Button)findViewById(R.id.limp);
        btnFavorito = (Button) findViewById(R.id.fav);


        btnFavorito.setOnClickListener(this);
        btneliminar.setOnClickListener(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        preferences = getSharedPreferences("MyPrefereces", Context.MODE_PRIVATE);

        longitud = getIntent().getStringExtra("longitud");
        latitud = getIntent().getStringExtra("latitud");

        double lon = Double.parseDouble(longitud);
        double lat = Double.parseDouble(latitud);


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lon);

        //agrego un marca con la ubicacion
        mMap.addMarker(new MarkerOptions().position(sydney).title("Mi ubicación"));
        //mover la camara a mi ubicacion
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //habilito los controles del zoom
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //doy zoom 16 para que se acerque
        CameraUpdate zoomCam = CameraUpdateFactory.zoomTo(11);
        mMap.animateCamera(zoomCam);

        // fijo el long click al mapa
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(@NonNull @NotNull LatLng latLng) {
        Toast.makeText(MapsActivity.this, "click posición" + latLng.latitude + latLng.longitude, Toast.LENGTH_SHORT).show();
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Mi posición"));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.punto));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        guardarPreferencias(latLng);
    }

    public void guardarPreferencias(LatLng latLng) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("latitud", (float) latLng.latitude);
        editor.putFloat("longitud", (float) latLng.longitude);
        editor.commit();
    }

    public void cargarPreferencia() {
        double lat = preferences.getFloat("latitud", 0);
        double log = preferences.getFloat("longitud", 0);

        if  (lat!=0){
            LatLng puntoPref= new LatLng(lat,log);
           // mMap.addMarker(new MarkerOptions().position(puntoPref).title("Mi posición"));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.punto));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(puntoPref));
        }else{
            AlertDialog.Builder alert= new AlertDialog.Builder(this);
            alert.setTitle("No tiene ningún sitio favorito");
            alert.setPositiveButton("Ok", null);
            alert.create().show();
        }
        Toast.makeText(MapsActivity.this,
                "mi favorito es: "+lat+log,Toast.LENGTH_SHORT).show();
    }

    public void  eliminarMarcas(){
        mMap.clear();
        Toast.makeText(this, "Marcas Eliminadas", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v == btnFavorito) {
            cargarPreferencia();

        }else if(v == btneliminar){

            eliminarMarcas();
        }

    }
}
