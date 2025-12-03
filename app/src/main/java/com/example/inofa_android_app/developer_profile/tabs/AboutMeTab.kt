package com.example.inofa_android_app.developer_profile.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inofa_android_app.data.Developer
import com.example.inofa_android_app.data.Skill
import com.example.inofa_android_app.data.mockDeveloper
import com.example.inofa_android_app.developer_profile.components.LightGreenBackground

@Composable
fun AboutMeTab(developer: Developer) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AboutSection(
                title = "Tentang Saya",
                content = developer.bio ?: "Tidak ada informasi bio."
            )
        }
        item {
            SkillsSection(
                title = "Keahlian",
                skills = developer.skills
            )
        }
        // Add more sections if needed, like Education, Experience, etc.
        item {
            Spacer(modifier = Modifier.height(60.dp)) // Space for bottom padding
        }
    }
}

@Composable
fun AboutSection(title: String, content: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillsSection(title: String, skills: List<String>) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            skills.forEach { skill ->
                SkillChip(skillName = skill)
            }
        }
    }
}

@Composable
fun SkillChip(skillName: String) {
    Box(
        modifier = Modifier
            .background(
                color = LightGreenBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = skillName,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF388E3C) // PrimaryGreen
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeTabPreview() {
    AboutMeTab(developer = mockDeveloper)
}