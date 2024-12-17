package com.example.noteapp.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.noteapp.dao.NoteDao;
import com.example.noteapp.database.NoteDatabase;
import com.example.noteapp.model.Note;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<String>> allCategories;
    private ExecutorService executorService;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
        allCategories = noteDao.getAllCategories();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Note note) {
        executorService.execute(() -> noteDao.insert(note));
    }

    public void update(Note note) {
        executorService.execute(() -> noteDao.update(note));
    }

    public void delete(Note note) {
        executorService.execute(() -> noteDao.delete(note));
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<String>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<Note>> searchNotes(String query) {
        return noteDao.searchNotes(query);
    }

    public LiveData<List<Note>> getNotesByCategory(String category) {
        return noteDao.getNotesByCategory(category);
    }

    public LiveData<Note> getNoteById(int id) {
        return noteDao.getNoteById(id);
    }
} 