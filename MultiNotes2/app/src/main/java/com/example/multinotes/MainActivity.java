package com.example.multinotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.util.LogPrinter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener
{

    private static final int REQUEST_CODE= 1;

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private static final List<Notes> notesList = new ArrayList<>();
    private NotesAdapter nAdapter;
    private Notes note;

    private int position;

    private TextView title;
    private TextView description;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        nAdapter = new NotesAdapter(notesList,this);

        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new AsyncTask(this).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.addMenu:
                Intent intent = new Intent(this,EditActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                return true;

            case R.id.aboutMenu:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View v)
    {
        position = recyclerView.getChildLayoutPosition(v);
       // Notes n = notesList.get(position);

        //should lead the user to edit the note
        Intent intent = new Intent(MainActivity.this, EditActivity.class);

        intent.putExtra("TITLE",notesList.get(position).getTitle());
        intent.putExtra("DESC",notesList.get(position).getDescription());
        intent.putExtra("DATE",notesList.get(position).getTimestamp());

        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public boolean onLongClick(View v)
    {
        position = recyclerView.getChildLayoutPosition(v);
        //Notes n = notesList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notesList.remove(position);
                nAdapter.notifyDataSetChanged();
                position = -1;
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                position = -1;
            }
        });
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setTitle("Delete");
        AlertDialog dialog = builder.create();
        dialog.show();

        //final int itemToDelete = position;
        //should ask if the user wants to delete the note
        return false;
    }

    @Override
    protected void onResume()
    {
        notesList.size();
        super.onResume();
        nAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause()
    {
        saveNotes();
        super.onPause();
    }

    private void saveNotes()
    {
        try
        {
            FileOutputStream fos = getApplicationContext().openFileOutput("Notes.json",Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos,"UTF-8"));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("notes");
            writeNotesArray(writer);
            writer.endObject();
            writer.close();;

        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    public void writeNotesArray(JsonWriter writer) throws IOException
    {
        writer.beginArray();
        for(Notes value : notesList)
        {
            writeNotesObject(writer, value);
        }
        writer.endArray();
    }

    public void writeNotesObject(JsonWriter writer, Notes value) throws IOException
    {
        writer.beginObject();
        writer.name("title").value(value.getTitle());
        writer.name("timestamp").value(value.getTimestamp());
        writer.name("description").value(value.getDescription());
        writer.endObject();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Notes edit_note = (Notes) data.getExtras().getSerializable("EDIT_NOTE");

                String status = data.getStringExtra("STATUS");
                if(status.equals("NO_CHANGE"))
                { }
                else if(status.equals("CHANGE"))
                {
                    notesList.remove(position);
                    notesList.add(0,edit_note);
                }
                else if(status.equals("NEW"))
                {
                    notesList.add(0, edit_note);
                }
            }
        }
    }

    public List<Notes> getNotesList()
    {
        return notesList;
    }
}
