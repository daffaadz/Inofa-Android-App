package com.example.inofa_android_app.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    /**
     * Convert ISO date string to relative time (e.g., "2 jam lalu", "3 hari lalu")
     */
    fun getRelativeTime(dateString: String?): String {
        if (dateString.isNullOrBlank()) return "Baru saja"
        
        return try {
            // Parse ISO format: "2024-12-16T10:30:45.000Z" or "2024-12-16 10:30:45"
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            format.timeZone = TimeZone.getTimeZone("UTC")
            
            val date = format.parse(dateString.replace("T", " ").replace("Z", "").substring(0, 19))
            val now = Date()
            
            if (date == null) return "Baru saja"
            
            val diffMs = now.time - date.time
            val diffSeconds = diffMs / 1000
            val diffMinutes = diffSeconds / 60
            val diffHours = diffMinutes / 60
            val diffDays = diffHours / 24
            val diffWeeks = diffDays / 7
            val diffMonths = diffDays / 30
            val diffYears = diffDays / 365
            
            when {
                diffSeconds < 60 -> "Baru saja"
                diffMinutes < 60 -> "$diffMinutes menit lalu"
                diffHours < 24 -> "$diffHours jam lalu"
                diffDays < 7 -> "$diffDays hari lalu"
                diffWeeks < 4 -> "$diffWeeks minggu lalu"
                diffMonths < 12 -> "$diffMonths bulan lalu"
                else -> "$diffYears tahun lalu"
            }
        } catch (e: Exception) {
            "Baru saja"
        }
    }
    
    /**
     * Get initial from name (first letter)
     */
    fun getInitial(name: String?): String {
        return name?.trim()?.firstOrNull()?.uppercase() ?: "U"
    }
}
