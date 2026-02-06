package com.example.therapistai;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MoodTrackingActivity extends AppCompatActivity {

    private TextView tvTitle, tvQuestion, tvProgress;
    private LinearLayout moodEmojiContainer, questionContainer;
    private MaterialCardView cardVeryHappy, cardHappy, cardNeutral, cardSad, cardVerySad;
    private RadioGroup radioGroup;
    private EditText etNote;
    private Button btnNext, btnSkip;
    private ImageButton btnBack;
    
    private int currentQuestionIndex = 0;
    private String selectedMood = "";
    private int moodScore = 0;
    private List<Integer> answers = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracking);
        
        initializeViews();
        setupQuestions();
        setupMoodSelection();
        startAnimations();
    }

    private void initializeViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvProgress = findViewById(R.id.tvProgress);
        moodEmojiContainer = findViewById(R.id.moodEmojiContainer);
        questionContainer = findViewById(R.id.questionContainer);
        cardVeryHappy = findViewById(R.id.cardVeryHappy);
        cardHappy = findViewById(R.id.cardHappy);
        cardNeutral = findViewById(R.id.cardNeutral);
        cardSad = findViewById(R.id.cardSad);
        cardVerySad = findViewById(R.id.cardVerySad);
        radioGroup = findViewById(R.id.radioGroup);
        etNote = findViewById(R.id.etNote);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);
        btnBack = findViewById(R.id.btnBack);
        
        // Back button
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupQuestions() {
        // PHQ-9 inspired questions adapted for daily check-in
        questions.add(new Question("How much energy did you have today?", 
            new String[]{"Very energetic", "Good energy", "Moderate", "Low energy", "Exhausted"}));
        
        questions.add(new Question("How well did you sleep last night?", 
            new String[]{"Very well", "Pretty well", "Okay", "Not great", "Poorly"}));
        
        questions.add(new Question("How was your appetite today?", 
            new String[]{"Normal & healthy", "Good", "Average", "Low", "Very low or overate"}));
        
        questions.add(new Question("How focused were you today?", 
            new String[]{"Very focused", "Mostly focused", "Somewhat", "Distracted", "Couldn't focus"}));
        
        questions.add(new Question("How connected did you feel to others?", 
            new String[]{"Very connected", "Connected", "Neutral", "Isolated", "Very isolated"}));
        
        questions.add(new Question("How hopeful do you feel about tomorrow?", 
            new String[]{"Very hopeful", "Hopeful", "Neutral", "Worried", "Anxious"}));
    }

    private void setupMoodSelection() {
        setupMoodCard(cardVeryHappy, "😊", "Great", 5);
        setupMoodCard(cardHappy, "🙂", "Good", 4);
        setupMoodCard(cardNeutral, "😐", "Okay", 3);
        setupMoodCard(cardSad, "😔", "Low", 2);
        setupMoodCard(cardVerySad, "😢", "Bad", 1);
        
        btnSkip.setOnClickListener(v -> finish());
    }

    private void setupMoodCard(MaterialCardView card, String emoji, String label, int score) {
        card.setOnClickListener(v -> {
            selectedMood = label;
            moodScore = score;
            animateCardSelection(card);
            
            // Transition to questions after delay
            card.postDelayed(() -> {
                moodEmojiContainer.setVisibility(View.GONE);
                questionContainer.setVisibility(View.VISIBLE);
                showQuestion(0);
            }, 300);
        });
    }

    private void showQuestion(int index) {
        if (index >= questions.size()) {
            showNoteScreen();
            return;
        }
        
        currentQuestionIndex = index;
        Question question = questions.get(index);
        
        tvProgress.setText(String.format(Locale.getDefault(), "%d/%d", index + 1, questions.size()));
        tvQuestion.setText(question.text);
        
        // Setup radio buttons
        radioGroup.removeAllViews();
        for (int i = 0; i < question.options.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(question.options[i]);
            radioButton.setTextSize(16);
            radioButton.setPadding(16, 24, 16, 24);
            radioButton.setId(i);
            radioGroup.addView(radioButton);
        }
        
        btnNext.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Score: 5 for first option, 1 for last option
            int score = 5 - selectedId;
            answers.add(score);
            
            // Animate to next question
            animateQuestionTransition();
            showQuestion(index + 1);
        });
        
        btnSkip.setOnClickListener(v -> {
            answers.add(3); // Neutral score for skipped
            showQuestion(index + 1);
        });
    }

    private void showNoteScreen() {
        tvQuestion.setText("Anything else you'd like to note?");
        tvProgress.setText("Final Step");
        radioGroup.setVisibility(View.GONE);
        etNote.setVisibility(View.VISIBLE);
        
        btnNext.setText("Complete");
        btnNext.setOnClickListener(v -> saveMoodEntry());
        
        btnSkip.setText("Skip Note");
        btnSkip.setOnClickListener(v -> saveMoodEntry());
    }

    private void saveMoodEntry() {
        // Calculate average score
        int totalScore = moodScore;
        for (int answer : answers) {
            totalScore += answer;
        }
        float averageScore = totalScore / (float)(answers.size() + 1);
        
        // Save to database/SharedPreferences
        MoodEntry entry = new MoodEntry(
            System.currentTimeMillis(),
            selectedMood,
            moodScore,
            averageScore,
            etNote.getText().toString(),
            answers
        );
        
        MoodDatabase.getInstance(this).saveMoodEntry(entry);
        
        // Show success and navigate
        Toast.makeText(this, "Mood tracked! 🎉", Toast.LENGTH_SHORT).show();
        
        Intent intent = new Intent(this, MoodHistoryActivity.class);
        startActivity(intent);
        finish();
    }

    private void animateCardSelection(View card) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(card, "scaleX", 1f, 0.9f, 1.1f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(card, "scaleY", 1f, 0.9f, 1.1f, 1f);
        scaleX.setDuration(400);
        scaleY.setDuration(400);
        scaleX.start();
        scaleY.start();
    }

    private void animateQuestionTransition() {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(questionContainer, "alpha", 1f, 0f);
        fadeOut.setDuration(200);
        fadeOut.start();
        
        questionContainer.postDelayed(() -> {
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(questionContainer, "alpha", 0f, 1f);
            fadeIn.setDuration(200);
            fadeIn.start();
        }, 200);
    }

    private void startAnimations() {
        tvTitle.setAlpha(0f);
        ObjectAnimator titleFade = ObjectAnimator.ofFloat(tvTitle, "alpha", 0f, 1f);
        titleFade.setDuration(600);
        titleFade.start();
        
        animateCard(cardVeryHappy, 100);
        animateCard(cardHappy, 200);
        animateCard(cardNeutral, 300);
        animateCard(cardSad, 400);
        animateCard(cardVerySad, 500);
    }

    private void animateCard(View card, long delay) {
        card.setAlpha(0f);
        card.setTranslationY(30f);
        
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(card, "alpha", 0f, 1f);
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(card, "translationY", 30f, 0f);
        
        fadeIn.setDuration(400);
        slideUp.setDuration(400);
        fadeIn.setStartDelay(delay);
        slideUp.setStartDelay(delay);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        slideUp.setInterpolator(new AccelerateDecelerateInterpolator());
        
        fadeIn.start();
        slideUp.start();
    }

    // Inner class for questions
    private static class Question {
        String text;
        String[] options;
        
        Question(String text, String[] options) {
            this.text = text;
            this.options = options;
        }
    }
}
