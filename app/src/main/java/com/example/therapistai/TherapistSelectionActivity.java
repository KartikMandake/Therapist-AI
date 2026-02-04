package com.example.therapistai;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.card.MaterialCardView;

/**
 * TherapistSelectionActivity - Allows users to select their therapy focus area
 * Personalizes the AI responses based on user selection
 */
public class TherapistSelectionActivity extends AppCompatActivity {
    
    private TextView tvSelectionTitle;
    private MaterialCardView cardLove, cardAnxiety, cardCareer, cardGeneral, cardDepression, cardStress;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist_selection);
        
        initializeViews();
        setupClickListeners();
        startAnimations();
    }
    
    private void initializeViews() {
        tvSelectionTitle = findViewById(R.id.tvSelectionTitle);
        cardLove = findViewById(R.id.cardLove);
        cardAnxiety = findViewById(R.id.cardAnxiety);
        cardCareer = findViewById(R.id.cardCareer);
        cardGeneral = findViewById(R.id.cardGeneral);
        cardDepression = findViewById(R.id.cardDepression);
        cardStress = findViewById(R.id.cardStress);
    }
    
    private void setupClickListeners() {
        setupCardClickListener(cardLove, "Love & Relationships", "💕");
        setupCardClickListener(cardAnxiety, "Anxiety & Worry", "😰");
        setupCardClickListener(cardCareer, "Career & Growth", "💼");
        setupCardClickListener(cardGeneral, "General Support", "💙");
        setupCardClickListener(cardDepression, "Depression & Mood", "🌧️");
        setupCardClickListener(cardStress, "Stress Management", "😓");
    }
    
    private void setupCardClickListener(MaterialCardView card, String type, String emoji) {
        card.setOnClickListener(v -> {
            animateCardSelection(v);
            v.postDelayed(() -> selectTherapistType(type, emoji), 300);
        });
    }
    
    private void startAnimations() {
        // Fade in title
        ObjectAnimator titleFade = ObjectAnimator.ofFloat(tvSelectionTitle, "alpha", 0f, 1f);
        titleFade.setDuration(600);
        titleFade.start();
        
        // Animate cards with stagger effect
        animateCard(cardLove, 100);
        animateCard(cardAnxiety, 200);
        animateCard(cardCareer, 300);
        animateCard(cardGeneral, 400);
        animateCard(cardDepression, 500);
        animateCard(cardStress, 600);
    }
    
    private void animateCard(View card, long delay) {
        card.setAlpha(0f);
        card.setTranslationY(50f);
        
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(card, "alpha", 0f, 1f);
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(card, "translationY", 50f, 0f);
        
        fadeIn.setDuration(500);
        slideUp.setDuration(500);
        fadeIn.setStartDelay(delay);
        slideUp.setStartDelay(delay);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        slideUp.setInterpolator(new AccelerateDecelerateInterpolator());
        
        fadeIn.start();
        slideUp.start();
    }
    
    private void selectTherapistType(String type, String emoji) {
        // Navigate to MainActivity with selected type
        Intent intent = new Intent(TherapistSelectionActivity.this, MainActivity.class);
        intent.putExtra("THERAPIST_TYPE", type);
        intent.putExtra("THERAPIST_EMOJI", emoji);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    
    private void animateCardSelection(View card) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(card, "scaleX", 1f, 0.95f, 1.05f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(card, "scaleY", 1f, 0.95f, 1.05f);
        scaleX.setDuration(300);
        scaleY.setDuration(300);
        scaleX.start();
        scaleY.start();
    }
}
