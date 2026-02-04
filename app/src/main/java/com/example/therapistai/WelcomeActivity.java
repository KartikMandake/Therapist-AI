package com.example.therapistai;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

/**
 * WelcomeActivity - First screen users see after login
 * Displays motivational content and guides to therapist selection
 */
public class WelcomeActivity extends AppCompatActivity {
    
    private TextView tvWelcomeTitle;
    private TextView tvWelcomeSubtitle;
    private TextView tvMotivationalQuote;
    private MaterialButton btnGetStarted;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        
        initializeViews();
        setupClickListeners();
        startAnimations();
    }
    
    private void initializeViews() {
        tvWelcomeTitle = findViewById(R.id.tvWelcomeTitle);
        tvWelcomeSubtitle = findViewById(R.id.tvWelcomeSubtitle);
        tvMotivationalQuote = findViewById(R.id.tvMotivationalQuote);
        btnGetStarted = findViewById(R.id.btnGetStarted);
    }
    
    private void setupClickListeners() {
        btnGetStarted.setOnClickListener(v -> {
            animateButtonClick(v);
            navigateToTherapistSelection();
        });
    }
    
    private void startAnimations() {
        // Fade in title
        ObjectAnimator titleFade = ObjectAnimator.ofFloat(tvWelcomeTitle, "alpha", 0f, 1f);
        titleFade.setDuration(800);
        
        // Slide in subtitle
        ObjectAnimator subtitleSlide = ObjectAnimator.ofFloat(tvWelcomeSubtitle, "translationY", 50f, 0f);
        ObjectAnimator subtitleFade = ObjectAnimator.ofFloat(tvWelcomeSubtitle, "alpha", 0f, 1f);
        subtitleSlide.setDuration(800);
        subtitleFade.setDuration(800);
        
        // Fade in quote
        ObjectAnimator quoteFade = ObjectAnimator.ofFloat(tvMotivationalQuote, "alpha", 0f, 1f);
        quoteFade.setDuration(1000);
        quoteFade.setStartDelay(400);
        
        // Slide up button
        ObjectAnimator buttonSlide = ObjectAnimator.ofFloat(btnGetStarted, "translationY", 100f, 0f);
        ObjectAnimator buttonFade = ObjectAnimator.ofFloat(btnGetStarted, "alpha", 0f, 1f);
        buttonSlide.setDuration(800);
        buttonFade.setDuration(800);
        buttonSlide.setStartDelay(600);
        buttonFade.setStartDelay(600);
        
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(titleFade, subtitleSlide, subtitleFade, quoteFade, buttonSlide, buttonFade);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }
    
    private void animateButtonClick(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f, 1f);
        scaleX.setDuration(200);
        scaleY.setDuration(200);
        
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }
    
    private void navigateToTherapistSelection() {
        Intent intent = new Intent(WelcomeActivity.this, TherapistSelectionActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
