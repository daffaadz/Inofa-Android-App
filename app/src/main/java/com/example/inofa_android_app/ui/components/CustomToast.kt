package com.example.inofa_android_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ToastType {
    SUCCESS, ERROR, WARNING, INFO, NEUTRAL
}

data class ToastConfig(
    val backgroundColor: Color,
    val iconColor: Color,
    val icon: ImageVector,
    val textColor: Color
)

fun getToastConfig(type: ToastType): ToastConfig {
    return when (type) {
        ToastType.SUCCESS -> ToastConfig(
            backgroundColor = Color(0xFFDCFCE7), // Light green
            iconColor = Color(0xFF10B981),
            icon = Icons.Default.CheckCircle,
            textColor = Color(0xFF065F46)
        )
        ToastType.ERROR -> ToastConfig(
            backgroundColor = Color(0xFFFEE2E2), // Light red
            iconColor = Color(0xFFEF4444),
            icon = Icons.Default.Error,
            textColor = Color(0xFF991B1B)
        )
        ToastType.WARNING -> ToastConfig(
            backgroundColor = Color(0xFFFEF3C7), // Light orange/yellow
            iconColor = Color(0xFFF59E0B),
            icon = Icons.Default.Warning,
            textColor = Color(0xFF92400E)
        )
        ToastType.INFO -> ToastConfig(
            backgroundColor = Color(0xFFDBEAFE), // Light blue
            iconColor = Color(0xFF3B82F6),
            icon = Icons.Default.Info,
            textColor = Color(0xFF1E40AF)
        )
        ToastType.NEUTRAL -> ToastConfig(
            backgroundColor = Color(0xFFF3F4F6), // Light gray
            iconColor = Color(0xFF6B7280),
            icon = Icons.Default.Info,
            textColor = Color(0xFF374151)
        )
    }
}

@Composable
fun CustomToast(
    message: String,
    subtitle: String? = null,
    type: ToastType = ToastType.NEUTRAL,
    onDismiss: () -> Unit
) {
    val config = getToastConfig(type)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = config.backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = config.icon,
                    contentDescription = null,
                    tint = config.iconColor,
                    modifier = Modifier.size(24.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = config.textColor
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            fontSize = 12.sp,
                            color = config.textColor.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = config.textColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun CustomSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = { data ->
            // Parse type from message prefix (e.g., "SUCCESS:Message")
            val messageText = data.visuals.message
            val (type, actualMessage) = when {
                messageText.startsWith("SUCCESS:") -> 
                    ToastType.SUCCESS to messageText.substringAfter("SUCCESS:")
                messageText.startsWith("ERROR:") -> 
                    ToastType.ERROR to messageText.substringAfter("ERROR:")
                messageText.startsWith("WARNING:") -> 
                    ToastType.WARNING to messageText.substringAfter("WARNING:")
                messageText.startsWith("INFO:") -> 
                    ToastType.INFO to messageText.substringAfter("INFO:")
                else -> 
                    ToastType.NEUTRAL to messageText
            }
            
            CustomToast(
                message = actualMessage,
                type = type,
                onDismiss = { data.dismiss() }
            )
        }
    )
}

// Extension function untuk menampilkan toast dengan tipe
suspend fun SnackbarHostState.showCustomToast(
    message: String,
    type: ToastType = ToastType.NEUTRAL,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    val prefixedMessage = when (type) {
        ToastType.SUCCESS -> "SUCCESS:$message"
        ToastType.ERROR -> "ERROR:$message"
        ToastType.WARNING -> "WARNING:$message"
        ToastType.INFO -> "INFO:$message"
        ToastType.NEUTRAL -> message
    }
    showSnackbar(
        message = prefixedMessage,
        duration = duration
    )
}
