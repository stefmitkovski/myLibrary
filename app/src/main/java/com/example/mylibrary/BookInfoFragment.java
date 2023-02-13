package com.example.mylibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class BookInfoFragment extends Fragment {

    public BookInfoFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Intent intent = getActivity().getIntent();
        Button btn = getActivity().findViewById(R.id.btn);
        TextView title = getActivity().findViewById(R.id.book_title);
        TextView author = getActivity().findViewById(R.id.book_author);
        TextView owner = getActivity().findViewById(R.id.book_owner);
        TextView status = getActivity().findViewById(R.id.book_status);

        title.setText("Наслов: "+ intent.getStringExtra("title"));
        author.setText("Автор: "+ intent.getStringExtra("author"));
        owner.setText("Сопственик: "+intent.getStringExtra("owner"));
        if(intent.getStringExtra("status").equals("true")){
            status.setText("Статус: слободна");
            btn.setText("Позајми");
        }else{
            status.setText("Статус: позајмена");
            btn.setText("Врати");
        }

        if(mAuth.getCurrentUser().getEmail().equals(intent.getStringExtra("owner"))){
           btn.setText("Избриши");
        }
    }
}