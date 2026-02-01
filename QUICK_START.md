# Quick Start Guide - Therapist AI 🚀

## Essential Setup (5 minutes)

### 1. Firebase Configuration
```bash
# Download google-services.json from Firebase Console
# Place it in: app/google-services.json
```

### 2. API Key Setup
Edit `app/src/main/java/com/example/therapistai/ApiConfig.java`:
```java
public static final String GEMINI_API_KEY = "your_actual_api_key_here";
```

### 3. Build & Run
```bash
./gradlew assembleDebug
# or use Android Studio's Run button
```

## Testing Without API Key

To test the app without setting up Gemini API, modify `MainActivity.java`:

Replace this line in `onCreate()`:
```java
geminiApiClient = new GeminiApiClient();
```

With:
```java
// Use mock client for testing
mockGeminiClient = new MockGeminiClient();
```

And update the `getAiResponse()` method to use:
```java
mockGeminiClient.sendMessage(therapeuticPrompt, new GeminiApiClient.ApiCallback() {
    // ... same callback code
});
```

## Key Features Implemented ✅

- ✅ Beautiful Material Design UI with pastel colors
- ✅ Firebase Authentication (Anonymous sign-in)
- ✅ Chat interface with RecyclerView
- ✅ Gemini API integration with OkHttp
- ✅ Loading indicator ("Thinking...")
- ✅ Smooth fade-in animations
- ✅ Therapeutic prompt engineering
- ✅ Error handling and fallback responses
- ✅ Motivational greeting message
- ✅ Internet permissions configured
- ✅ Clean, commented Java code

## File Summary

| File | Purpose |
|------|---------|
| `MainActivity.java` | Main chat interface and app logic |
| `ChatAdapter.java` | RecyclerView adapter for chat messages |
| `ChatMessage.java` | Data model for chat messages |
| `GeminiApiClient.java` | HTTP client for Gemini API calls |
| `ApiConfig.java` | Configuration constants and API key |
| `MockGeminiClient.java` | Mock client for testing without API |
| `activity_main.xml` | Main UI layout with Material Design |
| `item_chat_message.xml` | Chat bubble layout |
| `colors.xml` | Soothing pastel color theme |

## Next Steps

1. **Get API Key**: Visit [Google AI Studio](https://makersuite.google.com/app/apikey)
2. **Setup Firebase**: Follow `FIREBASE_SETUP.md` for detailed instructions
3. **Customize**: Modify colors, prompts, or UI as needed
4. **Test**: Run the app and start chatting with your AI therapist!

## Troubleshooting

- **Build errors**: Ensure `google-services.json` is in `app/` directory
- **API errors**: Check your Gemini API key in `ApiConfig.java`
- **Network issues**: Verify internet permissions in `AndroidManifest.xml`
- **Firebase issues**: Ensure Firebase project is properly configured

Happy coding! 💙