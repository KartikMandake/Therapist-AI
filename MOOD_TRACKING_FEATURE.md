# 📊 Mood Tracking Feature - Implementation Guide

## ✅ What's Been Created:

### 1. **MoodTrackingActivity** 
- Beautiful mood selection screen with 5 emoji options (😊 🙂 😐 😔 😢)
- 6 follow-up questions based on PHQ-9 clinical standards:
  - Energy levels
  - Sleep quality
  - Appetite
  - Focus/concentration
  - Social connection
  - Hopefulness
- Optional note/journal entry
- Smooth animations and transitions

### 2. **MoodHistoryActivity**
- View all past mood entries
- 30-day average score display
- Total check-ins counter
- Beautiful card-based list view
- Shows date, time, mood, score, and notes

### 3. **Data Management**
- `MoodEntry.java` - Data model for mood entries
- `MoodDatabase.java` - Local storage using SharedPreferences + Gson
- Automatic sorting by date
- Calculate averages and trends

### 4. **UI Components**
- Custom layouts with Material Design
- Smooth animations
- Color-coded moods
- Progress indicators
- Skip options

## 🚀 How to Use:

### To Launch Mood Tracking:
```java
// From any activity (e.g., MainActivity)
Intent intent = new Intent(this, MoodTrackingActivity.class);
startActivity(intent);
```

### Add a Button in MainActivity:
Add this to your MainActivity layout (activity_main.xml):
```xml
<ImageButton
    android:id="@+id/btnMoodTracking"
    android:layout_width="48dp"
    android:layout_height="48dp"
    android:background="?attr/selectableItemBackgroundBorderless"
    android:src="@drawable/ic_mood"
    android:contentDescription="Track Mood"
    android:tint="@color/primary_color" />
```

Then in MainActivity.java:
```java
ImageButton btnMoodTracking = findViewById(R.id.btnMoodTracking);
btnMoodTracking.setOnClickListener(v -> {
    Intent intent = new Intent(MainActivity.this, MoodTrackingActivity.class);
    startActivity(intent);
});
```

## 📱 User Flow:

1. **Select Mood** → User picks emoji (Great/Good/Okay/Low/Bad)
2. **Answer Questions** → 6 quick questions (can skip)
3. **Add Note** → Optional journal entry
4. **View History** → Automatically redirects to history page
5. **See Trends** → View past entries and average scores

## 🎨 Features:

✅ Clinical-standard questions (PHQ-9 inspired)
✅ Beautiful animations
✅ Local data storage (privacy-first)
✅ Score calculation and averaging
✅ Date/time tracking
✅ Optional notes
✅ Skip functionality
✅ Empty state handling
✅ Material Design UI

## 🔮 Future Enhancements:

- [ ] Add mood calendar view
- [ ] Create graphs/charts for trends
- [ ] Weekly/monthly insights
- [ ] Mood reminders/notifications
- [ ] Export data as PDF
- [ ] Mood patterns analysis
- [ ] Integration with AI chat (suggest topics based on mood)
- [ ] Dark mode support

## 📊 Data Structure:

Each mood entry stores:
- Timestamp
- Mood label (Great/Good/Okay/Low/Bad)
- Mood score (1-5)
- Average score (including question answers)
- Optional note
- All question answers (for future analysis)

## 🎯 Next Steps:

1. Add a mood tracking button to your MainActivity toolbar
2. Test the flow
3. Consider adding daily reminders
4. Integrate with your AI chat (e.g., "I see you've been feeling low lately, want to talk about it?")

---

**All files are ready to use! Just add the button to launch it from your main screen.**
