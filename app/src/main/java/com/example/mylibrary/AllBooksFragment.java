package com.example.mylibrary;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class AllBooksFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private myAdapter mAdapter;
    private ArrayList<Books> books;

    public AllBooksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_books, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        FloatingActionButton fab = getActivity().findViewById(R.id.addBook);
        fab.setOnClickListener(view -> startActivity(new Intent(getActivity(), CreateBookActivity.class)));

        IntentFilter filter = new IntentFilter();
        filter.addAction(LendingNotificationService.NOTIFY_OWNER);
        (getActivity()).registerReceiver(MyReceiver(), filter);

        books = new ArrayList<>();

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.all_books_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Books");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    books.clear();
                    for(DataSnapshot bookDataSnap : snapshot.getChildren()){
                        Books book = bookDataSnap.getValue(Books.class);
                        if(book.getStatus()){
                            books.add(book);
                        }else if(book.getBorrower() != null && book.getNotified() != null){
                            if(mAuth.getCurrentUser().getEmail().equals(book.getOwner()) && !book.getNotified()){
                                bookDataSnap.getRef().child("notified").setValue(true);
                                Intent intent = new Intent(getContext(), LendingNotificationService.class);
                                intent.putExtra("title", book.getTitle());
                                intent.putExtra("author", book.getAuthor());
                                intent.setAction(LendingNotificationService.NOTIFY_OWNER);
                                getActivity().startService(intent);
                            }
                        }
                    }
                }
                mAdapter = new myAdapter(books, R.layout.all_books_row, getContext());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private BroadcastReceiver MyReceiver() {
        return null;
    };
}