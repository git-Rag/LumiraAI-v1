package com.runanywhere.lumiraai.data


// Enhanced Message Data Class for Health Conversations
data class HealthMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val messageType: MessageType = MessageType.TEXT,
    val relatedHealthInfo: HealthCondition? = null,
    val severity: Severity? = null
)

enum class MessageType {
    TEXT,
    HEALTH_ADVICE,
    EMERGENCY_INFO,
    DAILY_TIP,
    VOICE_INPUT
}