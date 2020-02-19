package com.example.multinotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder
{
    TextView title;
    TextView description;
    TextView dateTime;

    public MyViewHolder(@NonNull View itemView)
    {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        description = itemView.findViewById(R.id.description);
        dateTime = itemView.findViewById(R.id.datetime);
    }
}
