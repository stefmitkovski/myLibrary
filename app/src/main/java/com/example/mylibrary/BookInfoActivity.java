package com.example.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

        mAuth = FirebaseAuth.getInstance();
        intent = getIntent();
        toolbar_title.setText(intent.getStringExtra("title"));
        back.setOnClickListener(view -> startActivity(new Intent(view.getContext(), MainActivity.class)));

        Button button = findViewById(R.id.btn);
        button.setOnClickListener(view -> {
            reference = FirebaseDatabase.getInstance().getReference().child("Books");
            if(button.getText().toString().equals("Позајми")){
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot bookDataSnap : snapshot.getChildren()){
                            Books book = bookDataSnap.getValue(Books.class);
                            if(book.getOwner().equals(intent.getStringExtra("owner")) &&
                                book.getTitle().equals(intent.getStringExtra("title")) && book.getStatus()){
                                bookDataSnap.getRef().child("status").setValue(false);
                                bookDataSnap.getRef().child("notified").setValue(false);
                                bookDataSnap.getRef().child("borrower").setValue(mAuth.getCurrentUser().getEmail());
                                startActivity(new Intent(view.getContext(), MainActivity.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else if(button.getText().toString().equals("Врати")){
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot bookDataSnap : snapshot.getChildren()){
                            Books book = bookDataSnap.getValue(Books.class);
                            if(book.getOwner().equals(intent.getStringExtra("owner")) &&
                                    book.getTitle().equals(intent.getStringExtra("title")) && !book.getStatus()){
                                    bookDataSnap.getRef().child("notified").removeValue();
                                    bookDataSnap.getRef().child("borrower").removeValue();
                                    bookDataSnap.getRef().child("status").setValue(true);
                                    startActivity(new Intent(view.getContext(), MainActivity.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else if(button.getText().toString().equals("Избриши")){
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot bookDataSnap : snapshot.getChildren()){
                            Books book = bookDataSnap.getValue(Books.class);
                            if(book.getOwner().equals(mAuth.getCurrentUser().getEmail()) &&
                                book.getBorrower() == null){
                                Toast.makeText(BookInfoActivity.this, "Se brise", Toast.LENGTH_SHORT).show();
//                                bookDataSnap.getRef().child("notified").removeValue();
//                                bookDataSnap.getRef().child("borrower").removeValue();
//                                bookDataSnap.getRef().child("status").setValue(true);
//                                startActivity(new Intent(view.getContext(), MainActivity.class));
                            }else{
                                Toast.makeText(BookInfoActivity.this, "Ne se brise", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        reference = FirebaseDatabase.getInstance().getReference().child("Location");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                intent = getIntent();
                boolean flag = false;
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