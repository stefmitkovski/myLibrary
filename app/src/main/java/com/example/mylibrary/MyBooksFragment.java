package com.example.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyBooksFragment extends Fragment {

    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;
    private myAdapter mAdapter;
    private ArrayList<Books> books;
    public MyBooksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_books, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        FloatingActionButton fab = getActivity().findViewById(R.id.addBook);
        fab.setOnClickListener(view -> startActivity(new Intent(getActivity(), CreateBookActivity.class)));

        books = new ArrayList<>();

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.my_books_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Books");
        TextView title = getActivity().findViewById(R.id.my_books_title);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    title.setVisibility(View.INVISIBLE);
                    books.clear();
                    for(DataSnapshot bookDataSnap : snapshot.getChildren()){
                        Books book = bookDataSnap.getValue(Books.class);
//                        Toast.makeText(getActivity(), ""+mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                        if(mAuth.getCurrentUser().getEmail().equals(book.getOwner())) {
                            books.add(book);
                        }
                    }
                }else{
                    title.setVisibility(View.VISIBLE);
                }
                mAdapter = new myAdapter(books, R.layout.all_books_row, getContext());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}