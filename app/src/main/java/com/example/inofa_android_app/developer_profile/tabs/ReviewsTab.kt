package com.example.inofa_android_app.developer_profile.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inofa_android_app.data.Review
import com.example.inofa_android_app.data.mockDeveloper
import com.example.inofa_android_app.developer_profile.components.RatingBar
import com.example.inofa_android_app.developer_profile.components.PrimaryGreen

@Composable
fun ReviewsTab(reviews: List<Review>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            OverallRatingSection(
                rating = mockDeveloper.rating,
                reviewCount = mockDeveloper.reviewCount
            )
        }
        items(reviews) { review ->
            ReviewItem(review = review)
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(alpha = 0.5f))
        }
        item {
            Spacer(modifier = Modifier.height(60.dp)) // Space for bottom padding
        }
    }
}

@Composable
fun OverallRatingSection(rating: Float, reviewCount: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Rating Keseluruhan",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RatingBar(rating = rating)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${rating} dari 5.0",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "$reviewCount Ulasan",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = review.reviewerName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            RatingBar(rating = review.rating)
        }
        Text(
            text = review.timeAgo,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Proyek: ${review.project}",
            style = MaterialTheme.typography.bodyMedium,
            color = PrimaryGreen,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = review.reviewText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewsTabPreview() {
    ReviewsTab(reviews = mockDeveloper.reviews)
}