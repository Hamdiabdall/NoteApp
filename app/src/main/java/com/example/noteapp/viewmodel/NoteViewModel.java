package com.example.noteapp.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.noteapp.model.Note;
import com.example.noteapp.repository.NoteRepository;
import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<String>> allCategories;

    public NoteViewModel(Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
        allCategories = repository.getAllCategories();
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<String>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<Note>> searchNotes(String query) {
        return repository.searchNotes("%" + query + "%");
    }

    public LiveData<List<Note>> getNotesByCategory(String category) {
        return repository.getNotesByCategory(category);
    }

    public LiveData<Note> getNoteById(int id) {
        return repository.getNoteById(id);
    }
} 