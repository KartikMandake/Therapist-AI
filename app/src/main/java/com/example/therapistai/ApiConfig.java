package com.example.therapistai;

/**
 * Configuration class for API keys and endpoints
 * 
 * SECURITY NOTE: In production, store API keys securely using:
 * 1. BuildConfig with gradle.properties
 * 2. Android Keystore
 * 3. Remote configuration
 * 
 * Never commit actual API keys to version control!
 */
public class ApiConfig {
    
    // Gemini API key for Therapist AI
    // Get your key from: https://makersuite.google.com/app/apikey
    public static final String GEMINI_API_KEY = "AIzaSyBuej1E5sQQLvGgcnXOsG630yXMLfjdlWo";
    
    // Gemini API endpoints - Updated to v1 (stable)
    public static final String GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1/models/";
    public static final String GEMINI_MODEL = "gemini-2.5-flash";  // Current stable model
    public static final String GEMINI_ENDPOINT = GEMINI_BASE_URL + GEMINI_MODEL + ":generateContent";
    
    // API configuration
    public static final int CONNECTION_TIMEOUT = 30; // seconds
    public static final int READ_TIMEOUT = 30; // seconds
    public static final int WRITE_TIMEOUT = 30; // seconds
    
    // Response configuration
    public static final double TEMPERATURE = 0.7; // Creativity level (0.0 - 1.0)
    public static final int TOP_K = 40; // Token selection diversity
    public static final double TOP_P = 0.95; // Nucleus sampling
    public static final int MAX_OUTPUT_TOKENS = 1024; // Maximum response length
    
    /**
     * Check if API key is configured
     * @return true if API key is set, false otherwise
     */
    public static boolean isApiKeyConfigured() {
        return !GEMINI_API_KEY.equals("YOUR_GEMINI_API_KEY_HERE") && 
               !GEMINI_API_KEY.isEmpty() &&
               GEMINI_API_KEY.length() > 30; // Basic length check
    }
    
    /**
     * Get API key info for debugging (masked for security)
     * @return Masked API key info
     */
    public static String getApiKeyInfo() {
        if (GEMINI_API_KEY.length() < 10) {
            return "Key too short: " + GEMINI_API_KEY.length() + " characters";
        }
        return "Key: " + GEMINI_API_KEY.substring(0, 8) + "..." + 
               GEMINI_API_KEY.substring(GEMINI_API_KEY.length() - 4) + 
               " (Length: " + GEMINI_API_KEY.length() + ")";
    }
}