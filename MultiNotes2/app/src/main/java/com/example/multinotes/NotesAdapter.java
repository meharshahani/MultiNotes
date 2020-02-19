package com.example.multinotes;

import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<MyViewHolder>
{
    private static final String TAG =  "NotesAdapter";
    private List<Notes> notesList;
    private MainActivity mainActivity;

    public NotesAdapter(List<Notes> notesList, MainActivity mainActivity)
    {
        this.notesList = notesList;
        this.mainActivity= mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_entry, parent,false);

        itemView.setOnClickListener((View.OnClickListener) mainActivity);
        itemView.setOnLongClickListener((View.OnLongClickListener) mainActivity);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
            Notes notes = notesList.get(position);

            holder.title.setText(notes.getTitle());
            //holder.description.setText(notes.getDescription());
            holder.dateTime.setText(notes.getTimestamp());
            if(notes.getDescription().length() > 80)
            {
                String new_Note = notes.getDescription().substring(0,79);
                new_Note = new_Note.concat("...");
                holder.description.setText(new_Note);
            }
            else
            {
                holder.description.setText(notes.getDescription());
            }
    }

    @Override
    public int getItemCount()
    {
        mainActivity.getSupportActionBar().setTitle("Notes "+"("+notesList.size()+")");
        return notesList.size();
    }
}
