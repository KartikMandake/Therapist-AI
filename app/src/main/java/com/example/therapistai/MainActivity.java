package com.example.therapistai;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity for Therapist AI - A compassionate AI companion app
 * Provides emotional support through conversational AI powered by Google Gemini
 */
public class MainActivity extends AppCompatActivity {
    
    // UI Components
    private TextInputEditText etUserMessage;
    private MaterialButton btnSendMessage;
    private MaterialButton btnProfile;
    private RecyclerView rvChatMessages;
    private LinearLayout llLoadingIndicator;
    
    // Chat functionality
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private GeminiApiClient geminiApiClient;
    
    // Firebase Authentication
    private FirebaseAuth firebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        
        // Check if user is signed in, if not redirect to login
        checkUserAuthentication();
        
        // Initialize UI components
        initializeViews();
        
        // Setup chat functionality
        setupChat();
        
        // Initialize Gemini API client
        geminiApiClient = new GeminiApiClient();
        
        // Setup click listeners
        setupClickListeners();
        
        // Add welcome message
        addWelcomeMessage();
    }
    
    /**
     * Check if user is authenticated, redirect to login if not
     */
    private void checkUserAuthentication() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not authenticated, redirect to login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        
        // User is authenticated, show welcome message based on auth type
        String welcomeMessage;
        if (currentUser.isAnonymous()) {
            welcomeMessage = "Welcome, Guest! 💙";
        } else {
            String displayName = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            if (displayName != null && !displayName.isEmpty()) {
                welcomeMessage = "Welcome back, " + displayName + "! 💙";
            } else if (email != null) {
                welcomeMessage = "Welcome back! 💙";
            } else {
                welcomeMessage = "Welcome! 💙";
            }
        }
        Toast.makeText(this, welcomeMessage, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        etUserMessage = findViewById(R.id.etUserMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        btnProfile = findViewById(R.id.btnProfile);
        rvChatMessages = findViewById(R.id.rvChatMessages);
        llLoadingIndicator = findViewById(R.id.llLoadingIndicator);
    }
    
    /**
     * Setup chat RecyclerView and adapter
     */
    private void setupChat() {
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Start from bottom
        rvChatMessages.setLayoutManager(layoutManager);
        rvChatMessages.setAdapter(chatAdapter);
    }
    
    /**
     * Setup click listeners for interactive elements
     */
    private void setupClickListeners() {
        btnSendMessage.setOnClickListener(v -> sendMessage());
        btnProfile.setOnClickListener(v -> openProfile());
        
        // Allow sending message with Enter key
        etUserMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }
    
    /**
     * Add a welcome message when the app starts
     */
    private void addWelcomeMessage() {
        String welcomeText = "Hello! I'm here to listen and support you. " +
                "Feel free to share what's on your mind - whether you're feeling stressed, " +
                "anxious, happy, or anything in between. How are you doing today? 💙";
        
        ChatMessage welcomeMessage = new ChatMessage(welcomeText, false);
        chatMessages.add(welcomeMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        scrollToBottom();
    }
    
    /**
     * Send user message and get AI response
     */
    private void sendMessage() {
        String userInput = etUserMessage.getText().toString().trim();
        
        if (TextUtils.isEmpty(userInput)) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Add user message to chat
        ChatMessage userMessage = new ChatMessage(userInput, true);
        chatMessages.add(userMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        scrollToBottom();
        
        // Clear input field
        etUserMessage.setText("");
        
        // Show loading indicator
        showLoadingIndicator(true);
        
        // Get AI response
        getAiResponse(userInput);
    }
    
    /**
     * Get AI response from Gemini API
     */
    private void getAiResponse(String userMessage) {
        // Create therapeutic prompt for Gemini
        String therapeuticPrompt = createTherapeuticPrompt(userMessage);
        
        geminiApiClient.sendMessage(therapeuticPrompt, new GeminiApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    hideLoadingIndicator();
                    addAiResponse(response);
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    hideLoadingIndicator();
                    
                    String errorResponse = "I'm sorry, I'm having trouble connecting right now. " +
                            "Please remember that you're not alone, and it's okay to take things one step at a time. " +
                            "Would you like to try again? 💙";
                    
                    addAiResponse(errorResponse);
                });
            }
        });
    }
    
    /**
     * Add AI response to chat with animation
     */
    private void addAiResponse(String response) {
        ChatMessage aiMessage = new ChatMessage(response, false);
        chatMessages.add(aiMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        scrollToBottom();
        animateNewMessage();
    }
    

    
    /**
     * Create a therapeutic prompt for the AI
     */
    private String createTherapeuticPrompt(String userMessage) {
        return "You are a compassionate AI therapist assistant. Your role is to provide emotional support, " +
                "active listening, and gentle guidance. Please respond to the following message with empathy " +
                "and care. Offer comfort, validation, and helpful coping strategies when appropriate. " +
                "Keep responses warm, supportive, and around 2-3 sentences. Do not provide medical advice. " +
                "User message: \"" + userMessage + "\"";
    }
    
    /**
     * Show or hide loading indicator
     */
    private void showLoadingIndicator(boolean show) {
        llLoadingIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    
    /**
     * Hide loading indicator
     */
    private void hideLoadingIndicator() {
        showLoadingIndicator(false);
    }
    
    /**
     * Scroll chat to bottom
     */
    private void scrollToBottom() {
        if (chatMessages.size() > 0) {
            rvChatMessages.smoothScrollToPosition(chatMessages.size() - 1);
        }
    }
    
    /**
     * Animate new message with fade-in effect
     */
    private void animateNewMessage() {
        if (chatMessages.size() > 0) {
            View lastMessageView = rvChatMessages.getLayoutManager()
                    .findViewByPosition(chatMessages.size() - 1);
            
            if (lastMessageView != null) {
                lastMessageView.setAlpha(0f);
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(lastMessageView, "alpha", 0f, 1f);
                fadeIn.setDuration(500);
                fadeIn.setInterpolator(new DecelerateInterpolator());
                fadeIn.start();
            }
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_profile) {
            openProfile();
            return true;
        } else if (id == R.id.action_clear_chat) {
            clearChat();
            return true;
        } else if (id == R.id.action_logout) {
            logoutUser();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Open user profile activity
     */
    private void openProfile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
    
    /**
     * Logout current user and return to login screen
     */
    private void logoutUser() {
        firebaseAuth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    /**
     * Clear all chat messages
     */
    private void clearChat() {
        chatMessages.clear();
        chatAdapter.notifyDataSetChanged();
        addWelcomeMessage();
        Toast.makeText(this, "Chat cleared", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources if needed
        if (geminiApiClient != null) {
            geminiApiClient.cleanup();
        }
    }
}