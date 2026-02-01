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
    private TextView tvUserEmail, tvUserType;
    private MaterialButton btnChangePassword, btnDeleteAccount, btnBack;
    
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
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
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
}