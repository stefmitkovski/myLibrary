package com.example.mylibrary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {

    private ArrayList<Books> myList;
    private int rowLayout;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Title;
        public TextView Author;
        public TextView Owner;
        public TextView Status;

        public ViewHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.book_title);
            Author = (TextView) itemView.findViewById(R.id.book_author);
            Owner = (TextView) itemView.findViewById(R.id.book_owner);
            Status = (TextView) itemView.findViewById(R.id.book_status);
        }
    }

    public myAdapter(ArrayList<Books> myList, int rowLayout, Context context) {
        this.myList = myList;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Books book = myList.get(i);
        viewHolder.Title.setText("Наслов: "+book.getTitle());
        viewHolder.Author.setText("Автор: "+book.getAuthor());
        viewHolder.Owner.setText("Сопственик: "+book.getOwner());
        if(book.getStatus() == true) {
            viewHolder.Status.setText("Статус: слободна");
        }else{
            viewHolder.Status.setText("Статус: позајмена");
        }
        viewHolder.Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BookInfoActivity.class);
                intent.putExtra("title",book.getTitle());
                intent.putExtra("author",book.getAuthor());
                intent.putExtra("owner",book.getOwner());
                if(book.getStatus() == true) {
                    intent.putExtra("status", "true");
                }else{
                    intent.putExtra("status","false");
                }
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList == null ? 0 : myList.size();
    }
}
