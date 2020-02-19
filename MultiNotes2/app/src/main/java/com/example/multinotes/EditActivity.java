package com.example.multinotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;


public class EditActivity extends AppCompatActivity
{
    private EditText title;
    private EditText description;
    private Notes note;
    int noteId;

    private String getPreviousTitle = "" , getPreviousContent = "";

    private static final String TAG = "EditActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);

        Intent i = getIntent();
        if(i.hasExtra("TITLE"))
        {
            getPreviousTitle = i.getStringExtra("TITLE");
            title.setText(getPreviousTitle);
        }
        if(i.hasExtra("DESC"))
        {
            getPreviousContent = i.getStringExtra("DESC");
            description.setText(getPreviousContent);
        }

        description.setMovementMethod(new ScrollingMovementMethod());
        description.setTextIsSelectable(true);
    }

    @Override
    public void onBackPressed()
    {
        if(title.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Note was not saved", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }

        else if(getPreviousTitle.equals(title.getText().toString()) && getPreviousContent.equals(description.getText().toString()))
        {
            super.onBackPressed();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveNote();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setMessage("Do you want to save this content?");
            builder.setTitle("Note Save");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    
    public void saveNote()
    {
         Notes newNote = new Notes();
         newNote.setTitle(title.getText().toString());
         newNote.setDescription(description.getText().toString());
         newNote.setTimestamp(Calendar.getInstance().getTime().toString());

         Intent resultIntent = new Intent();
         resultIntent.putExtra("EDIT_NOTE",newNote);

         if(title.getText().toString().isEmpty())
         {
             resultIntent.putExtra("STATUS", "NO_CHANGE");
             Toast.makeText(this, "Empty Note Cannot Be Saved", Toast.LENGTH_SHORT).show();
         }
         else if(getPreviousTitle.isEmpty() && getPreviousContent.isEmpty())
         {
             resultIntent.putExtra("STATUS", "NEW");
         }
         else if(getPreviousTitle.equals(title.getText().toString()) && getPreviousContent.equals(description.getText().toString()))
         {
             resultIntent.putExtra("STATUS", "NO_CHANGE");
         }
         else
             resultIntent.putExtra("STATUS", "CHANGE");

         setResult(RESULT_OK,resultIntent);
         finish();



//       // Log.d(TAG, "saveNote: Saving JSON File");
//        try
//        {
//            FileOutputStream fos = getApplicationContext().openFileOutput("Notes.json", Context.MODE_PRIVATE);
//
//            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos,"UTF-8"));
//            writer.setIndent(" ");
//            writer.beginObject();
//            writer.name("title").value(note.getTitle());
//            writer.name("description").value(note.getDescription());
//            writer.endObject();
//            writer.close();
//
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        catch (UnsupportedEncodingException e)
//        {
//            e.printStackTrace();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
    }
}




















