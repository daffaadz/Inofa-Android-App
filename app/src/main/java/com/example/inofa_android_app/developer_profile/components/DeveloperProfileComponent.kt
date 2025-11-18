package com.example.inofa_android_app.developer_profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inofa_android_app.data.Developer
import com.example.inofa_android_app.data.mockDeveloper
import com.example.inofa_android_app.ui.theme.InofaAndroidAppTheme

// Assuming the primary color is the green used for the button and text
val PrimaryGreen = Color(0xFF388E3C) // A darker green for better contrast
val LightGreenBackground = Color(0xFFE8F5E9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperProfileTopBar(
    onBackClick: () -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = "Profil Developer",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = Icons.Default.Share, // Using Share icon as a placeholder for the design's share/message icon
                    contentDescription = "Share"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun DeveloperProfileHeader(
    developer: Developer,
    actionButtonText: String,
    onActionClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(LightGreenBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = developer.name.first().toString(),
                style = MaterialTheme.typography.headlineLarge,
                color = PrimaryGreen,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Name and Title
        Text(
            text = developer.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = developer.title,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Location
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                modifier = Modifier.size(16.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = developer.location,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Rating and Reviews
        Row(verticalAlignment = Alignment.CenterVertically) {
            RatingBar(rating = developer.rating)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${developer.rating}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = " (${developer.reviewCount} ulasan)",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Stats (Proyek Selesai & Ulasan)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(value = developer.projectsCompleted.toString(), label = "Proyek Selesai")
            StatItem(value = developer.reviewCount.toString(), label = "Ulasan")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Action Button
        Button(
            onClick = onActionClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = actionButtonText, style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
fun RatingBar(rating: Float, maxRating: Int = 5) {
    Row {
        val fullStars = rating.toInt()
        val hasHalfStar = rating - fullStars >= 0.5f
        val emptyStars = maxRating - fullStars - if (hasHalfStar) 1 else 0

        repeat(fullStars) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107), // Amber color for stars
                modifier = Modifier.size(20.dp)
            )
        }
        if (hasHalfStar) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(20.dp)
            )
        }
        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeveloperProfileHeaderPreview() {
    InofaAndroidAppTheme {
        DeveloperProfileHeader(
            developer = mockDeveloper,
            actionButtonText = "Kirim Form Permintaan"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeveloperProfileTopBarPreview() {
    InofaAndroidAppTheme {
        DeveloperProfileTopBar()
    }
}