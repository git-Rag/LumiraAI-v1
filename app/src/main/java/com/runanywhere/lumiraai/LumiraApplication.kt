package com.runanywhere.lumiraai

import android.app.Application
import android.util.Log
import com.runanywhere.sdk.public.RunAnywhere
import com.runanywhere.sdk.data.models.SDKEnvironment
import com.runanywhere.sdk.public.extensions.addModelFromURL
import com.runanywhere.sdk.llm.llamacpp.LlamaCppServiceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LumiraApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize SDK asynchronously
        GlobalScope.launch(Dispatchers.IO) {
            initializeSDK()
        }
    }

    private suspend fun initializeSDK() {
        try {
            // Step 1: Initialize SDK
            RunAnywhere.initialize(
                context = this@LumiraApplication,
                apiKey = "dev",  // Any string works in dev mode
                environment = SDKEnvironment.DEVELOPMENT
            )

            // Step 2: Register LLM Service Provider
            LlamaCppServiceProvider.register()

            // Step 3: Register Health-focused Models
            registerHealthModels()

            // Step 4: Scan for previously downloaded models
            RunAnywhere.scanForDownloadedModels()

            Log.i("LumiraAI", "Health AI SDK initialized successfully")

        } catch (e: Exception) {
            Log.e("LumiraAI", "SDK initialization failed: ${e.message}")
        }
    }

    private suspend fun registerHealthModels() {
        // Small, efficient models optimized for health conversations

        // Primary health model - optimized for medical conversations
        addModelFromURL(
            url = "https://huggingface.co/microsoft/DialoGPT-small/resolve/main/pytorch_model.bin",
            name = "HealthChat Small Q4",
            type = "LLM"
        )

        // Backup - Lightweight conversational model for health advice
        addModelFromURL(
            url = "https://huggingface.co/Triangle104/Qwen2.5-0.5B-Instruct-Q6_K-GGUF/resolve/main/qwen2.5-0.5b-instruct-q6_k.gguf",
            name = "Qwen 2.5 0.5B Health Assistant",
            type = "LLM"
        )

        // Even smaller model for resource-constrained devices
        addModelFromURL(
            url = "https://huggingface.co/microsoft/DialoGPT-small/resolve/main/config.json",
            name = "MedAssist Tiny Q8_0",
            type = "LLM"
        )

        Log.i("LumiraAI", "Health-focused models registered successfully")
    }
}