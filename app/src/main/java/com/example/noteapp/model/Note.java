package com.example.noteapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private String category;
    private Date dateCreated;
    private Date dateModified;
    private String priority;
    private boolean isPinned;

    public Note(String title, String content, String category, String priority) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.priority = priority;
        this.dateCreated = new Date();
        this.dateModified = new Date();
        this.isPinned = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Date getDateCreated() { return dateCreated; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
    
    public Date getDateModified() { return dateModified; }
    public void setDateModified(Date dateModified) { this.dateModified = dateModified; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public boolean isPinned() { return isPinned; }
    public void setPinned(boolean pinned) { isPinned = pinned; }
} 