package com.example.therapistai;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * ProfileActivity - Shows user profile information and account options
 */
public class ProfileActivity extends AppCompatActivity {
    
    // UI Components
    private TextView tvUserEmail, tvUserType, tvSessionCount, tvAppVersion;
    private MaterialButton btnChangePassword, btnDeleteAccount, btnBack, btnNotifications, btnAbout;
    
    // Firebase Authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        
        // Initialize UI components
        initializeViews();
        
        // Setup user information
        setupUserInfo();
        
        // Setup click listeners
        setupClickListeners();
    }
    
    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserType = findViewById(R.id.tvUserType);
        tvSessionCount = findViewById(R.id.tvSessionCount);
        tvAppVersion = findViewById(R.id.tvAppVersion);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnAbout = findViewById(R.id.btnAbout);
        btnBack = findViewById(R.id.btnBack);
    }
    
    /**
     * Setup user information display
     */
    private void setupUserInfo() {
        if (currentUser != null) {
            if (currentUser.isAnonymous()) {
                tvUserEmail.setText("Guest User");
                tvUserType.setText("Anonymous Session");
                // Hide password change for anonymous users
                btnChangePassword.setEnabled(false);
                btnChangePassword.setText("Not Available for Guests");
            } else {
                String email = currentUser.getEmail();
                tvUserEmail.setText(email != null ? email : "No email");
                tvUserType.setText("Registered User");
            }
        }
        
        // Display session count
        int sessionCount = loadSessionCount();
        tvSessionCount.setText("Sessions: " + sessionCount);
        
        // Display app version
        tvAppVersion.setText("App Version: 1.0.0");
    }
    
    /**
     * Load session count (can be enhanced with SharedPreferences or database)
     */
    private int loadSessionCount() {
        return getSharedPreferences("therapist_ai", MODE_PRIVATE)
                .getInt("session_count", 1);
    }
    
    /**
     * Setup click listeners for all buttons
     */
    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnChangePassword.setOnClickListener(v -> {
            if (currentUser != null && !currentUser.isAnonymous()) {
                changePassword();
            } else {
                Toast.makeText(this, "Password change not available for guest users", 
                        Toast.LENGTH_SHORT).show();
            }
        });
        
        btnDeleteAccount.setOnClickListener(v -> showDeleteAccountDialog());
        
        btnNotifications.setOnClickListener(v -> showNotificationsSettings());
        
        btnAbout.setOnClickListener(v -> showAboutDialog());
    }
    
    /**
     * Send password reset email
     */
    private void changePassword() {
        if (currentUser != null && currentUser.getEmail() != null) {
            firebaseAuth.sendPasswordResetEmail(currentUser.getEmail())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, 
                                    "Password reset email sent to " + currentUser.getEmail(), 
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, 
                                    "Failed to send password reset email", 
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    
    /**
     * Show confirmation dialog for account deletion
     */
    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> deleteAccount())
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    
    /**
     * Delete user account
     */
    private void deleteAccount() {
        if (currentUser != null) {
            currentUser.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, 
                                    "Account deleted successfully", Toast.LENGTH_SHORT).show();
                            
                            // Return to login screen
                            firebaseAuth.signOut();
                            finish();
                        } else {
                            Toast.makeText(ProfileActivity.this, 
                                    "Failed to delete account: " + 
                                    (task.getException() != null ? task.getException().getMessage() : "Unknown error"), 
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
    
    /**
     * Show notifications settings dialog
     */
    private void showNotificationsSettings() {
        new AlertDialog.Builder(this)
                .setTitle("Notifications Settings")
                .setMessage("Notifications are currently enabled. You can customize notification preferences here.")
                .setPositiveButton("OK", null)
                .show();
    }
    
    /**
     * Show about dialog with app information
     */
    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About Therapist AI")
                .setMessage("Therapist AI v1.0.0\n\n" +
                        "A compassionate AI companion app providing emotional support through conversational AI. " +
                        "Powered by Google Gemini API.\n\n" +
                        "© 2026 Therapist AI. All rights reserved.\n\n" +
                        "Always remember: This app is not a substitute for professional mental health services. " +
                        "If you're experiencing a mental health crisis, please contact a mental health professional.")
                .setPositiveButton("OK", null)
                .show();
    }
}