package com.example.mylibrary;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout drawerLayout = findViewById(R.id.drawer);

        findViewById(R.id.imageMenu).setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        ImageView logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(view.getContext(), LoginActivity.class));
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Location");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean flag = false;
                if(snapshot.hasChildren()){
                    for(DataSnapshot locationDataSnap : snapshot.getChildren()){
                        Location location = locationDataSnap.getValue(Location.class);
                        if(location.getEmail().equals(mAuth.getCurrentUser().getEmail())){
                            flag = true;
                            break;
                        }
                    }
                }

                if(!flag){
                    double Latitude,Longitude;
                    Latitude = 40 + (44 - 40) * new Random().nextDouble();
                    Longitude = 20 + (24 - 20) * new Random().nextDouble();
                    Location location = new Location(mAuth.getCurrentUser().getEmail(),Latitude,Longitude);
                    reference.push().setValue(location);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        TextView titleText = findViewById(R.id.titleText);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> titleText.setText(navDestination.getLabel()));
    }
}