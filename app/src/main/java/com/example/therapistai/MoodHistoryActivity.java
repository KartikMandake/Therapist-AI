package com.example.therapistai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MoodHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvAverageScore, tvTotalEntries, tvEmptyState;
    private ImageButton btnBack;
    private MoodHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        initializeViews();
        loadMoodHistory();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        tvAverageScore = findViewById(R.id.tvAverageScore);
        tvTotalEntries = findViewById(R.id.tvTotalEntries);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        btnBack = findViewById(R.id.btnBack);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Back button
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadMoodHistory() {
        MoodDatabase db = MoodDatabase.getInstance(this);
        List<MoodEntry> entries = db.getAllMoodEntries();

        if (entries.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        tvEmptyState.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        // Show stats
        float avgScore = db.getAverageMoodScore(30);
        tvAverageScore.setText(String.format(Locale.getDefault(), "%.1f/5.0", avgScore));
        tvTotalEntries.setText(String.valueOf(entries.size()));

        // Setup adapter
        adapter = new MoodHistoryAdapter(entries);
        recyclerView.setAdapter(adapter);
    }

    // Adapter for RecyclerView
    private class MoodHistoryAdapter extends RecyclerView.Adapter<MoodHistoryAdapter.ViewHolder> {

        private List<MoodEntry> entries;

        MoodHistoryAdapter(List<MoodEntry> entries) {
            this.entries = entries;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mood_entry, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MoodEntry entry = entries.get(position);
            holder.bind(entry);
        }

        @Override
        public int getItemCount() {
            return entries.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvEmoji, tvMoodLabel, tvDate, tvScore, tvNote;
            MaterialCardView card;

            ViewHolder(View itemView) {
                super(itemView);
                tvEmoji = itemView.findViewById(R.id.tvEmoji);
                tvMoodLabel = itemView.findViewById(R.id.tvMoodLabel);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvScore = itemView.findViewById(R.id.tvScore);
                tvNote = itemView.findViewById(R.id.tvNote);
                card = itemView.findViewById(R.id.card);
            }

            void bind(MoodEntry entry) {
                tvEmoji.setText(entry.getMoodEmoji());
                tvMoodLabel.setText(entry.getMoodLabel());
                
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault());
                tvDate.setText(sdf.format(new Date(entry.getTimestamp())));
                
                tvScore.setText(String.format(Locale.getDefault(), "%.1f", entry.getAverageScore()));
                
                if (entry.getNote() != null && !entry.getNote().isEmpty()) {
                    tvNote.setVisibility(View.VISIBLE);
                    tvNote.setText(entry.getNote());
                } else {
                    tvNote.setVisibility(View.GONE);
                }
            }
        }
    }
}
