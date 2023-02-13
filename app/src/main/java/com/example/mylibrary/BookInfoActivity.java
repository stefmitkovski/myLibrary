package com.example.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    DatabaseReference reference;
    FirebaseAuth mAuth;
    GoogleMap mMap;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        ImageView back = findViewById(R.id.backButton);
        TextView toolbar_title = findViewById(R.id.titleText);

        intent = getIntent();
        toolbar_title.setText(intent.getStringExtra("title"));
        back.setOnClickListener(view -> startActivity(new Intent(view.getContext(), MainActivity.class)));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        reference = FirebaseDatabase.getInstance().getReference().child("Location");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                intent = getIntent();
                boolean flag = false;
                mAuth = FirebaseAuth.getInstance();
                if(!intent.getStringExtra("owner").equals(mAuth.getCurrentUser().getEmail())){
                    flag = true;
                } // Ако логираниот корисник и сопственикот се различни

                for(DataSnapshot locationDataSnap : snapshot.getChildren()){
                    Location location = locationDataSnap.getValue(Location.class);
                    if(mAuth.getCurrentUser().getEmail().equals(location.getEmail())) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Your Location"));
                        if(!flag){
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
                            break;
                        }
                    }
                    else if(intent.getStringExtra("owner").equals(location.getEmail()) && flag){
                        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Book Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}