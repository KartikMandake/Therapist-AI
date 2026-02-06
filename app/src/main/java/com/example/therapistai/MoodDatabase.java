package com.example.therapistai;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoodDatabase {
    private static MoodDatabase instance;
    private SharedPreferences prefs;
    private Gson gson;
    private static final String PREF_NAME = "MoodTrackingPrefs";
    private static final String KEY_MOOD_ENTRIES = "mood_entries";
    
    private MoodDatabase(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    
    public static synchronized MoodDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new MoodDatabase(context.getApplicationContext());
        }
        return instance;
    }
    
    public void saveMoodEntry(MoodEntry entry) {
        List<MoodEntry> entries = getAllMoodEntries();
        entries.add(entry);
        
        String json = gson.toJson(entries);
        prefs.edit().putString(KEY_MOOD_ENTRIES, json).apply();
    }
    
    public List<MoodEntry> getAllMoodEntries() {
        String json = prefs.getString(KEY_MOOD_ENTRIES, null);
        if (json == null) {
            return new ArrayList<>();
        }
        
        Type type = new TypeToken<List<MoodEntry>>(){}.getType();
        List<MoodEntry> entries = gson.fromJson(json, type);
        
        // Sort by timestamp (newest first)
        Collections.sort(entries, (a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
        
        return entries;
    }
    
    public List<MoodEntry> getRecentEntries(int count) {
        List<MoodEntry> all = getAllMoodEntries();
        return all.subList(0, Math.min(count, all.size()));
    }
    
    public MoodEntry getTodayEntry() {
        List<MoodEntry> entries = getAllMoodEntries();
        if (entries.isEmpty()) return null;
        
        long today = System.currentTimeMillis();
        long oneDayMs = 24 * 60 * 60 * 1000;
        
        for (MoodEntry entry : entries) {
            if (today - entry.getTimestamp() < oneDayMs) {
                return entry;
            }
        }
        return null;
    }
    
    public float getAverageMoodScore(int days) {
        List<MoodEntry> entries = getAllMoodEntries();
        if (entries.isEmpty()) return 0;
        
        long cutoffTime = System.currentTimeMillis() - (days * 24L * 60 * 60 * 1000);
        float total = 0;
        int count = 0;
        
        for (MoodEntry entry : entries) {
            if (entry.getTimestamp() >= cutoffTime) {
                total += entry.getAverageScore();
                count++;
            }
        }
        
        return count > 0 ? total / count : 0;
    }
    
    public void clearAllEntries() {
        prefs.edit().remove(KEY_MOOD_ENTRIES).apply();
    }
}
