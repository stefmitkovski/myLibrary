package com.example.mylibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateBookActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book);

        ImageView back = findViewById(R.id.back_to_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), MainActivity.class));
            }
        });

        Button submit = findViewById(R.id.create_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText title = findViewById(R.id.create_title);
                EditText author = findViewById(R.id.create_author);
                EditText description = findViewById(R.id.create_description);

                if(title.getText().toString().trim().equals("") ||
                author.getText().toString().trim().equals("") ||
                description.getText().toString().trim().equals("")){
                    Snackbar.make(view, "Заборавивте да внесете податоци", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }else{
                    mAuth = FirebaseAuth.getInstance();
                    Books book = new Books();
                    book.setTitle(title.getText().toString().trim());
                    book.setAuthor(author.getText().toString().trim());
                    book.setDescription(description.getText().toString().trim());
                    book.setOwner(mAuth.getCurrentUser().getEmail());
                    book.setStatus(true);

                    reference = FirebaseDatabase.getInstance().getReference().child("Books");
                    reference.push().setValue(book);

                    startActivity(new Intent(view.getContext(), MainActivity.class));

                }
            }
        });
    }
}