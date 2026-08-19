package com.example.ui.model

enum class OrderStatus(val displayName: String) {
    NEW("New"),
    CONFIRMED("Confirmed"),
    PROCESSING("Processing"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    companion object {
        fun fromString(status: String): OrderStatus {
            return entries.find { it.displayName.equals(status, ignoreCase = true) } ?: NEW
        }
    }
}

data class TeaBlend(
    val id: String,
    val name: String,
    val grade: String,
    val description: String,
    val flavorProfile: String,
    val recommendedFor: String,
    val availableSizes: List<String> = listOf("125g", "250g", "500g", "1kg", "Custom")
)

val SKT_TEA_CATALOG = listOf(
    TeaBlend(
        id = "skt-royal-danedar",
        name = "SK Royal Danedar",
        grade = "Supreme Granular Blend",
        description = "Our flagship high-grown Kenyan & Assam granular tea with robust aroma, rich golden-amber liquor, and unforgettable strong taste.",
        flavorProfile = "Bold, Malty, Aromatic, Deep Amber",
        recommendedFor = "Tea Stalls, Cafes & Family Morning Chai"
    ),
    TeaBlend(
        id = "skt-gold-leaf",
        name = "SK Premium Gold Leaf",
        grade = "Orthodox Broken Orange Pekoe",
        description = "Carefully handpicked tender whole and broken tea leaves infused with natural sweetness, smooth character, and golden sparkle.",
        flavorProfile = "Velvety, Floral Aroma, Smooth Finish",
        recommendedFor = "Premium Tea Lounges, Executive Hotels & Connoisseurs"
    ),
    TeaBlend(
        id = "skt-hotel-karak",
        name = "SK Special Karak Hotel Blend",
        grade = "Extra Strong Commercial Blend",
        description = "Crafted specifically for commercial tea stalls, restaurants, and hotels desiring quick brewing, high yield, deep reddish color, and rich thickness with milk.",
        flavorProfile = "Intense, High Extraction, Thick Creamy Karak",
        recommendedFor = "High-Volume Tea Stalls, Dhaba & Catering"
    ),
    TeaBlend(
        id = "skt-classic-family",
        name = "SK Classic Family Mixture",
        grade = "Balanced Traditional Blend",
        description = "A harmonious combination of bright leaf and strong dust tea, delivering the beloved traditional everyday taste at wholesale value.",
        flavorProfile = "Comforting, Balanced, Fragrant",
        recommendedFor = "Retail Grocery Stores, Wholesale & Homes"
    )
)

data class PackOption(
    val id: String,
    val label: String,
    val grams: Int,
    val kgEquivalent: Double,
    val description: String
)

val PACK_OPTIONS = listOf(
    PackOption("125g", "125 Grams", 125, 0.125, "Mini Trial Pouch - 1/8 KG"),
    PackOption("250g", "250 Grams", 250, 0.25, "Quarter KG Pouch - Retail Friendly"),
    PackOption("500g", "500 Grams", 500, 0.50, "Half KG Pouch - Most Popular"),
    PackOption("1kg", "1 Kilogram", 1000, 1.00, "1 KG Master Pack - Best Value"),
    PackOption("custom", "Custom KG / Grams", 0, 0.0, "Bulk Commercial Custom Volume")
)

data class AdminAccount(
    val name: String,
    val role: String,
    val accessCode: String,
    val phone: String
)

val AUTHORIZED_ADMINS = listOf(
    AdminAccount("Muhammad Azam", "Company Owner", "Admin@1973", "0331-8701808"),
    AdminAccount("Muhammad Zeeshan", "General Manager", "Admin@1973", "0344-9293698")
)

data class CompanyProfile(
    val name: String = "SK Tea Company",
    val tagline: String = "Quality Tea, Trusted Service.",
    val owner: String = "Muhammad Azam",
    val ownerPhone: String = "0331-8701808",
    val manager: String = "Muhammad Zeeshan",
    val managerPhone: String = "0344-9293698",
    val whatsappNumber: String = "+92 331 8701808",
    val email: String = "orders@sk-tea.com",
    val address: String = "SK Tea Processing & Wholesale Terminal, G.T. Road Industrial Estate, Punjab",
    val city: String = "Lahore, Pakistan",
    val hours: String = "Monday - Saturday: 8:00 AM - 9:00 PM (Orders Open 24/7)"
)

val COMPANY_CONTACT = CompanyProfile()

sealed interface NavTab {
    val title: String
    data object Home : NavTab { override val title = "Home" }
    data object Products : NavTab { override val title = "Products" }
    data object PlaceOrder : NavTab { override val title = "Place Order" }
    data object TrackOrder : NavTab { override val title = "Track Order" }
    data object History : NavTab { override val title = "My Orders" }
    data object Admin : NavTab { override val title = "Admin Portal" }
    data object ContactAbout : NavTab { override val title = "About SK Tea" }

    // Alias for compatibility
    val OrderHistory: NavTab get() = History
}
