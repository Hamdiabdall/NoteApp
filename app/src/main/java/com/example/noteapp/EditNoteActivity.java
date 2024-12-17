package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.noteapp.model.Note;
import com.example.noteapp.viewmodel.NoteViewModel;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE_ID = "com.example.noteapp.EXTRA_NOTE_ID";
    
    private NoteViewModel noteViewModel;
    private TextInputEditText editTextTitle;
    private TextInputEditText editTextContent;
    private AutoCompleteTextView editTextCategory;
    private ChipGroup priorityChipGroup;
    private FloatingActionButton fabSave;
    private Note currentNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // Initialize views
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        editTextCategory = findViewById(R.id.editTextCategory);
        priorityChipGroup = findViewById(R.id.priorityChipGroup);
        fabSave = findViewById(R.id.fabSave);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        // Initialize ViewModel
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // Set up category dropdown
        noteViewModel.getAllCategories().observe(this, categories -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                categories
            );
            editTextCategory.setAdapter(adapter);
        });

        // Check if editing existing note
        int noteId = getIntent().getIntExtra(EXTRA_NOTE_ID, -1);
        if (noteId != -1) {
            setTitle("Edit Note");
            noteViewModel.getNoteById(noteId).observe(this, note -> {
                if (note != null) {
                    currentNote = note;
                    editTextTitle.setText(note.getTitle());
                    editTextContent.setText(note.getContent());
                    editTextCategory.setText(note.getCategory());
                    setPriorityChip(note.getPriority());
                }
            });
        } else {
            setTitle("New Note");
            currentNote = null;
        }

        // Save button click listener
        fabSave.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String priority = getSelectedPriority();

        if (title.isEmpty()) {
            editTextTitle.setError("Title is required");
            return;
        }

        if (currentNote == null) {
            // Create new note
            Note note = new Note(title, content, category, priority);
            noteViewModel.insert(note);
        } else {
            // Update existing note
            currentNote.setTitle(title);
            currentNote.setContent(content);
            currentNote.setCategory(category);
            currentNote.setPriority(priority);
            currentNote.setDateModified(new Date());
            noteViewModel.update(currentNote);
        }

        finish();
    }

    private String getSelectedPriority() {
        int selectedChipId = priorityChipGroup.getCheckedChipId();
        if (selectedChipId != -1) {
            Chip selectedChip = findViewById(selectedChipId);
            return selectedChip.getText().toString();
        }
        return "Low"; // Default priority
    }

    private void setPriorityChip(String priority) {
        int chipId;
        switch (priority.toLowerCase()) {
            case "high":
                chipId = R.id.chipHigh;
                break;
            case "medium":
                chipId = R.id.chipMedium;
                break;
            default:
                chipId = R.id.chipLow;
        }
        priorityChipGroup.check(chipId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 