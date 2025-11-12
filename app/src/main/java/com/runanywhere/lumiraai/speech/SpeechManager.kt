package com.runanywhere.lumiraai.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

/**
 * Manages Speech-to-Text and Text-to-Speech for LumiraAI
 * Provides offline speech capabilities when available
 */
class SpeechManager(private val context: Context) {

    // Speech Recognition
    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null

    // State flows for UI updates
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking

    private val _speechRecognitionResult = MutableStateFlow<String?>(null)
    val speechRecognitionResult: StateFlow<String?> = _speechRecognitionResult

    private val _speechError = MutableStateFlow<String?>(null)
    val speechError: StateFlow<String?> = _speechError

    private val _ttsInitialized = MutableStateFlow(false)
    val ttsInitialized: StateFlow<Boolean> = _ttsInitialized

    // Supported languages for health conversations
    private val supportedLanguages = listOf(
        "en-IN", // English (India)
        "hi-IN", // Hindi
        "ta-IN", // Tamil
        "te-IN", // Telugu
        "mr-IN"  // Marathi
    )

    init {
        initializeSpeechRecognizer()
        initializeTextToSpeech()
    }

    private fun initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    _isListening.value = true
                    _speechError.value = null
                }

                override fun onBeginningOfSpeech() {
                    // User started speaking
                }

                override fun onRmsChanged(rmsdB: Float) {
                    // Volume level changed - could be used for mic visualization
                }

                override fun onBufferReceived(buffer: ByteArray?) {
                    // Audio buffer received
                }

                override fun onEndOfSpeech() {
                    _isListening.value = false
                }

                override fun onError(error: Int) {
                    _isListening.value = false
                    _speechError.value = when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                        SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                        SpeechRecognizer.ERROR_NETWORK -> "Network error"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                        SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized"
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
                        SpeechRecognizer.ERROR_SERVER -> "Server error"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                        else -> "Speech recognition error"
                    }
                }

                override fun onResults(results: Bundle?) {
                    _isListening.value = false
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (matches != null && matches.isNotEmpty()) {
                        _speechRecognitionResult.value = matches[0]
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    // Could be used for real-time transcription display
                }

                override fun onEvent(eventType: Int, params: Bundle?) {
                    // Recognition event
                }
            })
        }
    }

    private fun initializeTextToSpeech() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale("en", "IN") // Default to English (India)
                _ttsInitialized.value = true

                // Set up progress listener
                textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isSpeaking.value = true
                    }

                    override fun onDone(utteranceId: String?) {
                        _isSpeaking.value = false
                    }

                    override fun onError(utteranceId: String?) {
                        _isSpeaking.value = false
                    }
                })
            } else {
                _speechError.value = "Text-to-speech initialization failed"
            }
        }
    }

    /**
     * Start listening for speech input
     */
    fun startListening(language: String = "en-IN") {
        if (speechRecognizer == null) {
            _speechError.value = "Speech recognition not available"
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)

            // Try to use offline recognition if available
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)

            // Health-specific prompts
            putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "लुमिरा आपकी बात सुन रहा है... / Lumira is listening..."
            )
        }

        _speechRecognitionResult.value = null
        speechRecognizer?.startListening(intent)
    }

    /**
     * Stop listening for speech input
     */
    fun stopListening() {
        speechRecognizer?.stopListening()
        _isListening.value = false
    }

    /**
     * Speak the given text
     */
    fun speak(text: String, language: String = "en-IN") {
        if (textToSpeech == null || !_ttsInitialized.value) {
            _speechError.value = "Text-to-speech not ready"
            return
        }

        // Set language if different from current
        val locale = when (language) {
            "hi-IN" -> Locale("hi", "IN")
            "ta-IN" -> Locale("ta", "IN")
            "te-IN" -> Locale("te", "IN")
            "mr-IN" -> Locale("mr", "IN")
            else -> Locale("en", "IN")
        }

        textToSpeech?.language = locale

        // Add health disclaimer prefix for medical advice
        val fullText =
            if (text.contains("fever") || text.contains("symptoms") || text.contains("medicine")) {
                "यह चिकित्सा सलाह नहीं है। गंभीर स्थिति में डॉक्टर से मिलें। $text"
            } else {
                text
            }

        val utteranceId = "lumira_speech_${System.currentTimeMillis()}"
        textToSpeech?.speak(fullText, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    /**
     * Stop current speech
     */
    fun stopSpeaking() {
        textToSpeech?.stop()
        _isSpeaking.value = false
    }

    /**
     * Check if speech recognition is available
     */
    fun isSpeechRecognitionAvailable(): Boolean {
        return speechRecognizer != null
    }

    /**
     * Check if text-to-speech is ready
     */
    fun isTextToSpeechReady(): Boolean {
        return _ttsInitialized.value
    }

    /**
     * Get available languages for speech recognition
     */
    fun getSupportedLanguages(): List<String> {
        return supportedLanguages
    }

    /**
     * Clear speech recognition result
     */
    fun clearResult() {
        _speechRecognitionResult.value = null
    }

    /**
     * Clear error messages
     */
    fun clearError() {
        _speechError.value = null
    }

    /**
     * Release resources
     */
    fun release() {
        speechRecognizer?.destroy()
        textToSpeech?.shutdown()
        speechRecognizer = null
        textToSpeech = null
    }
}

/**
 * Helper class for managing speech recognition permissions
 */
object SpeechPermissions {
    const val RECORD_AUDIO = android.Manifest.permission.RECORD_AUDIO

    fun getRequiredPermissions(): Array<String> {
        return arrayOf(RECORD_AUDIO)
    }
}

/**
 * Extension functions for health-specific speech
 */
object HealthSpeechUtils {

    /**
     * Convert health terms to more speech-friendly versions
     */
    fun preprocessHealthText(text: String): String {
        return text
            .replace("°C", " degrees Celsius")
            .replace("°F", " degrees Fahrenheit")
            .replace("mg", " milligrams")
            .replace("ml", " milliliters")
            .replace("ORS", "Oral Rehydration Solution")
            .replace("IV", "Intravenous")
            .replace("B.P.", "Blood Pressure")
            .replace("HR", "Heart Rate")
    }

    /**
     * Extract health keywords from speech input
     */
    fun extractHealthKeywords(text: String): List<String> {
        val healthKeywords = listOf(
            "fever", "बुखार", "காய்ச்சல்", "జ్వరం",
            "cough", "खांसी", "இருமல்", "దగ్గు",
            "headache", "सिर दर्द", "தலைவலி", "తలనొప్పి",
            "stomach", "पेट", "வயிறு", "కడుపు",
            "pain", "दर्द", "வலி", "నొప్పి",
            "vomiting", "उल्टी", "வாந்தி", "వాంతులు",
            "diarrhea", "दस्त", "வயிற்றுப்போக்கு", "అతిసారం"
        )

        return healthKeywords.filter { keyword ->
            text.lowercase().contains(keyword.lowercase())
        }
    }
}