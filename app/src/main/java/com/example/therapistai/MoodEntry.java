package com.example.therapistai;

import java.util.List;

public class MoodEntry {
    private long timestamp;
    private String moodLabel;
    private int moodScore;
    private float averageScore;
    private String note;
    private List<Integer> questionAnswers;
    
    public MoodEntry(long timestamp, String moodLabel, int moodScore, 
                     float averageScore, String note, List<Integer> questionAnswers) {
        this.timestamp = timestamp;
        this.moodLabel = moodLabel;
        this.moodScore = moodScore;
        this.averageScore = averageScore;
        this.note = note;
        this.questionAnswers = questionAnswers;
    }
    
    // Getters
    public long getTimestamp() { return timestamp; }
    public String getMoodLabel() { return moodLabel; }
    public int getMoodScore() { return moodScore; }
    public float getAverageScore() { return averageScore; }
    public String getNote() { return note; }
    public List<Integer> getQuestionAnswers() { return questionAnswers; }
    
    // Get emoji based on mood
    public String getMoodEmoji() {
        switch (moodScore) {
            case 5: return "😊";
            case 4: return "🙂";
            case 3: return "😐";
            case 2: return "😔";
            case 1: return "😢";
            default: return "😐";
        }
    }
    
    // Get color based on mood
    public String getMoodColor() {
        switch (moodScore) {
            case 5: return "#48BB78"; // Green
            case 4: return "#68D391"; // Light green
            case 3: return "#FFB366"; // Orange
            case 2: return "#FF9A76"; // Light red
            case 1: return "#F56565"; // Red
            default: return "#A0AEC0"; // Gray
        }
    }
}
