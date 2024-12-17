package com.example.noteapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.noteapp.model.Note;
import com.example.noteapp.dao.NoteDao;
import com.example.noteapp.util.DateConverter;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;
    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                NoteDatabase.class,
                "note_database"
            )
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }
} 