package com.example.mylibrary;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button back = findViewById(R.id.back_to_login);
        back.setOnClickListener(view -> startActivity(new Intent(view.getContext(), LoginActivity.class)));

        Button register = findViewById(R.id.register_submit);
        register.setOnClickListener(view -> {
            EditText email = findViewById(R.id.register_email);
            EditText password = findViewById(R.id.register_password);
            EditText password_confirmation = findViewById(R.id.register_password_confirmation);

            startActivity(new Intent(view.getContext(), MainActivity.class));

            if(email.getText().toString().trim().equals("") ||
                    password.getText().toString().trim().equals("") ||
                    password_confirmation.getText().toString().trim().equals("")){
                Toast.makeText(RegisterActivity.this, "Заборавивте да внесете некое поле", Toast.LENGTH_SHORT).show();
            }else if(!password.getText().toString().equals(password_confirmation.getText().toString())){
                Toast.makeText(RegisterActivity.this, "Внесовте две различни лозинки", Toast.LENGTH_SHORT).show();
            }else{
                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(view.getContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            private void updateUI(FirebaseUser CurrentUser) {
                                Intent intent = new Intent(view.getContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        });
            }
        });
    }
}