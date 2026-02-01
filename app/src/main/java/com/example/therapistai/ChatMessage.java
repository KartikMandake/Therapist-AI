package com.example.therapistai;

/**
 * Model class representing a chat message in the Therapist AI app
 */
public class ChatMessage {
    private String message;
    private boolean isUserMessage;
    private long timestamp;
    
    /**
     * Constructor for ChatMessage
     * @param message The text content of the message
     * @param isUserMessage True if message is from user, false if from AI
     */
    public ChatMessage(String message, boolean isUserMessage) {
        this.message = message;
        this.isUserMessage = isUserMessage;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public String getMessage() {
        return message;
    }
    
    public boolean isUserMessage() {
        return isUserMessage;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    // Setters
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setUserMessage(boolean userMessage) {
        isUserMessage = userMessage;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}