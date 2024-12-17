package com.example.noteapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.noteapp.model.Note;
import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM notes ORDER BY isPinned DESC, dateModified DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes WHERE title LIKE :searchQuery OR content LIKE :searchQuery")
    LiveData<List<Note>> searchNotes(String searchQuery);

    @Query("SELECT * FROM notes WHERE category = :category")
    LiveData<List<Note>> getNotesByCategory(String category);

    @Query("SELECT DISTINCT category FROM notes")
    LiveData<List<String>> getAllCategories();

    @Query("SELECT * FROM notes WHERE id = :id")
    LiveData<Note> getNoteById(int id);

    @Query("UPDATE notes SET isPinned = :isPinned WHERE id = :id")
    void updatePinStatus(int id, boolean isPinned);
} 