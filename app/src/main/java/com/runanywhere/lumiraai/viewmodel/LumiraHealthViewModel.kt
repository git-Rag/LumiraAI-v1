package com.runanywhere.lumiraai.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runanywhere.lumiraai.data.HealthCondition
import com.runanywhere.lumiraai.data.HealthMessage
import com.runanywhere.lumiraai.data.HealthTip
import com.runanywhere.lumiraai.data.MessageType
import com.runanywhere.lumiraai.data.LumiraHealthDatabase
import com.runanywhere.lumiraai.speech.HealthSpeechUtils
import com.runanywhere.lumiraai.speech.SpeechManager
import com.runanywhere.sdk.public.RunAnywhere
import com.runanywhere.sdk.public.extensions.listAvailableModels
import com.runanywhere.sdk.models.ModelInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Health Conversation ViewModel
class LumiraHealthViewModel(private val context: Context) : ViewModel() {

    // Speech Manager
    private val speechManager = SpeechManager(context)

    // Chat state
    private val _messages = MutableStateFlow<List<HealthMessage>>(emptyList())
    val messages: StateFlow<List<HealthMessage>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Model management
    private val _availableModels = MutableStateFlow<List<ModelInfo>>(emptyList())
    val availableModels: StateFlow<List<ModelInfo>> = _availableModels

    private val _downloadProgress = MutableStateFlow<Float?>(null)
    val downloadProgress: StateFlow<Float?> = _downloadProgress

    private val _currentModelId = MutableStateFlow<String?>(null)
    val currentModelId: StateFlow<String?> = _currentModelId

    private val _statusMessage = MutableStateFlow<String>("Initializing LumiraAI...")
    val statusMessage: StateFlow<String> = _statusMessage

    // Speech state
    val isListening = speechManager.isListening
    val isSpeaking = speechManager.isSpeaking
    val speechError = speechManager.speechError
    val ttsInitialized = speechManager.ttsInitialized

    // Health-specific state
    private val _dailyHealthTip = MutableStateFlow<HealthTip?>(null)
    val dailyHealthTip: StateFlow<HealthTip?> = _dailyHealthTip

    private val _currentLanguage = MutableStateFlow("en-IN")
    val currentLanguage: StateFlow<String> = _currentLanguage

    private val _isVoiceModeActive = MutableStateFlow(false)
    val isVoiceModeActive: StateFlow<Boolean> = _isVoiceModeActive

    init {
        loadAvailableModels()
        loadDailyHealthTip()
        observeSpeechRecognition()
        
        // Add welcome message
        addWelcomeMessage()
    }

    private fun addWelcomeMessage() {
        val welcomeMessage = HealthMessage(
            text = "नमस्ते! मैं लुमिरा हूं, आपका स्वास्थ्य सहायक। मैं आपकी स्वास्थ्य संबंधी समस्याओं में मदद कर सकता हूं। बोलकर या टाइप करके अपनी बात कहें।\n\nHello! I'm Lumira, your health companion. I can help with basic health questions and first aid guidance. Please speak or type your health concerns.",
            isUser = false,
            messageType = MessageType.TEXT
        )
        _messages.value = listOf(welcomeMessage)
    }

    private fun observeSpeechRecognition() {
        viewModelScope.launch {
            speechManager.speechRecognitionResult.collect { result ->
                if (!result.isNullOrBlank()) {
                    processVoiceInput(result)
                    speechManager.clearResult()
                }
            }
        }
    }

    private fun loadAvailableModels() {
        viewModelScope.launch {
            try {
                val models = listAvailableModels()
                _availableModels.value = models
                _statusMessage.value = if (models.isNotEmpty()) {
                    "Ready - Please download and load a health model"
                } else {
                    "Loading models..."
                }
            } catch (e: Exception) {
                _statusMessage.value = "Error loading models: ${e.message}"
            }
        }
    }

    private fun loadDailyHealthTip() {
        viewModelScope.launch {
            val tips = LumiraHealthDatabase.getHealthTips()
            if (tips.isNotEmpty()) {
                _dailyHealthTip.value = tips.random()
            }
        }
    }

    fun downloadModel(modelId: String) {
        viewModelScope.launch {
            try {
                _statusMessage.value = "Downloading health model..."
                RunAnywhere.downloadModel(modelId).collect { progress ->
                    _downloadProgress.value = progress
                    _statusMessage.value = "Downloading: ${(progress * 100).toInt()}%"
                }
                _downloadProgress.value = null
                _statusMessage.value = "Download complete! Please load the model."
            } catch (e: Exception) {
                _statusMessage.value = "Download failed: ${e.message}"
                _downloadProgress.value = null
            }
        }
    }

    fun loadModel(modelId: String) {
        viewModelScope.launch {
            try {
                _statusMessage.value = "Loading health model..."
                val success = RunAnywhere.loadModel(modelId)
                if (success) {
                    _currentModelId.value = modelId
                    _statusMessage.value = "लुमिरा तैयार है! Lumira is ready to help with your health questions!"
                } else {
                    _statusMessage.value = "Failed to load model"
                }
            } catch (e: Exception) {
                _statusMessage.value = "Error loading model: ${e.message}"
            }
        }
    }

    fun sendTextMessage(text: String) {
        if (_currentModelId.value == null) {
            _statusMessage.value = "Please load a health model first"
            return
        }

        processHealthQuery(text, isVoiceInput = false)
    }

    fun startVoiceInput() {
        if (!speechManager.isSpeechRecognitionAvailable()) {
            _statusMessage.value = "Speech recognition not available"
            return
        }
        
        _isVoiceModeActive.value = true
        speechManager.startListening(_currentLanguage.value)
    }

    fun stopVoiceInput() {
        speechManager.stopListening()
        _isVoiceModeActive.value = false
    }

    private fun processVoiceInput(spokenText: String) {
        _isVoiceModeActive.value = false
        processHealthQuery(spokenText, isVoiceInput = true)
    }

    private fun processHealthQuery(queryText: String, isVoiceInput: Boolean) {
        // Add user message
        val userMessage = HealthMessage(
            text = queryText,
            isUser = true,
            messageType = if (isVoiceInput) MessageType.VOICE_INPUT else MessageType.TEXT
        )
        _messages.value = _messages.value + userMessage

        viewModelScope.launch {
            _isLoading.value = true

            try {
                // First, search local health database
                val localHealthInfo = searchLocalHealthKnowledge(queryText)
                
                if (localHealthInfo.isNotEmpty()) {
                    // Use local health knowledge
                    val healthCondition = localHealthInfo.first()
                    val response = formatHealthAdvice(healthCondition, queryText)
                    
                    val healthMessage = HealthMessage(
                        text = response,
                        isUser = false,
                        messageType = MessageType.HEALTH_ADVICE,
                        relatedHealthInfo = healthCondition,
                        severity = healthCondition.severity
                    )
                    _messages.value = _messages.value + healthMessage
                    
                    // Speak the response if voice mode was used
                    if (isVoiceInput && speechManager.isTextToSpeechReady()) {
                        val spokenResponse = HealthSpeechUtils.preprocessHealthText(response)
                        speechManager.speak(spokenResponse, _currentLanguage.value)
                    }
                } else {
                    // Fall back to LLM with health context
                    generateAIHealthResponse(queryText, isVoiceInput)
                }
            } catch (e: Exception) {
                val errorMessage = HealthMessage(
                    text = "मुझे खुशी होगी आपकी मदद करने में, लेकिन अभी कुछ तकनीकी समस्या है। कृपया फिर से कोशिश करें। I'd be happy to help, but there's a technical issue right now. Please try again.",
                    isUser = false,
                    messageType = MessageType.TEXT
                )
                _messages.value = _messages.value + errorMessage
            }

            _isLoading.value = false
        }
    }

    private fun searchLocalHealthKnowledge(query: String): List<HealthCondition> {
        val healthKeywords = HealthSpeechUtils.extractHealthKeywords(query)
        
        if (healthKeywords.isNotEmpty()) {
            return LumiraHealthDatabase.searchHealthInfo(healthKeywords.joinToString(" "))
        }
        
        return LumiraHealthDatabase.searchHealthInfo(query)
    }

    private fun formatHealthAdvice(condition: HealthCondition, originalQuery: String): String {
        val disclaimer = "⚠️ यह चिकित्सा सलाह नहीं है। गंभीर स्थिति में तुरंत डॉक्टर से मिलें।\n⚠️ This is not medical advice. Consult a doctor immediately for serious conditions.\n\n"
        
        val advice = buildString {
            append("${condition.name} के बारे में जानकारी:\n\n")
            
            append("🔍 लक्षण (Symptoms):\n")
            condition.symptoms.forEachIndexed { index, symptom ->
                append("${index + 1}. $symptom\n")
            }
            
            append("\n🚑 प्राथमिक उपचार (First Aid):\n")
            condition.firstAidSteps.forEachIndexed { index, step ->
                append("${index + 1}. $step\n")
            }
            
            if (condition.severity == com.runanywhere.lumiraai.data.Severity.MODERATE || 
                condition.severity == com.runanywhere.lumiraai.data.Severity.SEVERE) {
                append("\n🏥 डॉक्टर से कब मिलें (When to seek help):\n")
                condition.whenToSeekHelp.forEachIndexed { index, reason ->
                    append("${index + 1}. $reason\n")
                }
            }
            
            append("\n🛡️ बचाव (Prevention):\n")
            condition.prevention.forEachIndexed { index, prevention ->
                append("${index + 1}. $prevention\n")
            }
        }
        
        return disclaimer + advice
    }

    private suspend fun generateAIHealthResponse(query: String, isVoiceInput: Boolean) {
        val healthContext = """
        You are Lumira (लुमिरा), a compassionate rural health assistant. Provide helpful, accurate health guidance while being culturally sensitive to Indian rural contexts. Always include medical disclaimers. Respond in both Hindi and English when appropriate.
        
        Guidelines:
        1. Always start with a medical disclaimer
        2. Provide practical, culturally appropriate advice
        3. Suggest when to seek professional medical help
        4. Use simple, understandable language
        5. Include home remedies when safe and effective
        6. Be empathetic and supportive
        
        User query: $query
        """

        var assistantResponse = ""
        RunAnywhere.generateStream(healthContext).collect { token ->
            assistantResponse += token

            // Update assistant message in real-time
            val currentMessages = _messages.value.toMutableList()
            if (currentMessages.lastOrNull()?.isUser == false) {
                currentMessages[currentMessages.lastIndex] = HealthMessage(
                    text = assistantResponse,
                    isUser = false,
                    messageType = MessageType.HEALTH_ADVICE
                )
            } else {
                currentMessages.add(HealthMessage(
                    text = assistantResponse,
                    isUser = false,
                    messageType = MessageType.HEALTH_ADVICE
                ))
            }
            _messages.value = currentMessages
        }

        // Speak the response if voice mode was used
        if (isVoiceInput && speechManager.isTextToSpeechReady()) {
            val spokenResponse = HealthSpeechUtils.preprocessHealthText(assistantResponse)
            speechManager.speak(spokenResponse, _currentLanguage.value)
        }
    }

    fun setLanguage(language: String) {
        _currentLanguage.value = language
    }

    fun clearConversation() {
        _messages.value = emptyList()
        addWelcomeMessage()
    }

    fun refreshModels() {
        loadAvailableModels()
    }

    fun getDailyTip(): HealthTip? {
        return _dailyHealthTip.value
    }

    fun stopSpeaking() {
        speechManager.stopSpeaking()
    }

    override fun onCleared() {
        super.onCleared()
        speechManager.release()
    }
}