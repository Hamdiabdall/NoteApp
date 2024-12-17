package com.example.noteapp;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

public class NoteApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize night mode based on saved preferences
        boolean isDarkMode = getSharedPreferences("settings", MODE_PRIVATE)
            .getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
            isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
} 