package com.example.inofa_android_app.data

import androidx.compose.ui.graphics.Color

// --- Developer Profile Data Models (sesuai backend API) ---

data class Skill(
    val name: String,
    val color: Color = Color(0xFFE8F5E9) // Light Green for background
)

data class PortfolioItem(
    val id: Int,
    val title: String,
    val description: String?,
    val link: String?,
    val imageUrl: String? // image_url dari backend
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
    val email: String,
    val name: String,
    val bio: String?,
    val location: String?,
    val skills: List<String>, // Array of skill strings dari backend
    val whatsapp: String?,
    val whatsappLink: String?,
    val portfolio: List<PortfolioItem>,
    // Tambahan untuk UI
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val projectsCompleted: Int = 0,
    val reviews: List<Review> = emptyList()
)

// --- Home Screen Data Models ---

data class Category(
    val id: Int,
    val name: String,
    val iconResName: String, // Mock resource name
    val devCount: Int
)

// --- Project Data Model (sesuai backend) ---

data class Project(
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String?,
    val budget: Double?,
    val skillRequirements: List<String>,
    val constraints: String?,
    val createdAt: String,
    val updatedAt: String
)

// --- Mock Data Implementation (sesuai struktur backend) ---

val mockPortfolio = listOf(
    PortfolioItem(
        id = 1,
        title = "Sistem Manajemen Toko Online",
        description = "Platform e-commerce lengkap dengan fitur keranjang, payment gateway, dan admin dashboard",
        link = "https://github.com/developer/ecommerce-app",
        imageUrl = null
    ),
    PortfolioItem(
        id = 2,
        title = "Aplikasi Mobile Booking",
        description = "Aplikasi pemesanan layanan dengan integrasi payment dan real-time notification",
        link = "https://play.google.com/store/apps/booking",
        imageUrl = null
    ),
    PortfolioItem(
        id = 3,
        title = "Dashboard Analytics",
        description = "Dashboard untuk visualisasi data dengan chart interaktif dan export ke PDF",
        link = "https://analytics-dashboard.demo.com",
        imageUrl = null
    ),
    PortfolioItem(
        id = 4,
        title = "REST API Backend",
        description = "Backend service dengan Node.js, Express, dan PostgreSQL untuk aplikasi mobile",
        link = "https://github.com/developer/rest-api",
        imageUrl = null
    )
)

val mockReviews = listOf(
    Review(1, "Sarah Wijaya", "2 minggu lalu", "E-Commerce Mobile App", "Aplikasi sangat profesional dan responsif. Aplikasi yang dibangun sangat berkualitas dan sesuai dengan requirement. Highly recommended!", 5.0f),
    Review(2, "Budi Santoso", "1 bulan lalu", "Dashboard Analytics", "Pekerjaan yang luar biasa! Developer memiliki pemahaman teknis yang kuat dan mampu menyelesaikan proyek tepat waktu.", 5.0f),
    Review(3, "Dewi Lestari", "2 bulan lalu", "Company Website", "Hasil kerja sangat memuaskan. Komunikasi lancar dan selalu update progress secara berkala.", 4.5f)
)

// Mock developers sesuai struktur backend response
val mockDeveloper = Developer(
    id = 1,
    email = "ahmad.nur@example.com",
    name = "Ahmad Nur Hidayat",
    bio = "Full-stack Developer dengan pengalaman 5+ tahun dalam membangun aplikasi web dan mobile yang scalable dan user-friendly. Passionate dalam menggunakan teknologi terbaru untuk menyelesaikan masalah bisnis yang kompleks.",
    location = "Jakarta, Indonesia",
    skills = listOf("React", "Node.js", "TypeScript", "Next.js", "PostgreSQL", "MongoDB", "Express.js", "React Native"),
    whatsapp = "6281234567890",
    whatsappLink = "https://wa.me/6281234567890",
    portfolio = mockPortfolio,
    rating = 4.8f,
    reviewCount = 127,
    projectsCompleted = 89,
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
    Developer(
        id = 2,
        email = "budi.santoso@example.com",
        name = "Budi Santoso",
        bio = "Backend Developer dengan spesialisasi Node.js dan database optimization. Berpengalaman dalam membangun RESTful API dan microservices architecture.",
        location = "Bandung, Indonesia",
        skills = listOf("Node.js", "Python", "PostgreSQL", "MongoDB", "Redis", "Docker"),
        whatsapp = "6282345678901",
        whatsappLink = "https://wa.me/6282345678901",
        portfolio = listOf(
            PortfolioItem(5, "API Gateway Service", "Microservices architecture dengan Kong API Gateway", "https://github.com/budi/api-gateway", null),
            PortfolioItem(6, "Payment Integration", "Integrasi payment gateway untuk e-commerce", null, null)
        ),
        rating = 4.7f,
        reviewCount = 95,
        projectsCompleted = 70
    ),
    Developer(
        id = 3,
        email = "citra.dewi@example.com",
        name = "Citra Dewi",
        bio = "UI/UX Designer dengan fokus pada user-centered design. Mahir dalam Figma, Adobe XD, dan prototyping tools.",
        location = "Surabaya, Indonesia",
        skills = listOf("Figma", "Adobe XD", "UI Design", "UX Research", "Prototyping", "User Testing"),
        whatsapp = "6283456789012",
        whatsappLink = "https://wa.me/6283456789012",
        portfolio = listOf(
            PortfolioItem(7, "Mobile Banking App Design", "Complete UI/UX design untuk aplikasi mobile banking", "https://figma.com/banking-app", null),
            PortfolioItem(8, "E-Learning Platform", "Design system dan UI untuk platform pembelajaran online", "https://figma.com/elearning", null)
        ),
        rating = 4.9f,
        reviewCount = 150,
        projectsCompleted = 105
    ),
    Developer(
        id = 4,
        email = "dedi.kurniawan@example.com",
        name = "Dedi Kurniawan",
        bio = "Mobile Developer dengan spesialisasi Android dan Flutter. Berpengalaman membuat aplikasi yang smooth dan performant.",
        location = "Yogyakarta, Indonesia",
        skills = listOf("Flutter", "Kotlin", "Android", "Firebase", "REST API", "SQLite"),
        whatsapp = "6284567890123",
        whatsappLink = "https://wa.me/6284567890123",
        portfolio = listOf(
            PortfolioItem(9, "Delivery App", "Aplikasi delivery dengan real-time tracking", "https://play.google.com/delivery", null),
            PortfolioItem(10, "Fitness Tracker", "Aplikasi tracking workout dan kalori", null, null)
        ),
        rating = 4.6f,
        reviewCount = 78,
        projectsCompleted = 62
    ),
    Developer(
        id = 5,
        email = "eka.putri@example.com",
        name = "Eka Putri",
        bio = "Frontend Developer dengan keahlian React dan Vue.js. Fokus pada performance optimization dan user experience.",
        location = "Jakarta, Indonesia",
        skills = listOf("React", "Vue.js", "JavaScript", "TypeScript", "Tailwind CSS", "Webpack"),
        whatsapp = "6285678901234",
        whatsappLink = "https://wa.me/6285678901234",
        portfolio = listOf(
            PortfolioItem(11, "Admin Dashboard", "Dashboard dengan React dan Material-UI", "https://admin-dashboard.demo.com", null),
            PortfolioItem(12, "Landing Page", "Landing page untuk startup dengan animasi smooth", "https://startup-landing.com", null)
        ),
        rating = 4.8f,
        reviewCount = 112,
        projectsCompleted = 88
    ),
    Developer(
        id = 6,
        email = "fajar.rahman@example.com",
        name = "Fajar Rahman",
        bio = "DevOps Engineer dengan pengalaman dalam cloud infrastructure dan CI/CD. Ahli dalam AWS dan Kubernetes.",
        location = "Tangerang, Indonesia",
        skills = listOf("AWS", "Docker", "Kubernetes", "Jenkins", "Terraform", "Linux"),
        whatsapp = "6286789012345",
        whatsappLink = "https://wa.me/6286789012345",
        portfolio = listOf(
            PortfolioItem(13, "Cloud Migration", "Migrasi aplikasi monolith ke microservices di AWS", null, null),
            PortfolioItem(14, "CI/CD Pipeline", "Setup automated deployment dengan Jenkins", null, null)
        ),
        rating = 4.5f,
        reviewCount = 65,
        projectsCompleted = 54
    )
)

// Mock projects sesuai struktur backend
val mockProjects = listOf(
    Project(
        id = 1,
        userId = 101,
        title = "Website Company Profile",
        description = "Membutuhkan website company profile dengan design modern dan responsive",
        budget = 15000000.0,
        skillRequirements = listOf("React", "Next.js", "Tailwind CSS"),
        constraints = "Deadline 1 bulan, harus SEO-friendly",
        createdAt = "2024-12-01T10:00:00Z",
        updatedAt = "2024-12-01T10:00:00Z"
    ),
    Project(
        id = 2,
        userId = 102,
        title = "Aplikasi Mobile E-Commerce",
        description = "Aplikasi mobile untuk toko online dengan fitur payment gateway",
        budget = 50000000.0,
        skillRequirements = listOf("Flutter", "Firebase", "REST API"),
        constraints = "Support Android dan iOS, Integrasi dengan Midtrans",
        createdAt = "2024-12-02T14:30:00Z",
        updatedAt = "2024-12-02T14:30:00Z"
    ),
    Project(
        id = 3,
        userId = 103,
        title = "Dashboard Analytics",
        description = "Dashboard untuk monitoring data penjualan dengan visualisasi chart",
        budget = 25000000.0,
        skillRequirements = listOf("React", "Node.js", "PostgreSQL", "Chart.js"),
        constraints = "Real-time data update, Export ke PDF dan Excel",
        createdAt = "2024-11-28T09:15:00Z",
        updatedAt = "2024-11-28T09:15:00Z"
    ),
    Project(
        id = 4,
        userId = 104,
        title = "REST API Backend",
        description = "Backend service untuk aplikasi mobile dengan authentication dan authorization",
        budget = 20000000.0,
        skillRequirements = listOf("Node.js", "Express", "MongoDB", "JWT"),
        constraints = "Dokumentasi API lengkap, Unit testing coverage min 80%",
        createdAt = "2024-11-25T16:45:00Z",
        updatedAt = "2024-11-25T16:45:00Z"
    )
)