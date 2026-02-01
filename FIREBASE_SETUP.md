# Firebase Setup Instructions for Therapist AI

## Step 1: Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Create a project" or "Add project"
3. Enter project name: "Therapist AI" 
4. Enable Google Analytics (optional)
5. Click "Create project"

## Step 2: Add Android App to Firebase
1. In Firebase console, click "Add app" and select Android
2. Enter package name: `com.example.therapistai`
3. Enter app nickname: "Therapist AI"
4. Click "Register app"

## Step 3: Download Configuration File
1. Download the `google-services.json` file
2. Place it in the `app/` directory of your Android project
3. **IMPORTANT**: The file should be at `app/google-services.json`

## Step 4: Enable Authentication
1. In Firebase console, go to "Authentication" 
2. Click "Get started"
3. Go to "Sign-in method" tab
4. Enable "Email/Password" provider
5. Enable "Anonymous" provider (for demo purposes)

## Step 5: Enable Firestore (Optional)
1. In Firebase console, go to "Firestore Database"
2. Click "Create database"
3. Choose "Start in test mode" for development
4. Select a location for your database

## Step 6: Get Gemini API Key
1. Go to [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Click "Create API Key"
3. Copy the generated API key
4. Replace `YOUR_GEMINI_API_KEY_HERE` in `GeminiApiClient.java` with your actual key

## Security Note
For production apps:
- Store API keys securely using BuildConfig or Android Keystore
- Implement proper user authentication
- Set up Firestore security rules
- Enable ProGuard/R8 code obfuscation

## File Structure After Setup
```
app/
├── google-services.json  ← Place Firebase config here
├── src/main/java/com/example/therapistai/
│   ├── MainActivity.java
│   ├── ChatAdapter.java
│   ├── ChatMessage.java
│   └── GeminiApiClient.java  ← Update API key here
└── ...
```