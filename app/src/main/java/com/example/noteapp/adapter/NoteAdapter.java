package com.example.noteapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.noteapp.R;
import com.example.noteapp.model.Note;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {
    private OnNoteClickListener listener;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                   oldItem.getContent().equals(newItem.getContent()) &&
                   oldItem.getCategory().equals(newItem.getCategory()) &&
                   oldItem.isPinned() == newItem.isPinned();
        }
    };

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getItem(position);
        holder.bind(note);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView contentTextView;
        private TextView categoryTextView;
        private TextView dateTextView;
        private ImageView pinIcon;

        NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.noteTitle);
            contentTextView = itemView.findViewById(R.id.noteContent);
            categoryTextView = itemView.findViewById(R.id.noteCategory);
            dateTextView = itemView.findViewById(R.id.noteDate);
            pinIcon = itemView.findViewById(R.id.pinIcon);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onNoteClick(getItem(position));
                }
            });
        }

        void bind(Note note) {
            titleTextView.setText(note.getTitle());
            contentTextView.setText(note.getContent());
            categoryTextView.setText(note.getCategory());
            dateTextView.setText(dateFormat.format(note.getDateModified()));
            pinIcon.setVisibility(note.isPinned() ? View.VISIBLE : View.GONE);
        }
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public void setOnNoteClickListener(OnNoteClickListener listener) {
        this.listener = listener;
    }
} 