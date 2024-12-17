package com.example.noteapp.database;

import android.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.noteapp.model.Note;

public class DatabaseHelper {
    private static final String CREATE_TABLE_QUERY = 
        "CREATE TABLE IF NOT EXISTS notes (" +
        "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
        "title VARCHAR(255), " +
        "content TEXT, " +
        "category VARCHAR(50), " +
        "date_created DATETIME, " +
        "date_modified DATETIME, " +
        "priority VARCHAR(20), " +
        "is_pinned BOOLEAN)";

    private static final String INSERT_NOTE = 
        "INSERT INTO notes (title, content, category, date_created, date_modified, priority, is_pinned) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_NOTE = 
        "UPDATE notes SET title=?, content=?, category=?, date_modified=?, priority=?, is_pinned=? " +
        "WHERE id=?";

    private static final String DELETE_NOTE = "DELETE FROM notes WHERE id = ?";
    private static final String GET_ALL_NOTES = "SELECT * FROM notes ORDER BY is_pinned DESC, date_modified DESC";
    private static final String GET_NOTE_BY_ID = "SELECT * FROM notes WHERE id = ?";

    public void createTable() {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_TABLE_QUERY)) {
            stmt.execute();
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error creating table", e);
        }
    }

    public long insertNote(Note note) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_NOTE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, note.getTitle());
            stmt.setString(2, note.getContent());
            stmt.setString(3, note.getCategory());
            stmt.setTimestamp(4, new java.sql.Timestamp(note.getDateCreated().getTime()));
            stmt.setTimestamp(5, new java.sql.Timestamp(note.getDateModified().getTime()));
            stmt.setString(6, note.getPriority());
            stmt.setBoolean(7, note.isPinned());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating note failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating note failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error inserting note", e);
            return -1;
        }
    }

    public boolean updateNote(Note note) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_NOTE)) {
            
            stmt.setString(1, note.getTitle());
            stmt.setString(2, note.getContent());
            stmt.setString(3, note.getCategory());
            stmt.setTimestamp(4, new java.sql.Timestamp(note.getDateModified().getTime()));
            stmt.setString(5, note.getPriority());
            stmt.setBoolean(6, note.isPinned());
            stmt.setInt(7, note.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error updating note", e);
            return false;
        }
    }

    public boolean deleteNote(int id) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_NOTE)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error deleting note", e);
            return false;
        }
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_NOTES);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Note note = new Note(
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("category"),
                    rs.getString("priority")
                );
                note.setId(rs.getInt("id"));
                note.setDateCreated(rs.getTimestamp("date_created"));
                note.setDateModified(rs.getTimestamp("date_modified"));
                note.setPinned(rs.getBoolean("is_pinned"));
                notes.add(note);
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error getting all notes", e);
        }
        return notes;
    }

    public Note getNoteById(int id) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_NOTE_BY_ID)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Note note = new Note(
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("category"),
                        rs.getString("priority")
                    );
                    note.setId(rs.getInt("id"));
                    note.setDateCreated(rs.getTimestamp("date_created"));
                    note.setDateModified(rs.getTimestamp("date_modified"));
                    note.setPinned(rs.getBoolean("is_pinned"));
                    return note;
                }
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error getting note by id", e);
        }
        return null;
    }
}