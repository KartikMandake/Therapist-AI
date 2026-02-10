package com.example.therapistai;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * LoginActivity - Handles user authentication for Therapist AI
 * Provides email/password login, registration, and guest access
 */
public class LoginActivity extends AppCompatActivity {
    
    // UI Components
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin, btnRegister, btnGuestAccess;
    private ProgressBar progressBar;
    //commit
    // Firebase Authentication
    private FirebaseAuth firebaseAuth;
    //just a login push
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        
        // Check if user is already signed in
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, go to welcome activity
            navigateToMainActivity();
            return;
        }
        
        // Initialize UI components
        initializeViews();
        
        // Setup click listeners
        setupClickListeners();
    }
    
    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnGuestAccess = findViewById(R.id.btnGuestAccess);
        progressBar = findViewById(R.id.progressBar);
    }
    
    /**
     * Setup click listeners for all buttons
     */
    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> registerUser());
        btnGuestAccess.setOnClickListener(v -> signInAsGuest());
    }
    
    /**
     * Login existing user with email and password
     */
    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        //comment
        // Validate input
        if (!validateInput(email, password)) {
            return;
        }
        //hellohellllohellllllo
        // Show loading
        showLoading(true);
        
        // Sign in with Firebase
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showLoading(false);
                    
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, 
                                "Welcome back! 💙", Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    } else {
                        // Sign in failed
                        String errorMessage = task.getException() != null ? 
                                task.getException().getMessage() : "Login failed";
                        Toast.makeText(LoginActivity.this, 
                                "Login failed: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
    
    /**
     * Register new user with email and password
     */
    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // Validate input
        if (!validateInput(email, password)) {
            return;
        }
        
        // Additional validation for registration
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Show loading
        showLoading(true);
        
        // Create user with Firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showLoading(false);
                    
                    if (task.isSuccessful()) {
                        // Registration success
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, 
                                "Account created successfully! Welcome to Therapist AI 💙", 
                                Toast.LENGTH_LONG).show();
                        navigateToMainActivity();
                    } else {
                        // Registration failed
                        String errorMessage = task.getException() != null ? 
                                task.getException().getMessage() : "Registration failed";
                        Toast.makeText(LoginActivity.this, 
                                "Registration failed: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
    
    /**
     * Sign in as guest (anonymous authentication)
     */
    private void signInAsGuest() {
        showLoading(true);
        
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    showLoading(false);
                    
                    if (task.isSuccessful()) {
                        // Anonymous sign in success
                        Toast.makeText(LoginActivity.this, 
                                "Welcome! You're using guest mode 💙", Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    } else {
                        // Anonymous sign in failed
                        String errorMessage = task.getException() != null ? 
                                task.getException().getMessage() : "Guest access failed";
                        Toast.makeText(LoginActivity.this, 
                                "Guest access failed: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
    
    /**
     * Validate email and password input
     */
    private boolean validateInput(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return false;
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return false;
        }
        
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Show or hide loading indicator
     */
    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
        btnRegister.setEnabled(!show);
        btnGuestAccess.setEnabled(!show);
    }
    
    /**
     * Navigate to WelcomeActivity after successful authentication
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}