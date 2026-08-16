package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.SktBrandLogo
import com.example.ui.components.launchWhatsApp
import com.example.ui.model.NavTab
import com.example.ui.model.SKT_TEA_CATALOG
import com.example.ui.model.TeaBlend
import com.example.ui.theme.TeaCreamBg
import com.example.ui.theme.TeaGold
import com.example.ui.theme.TeaGoldContainer
import com.example.ui.theme.TeaGoldLight
import com.example.ui.theme.TeaGreenContainer
import com.example.ui.theme.TeaGreenDark
import com.example.ui.theme.TeaGreenLight
import com.example.ui.theme.TeaGreenPrimary
import com.example.ui.theme.TeaSurfaceVariant
import com.example.ui.theme.TeaTextPrimary
import com.example.ui.theme.TeaTextSecondary

@Composable
fun HomeScreen(
    onNavigate: (NavTab) -> Unit,
    onSelectBlendToOrder: (String) -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TeaCreamBg)
    ) {
        // 1. Hero Section
        item {
            HeroBannerSection(
                onPlaceOrder = { onNavigate(NavTab.PlaceOrder) },
                onContactUs = { onNavigate(NavTab.ContactAbout) }
            )
        }

        // 2. Trust Metrics Strip
        item {
            TrustMetricsStrip()
        }

        // 3. How It Works (4-Step Flow)
        item {
            HowItWorksSection(onStartOrder = { onNavigate(NavTab.PlaceOrder) })
        }

        // 4. Featured Tea Products
        item {
            FeaturedProductsSection(
                onViewAll = { onNavigate(NavTab.Products) },
                onOrderBlend = onSelectBlendToOrder
            )
        }

        // 5. Why Choose SKT Tea
        item {
            WhyChooseSktSection()
        }

        // 6. Direct WhatsApp & Support Banner
        item {
            WhatsAppCalloutBanner(
                onWhatsAppClick = {
                    launchWhatsApp(
                        context = context,
                        message = "Hello SKT Tea Company, I would like to inquire about placing a bulk tea order for my shop."
                    )
                },
                onTrackClick = { onNavigate(NavTab.TrackOrder) }
            )
        }

        // 7. Executive Leadership & About Preview
        item {
            ExecutiveLeadershipPreview(
                onReadMore = { onNavigate(NavTab.ContactAbout) }
            )
        }

        // 8. Footer Info
        item {
            CompanyFooterSection(onNavigate = onNavigate)
        }
    }
}

@Composable
private fun HeroBannerSection(
    onPlaceOrder: () -> Unit,
    onContactUs: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(TeaGreenDark, TeaGreenPrimary)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            // Top Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = TeaGold.copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(1.dp, TeaGold.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = TeaGoldLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PREMIUM WHOLESALE & RETAIL TEA",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGoldLight,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Brand Title
            Text(
                text = "SKT Tea Company",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "“Quality Tea, Trusted Service.”",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = TeaGoldLight,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Supplying high-aroma granular and golden leaf tea blends to commercial tea shops, hotels, and retail stores across Pakistan with reliable doorstep delivery.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.88f),
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Hero Image Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TeaGold.copy(alpha = 0.4f))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_tea),
                        contentDescription = "Fresh Tea Leaves and Amber Brew",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                                )
                            )
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(TeaGoldLight)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Fresh Crop • 100% Pure • Airtight Sealed",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onPlaceOrder,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("home_place_order_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = TeaGold),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalMall,
                        contentDescription = null,
                        tint = TeaGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Place Your Order",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGreenDark
                        )
                    )
                }

                OutlinedButton(
                    onClick = onContactUs,
                    modifier = Modifier
                        .weight(0.85f)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Contact Us",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun TrustMetricsStrip() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = TeaSurfaceVariant,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TrustMetricItem(number = "500+", label = "Shops Supplied")
            VerticalDivider()
            TrustMetricItem(number = "100%", label = "Pure Harvest")
            VerticalDivider()
            TrustMetricItem(number = "24-48h", label = "Fast Delivery")
        }
    }
}

@Composable
private fun TrustMetricItem(number: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TeaGreenPrimary
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TeaTextSecondary,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(28.dp)
            .background(Color.LightGray.copy(alpha = 0.6f))
    )
}

@Composable
private fun HowItWorksSection(onStartOrder: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(4.dp, 22.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(TeaGold)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "HOW IT WORKS",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaGreenPrimary,
                    letterSpacing = 1.5.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Simple 4-Step Ordering Process",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TeaTextPrimary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        val steps = listOf(
            StepData(
                number = "1",
                title = "Select Tea Quantity",
                desc = "Choose 250g, 500g, 1KG packs or enter custom bulk kilograms.",
                icon = Icons.Default.Eco
            ),
            StepData(
                number = "2",
                title = "Enter Shop Details",
                desc = "Provide your name, shop/hotel name, phone, and delivery address.",
                icon = Icons.Default.Storefront
            ),
            StepData(
                number = "3",
                title = "Review Order Summary",
                desc = "Verify blend type, total KG calculation, and delivery details.",
                icon = Icons.Default.ReceiptLong
            ),
            StepData(
                number = "4",
                title = "Submit & Track",
                desc = "Get unique SKT Order ID, instant WhatsApp dispatch tracking.",
                icon = Icons.Default.TrackChanges
            )
        )

        steps.forEach { step ->
            StepCardItem(step = step)
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onStartOrder,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TeaGreenPrimary),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Start Your Tea Order Now", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    }
}

private data class StepData(
    val number: String,
    val title: String,
    val desc: String,
    val icon: ImageVector
)

@Composable
private fun StepCardItem(step: StepData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E3DA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TeaGoldContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = step.number,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenDark
                    )
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = step.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = step.desc,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TeaTextSecondary,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun FeaturedProductsSection(
    onViewAll: () -> Unit,
    onOrderBlend: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3ECE1))
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "FEATURED BLENDS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenPrimary,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Top Tea Selections",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaTextPrimary
                    )
                )
            }

            OutlinedButton(
                onClick = onViewAll,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TeaGreenPrimary)
            ) {
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TeaGreenPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SKT_TEA_CATALOG.take(3).forEach { blend ->
            FeaturedBlendCard(blend = blend, onOrderClick = { onOrderBlend(blend.name) })
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun FeaturedBlendCard(
    blend: TeaBlend,
    onOrderClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = blend.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGreenPrimary
                        )
                    )
                    Text(
                        text = blend.grade,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TeaGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = TeaGreenContainer
                ) {
                    Text(
                        text = "250g • 500g • 1kg",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGreenDark,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = blend.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TeaTextSecondary,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = TeaCreamBg
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = null,
                        tint = TeaGreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Best For: ${blend.recommendedFor}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TeaTextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onOrderClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = TeaGreenPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocalMall,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Order This Tea Blend", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun WhyChooseSktSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {
        Text(
            text = "WHY SKT TEA",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TeaGold,
                letterSpacing = 1.sp
            )
        )
        Text(
            text = "Committed to Pure Flavor & Fast Delivery",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TeaTextPrimary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        val pillars = listOf(
            Triple(Icons.Default.Eco, "100% Pure Harvest Leaves", "Directly sourced from premium gardens without synthetic colors, additives, or stale fillers."),
            Triple(Icons.Default.Security, "Airtight Seal Packaging", "Nitrogen-flushed master bags ensuring maximum aroma preservation and long shelf life."),
            Triple(Icons.Default.LocalShipping, "Dedicated Shop Route Delivery", "Same-day or next-day direct commercial delivery right to your shop counter."),
            Triple(Icons.Default.Star, "Competitive Wholesale Margins", "Transparent tier pricing designed to maximize profit margins for tea sellers.")
        )

        pillars.forEach { (icon, title, desc) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(TeaGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = TeaGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaTextPrimary
                        )
                    )
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TeaTextSecondary,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun WhatsAppCalloutBanner(
    onWhatsAppClick: () -> Unit,
    onTrackClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = TeaGreenDark)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
                tint = TeaGold,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Need Instant Help with an Order?",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Our management team is active on WhatsApp to take custom requests, confirm deliveries, and answer inquiries.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onWhatsAppClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Chat on WhatsApp", color = Color.White, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onTrackClick,
                    modifier = Modifier.weight(0.9f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TeaGoldLight),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TeaGoldLight)
                ) {
                    Text("Track Order ID", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ExecutiveLeadershipPreview(onReadMore: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2DBD0))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "SKT LEADERSHIP",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaGold,
                    letterSpacing = 1.5.sp
                )
            )
            Text(
                text = "Guided by Experience & Trust",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaGreenPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LeaderCard(name = "Muhammad Azam", role = "Company Owner", modifier = Modifier.weight(1f))
                LeaderCard(name = "Muhammad Zeeshan", role = "General Manager", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onReadMore,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("View Company Profile & Contact Details")
            }
        }
    }
}

@Composable
private fun LeaderCard(name: String, role: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = TeaCreamBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaTextPrimary
                )
            )
            Text(
                text = role,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TeaGreenPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
private fun CompanyFooterSection(onNavigate: (NavTab) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(TeaGreenDark)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SktBrandLogo(size = 34, showText = true)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Quality Tea, Trusted Service.",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TeaGoldLight,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Owner: Muhammad Azam  •  Manager: Muhammad Zeeshan",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White.copy(alpha = 0.75f)
                ),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "© 2026 SKT Tea Company. All Rights Reserved.",
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp
            )
        )
    }
}
