package com.example.firebase_project.ui.home;


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

import com.example.firebase_project.R;
import com.example.firebase_project.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Button btnUb;
    private EditText txtLatitud;
    private EditText txtLongitud;
    private EditText txtAltitud;

    private TextView textCorreo, textUser, textImg;
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


        //textImg= root.findViewById(R.id.idFoto);
        textCorreo = root.findViewById(R.id.textCorreo);
        textUser = root.findViewById(R.id.textuser);


        String id = mAuth.getCurrentUser().getUid();

        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String N = snapshot.child("name").getValue().toString();
                    String C = snapshot.child("email").getValue().toString();
                    //String D= snapshot.child("img").getValue().toString();

                    textUser.setText(N);
                    textCorreo.setText(C);

                    // textImg.setText(D);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("No se puede encontrar ningún usuario" + error);

            }
        });
        /*@Override
        public void onDestroyView () {
            super.onDestroyView();
            binding = null;
        }*/

        return root;

    }
}


