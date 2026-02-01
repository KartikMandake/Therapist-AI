# Therapist AI 🤖💙

A compassionate Android app that provides emotional support and guidance through conversational AI powered by Google Gemini API.

## Features ✨

- **Empathetic AI Conversations**: Chat with an AI therapist that provides emotional support
- **Beautiful Material Design**: Soothing pastel theme with smooth animations
- **Firebase Authentication**: Secure user authentication (Email/Password + Anonymous)
- **Real-time Chat Interface**: Scrollable chat with user and AI message bubbles
- **Thinking Indicator**: Shows "Thinking..." while AI processes responses
- **Motivational Quotes**: Gentle encouragement displayed in the app
- **Secure API Integration**: Safe handling of Gemini API requests

## Screenshots 📱

The app features a calming blue and lavender theme with:
- Clean chat interface with message bubbles
- Smooth fade-in animations for AI responses
- Rounded corners and gentle shadows
- Motivational greeting message

## Tech Stack 🛠️

- **Language**: Java
- **UI**: Material Design Components
- **Authentication**: Firebase Auth
- **AI**: Google Gemini Pro API
- **HTTP Client**: OkHttp3
- **JSON Parsing**: Gson
- **Architecture**: Single Activity with RecyclerView

## Setup Instructions 🚀

### 1. Clone the Repository
```bash
git clone <your-repo-url>
cd therapist-ai
```

### 2. Firebase Setup
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project named "Therapist AI"
3. Add Android app with package name: `com.example.therapistai`
4. Download `google-services.json` and place it in the `app/` directory
5. Enable Authentication → Email/Password and Anonymous providers

### 3. Gemini API Setup
1. Visit [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Create an API key for Gemini
3. Open `app/src/main/java/com/example/therapistai/ApiConfig.java`
4. Replace `YOUR_GEMINI_API_KEY_HERE` with your actual API key

### 4. Build and Run
```bash
./gradlew assembleDebug
```

## Project Structure 📁

```
app/src/main/
├── java/com/example/therapistai/
│   ├── MainActivity.java          # Main chat interface
│   ├── ChatAdapter.java          # RecyclerView adapter for messages
│   ├── ChatMessage.java          # Message model class
│   ├── GeminiApiClient.java      # API client for Gemini
│   └── ApiConfig.java            # API configuration and keys
├── res/
│   ├── layout/
│   │   ├── activity_main.xml     # Main activity layout
│   │   └── item_chat_message.xml # Chat message item layout
│   ├── values/
│   │   ├── colors.xml            # Soothing color palette
│   │   └── strings.xml           # App strings
│   └── drawable/
│       └── loading_background.xml # Loading indicator background
└── AndroidManifest.xml           # App permissions and config
```

## Key Dependencies 📦

```kotlin
// Firebase
implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
implementation("com.google.firebase:firebase-auth")

// HTTP & JSON
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.google.code.gson:gson:2.10.1")

// UI Components
implementation("androidx.recyclerview:recyclerview:1.3.2")
implementation("com.google.android.material:material:1.13.0")
```

## API Integration 🔌

### Gemini API Request Format
```json
{
  "contents": [{
    "parts": [{"text": "User message with therapeutic prompt"}]
  }],
  "generationConfig": {
    "temperature": 0.7,
    "topK": 40,
    "topP": 0.95,
    "maxOutputTokens": 1024
  },
  "safetySettings": [...]
}
```

### Therapeutic Prompt Engineering
The app enhances user messages with therapeutic context:
```java
String therapeuticPrompt = "You are a compassionate AI therapist assistant. " +
    "Your role is to provide emotional support, active listening, and gentle guidance. " +
    "Please respond with empathy and care. Offer comfort, validation, and helpful " +
    "coping strategies when appropriate. Keep responses warm, supportive, and " +
    "around 2-3 sentences. Do not provide medical advice. " +
    "User message: \"" + userMessage + "\"";
```

## Security Considerations 🔒

- API keys should be stored securely (not hardcoded)
- Firebase security rules should be configured for production
- User data should be encrypted
- Network traffic uses HTTPS
- Input validation prevents malicious requests

## Customization 🎨

### Colors
Edit `app/src/main/res/values/colors.xml` to change the theme:
- `primary_color`: Main accent color
- `background_color`: App background
- `user_message_background`: User message bubble color
- `ai_message_background`: AI message bubble color

### AI Personality
Modify the therapeutic prompt in `MainActivity.java` → `createTherapeuticPrompt()` to adjust the AI's response style.

## Contributing 🤝

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License 📄

This project is licensed under the MIT License - see the LICENSE file for details.

## Disclaimer ⚠️

This app is for emotional support and companionship only. It is not a substitute for professional mental health care. If you're experiencing a mental health crisis, please contact a qualified healthcare provider or emergency services.

## Support 💬

For questions or issues:
- Open an issue on GitHub
- Check the Firebase setup guide
- Verify your Gemini API key configuration

---

**Built with ❤️ for mental health and wellbeing**