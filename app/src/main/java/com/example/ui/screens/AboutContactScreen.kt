package com.example.ui.screens

import com.example.ui.theme.TeaBorder

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SktBrandLogo
import com.example.ui.components.launchWhatsApp
import com.example.ui.components.makePhoneCall
import com.example.ui.model.COMPANY_CONTACT
import com.example.ui.model.NavTab
import com.example.ui.theme.TeaCreamBg
import com.example.ui.theme.TeaGold
import com.example.ui.theme.TeaGoldContainer
import com.example.ui.theme.TeaGoldLight
import com.example.ui.theme.TeaGreenContainer
import com.example.ui.theme.TeaGreenDark
import com.example.ui.theme.TeaGreenPrimary
import com.example.ui.theme.TeaTextPrimary
import com.example.ui.theme.TeaTextSecondary

@Composable
fun AboutContactScreen(
    onNavigate: (NavTab) -> Unit
) {
    val context = LocalContext.current
    var inquiryName by remember { mutableStateOf("") }
    var inquiryShop by remember { mutableStateOf("") }
    var inquiryMessage by remember { mutableStateOf("") }
    var inquirySent by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TeaCreamBg)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TeaGreenDark)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SktBrandLogo(size = 48, showText = false)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "SK Tea Company",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "“Quality Tea, Trusted Service.”",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TeaGoldLight,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    )
                }
            }
        }

        // 2. Company Story & Heritage
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(4.dp, 20.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(TeaGold)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "OUR COMPANY & MISSION",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TeaGreenPrimary,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "SK Tea Company is dedicated to supplying the finest, unadulterated tea leaves and granulated CTC blends to tea stalls, hotels, restaurants, and retail grocers.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TeaTextPrimary,
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "We ensure that every batch retains its natural briskness, deep golden-amber liquor, and rich authentic aroma. Our mission is to make premium tea accessible, reliable, and profitable for commercial tea vendors.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TeaTextSecondary,
                            lineHeight = 22.sp
                        )
                    )
                }
            }
        }

        // 3. Payment Method & Instructions (COD ONLY)
        item {
            PaymentInstructionsSection()
        }

        // 4. Frequently Asked Questions (FAQs)
        item {
            FaqSection()
        }

        // 4. Executive Leadership
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "EXECUTIVE LEADERSHIP",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGold,
                            letterSpacing = 1.2.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Owner: Muhammad Azam
                    LeaderContactRow(
                        name = "Muhammad Azam",
                        role = "Owner & Founder",
                        phone = COMPANY_CONTACT.ownerPhone,
                        onCall = { makePhoneCall(context, COMPANY_CONTACT.ownerPhone) },
                        onWhatsApp = {
                            launchWhatsApp(
                                context,
                                "Hello Muhammad Azam sb, I would like to discuss business with SK Tea Company.",
                                COMPANY_CONTACT.ownerPhone
                            )
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEFE8DE))

                    // Manager: Muhammad Zeeshan
                    LeaderContactRow(
                        name = "Muhammad Zeeshan",
                        role = "General Manager",
                        phone = COMPANY_CONTACT.managerPhone,
                        onCall = { makePhoneCall(context, COMPANY_CONTACT.managerPhone) },
                        onWhatsApp = {
                            launchWhatsApp(
                                context,
                                "Hello Muhammad Zeeshan sb, I would like to inquire about SK Tea order supply.",
                                COMPANY_CONTACT.managerPhone
                            )
                        }
                    )
                }
            }
        }

        // 4. Official Contact Details & Warehouse
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "OFFICIAL CONTACT & DISPATCH",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGold,
                            letterSpacing = 1.2.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ContactDetailRow(
                        icon = Icons.Default.Phone,
                        title = "Order Helpline",
                        value = COMPANY_CONTACT.ownerPhone,
                        actionLabel = "Call",
                        onAction = { makePhoneCall(context, COMPANY_CONTACT.ownerPhone) }
                    )

                    ContactDetailRow(
                        icon = Icons.Default.Chat,
                        title = "Official WhatsApp",
                        value = COMPANY_CONTACT.whatsappNumber,
                        actionLabel = "Chat",
                        onAction = {
                            launchWhatsApp(
                                context,
                                "Hello SK Tea Company, I am inquiring about tea supply.",
                                COMPANY_CONTACT.whatsappNumber
                            )
                        }
                    )

                    ContactDetailRow(
                        icon = Icons.Default.LocationOn,
                        title = "Head Office & Dispatch Center",
                        value = "${COMPANY_CONTACT.address}, ${COMPANY_CONTACT.city}",
                        actionLabel = null,
                        onAction = {}
                    )

                    ContactDetailRow(
                        icon = Icons.Default.AccessTime,
                        title = "Operating Hours",
                        value = COMPANY_CONTACT.hours,
                        actionLabel = null,
                        onAction = {}
                    )
                }
            }
        }

        // 5. Send Direct Inquiry Form
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "DIRECT INQUIRY & FEEDBACK",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGold,
                            letterSpacing = 1.2.sp
                        )
                    )
                    Text(
                        text = "Send a Message to Management",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGreenPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (inquirySent) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = TeaGreenContainer
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Thank you! Your message has been prepared for dispatch.",
                                    fontWeight = FontWeight.Bold,
                                    color = TeaGreenDark
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    OutlinedTextField(
                        value = inquiryName,
                        onValueChange = { inquiryName = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        label = { Text("Your Name") },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedBorderColor = TeaGreenPrimary,
                            unfocusedBorderColor = TeaBorder,
                            focusedLabelColor = TeaGreenPrimary,
                            unfocusedLabelColor = Color(0xFF444444),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inquiryShop,
                        onValueChange = { inquiryShop = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        label = { Text("Shop / Business Name") },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedBorderColor = TeaGreenPrimary,
                            unfocusedBorderColor = TeaBorder,
                            focusedLabelColor = TeaGreenPrimary,
                            unfocusedLabelColor = Color(0xFF444444),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inquiryMessage,
                        onValueChange = { inquiryMessage = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        label = { Text("Your Message / Requirement") },
                        minLines = 3,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedBorderColor = TeaGreenPrimary,
                            unfocusedBorderColor = TeaBorder,
                            focusedLabelColor = TeaGreenPrimary,
                            unfocusedLabelColor = Color(0xFF444444),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val msg = """
                                *SK Tea Customer Inquiry*
                                Name: ${inquiryName.ifBlank { "Client" }}
                                Shop: ${inquiryShop.ifBlank { "N/A" }}
                                Message: ${inquiryMessage.ifBlank { "Requesting wholesale price quote" }}
                            """.trimIndent()
                            inquirySent = true
                            launchWhatsApp(context, msg)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Send via WhatsApp", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LeaderContactRow(
    name: String,
    role: String,
    phone: String,
    onCall: () -> Unit,
    onWhatsApp: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium.copy(
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
            Text(
                text = phone,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TeaTextSecondary
                )
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            IconButton(
                onClick = onCall,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(TeaGreenContainer)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Call",
                    tint = TeaGreenDark,
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(
                onClick = onWhatsApp,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF25D366).copy(alpha = 0.2f))
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "WhatsApp",
                    tint = Color(0xFF15803D),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun ContactDetailRow(
    icon: ImageVector,
    title: String,
    value: String,
    actionLabel: String?,
    onAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TeaGreenPrimary,
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaTextSecondary
                    )
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TeaTextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        if (actionLabel != null) {
            OutlinedButton(
                onClick = onAction,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(actionLabel, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private data class FaqData(
    val question: String,
    val answer: String
)

@Composable
private fun FaqSection() {
    val faqs = listOf(
        FaqData(
            question = "How can I place a wholesale tea order for my shop or hotel?",
            answer = "You can place your order directly using the 'Place Order' tab in this app, or message our sales team on WhatsApp (0306-6922254). Choose your blend and package sizes, and we will dispatch directly to your address."
        ),
        FaqData(
            question = "What is the minimum order quantity (MOQ)?",
            answer = "For retail consumer packets (125g, 250g, 500g, 1kg), the minimum total order is 5 Kg. For commercial bulk sacks, you can order from 10 Kg up to custom bulk sacks."
        ),
        FaqData(
            question = "What delivery areas do you cover across Pakistan?",
            answer = "We provide same-day / next-day delivery across Lahore and fast cargo dispatch services to all major cities in Punjab and nationwide across Pakistan."
        ),
        FaqData(
            question = "Are factory wholesale rates guaranteed?",
            answer = "Yes! SK Tea supplies directly from our primary processing center in Akbari Mandi, Lahore, bypassing all middlemen to offer maximum profitability to shopkeepers and hotel owners."
        ),
        FaqData(
            question = "How can I track my order after submitting?",
            answer = "Open the 'Track Order' tab in this app and enter your Mobile Number or Order ID to check your live order status (Received, Packing, Out for Delivery, Delivered)."
        ),
        FaqData(
            question = "What payment methods are accepted?",
            answer = "We accept Cash on Delivery (COD) ONLY. No advance online payment or bank deposit is required. You pay cash directly to the delivery rider or transport courier when your tea parcel arrives."
        )
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = null,
                    tint = TeaGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "FREQUENTLY ASKED QUESTIONS (FAQs)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenPrimary,
                        letterSpacing = 1.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            faqs.forEachIndexed { index, faq ->
                FaqItemRow(faq = faq)
                if (index < faqs.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = Color(0xFFEFE8DE)
                    )
                }
            }
        }
    }
}

@Composable
private fun FaqItemRow(faq: FaqData) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { expanded = !expanded }
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = faq.question,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TeaTextPrimary
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = TeaGreenPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = faq.answer,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TeaTextSecondary,
                        lineHeight = 20.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun PaymentInstructionsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = null,
                        tint = TeaGreenPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PAYMENT METHOD & TERMS",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGreenPrimary,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = TeaGreenContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = TeaGreenDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "COD ONLY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TeaGreenDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = TeaGoldContainer.copy(alpha = 0.35f),
                border = androidx.compose.foundation.BorderStroke(1.dp, TeaGold.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = null,
                        tint = TeaGreenDark,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "100% Cash on Delivery (COD)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TeaGreenDark
                            )
                        )
                        Text(
                            text = "We strictly accept payments via Cash on Delivery upon order arrival.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TeaTextSecondary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "How Payment Works:",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            PaymentStepRow(
                stepNum = "1",
                title = "No Advance Payment Required",
                desc = "Place your wholesale tea order online without paying any advance deposit or online fee."
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFEFE8DE))

            PaymentStepRow(
                stepNum = "2",
                title = "Order Dispatch & Bill Generation",
                desc = "Your tea order is packaged and dispatched from Akbari Mandi with an official printed invoice."
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFEFE8DE))

            PaymentStepRow(
                stepNum = "3",
                title = "Pay Cash Upon Delivery",
                desc = "When the courier rider or cargo transport arrives at your shop/hotel, pay the exact cash amount on the bill."
            )
        }
    }
}

@Composable
private fun PaymentStepRow(
    stepNum: String,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(TeaGreenPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNum,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaTextPrimary,
                    fontSize = 13.sp
                )
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TeaTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            )
        }
    }
}
