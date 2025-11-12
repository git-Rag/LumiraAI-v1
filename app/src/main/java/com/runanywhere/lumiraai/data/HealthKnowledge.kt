package com.runanywhere.lumiraai.data

import kotlinx.serialization.Serializable

/**
 * Health Knowledge Base - Offline medical guidance data structures
 * Contains verified medical information for rural health scenarios
 */

@Serializable
data class HealthCondition(
    val id: String,
    val name: String,
    val category: HealthCategory,
    val symptoms: List<String>,
    val firstAidSteps: List<String>,
    val whenToSeekHelp: List<String>,
    val prevention: List<String>,
    val severity: Severity,
    val ageSpecific: Map<String, String> = emptyMap(), // "children", "adults", "elderly"
    val tags: List<String> = emptyList()
)

@Serializable
enum class HealthCategory {
    FEVER_ILLNESS,
    RESPIRATORY,
    DIGESTIVE,
    SKIN_CONDITIONS,
    INJURIES_WOUNDS,
    MATERNAL_HEALTH,
    CHILD_HEALTH,
    NUTRITION,
    MENTAL_HEALTH,
    PREVENTIVE_CARE,
    EMERGENCY
}

@Serializable
enum class Severity {
    MILD,      // Self-care possible
    MODERATE,  // May need consultation
    SEVERE,    // Requires immediate medical attention
    EMERGENCY  // Life-threatening
}

@Serializable
data class HealthTip(
    val id: String,
    val title: String,
    val description: String,
    val category: HealthCategory,
    val season: String? = null, // "summer", "winter", "monsoon", "all"
    val targetGroup: String? = null // "children", "pregnant_women", "elderly", "all"
)

@Serializable
data class NutritionInfo(
    val id: String,
    val foodItem: String,
    val benefits: List<String>,
    val goodFor: List<String>, // conditions it helps with
    val localNames: Map<String, String> = emptyMap(), // language to local name mapping
    val season: String = "all"
)

@Serializable
data class EmergencyContact(
    val id: String,
    val type: String, // "ambulance", "hospital", "poison_control", "helpline"
    val number: String,
    val description: String,
    val availability: String = "24/7"
)

/**
 * Sample Health Knowledge Database
 * In a real app, this would be loaded from JSON files or a local database
 */
object LumiraHealthDatabase {

    val healthConditions = listOf(
        // Fever and Common Illnesses
        HealthCondition(
            id = "fever_common",
            name = "Common Fever",
            category = HealthCategory.FEVER_ILLNESS,
            symptoms = listOf(
                "Body temperature above 100.4°F (38°C)",
                "Feeling hot or flushed",
                "Chills or shivering",
                "Headache",
                "Body aches",
                "Weakness or fatigue"
            ),
            firstAidSteps = listOf(
                "Rest in a cool, comfortable place",
                "Drink plenty of fluids - water, ORS, coconut water",
                "Take paracetamol as per age-appropriate dosage",
                "Use cold compresses on forehead",
                "Wear light, loose clothing",
                "Monitor temperature every 2-4 hours"
            ),
            whenToSeekHelp = listOf(
                "Fever above 103°F (39.4°C)",
                "Fever lasting more than 3 days",
                "Difficulty breathing",
                "Severe headache or neck stiffness",
                "Persistent vomiting",
                "Signs of dehydration",
                "Confusion or drowsiness"
            ),
            prevention = listOf(
                "Maintain good hygiene",
                "Wash hands regularly",
                "Avoid crowded places during illness outbreaks",
                "Stay hydrated",
                "Get adequate sleep",
                "Eat nutritious food"
            ),
            severity = Severity.MILD,
            ageSpecific = mapOf(
                "children" to "For children under 3 months with fever, seek immediate medical attention",
                "elderly" to "Elderly people may not show high fever even with serious infections"
            )
        ),

        // Respiratory Issues
        HealthCondition(
            id = "cough_cold",
            name = "Cough and Cold",
            category = HealthCategory.RESPIRATORY,
            symptoms = listOf(
                "Runny or stuffy nose",
                "Cough (dry or with phlegm)",
                "Sneezing",
                "Mild headache",
                "Low-grade fever",
                "Sore throat"
            ),
            firstAidSteps = listOf(
                "Rest and stay hydrated",
                "Gargle with warm salt water",
                "Inhale steam from hot water",
                "Use honey for cough (not for children under 1 year)",
                "Take warm liquids like tea, soup",
                "Use a humidifier or breathe moist air"
            ),
            whenToSeekHelp = listOf(
                "Difficulty breathing",
                "Persistent high fever",
                "Chest pain",
                "Coughing up blood",
                "Symptoms worsening after a week",
                "Severe headache with neck stiffness"
            ),
            prevention = listOf(
                "Wash hands frequently",
                "Avoid touching face with unwashed hands",
                "Stay away from sick people",
                "Cover cough and sneeze",
                "Maintain good nutrition",
                "Get adequate rest"
            ),
            severity = Severity.MILD
        ),

        // Digestive Issues
        HealthCondition(
            id = "diarrhea",
            name = "Diarrhea",
            category = HealthCategory.DIGESTIVE,
            symptoms = listOf(
                "Loose, watery stools",
                "Frequent bowel movements",
                "Abdominal cramps",
                "Nausea",
                "Vomiting",
                "Fever (sometimes)"
            ),
            firstAidSteps = listOf(
                "Drink plenty of fluids - ORS solution is best",
                "BRAT diet: Bananas, Rice, Applesauce, Toast",
                "Avoid dairy products temporarily",
                "Take probiotics if available",
                "Rest and avoid strenuous activity",
                "Monitor for signs of dehydration"
            ),
            whenToSeekHelp = listOf(
                "Signs of severe dehydration",
                "Blood in stool",
                "High fever (above 102°F)",
                "Severe abdominal pain",
                "Diarrhea lasting more than 3 days",
                "Vomiting that prevents keeping fluids down"
            ),
            prevention = listOf(
                "Drink clean, boiled water",
                "Eat freshly cooked food",
                "Wash hands before eating",
                "Avoid street food",
                "Store food properly",
                "Maintain kitchen hygiene"
            ),
            severity = Severity.MODERATE,
            ageSpecific = mapOf(
                "children" to "Children dehydrate quickly - seek help if persistent vomiting or decreased urination",
                "elderly" to "Elderly are at higher risk of dehydration complications"
            )
        ),

        // Injuries and Wounds
        HealthCondition(
            id = "minor_cuts",
            name = "Minor Cuts and Wounds",
            category = HealthCategory.INJURIES_WOUNDS,
            symptoms = listOf(
                "Bleeding from skin break",
                "Pain at wound site",
                "Possible bruising around area"
            ),
            firstAidSteps = listOf(
                "Wash your hands before treating",
                "Stop bleeding by applying direct pressure",
                "Clean wound with clean water",
                "Apply antiseptic if available",
                "Cover with clean bandage",
                "Change bandage daily",
                "Keep wound dry and clean"
            ),
            whenToSeekHelp = listOf(
                "Deep cut that won't stop bleeding",
                "Signs of infection (pus, red streaks, fever)",
                "Unable to clean debris from wound",
                "Cut from dirty or rusty object",
                "Not up to date with tetanus vaccination",
                "Wound not healing after a week"
            ),
            prevention = listOf(
                "Handle sharp objects carefully",
                "Keep first aid supplies available",
                "Maintain clean environment",
                "Wear protective gear when needed"
            ),
            severity = Severity.MILD
        ),

        // Maternal Health
        HealthCondition(
            id = "pregnancy_care",
            name = "Basic Pregnancy Care",
            category = HealthCategory.MATERNAL_HEALTH,
            symptoms = listOf(
                "This is preventive care, not symptoms"
            ),
            firstAidSteps = listOf(
                "Eat nutritious food rich in iron and folic acid",
                "Take prescribed prenatal vitamins",
                "Drink plenty of clean water",
                "Get adequate rest",
                "Avoid alcohol and smoking",
                "Exercise as recommended by healthcare provider"
            ),
            whenToSeekHelp = listOf(
                "Severe nausea and vomiting",
                "Bleeding during pregnancy",
                "Severe abdominal pain",
                "Decreased fetal movement",
                "High blood pressure symptoms",
                "Signs of infection"
            ),
            prevention = listOf(
                "Regular prenatal checkups",
                "Balanced nutrition",
                "Avoid harmful substances",
                "Manage stress",
                "Stay active as advised"
            ),
            severity = Severity.MODERATE,
            ageSpecific = mapOf(
                "adults" to "Pregnancy requires regular monitoring and professional care"
            )
        )
    )

    val healthTips = listOf(
        HealthTip(
            id = "hydration_summer",
            title = "Stay Hydrated in Summer",
            description = "Drink at least 8-10 glasses of water daily. Include ORS, coconut water, and buttermilk. Avoid excessive tea, coffee, and sugary drinks.",
            category = HealthCategory.PREVENTIVE_CARE,
            season = "summer",
            targetGroup = "all"
        ),
        HealthTip(
            id = "hand_hygiene",
            title = "Proper Hand Washing",
            description = "Wash hands with soap for at least 20 seconds, especially before eating, after using toilet, and after coughing/sneezing.",
            category = HealthCategory.PREVENTIVE_CARE,
            season = "all",
            targetGroup = "all"
        ),
        HealthTip(
            id = "child_nutrition",
            title = "Balanced Diet for Children",
            description = "Include variety of foods: cereals, pulses, vegetables, fruits, milk products. Avoid junk food and excessive sweets.",
            category = HealthCategory.CHILD_HEALTH,
            season = "all",
            targetGroup = "children"
        )
    )

    val nutritionInfo = listOf(
        NutritionInfo(
            id = "tulsi",
            foodItem = "Tulsi (Holy Basil)",
            benefits = listOf("Boosts immunity", "Helps with cough and cold", "Reduces stress"),
            goodFor = listOf("respiratory issues", "fever", "stress"),
            localNames = mapOf("hindi" to "तुलसी", "tamil" to "துளசி", "telugu" to "తులసి"),
            season = "all"
        ),
        NutritionInfo(
            id = "ginger",
            foodItem = "Ginger",
            benefits = listOf("Reduces nausea", "Anti-inflammatory", "Aids digestion"),
            goodFor = listOf("nausea", "indigestion", "cold"),
            localNames = mapOf("hindi" to "अदरक", "tamil" to "இஞ்சி", "telugu" to "అల్లం"),
            season = "all"
        )
    )

    val emergencyContacts = listOf(
        EmergencyContact(
            id = "ambulance",
            type = "ambulance",
            number = "108",
            description = "National Emergency Ambulance Service",
            availability = "24/7"
        ),
        EmergencyContact(
            id = "health_helpline",
            type = "helpline",
            number = "104",
            description = "National Health Helpline",
            availability = "24/7"
        )
    )

    /**
     * Get health information based on symptoms or keywords
     */
    fun searchHealthInfo(query: String): List<HealthCondition> {
        val lowercaseQuery = query.lowercase()
        return healthConditions.filter { condition ->
            condition.name.lowercase().contains(lowercaseQuery) ||
                    condition.symptoms.any { it.lowercase().contains(lowercaseQuery) } ||
                    condition.tags.any { it.lowercase().contains(lowercaseQuery) }
        }
    }

    /**
     * Get health tips for specific category or season
     */
    fun getHealthTips(category: HealthCategory? = null, season: String? = null): List<HealthTip> {
        return healthTips.filter { tip ->
            (category == null || tip.category == category) &&
                    (season == null || tip.season == "all" || tip.season == season)
        }
    }
}