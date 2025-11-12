# LumiraAI - Offline Rural Health Companion

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green?style=for-the-badge&logo=android" alt="Android">
  <img src="https://img.shields.io/badge/Language-Kotlin-blue?style=for-the-badge&logo=kotlin" alt="Kotlin">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-yellow?style=for-the-badge&logo=jetpackcompose" alt="Jetpack Compose">
  <img src="https://img.shields.io/badge/AI-RunAnywhere%20SDK-purple?style=for-the-badge" alt="RunAnywhere SDK">
</p>

A fully offline, voice-first rural health companion powered by the RunAnywhere SDK. LumiraAI
provides
basic medical guidance, first-aid instructions, and health education entirely on-device, ensuring
privacy and accessibility even without internet connectivity.

## 🌟 Key Features

### 🎤 Voice-First Interface

- **Offline Speech Recognition**: Convert voice queries to text using Android's built-in speech
  recognition
- **Text-to-Speech**: AI responses are spoken aloud in multiple languages
- **Real-time Audio Feedback**: Visual indicators for listening and speaking states
- **Multi-language Support**: Hindi, English, Tamil, Telugu, and Marathi

### 🏥 Comprehensive Health Knowledge

- **Offline Health Database**: 5-10 MB of verified medical information
- **Symptom Assessment**: Smart symptom-to-condition matching
- **First-Aid Guidance**: Step-by-step emergency instructions
- **Preventive Care**: Health tips and wellness advice
- **Maternal & Child Health**: Specialized information for women and children
- **Nutrition Guidance**: Local food recommendations and benefits

### 🤖 Intelligent AI Assistant

- **Local LLM Inference**: All AI processing happens on-device
- **Health-Focused Responses**: Trained to provide medical guidance
- **Cultural Sensitivity**: Responses tailored for Indian rural contexts
- **Medical Disclaimers**: Always includes appropriate warnings
- **Severity Assessment**: Color-coded severity indicators for health conditions

### 🎨 "RuralCare Light" Design

- **Wellness Aesthetic**: Soft sky-blue, white, and green color palette
- **Intuitive UI**: Large buttons optimized for rural users
- **Animated Interactions**: Smooth voice input animations
- **Material Design 3**: Modern, accessible interface components

### 🔒 Privacy & Security

- **100% Offline**: No data leaves the device
- **Local Storage**: All conversations and health data stored locally
- **Encrypted Data**: Health information is securely encrypted
- **No Analytics**: No tracking or data collection

## 🚀 Quick Start

### Prerequisites

- Android 7.0 (API 24) or higher
- ~500 MB free storage space
- Microphone permission for voice input

### 1. Installation

```bash
git clone https://github.com/your-repo/lumiraai
cd lumiraai
./gradlew assembleDebug
```

### 2. First Launch Setup

1. **Grant Permissions**: Allow microphone access when prompted
2. **Download AI Model**:
    - Tap "Models" button in the top bar
    - Select "Qwen 2.5 0.5B Health Assistant" (recommended)
    - Tap "Download" and wait for completion
3. **Load Model**: Tap "Load" once download finishes
4. **Start Chatting**: You'll see "लुमिरा तैयार है! Lumira is ready to help!"

### 3. Using Voice Input

1. Tap the **microphone button** (large blue FAB)
2. Speak your health question clearly
3. Wait for the transcription to appear
4. LumiraAI will respond with both text and voice

### 4. Sample Health Queries

Try asking LumiraAI about:

- "मुझे बुखार है, क्या करूं?" (I have fever, what should I do?)
- "बच्चे को खांसी है" (Child has cough)
- "पेट दर्द का इलाज" (Treatment for stomach pain)
- "डायरिया में क्या खाना चाहिए?" (What to eat during diarrhea?)

## 📱 User Interface Guide

### Main Chat Screen

- **Header**: App name, status, and voice indicators
- **Models Button**: Access to AI model management
- **Message Bubbles**: Color-coded by message type and severity
- **Voice Controls**: Large microphone and speaker buttons
- **Text Input**: Traditional typing option with send button

### Message Types & Colors

| Type | Color | Description |
|------|-------|-------------|
| User Messages | Blue | Your questions and inputs |
| Health Advice | Green Container | Medical guidance and first-aid |
| Emergency Info | Red Container | Urgent medical information |
| General Chat | Gray | General conversation |

### Severity Indicators

| Level | Color | Meaning |
|-------|-------|---------|
| हल्का • Mild | Green | Self-care possible |
| मध्यम • Moderate | Orange | May need consultation |
| गंभीर • Severe | Red | Requires medical attention |
| आपातकाल • Emergency | Dark Red | Life-threatening |

## 🏗️ Technical Architecture

### Core Components

```
LumiraApplication (Initialization)
    ↓
LumiraHealthViewModel (Business Logic)
    ↓
HealthChatScreen (UI Layer)
    ↓
SpeechManager (Voice I/O)
    ↓
LumiraHealthDatabase (Offline Knowledge)
```

### Key Technologies

- **RunAnywhere SDK**: On-device AI inference
- **Jetpack Compose**: Modern Android UI framework
- **Android Speech API**: Voice recognition and TTS
- **Material Design 3**: UI components and theming
- **Kotlin Coroutines**: Asynchronous operations

### Health Knowledge Structure

```kotlin
HealthCondition {
    symptoms: List<String>
    firstAidSteps: List<String>
    whenToSeekHelp: List<String>
    prevention: List<String>
    severity: Severity
    category: HealthCategory
}
```

## 🎯 Health Categories Covered

### 🤒 Common Conditions

- Fever and flu symptoms
- Cough and cold
- Headaches and body aches
- Digestive issues (diarrhea, nausea)
- Minor cuts and wounds
- Skin rashes and allergies

### 👩‍⚕️ Specialized Care

- **Maternal Health**: Pregnancy care, nutrition during pregnancy
- **Child Health**: Pediatric symptoms, vaccination schedules
- **Elderly Care**: Age-specific health concerns
- **Mental Health**: Stress management, basic mental wellness

### 🚨 Emergency Guidance

- First-aid procedures
- When to seek immediate medical attention
- Emergency contact numbers (108, 104, 100)
- Basic life support instructions

## 🗣️ Language Support

### Primary Languages

- **Hindi**: मुख्य भाषा (Primary interface language)
- **English**: Secondary interface language
- **Tamil**: தமிழ் (Regional support)
- **Telugu**: తెలుగు (Regional support)
- **Marathi**: मराठी (Regional support)

### Speech Recognition

- Optimized for Indian accents and pronunciation
- Handles code-mixing (Hindi-English)
- Regional language keyword recognition

## ⚙️ Configuration & Customization

### Model Selection

The app supports multiple AI models:

1. **Qwen 2.5 0.5B Health Assistant** (Recommended - 374 MB)
2. **HealthChat Small Q4** (Lightweight - 120 MB)
3. **MedAssist Tiny Q8_0** (Ultra-light - 50 MB)

### Language Settings

```kotlin
viewModel.setLanguage("hi-IN") // Hindi
viewModel.setLanguage("en-IN") // English
viewModel.setLanguage("ta-IN") // Tamil
```

### Adding Custom Health Data

1. Edit `LumiraHealthDatabase.kt`
2. Add new `HealthCondition` entries
3. Include local language translations
4. Rebuild the app

## 🛠️ Development Setup

### Dependencies

```kotlin
// Core RunAnywhere SDK
implementation(files("libs/RunAnywhereKotlinSDK-release.aar"))
implementation(files("libs/runanywhere-llm-llamacpp-release.aar"))

// Speech & Audio
implementation("androidx.activity:activity-ktx:1.8.2")

// UI Framework
implementation("androidx.compose.material:material-icons-extended:1.5.4")
implementation("androidx.navigation:navigation-compose:2.7.6")

// Data Storage
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.datastore:datastore-preferences:1.0.0")
```

### Build Configuration

```kotlin
android {
    compileSdk = 36
    minSdk = 24
    targetSdk = 36
    
    buildFeatures {
        compose = true
    }
}
```

### Required Permissions

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.SPEECH_RECOGNITION" />
```

## 🔧 Troubleshooting

### Common Issues

#### Models Not Loading

- **Solution**: Wait for SDK initialization, check internet for initial download
- **Check**: Logcat for "LumiraAI: Health AI SDK initialized successfully"

#### Voice Recognition Not Working

- **Solution**: Grant microphone permission, ensure device volume is up
- **Check**: Test with Android's built-in voice recorder

#### Slow AI Responses

- **Expected**: On-device inference takes 2-10 seconds depending on device
- **Optimization**: Use smaller models for faster responses

#### App Crashes

- **Common Cause**: Insufficient memory for model loading
- **Solution**: Close other apps, try smaller model, restart device

### Performance Optimization

1. **Memory Management**: Models are loaded lazily and can be unloaded
2. **Storage**: Clean up old conversation logs periodically
3. **Battery**: Voice recognition uses minimal battery when not active

## 🌍 Localization Guide

### Adding New Languages

1. **Update SpeechManager**:

```kotlin
private val supportedLanguages = listOf(
    "en-IN", "hi-IN", "ta-IN", "te-IN", "mr-IN", "bn-IN" // Add Bengali
)
```

2. **Add Health Translations**:

```kotlin
val localNames = mapOf(
    "hindi" to "अदरक",
    "tamil" to "இஞ்சி", 
    "bengali" to "আদা" // Add Bengali translation
)
```

3. **Update UI Strings**: Create language-specific string resources

## 📊 App Statistics

### Storage Requirements

- **Base App**: ~50 MB
- **Small Model**: +120 MB
- **Medium Model**: +374 MB
- **Health Database**: ~10 MB
- **User Data**: ~1-5 MB

### Performance Metrics

- **Cold Start**: 2-3 seconds
- **Model Loading**: 10-30 seconds (one-time)
- **Voice Recognition**: 1-2 seconds
- **AI Response**: 3-15 seconds
- **Speech Synthesis**: 1-3 seconds

## 🤝 Contributing

### Development Guidelines

1. **Code Style**: Follow Kotlin coding conventions
2. **Medical Accuracy**: All health information must be verified
3. **Cultural Sensitivity**: Consider rural Indian contexts
4. **Privacy First**: No data should leave the device
5. **Accessibility**: Support for users with disabilities

### Adding Health Content

1. Research from verified medical sources
2. Include appropriate disclaimers
3. Translate to supported languages
4. Test with rural users
5. Submit pull request with documentation

## 📄 License & Disclaimer

### Medical Disclaimer

**⚠️ IMPORTANT: LumiraAI is NOT a substitute for professional medical advice, diagnosis, or
treatment. Always consult qualified healthcare providers for medical emergencies and serious health
conditions.**

### License

This project follows the RunAnywhere SDK license terms. The health content is provided for
educational purposes only.

### Privacy Policy

- **Data Collection**: None
- **Storage**: All local on device
- **Sharing**: No data sharing with third parties
- **Analytics**: No usage tracking or analytics

## 🆘 Emergency Contacts

The app includes these emergency numbers (India):

- **Ambulance**: 108
- **Health Helpline**: 104
- **Police**: 100
- **Women Helpline**: 1091
- **Child Helpline**: 1098

## 📞 Support & Community

### Getting Help

- **GitHub Issues**: Report bugs and feature requests
- **Documentation**: Check the `/docs` folder for detailed guides
- **Community**: Join our health tech community discussions

### Contact

- **Project Lead**: Your Name (your.email@example.com)
- **Medical Advisor**: Dr. Name (doctor@example.com)
- **Technical Support**: Create GitHub issue

---

<p align="center">
  <strong>लुमिरा - आपका डिजिटल स्वास्थ्य साथी</strong><br>
  <em>Lumira - Your Digital Health Companion</em>
</p>

<p align="center">
  Made with ❤️ for rural India 🇮🇳
</p>