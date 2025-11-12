# LumiraAI - Offline Rural Health Companion

A fully offline, voice-first rural health companion powered by the RunAnywhere SDK. LumiraAI
provides
basic medical guidance, first-aid instructions, and health education entirely on-device, ensuring
privacy and accessibility even without internet connectivity.

## What LumiraAI Does

This is a comprehensive healthcare assistant that demonstrates:

1. **Voice-First Interface**: Speak your health questions in Hindi or English
2. **Offline Health Guidance**: Access verified medical information without internet
3. **Smart Symptom Assessment**: Match symptoms to conditions with first-aid steps
4. **Cultural Sensitivity**: Designed for Indian rural healthcare contexts
5. **Complete Privacy**: All data stays on your device

## Key Features

### Voice-First Design

- **Offline Speech Recognition** using Android's built-in capabilities
- **Multi-language Text-to-Speech** (Hindi, English, Tamil, Telugu, Marathi)
- **Real-time Audio Feedback** with animated voice indicators
- **Medical Disclaimers** automatically added to health advice

### Comprehensive Health Knowledge

- **Offline Medical Database** with 10+ verified health conditions
- **First-Aid Instructions** for common rural health scenarios
- **Symptom-to-Condition Matching** with smart keyword extraction
- **Severity Assessment** with color-coded indicators
- **Emergency Contact Information** for India (108, 104, 100)

### "RuralCare Light" Theme

- **Wellness Color Palette**: Soft sky-blue, white, and green accents
- **Large Touch Targets** optimized for rural users
- **Smooth Animations** including mic pulsing and typing indicators
- **Material Design 3** with rounded corners and wellness aesthetics

### Complete Privacy

- **100% Offline Operation** - No data leaves the device
- **Local Health Database** - All medical information stored locally
- **Encrypted Conversations** - Secure health data storage
- **No Analytics** - No tracking or data collection

## Quick Start

### 1. Setup and Build
```bash
# Clone the repository
git clone https://github.com/LumiraAI/Lumira.git
cd lumiraai

# Build the app
./gradlew assembleDebug
# OR open in Android Studio and click Run
```

### 2. First Launch

1. **Grant Permissions**: Allow microphone access when prompted
2. **Download Health Model**:
    - Tap "Models" in the header
    - Choose "Qwen 2.5 0.5B Health Assistant" (374 MB)
    - Tap "Download" and wait for completion
3. **Load Model**: Tap "Load" once download finishes
4. **Ready to Use**: See "लुमिरा तैयार है! Lumira is ready to help!"

### 3. Using Voice Interface

1. **Tap the Microphone** (large blue floating button)
2. **Speak Your Question**: "मुझे बुखार है, क्या करूं?" (I have fever, what should I do?)
3. **Listen to Response**: Lumira responds with both text and voice
4. **Follow Guidance**: Get step-by-step health advice

## Technical Architecture

### Android Components

- **Language**: Kotlin with Jetpack Compose
- **UI Framework**: Material Design 3 with custom wellness theme
- **Architecture**: MVVM with StateFlow reactive programming
- **Speech**: Android Speech Recognition & Text-to-Speech APIs
- **AI**: RunAnywhere SDK for on-device inference

### AI Pipeline

```
Voice Input → Speech-to-Text → Health Query Processing → 
Local Knowledge Search → AI Response Generation → Text-to-Speech
```

### Health Knowledge Structure

```kotlin
HealthCondition {
    symptoms: List<String>
    firstAidSteps: List<String>
    whenToSeekHelp: List<String>
    prevention: List<String>
    severity: Severity (Mild/Moderate/Severe/Emergency)
    category: HealthCategory
}
```

## Medical Content Coverage

### Common Conditions

- **Fever**: Temperature management, when to seek help
- **Cough & Cold**: Respiratory symptoms, home remedies
- **Diarrhea**: Dehydration prevention, BRAT diet
- **Minor Injuries**: Wound care, bleeding control

### Specialized Care

- **Maternal Health**: Pregnancy care, nutrition guidance
- **Child Health**: Pediatric symptoms, age-specific advice
- **Nutrition**: Local foods (Tulsi, Ginger) with health benefits

### Emergency Information

- **Emergency Numbers**: 108 (Ambulance), 104 (Health), 100 (Police)
- **Severity Indicators**: Color-coded urgency levels
- **When to Seek Help**: Clear guidelines for medical attention

## Multi-Language Support

### Primary Languages

- **Hindi**: मुख्य भाषा (Primary interface)
- **English**: Secondary interface language

### Regional Support

- **Tamil**: தமிழ் - Health keyword recognition
- **Telugu**: తెలుగు - Local terms and phrases
- **Marathi**: मराठी - Cultural context awareness

### Voice Features

- **Indian Accent Optimization** for speech recognition
- **Code-mixing Support** (Hindi-English conversations)
- **Medical Term Pronunciation** with proper text-to-speech

## App Navigation

### Bottom Navigation Tabs

1. **लुमिरा Chat** - Main health conversation interface
2. **Tips** - Daily health tips and wellness advice
3. **Models** - AI model management and downloads
4. **Emergency** - Emergency contacts and urgent care info

### Voice-First Flow

1. User taps microphone → Animated listening state
2. Speech converted to text → Health keyword extraction
3. Local database searched → Matched conditions found
4. AI generates response → Spoken aloud with disclaimer
5. Visual severity indicators → Color-coded advice level

## Configuration

### Available AI Models

| Model                          | Size    | Best For               | Speed     |
|--------------------------------|---------|------------------------|-----------|
| Qwen 2.5 0.5B Health Assistant | 374 MB  | General health queries | Medium    |
| HealthChat Small Q4            | ~120 MB | Quick responses        | Fast      |
| MedAssist Tiny Q8_0            | ~50 MB  | Basic advice           | Very Fast |

### Device Requirements

- **Android**: 7.0 (API 24) or higher
- **Storage**: 500 MB free space (with model)
- **RAM**: 3 GB recommended for smooth performance
- **Microphone**: Required for voice input

## File Structure

```
app/src/main/java/com/runanywhere/lumiraai/
├── LumiraApplication.kt          # SDK initialization with health models
├── MainActivity.kt              # Main UI with voice interface & navigation
├── data/
│   ├── HealthKnowledge.kt      # Medical database structures
│   └── HealthMessage.kt        # Message data classes
├── viewmodel/
│   └── LumiraHealthViewModel.kt # Business logic & health processing
├── speech/
│   └── SpeechManager.kt        # Voice I/O management
└── ui/theme/                   # RuralCare Light theme
    ├── Color.kt               # Wellness color palette
    ├── Theme.kt               # Material 3 theme implementation
    └── Type.kt                # Typography definitions

app/src/main/assets/
└── health_knowledge.json      # Offline medical database (10 MB)
```

## Usage Examples

### Voice Interactions

```
User: "मुझे बुखार है" (I have fever)
Lumira: "⚠️ यह चिकित्सा सलाह नहीं है...
        बुखार के लिए:
        1. ठंडी जगह आराम करें
        2. भरपूर पानी पिएं..."

User: "बच्चे को दस्त है" (Child has diarrhea)
Lumira: "बच्चों में दस्त के लिए:
        1. ORS घोल दें
        2. BRAT आहार..."
```

### Emergency Scenarios

- Immediate access to emergency numbers
- Severity-based advice escalation
- Clear "when to seek help" guidelines

## Troubleshooting

### Common Issues

#### Models Not Loading

- **Cause**: SDK initialization pending
- **Solution**: Wait 10-15 seconds, check "Models" section
- **Debug**: Look for "LumiraAI: Health AI SDK initialized" in logs

#### Voice Recognition Fails

- **Cause**: Missing microphone permission
- **Solution**: Grant RECORD_AUDIO permission in app settings
- **Alternative**: Use text input as fallback

#### Slow AI Responses

- **Expected**: On-device inference takes 5-15 seconds
- **Optimization**: Use smaller models for faster responses
- **Device**: Close other apps to free memory

#### Speech Not Working

- **Cause**: Text-to-Speech not initialized
- **Solution**: Check device TTS settings, restart app
- **Fallback**: Read text responses manually

## Extending LumiraAI

### Adding Health Conditions

1. Edit `LumiraHealthDatabase.kt`
2. Add new `HealthCondition` entries with symptoms, treatments
3. Include Hindi translations for all content
4. Test with voice input scenarios

### Supporting New Languages

1. Update `SpeechManager.supportedLanguages`
2. Add language mappings in health database
3. Update voice prompts and TTS settings
4. Create culturally appropriate responses

### Custom AI Models

1. Register models in `LumiraApplication.registerHealthModels()`
2. Focus on small, quantized models for mobile devices
3. Test inference speed and memory usage
4. Optimize for health-specific conversations

## Medical Disclaimer

**⚠️ CRITICAL DISCLAIMER: LumiraAI is NOT a substitute for professional medical advice, diagnosis,
or
treatment. This app provides general health information for educational purposes only. Always
consult qualified healthcare providers for medical emergencies, serious health conditions, or any
health concerns. In case of emergency, call 108 (Ambulance) immediately.**

## Project Goals Achieved

✅ **Fully Offline Operation** - No internet required after setup  
✅ **Voice-First Interface** - Primary interaction through speech  
✅ **Health-Focused Content** - Verified medical information  
✅ **Cultural Sensitivity** - Indian rural healthcare contexts  
✅ **Privacy-Focused** - All data stays on device  
✅ **Multi-language Support** - Hindi, English, regional languages  
✅ **Beautiful UI** - RuralCare Light wellness theme  
✅ **Emergency Ready** - Quick access to emergency services

---

**लुमिरा - आपका डिजिटल स्वास्थ्य साथी**  
*Lumira - Your Digital Health Companion*

Made with ❤️ for rural India 🇮🇳

## Support

- **Technical Issues**: Create GitHub issue with logs
- **Medical Content**: Verified by healthcare professionals
- **Community**: Join rural health tech discussions

**Version**: 1.0  
**License**: RunAnywhere SDK License  
**Last Updated**: January 2025
