package com.example.therapistai;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Client class for interacting with Google Gemini API
 * Handles API requests and responses for the Therapist AI app
 */
public class GeminiApiClient {
    
    private static final String TAG = "GeminiApiClient";
    

    
    private OkHttpClient httpClient;
    private Gson gson;
    
    /**
     * Interface for API callback responses
     */
    public interface ApiCallback {
        void onSuccess(String response);
        void onError(String error);
    }
    
    /**
     * Constructor - Initialize HTTP client and JSON parser
     */
    public GeminiApiClient() {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(ApiConfig.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ApiConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
        
        gson = new Gson();
    }
    
    /**
     * Send message to Gemini API and get response
     * @param message The user's message to send to the AI
     * @param callback Callback to handle success/error responses
     */
    public void sendMessage(String message, ApiCallback callback) {
        // Validate API key
        if (!ApiConfig.isApiKeyConfigured()) {
            callback.onError("Please configure your Gemini API key in ApiConfig.java");
            return;
        }
        
        try {
            // Create request body for Gemini API
            JsonObject requestBody = createRequestBody(message);
            String jsonBody = gson.toJson(requestBody);
            
            // Log request details for debugging
            String requestUrl = ApiConfig.GEMINI_ENDPOINT + "?key=" + ApiConfig.GEMINI_API_KEY;
            Log.d(TAG, "Request URL: " + requestUrl);
            Log.d(TAG, "Request body: " + jsonBody);
            
            // Create HTTP request
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                    .addHeader("Content-Type", "application/json")
                    .build();
            
            // Execute request asynchronously
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "API request failed", e);
                    callback.onError("Network error: " + e.getMessage());
                }
                
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseBody = response.body().string();
                        Log.d(TAG, "Response code: " + response.code());
                        Log.d(TAG, "Response body: " + responseBody);
                        
                        if (response.isSuccessful()) {
                            String aiResponse = parseGeminiResponse(responseBody);
                            callback.onSuccess(aiResponse);
                        } else {
                            Log.e(TAG, "API request unsuccessful: " + response.code() + " - " + responseBody);
                            
                            // Provide more specific error messages
                            String errorMessage;
                            switch (response.code()) {
                                case 400:
                                    errorMessage = "Invalid request. Please check API configuration.";
                                    break;
                                case 401:
                                    errorMessage = "Invalid API key. Please check your Gemini API key.";
                                    break;
                                case 403:
                                    errorMessage = "API access forbidden. Check your API key permissions.";
                                    break;
                                case 429:
                                    errorMessage = "Too many requests. Please try again later.";
                                    break;
                                case 500:
                                    errorMessage = "Server error. Please try again later.";
                                    break;
                                default:
                                    errorMessage = "API error: " + response.code();
                            }
                            callback.onError(errorMessage);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response", e);
                        callback.onError("Error parsing response: " + e.getMessage());
                    } finally {
                        response.close();
                    }
                }
            });
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    /**
     * Create request body for Gemini API
     * @param message The user's message
     * @return JsonObject formatted for Gemini API
     */
    private JsonObject createRequestBody(String message) {
        JsonObject requestBody = new JsonObject();
        
        // Create contents array
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        
        // Create parts array
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", message);
        parts.add(part);
        
        content.add("parts", parts);
        contents.add(content);
        
        requestBody.add("contents", contents);
        
        // Simplified generation config
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", 0.7);
        generationConfig.addProperty("maxOutputTokens", 1024);
        
        requestBody.add("generationConfig", generationConfig);
        
        return requestBody;
    }
    
    /**
     * Parse response from Gemini API
     * @param responseBody Raw JSON response from API
     * @return Extracted text response
     */
    private String parseGeminiResponse(String responseBody) {
        try {
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            
            if (jsonResponse.has("candidates")) {
                JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
                if (candidates.size() > 0) {
                    JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                    if (firstCandidate.has("content")) {
                        JsonObject content = firstCandidate.getAsJsonObject("content");
                        if (content.has("parts")) {
                            JsonArray parts = content.getAsJsonArray("parts");
                            if (parts.size() > 0) {
                                JsonObject firstPart = parts.get(0).getAsJsonObject();
                                if (firstPart.has("text")) {
                                    return firstPart.get("text").getAsString();
                                }
                            }
                        }
                    }
                }
            }
            
            // Fallback response if parsing fails
            return "I'm here to listen and support you. Could you tell me more about how you're feeling?";
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing Gemini response", e);
            return "I'm having trouble understanding right now, but I want you to know that your feelings are valid and important.";
        }
    }
    
    /**
     * Clean up resources
     */
    public void cleanup() {
        if (httpClient != null) {
            httpClient.dispatcher().executorService().shutdown();
        }
    }
}