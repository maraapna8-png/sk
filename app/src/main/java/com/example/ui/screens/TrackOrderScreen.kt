package com.example.ui.screens

import com.example.ui.theme.TeaBorder

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.OrderEntity
import com.example.ui.components.OrderTimelineStepper
import com.example.ui.components.SktStatusBadge
import com.example.ui.components.formatTimestamp
import com.example.ui.components.launchWhatsApp
import com.example.ui.model.NavTab
import com.example.ui.theme.TeaCreamBg
import com.example.ui.theme.TeaGold
import com.example.ui.theme.TeaGoldContainer
import com.example.ui.theme.TeaGreenContainer
import com.example.ui.theme.TeaGreenDark
import com.example.ui.theme.TeaGreenPrimary
import com.example.ui.theme.TeaSurfaceVariant
import com.example.ui.theme.TeaTextPrimary
import com.example.ui.theme.TeaTextSecondary
import com.example.ui.viewmodel.SktViewModel
import java.util.Locale

@Composable
fun TrackOrderScreen(
    viewModel: SktViewModel,
    onNavigate: (NavTab) -> Unit
) {
    val query by viewModel.trackingQuery.collectAsStateWithLifecycle()
    val trackedOrder by viewModel.trackedOrder.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearchingTracking.collectAsStateWithLifecycle()
    val trackingMessage by viewModel.trackingMessage.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TeaCreamBg)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        item {
            Column {
                Text(
                    text = "LIVE DISPATCH TRACKING",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TeaGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                )
                Text(
                    text = "Track Your Tea Delivery",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Enter your Order ID (e.g. SK-001042) to check real-time packing, preparation, and route dispatch status.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TeaTextSecondary)
                )
            }
        }

        // Search Input Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { viewModel.updateTrackingQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("track_order_input"),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        label = { Text("Order ID or Mobile Number") },
                        placeholder = { Text("e.g. SK-001042 or 0331-8701808") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = TeaGreenPrimary
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { viewModel.trackOrder() }),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedBorderColor = TeaGreenPrimary,
                            unfocusedBorderColor = TeaBorder,
                            focusedLabelColor = TeaGreenPrimary,
                            unfocusedLabelColor = Color(0xFF444444),
                            focusedPlaceholderColor = Color(0xFF777777),
                            unfocusedPlaceholderColor = Color(0xFF888888),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.trackOrder() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("track_order_submit_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = TeaGreenPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isSearching) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Locating Order...")
                        } else {
                            Icon(
                                imageVector = Icons.Default.TrackChanges,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Track Order", fontWeight = FontWeight.Bold)
                        }
                    }

                    // Quick Sample Order Chips for quick testing
                    if (allOrders.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Recent Order IDs to Track:",
                            style = MaterialTheme.typography.labelSmall.copy(color = TeaTextSecondary)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            allOrders.take(3).forEach { order ->
                                Surface(
                                    modifier = Modifier.clickable {
                                        viewModel.updateTrackingQuery(order.orderNumber)
                                        viewModel.trackOrder(order.orderNumber)
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    color = TeaCreamBg,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, TeaGold.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = order.orderNumber,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TeaGreenDark,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Error / Not found message
        if (trackingMessage != null) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEF2F2),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = trackingMessage!!,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF991B1B))
                        )
                    }
                }
            }
        }

        // Tracked Order Details Card
        if (trackedOrder != null) {
            item {
                val order = trackedOrder!!
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TeaGold.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "TRACKING RESULT",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TeaGold,
                                        letterSpacing = 1.sp
                                    )
                                )
                                Text(
                                    text = order.orderNumber,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TeaGreenDark
                                    )
                                )
                            }

                            SktStatusBadge(status = order.status)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFEFE8DE))
                        Spacer(modifier = Modifier.height(16.dp))

                        // Visual Stepper
                        Text(
                            text = "Delivery Timeline Progress",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TeaTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OrderTimelineStepper(currentStatus = order.status)

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFEFE8DE))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Customer & Shop Info
                        Text(
                            text = "Customer & Delivery Address",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TeaGreenPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        TrackRow("Customer", order.customerName)
                        TrackRow("Shop / Hotel", order.shopName)
                        TrackRow("Mobile", order.mobileNumber)
                        TrackRow("Destination", "${order.address}, ${order.city}")
                        TrackRow("Order Date", formatTimestamp(order.timestamp))
                        TrackRow("Last Update", formatTimestamp(order.updatedTimestamp))

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = Color(0xFFEFE8DE))
                        Spacer(modifier = Modifier.height(10.dp))

                        // Tea Blend & KG Info
                        Text(
                            text = "Tea Selection & Weight",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TeaGreenPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        TrackRow("Tea Blend", order.teaBlend, isBold = true)
                        TrackRow("Package Size", order.teaSize)
                        TrackRow("Units Ordered", "${order.unitCount} packets")
                        TrackRow(
                            "Total Volume",
                            "${String.format(Locale.US, "%.2f", order.totalKg)} KG",
                            isHighlight = true
                        )

                        if (order.notes.isNotBlank()) {
                            TrackRow("Special Notes", order.notes)
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Quick Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val msg = """
                                        *SK Order Status Inquiry*
                                        Order ID: ${order.orderNumber}
                                        Customer: ${order.customerName}
                                        Shop: ${order.shopName}
                                        Status: ${order.status}
                                        Total KG: ${order.totalKg} KG
                                        Please provide delivery ETA.
                                    """.trimIndent()
                                    launchWhatsApp(context, msg)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("WhatsApp SK", color = Color.White, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.reorder(order)
                                },
                                modifier = Modifier.weight(0.85f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, TeaGreenPrimary)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Replay,
                                    contentDescription = null,
                                    tint = TeaGreenPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reorder", color = TeaGreenPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
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
private fun TrackRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(color = TeaTextSecondary),
            modifier = Modifier.weight(0.38f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isHighlight || isBold) FontWeight.Bold else FontWeight.Normal,
                color = if (isHighlight) TeaGreenPrimary else TeaTextPrimary,
                textAlign = TextAlign.End
            ),
            modifier = Modifier.weight(0.62f)
        )
    }
}
