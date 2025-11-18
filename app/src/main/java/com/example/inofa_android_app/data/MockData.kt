package com.example.inofa_android_app.data

import androidx.compose.ui.graphics.Color

// --- Developer Profile Data Models ---

data class Skill(
    val name: String,
    val color: Color = Color(0xFFE8F5E9) // Light Green for background
)

data class PortfolioItem(
    val id: Int,
    val title: String,
    val category: String,
    val imageUrl: String // Mock URL or resource name
)

data class Review(
    val id: Int,
    val reviewerName: String,
    val timeAgo: String,
    val project: String,
    val reviewText: String,
    val rating: Float // 1.0 to 5.0
)

data class Developer(
    val id: Int,
    val name: String,
    val title: String,
    val location: String,
    val rating: Float,
    val reviewCount: Int,
    val projectsCompleted: Int,
    val aboutMe: String,
    val skills: List<Skill>,
    val portfolio: List<PortfolioItem>,
    val reviews: List<Review>
)

// --- Home Screen Data Models ---

data class Category(
    val id: Int,
    val name: String,
    val iconResName: String, // Mock resource name
    val devCount: Int
)

// --- Mock Data Implementation ---

val mockSkills = listOf(
    Skill("React"),
    Skill("Node.js"),
    Skill("TypeScript"),
    Skill("Next.js"),
    Skill("MongoDB"),
    Skill("PostgreSQL"),
    Skill("Tailwind CSS"),
    Skill("React Native"),
    Skill("Express.js"),
    Skill("GraphQL"),
    Skill("Docker"),
    Skill("AWS")
)

val mockPortfolio = listOf(
    PortfolioItem(1, "SaaS Dashboard Platform", "Web Development", "mock_image_1"),
    PortfolioItem(2, "Mobile E-commerce App", "Mobile Development", "mock_image_2"),
    PortfolioItem(3, "Data Visualization Tool", "Data Analysis", "mock_image_3"),
    PortfolioItem(4, "Company Website Redesign", "Web Development", "mock_image_4"),
    PortfolioItem(5, "API Gateway Implementation", "Backend Development", "mock_image_5"),
    PortfolioItem(6, "Cloud Infrastructure Setup", "DevOps", "mock_image_6"),
)

val mockReviews = listOf(
    Review(1, "Sarah Wijaya", "2 minggu lalu", "E-Commerce Mobile App", "Aplikasi sangat profesional dan responsif. Aplikasi yang dibangun sangat berkualitas dan sesuai dengan requirement. Highly recommended!", 5.0f),
    Review(2, "Budi Santoso", "1 bulan lalu", "Dashboard Analytics", "Pekerjaan yang luar biasa! Ahmad memiliki pemahaman teknis yang kuat dan mampu menyelesaikan proyek tepat waktu.", 5.0f),
    Review(3, "Dewi Lestari", "2 bulan lalu", "Company Website", "Hasil kerja sangat memuaskan. Komunikasi lancar dan selalu update progress secara berkala. Terima kasih Ahmad!", 4.5f),
    Review(4, "Andi Prasetyo", "3 bulan lalu", "Mobile App Development", "Developer yang sangat skilled dan berpengalaman. Code quality excellent dan dokumentasi lengkap.", 5.0f),
)

val mockDeveloper = Developer(
    id = 1,
    name = "Ahmad Nur",
    title = "Full-stack Developer",
    location = "Jakarta, Indonesia",
    rating = 4.9f,
    reviewCount = 127,
    projectsCompleted = 89,
    aboutMe = "Saya adalah Full-stack Developer dengan pengalaman 5+ tahun dalam membangun aplikasi web dan mobile yang scalable dan user-friendly. Passionate dalam menggunakan teknologi terbaru untuk menyelesaikan masalah bisnis yang kompleks.",
    skills = mockSkills,
    portfolio = mockPortfolio,
    reviews = mockReviews
)

val mockCategories = listOf(
    Category(1, "Full-stack", "code", 12),
    Category(2, "UI/UX", "palette", 8),
    Category(3, "Mobile", "phone_android", 10),
    Category(4, "Backend", "server", 5),
)

val mockFeaturedDevelopers = listOf(
    mockDeveloper,
    mockDeveloper.copy(id = 2, name = "Budi Santoso", rating = 4.8f, reviewCount = 95, projectsCompleted = 70),
    mockDeveloper.copy(id = 3, name = "Citra Dewi", rating = 5.0f, reviewCount = 150, projectsCompleted = 105),
)

val mockData = mockDeveloper