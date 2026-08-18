package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.OrderEntity
import com.example.ui.theme.StatusCancelled
import com.example.ui.theme.StatusConfirmed
import com.example.ui.theme.StatusDelivered
import com.example.ui.theme.StatusNew
import com.example.ui.theme.StatusOutForDelivery
import com.example.ui.theme.StatusProcessing
import com.example.ui.theme.TeaCreamBg
import com.example.ui.theme.TeaGold
import com.example.ui.theme.TeaGoldContainer
import com.example.ui.theme.TeaGreenDark
import com.example.ui.theme.TeaGreenLight
import com.example.ui.theme.TeaGreenPrimary
import com.example.ui.theme.TeaTextPrimary
import com.example.ui.theme.TeaTextSecondary
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Utility: Format Date
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun formatDateOnly(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

// Utility: Open WhatsApp or Share
fun launchWhatsApp(context: Context, message: String, phoneNumber: String = "+923318701808") {
    try {
        val cleanNumber = phoneNumber.replace("+", "").replace("-", "").replace(" ", "")
        val encodedMessage = URLEncoder.encode(message, "UTF-8")
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanNumber&text=$encodedMessage")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback to general text share intent
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Share SK Tea Order"))
    }
}

fun makePhoneCall(context: Context, phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phoneNumber.replace(" ", "")}"))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Contact: $phoneNumber", Toast.LENGTH_SHORT).show()
    }
}

// SK Logo Brand Badge
@Composable
fun SktBrandLogo(
    modifier: Modifier = Modifier,
    size: Int = 40,
    showText: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, TeaGold.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .background(TeaGreenDark),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_skt_logo),
                contentDescription = "SK Tea Logo",
                modifier = Modifier.size((size * 0.9).dp),
                contentScale = ContentScale.Crop
            )
        }

        if (showText) {
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "SK",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "TEA",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
                Text(
                    text = "COMPANY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp,
                        color = TeaGold.copy(alpha = 0.85f)
                    )
                )
            }
        }
    }
}

// Order Status Badge
@Composable
fun SktStatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon) = when (status.lowercase(Locale.ROOT)) {
        "new" -> Triple(StatusNew.copy(alpha = 0.12f), StatusNew, Icons.Default.NewReleases)
        "confirmed" -> Triple(StatusConfirmed.copy(alpha = 0.12f), StatusConfirmed, Icons.Default.Verified)
        "processing" -> Triple(StatusProcessing.copy(alpha = 0.12f), StatusProcessing, Icons.Default.HourglassEmpty)
        "out for delivery" -> Triple(StatusOutForDelivery.copy(alpha = 0.12f), StatusOutForDelivery, Icons.Default.DeliveryDining)
        "delivered" -> Triple(StatusDelivered.copy(alpha = 0.12f), StatusDelivered, Icons.Default.DoneAll)
        "cancelled" -> Triple(StatusCancelled.copy(alpha = 0.12f), StatusCancelled, Icons.Default.Close)
        else -> Triple(Color.Gray.copy(alpha = 0.12f), Color.DarkGray, Icons.Default.CheckCircle)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )
        }
    }
}

// Metric Stat Card
@Composable
fun SktStatCard(
    title: String,
    value: String,
    subtitle: String? = null,
    icon: ImageVector,
    accentColor: Color = TeaGreenPrimary,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TeaTextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaTextPrimary
                    )
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = accentColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

// Order Timeline Stepper
@Composable
fun OrderTimelineStepper(
    currentStatus: String,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        "New" to "Order Placed",
        "Confirmed" to "Verified by SK Tea",
        "Processing" to "Packed & Sealed",
        "Out for Delivery" to "Dispatched to Shop",
        "Delivered" to "Successfully Received"
    )

    val isCancelled = currentStatus.equals("Cancelled", ignoreCase = true)

    val currentIndex = when (currentStatus.lowercase(Locale.ROOT)) {
        "new" -> 0
        "confirmed" -> 1
        "processing" -> 2
        "out for delivery" -> 3
        "delivered" -> 4
        else -> 0
    }

    if (isCancelled) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = StatusCancelled.copy(alpha = 0.1f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, StatusCancelled.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = StatusCancelled,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Order Cancelled",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StatusCancelled
                        )
                    )
                    Text(
                        text = "This order has been cancelled. For inquiries or reordering, please contact SK Tea management.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TeaTextSecondary)
                    )
                }
            }
        }
        return
    }

    Column(modifier = modifier.fillMaxWidth()) {
        steps.forEachIndexed { index, (statusName, desc) ->
            val isCompleted = index <= currentIndex
            val isCurrent = index == currentIndex
            val stepColor = if (isCompleted) TeaGreenPrimary else Color.LightGray

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(36.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) TeaGreenPrimary else Color(0xFFE5E7EB))
                            .border(
                                width = if (isCurrent) 2.dp else 0.dp,
                                color = if (isCurrent) TeaGold else Color.Transparent,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.Gray,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    if (index < steps.size - 1) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(36.dp)
                                .background(if (index < currentIndex) TeaGreenPrimary else Color(0xFFE5E7EB))
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.padding(bottom = if (index < steps.size - 1) 18.dp else 0.dp)) {
                    Text(
                        text = statusName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (isCompleted) TeaTextPrimary else Color.Gray
                        )
                    )
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isCompleted) TeaTextSecondary else Color.LightGray
                        )
                    )
                }
            }
        }
    }
}

// Order Review Dialog
@Composable
fun OrderReviewDialog(
    customerName: String,
    shopName: String,
    mobileNumber: String,
    address: String,
    city: String,
    teaBlend: String,
    teaSize: String,
    unitCount: Int,
    totalKg: Double,
    notes: String,
    isSubmitting: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isSubmitting) onDismiss() },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = TeaGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Review Your Order",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenPrimary
                    )
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Please verify all tea quantity and shop delivery details before submitting:",
                    style = MaterialTheme.typography.bodySmall.copy(color = TeaTextSecondary)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = TeaCreamBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TeaGold.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        ReviewRow("Customer Name", customerName)
                        ReviewRow("Shop Name", shopName)
                        ReviewRow("Mobile Number", mobileNumber)
                        ReviewRow("City / Area", city)
                        ReviewRow("Full Address", address)
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 6.dp),
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )
                        ReviewRow("Tea Selection", teaBlend, isBold = true)
                        ReviewRow("Packages Selected", teaSize)
                        ReviewRow("Total Packets", "$unitCount packets")
                        ReviewRow("Total Weight", "${String.format(Locale.US, "%.2f", totalKg)} KG", isHighlight = true)
                        if (notes.isNotBlank()) {
                            ReviewRow("Special Notes", notes)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isSubmitting,
                modifier = Modifier.testTag("confirm_submit_order_button"),
                colors = ButtonDefaults.buttonColors(containerColor = TeaGreenPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submitting...")
                } else {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Confirm & Place Order")
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isSubmitting,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Edit Details")
            }
        }
    )
}

@Composable
private fun ReviewRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(color = TeaTextSecondary),
            modifier = Modifier.weight(0.45f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isHighlight || isBold) FontWeight.Bold else FontWeight.Normal,
                color = if (isHighlight) TeaGreenPrimary else TeaTextPrimary,
                textAlign = TextAlign.End
            ),
            modifier = Modifier.weight(0.55f)
        )
    }
}
