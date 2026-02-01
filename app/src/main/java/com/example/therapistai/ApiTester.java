package com.example.therapistai;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Simple API tester to debug Gemini API connection issues
 */
public class ApiTester {
    
    private static final String TAG = "ApiTester";
    
    /**
     * Test the Gemini API connection with a simple request
     */
    public static void testApiConnection() {
        new Thread(() -> {
            try {
                // Create simple test request
                JsonObject requestBody = createSimpleTestRequest();
                String jsonBody = new Gson().toJson(requestBody);
                
                // Create HTTP client
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
                
                // Create request
                String url = ApiConfig.GEMINI_ENDPOINT + "?key=" + ApiConfig.GEMINI_API_KEY;
                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                        .addHeader("Content-Type", "application/json")
                        .build();
                
                Log.d(TAG, "Testing API with URL: " + url);
                Log.d(TAG, "API Key Info: " + ApiConfig.getApiKeyInfo());
                Log.d(TAG, "Request body: " + jsonBody);
                
                // Execute request
                try (Response response = client.newCall(request).execute()) {
                    String responseBody = response.body().string();
                    
                    Log.d(TAG, "Response code: " + response.code());
                    Log.d(TAG, "Response body: " + responseBody);
                    
                    if (response.isSuccessful()) {
                        Log.i(TAG, "✅ API connection successful!");
                    } else {
                        Log.e(TAG, "❌ API connection failed with code: " + response.code());
                        
                        // Parse error details
                        try {
                            JsonObject errorJson = new Gson().fromJson(responseBody, JsonObject.class);
                            if (errorJson.has("error")) {
                                JsonObject error = errorJson.getAsJsonObject("error");
                                String message = error.has("message") ? error.get("message").getAsString() : "Unknown error";
                                Log.e(TAG, "Error message: " + message);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Could not parse error response", e);
                        }
                    }
                }
                
            } catch (IOException e) {
                Log.e(TAG, "Network error during API test", e);
            } catch (Exception e) {
                Log.e(TAG, "Unexpected error during API test", e);
            }
        }).start();
    }
    
    /**
     * Create a simple test request for Gemini API
     */
    private static JsonObject createSimpleTestRequest() {
        JsonObject requestBody = new JsonObject();
        
        // Create contents array
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        
        // Create parts array with simple test message
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", "Hello, can you respond with a simple greeting?");
        parts.add(part);
        
        content.add("parts", parts);
        contents.add(content);
        requestBody.add("contents", contents);
        
        // Add basic generation config
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", 0.7);
        generationConfig.addProperty("maxOutputTokens", 100);
        requestBody.add("generationConfig", generationConfig);
        
        return requestBody;
    }
}