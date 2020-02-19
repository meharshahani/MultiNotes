package com.example.multinotes;


import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Notes implements Serializable
{
    private String title;
    private String description;
    private String dateTime;

     String getDescription()
    {
        return description;
    }

    void setDescription(String description)
    {
        this.description = description;
    }

    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    @NonNull
    @Override
    public String toString()
    {
        return title + ": " + description;
    }


    public String getTimestamp()
    {
        return dateTime;
    }
    public void setTimestamp(String timestamp)
    {
        this.dateTime = timestamp;
    }

}
