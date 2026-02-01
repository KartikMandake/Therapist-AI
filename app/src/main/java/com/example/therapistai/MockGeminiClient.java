package com.example.therapistai;

import android.os.Handler;
import android.os.Looper;

import java.util.Random;

/**
 * Mock client for testing Therapist AI without actual Gemini API
 * Use this for development and testing purposes
 */
public class MockGeminiClient {
    
    private static final String[] MOCK_RESPONSES = {
        "I hear you, and I want you to know that your feelings are completely valid. It's okay to feel this way, and you're not alone in this experience. 💙",
        
        "Thank you for sharing that with me. It takes courage to open up about how you're feeling. Remember that it's okay to take things one step at a time. 🌟",
        
        "I can sense that you're going through something difficult right now. Your emotions are important, and it's healthy to acknowledge them. What would feel most supportive for you right now?",
        
        "It sounds like you're carrying a lot on your shoulders. Please remember to be gentle with yourself - you're doing the best you can with what you have. 💚",
        
        "I'm glad you felt comfortable sharing this with me. Sometimes just expressing our thoughts and feelings can be the first step toward feeling better. How are you taking care of yourself today?",
        
        "Your feelings matter, and so do you. It's completely normal to have ups and downs - that's part of being human. Is there something small you could do today that might bring you a moment of peace?",
        
        "I can hear the strength in your words, even if you might not feel strong right now. You've made it through difficult times before, and you have that resilience within you. 🌈",
        
        "Thank you for trusting me with your thoughts. Remember that healing isn't linear, and it's okay to have difficult days. What's one thing you're grateful for today, even if it's small?"
    };
    
    private Random random;
    private Handler handler;
    
    public MockGeminiClient() {
        random = new Random();
        handler = new Handler(Looper.getMainLooper());
    }
    
    /**
     * Simulate API call with mock response
     * @param message User's message (not used in mock)
     * @param callback Callback to return mock response
     */
    public void sendMessage(String message, GeminiApiClient.ApiCallback callback) {
        // Simulate network delay (1-3 seconds)
        int delay = 1000 + random.nextInt(2000);
        
        handler.postDelayed(() -> {
            // Randomly select a therapeutic response
            String response = MOCK_RESPONSES[random.nextInt(MOCK_RESPONSES.length)];
            callback.onSuccess(response);
        }, delay);
    }
    
    /**
     * Clean up resources (no-op for mock)
     */
    public void cleanup() {
        // Nothing to clean up for mock client
    }
}