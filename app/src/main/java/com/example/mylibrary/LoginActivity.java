package com.example.mylibrary;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView redirect = findViewById(R.id.redirect_register);
        redirect.setOnClickListener(view -> startActivity(new Intent(view.getContext(), RegisterActivity.class)));

        Button main = findViewById(R.id.login_submit);
        main.setOnClickListener(view -> {
            EditText email = findViewById(R.id.login_email);
            EditText password = findViewById(R.id.login_password);

            if(email.getText().toString().trim().equals("") ||
            password.getText().toString().trim().equals("")){
                Toast.makeText(LoginActivity.this, "Заборавивте да внесете некое поле", Toast.LENGTH_SHORT).show();
            }else{
                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "Login:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "Login:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            private void updateUI(FirebaseUser user) {
                                if(user != null){
                                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });
    }
}