package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.OrderEntity
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
import com.example.ui.theme.TeaTextPrimary
import com.example.ui.theme.TeaTextSecondary
import com.example.ui.viewmodel.SktViewModel
import java.util.Locale

@Composable
fun OrderHistoryScreen(
    viewModel: SktViewModel,
    onNavigate: (NavTab) -> Unit
) {
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val mobileFilter by viewModel.historyMobileFilter.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val filteredOrders = if (mobileFilter.isBlank()) {
        allOrders
    } else {
        val q = mobileFilter.trim().lowercase(Locale.ROOT)
        allOrders.filter {
            it.mobileNumber.contains(q) ||
            it.customerName.lowercase(Locale.ROOT).contains(q) ||
            it.shopName.lowercase(Locale.ROOT).contains(q) ||
            it.orderNumber.lowercase(Locale.ROOT).contains(q)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TeaCreamBg)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "CUSTOMER ORDER RECORDS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TeaGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                )
                Text(
                    text = "Order History & Quick Reorder",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Search by your phone number or shop name to review previous deliveries and quickly place repeat orders.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TeaTextSecondary)
                )
            }
        }

        // Search Filter
        item {
            OutlinedTextField(
                value = mobileFilter,
                onValueChange = { viewModel.updateHistoryMobileFilter(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Filter by Mobile Number, Shop or Order ID") },
                placeholder = { Text("e.g. 0300-8451293 or Bismillah") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = TeaGreenPrimary
                    )
                },
                shape = RoundedCornerShape(10.dp)
            )
        }

        // Empty state
        if (filteredOrders.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(TeaGreenContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = TeaGreenPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "No Orders Found",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TeaTextPrimary
                            )
                        )

                        Text(
                            text = if (mobileFilter.isBlank()) "You have not placed any orders yet." else "No orders matching '$mobileFilter'.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TeaTextSecondary,
                                textAlign = TextAlign.Center
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { onNavigate(NavTab.PlaceOrder) },
                            colors = ButtonDefaults.buttonColors(containerColor = TeaGreenPrimary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.LocalMall, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Place a New Order")
                        }
                    }
                }
            }
        } else {
            items(filteredOrders) { order ->
                OrderHistoryCard(
                    order = order,
                    onReorder = {
                        viewModel.reorder(order)
                    },
                    onTrack = {
                        viewModel.updateTrackingQuery(order.orderNumber)
                        viewModel.trackOrder(order.orderNumber)
                        onNavigate(NavTab.TrackOrder)
                    },
                    onShareWhatsApp = {
                        val msg = """
                            *SKT Tea Order Receipt*
                            Order ID: ${order.orderNumber}
                            Shop Name: ${order.shopName}
                            Customer: ${order.customerName}
                            Tea Blend: ${order.teaBlend} (${order.teaSize})
                            Total Volume: ${order.totalKg} KG (${order.unitCount} units)
                            Status: ${order.status}
                            Address: ${order.address}, ${order.city}
                        """.trimIndent()
                        launchWhatsApp(context, msg)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun OrderHistoryCard(
    order: OrderEntity,
    onReorder: () -> Unit,
    onTrack: () -> Unit,
    onShareWhatsApp: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Order Number + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = order.orderNumber,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGreenPrimary
                        )
                    )
                    Text(
                        text = formatTimestamp(order.timestamp),
                        style = MaterialTheme.typography.labelSmall.copy(color = TeaTextSecondary)
                    )
                }

                SktStatusBadge(status = order.status)
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFEFE8DE))
            Spacer(modifier = Modifier.height(10.dp))

            // Shop and Blend details
            Text(
                text = "${order.shopName} (${order.customerName})",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${order.teaBlend} • ${order.teaSize} (${order.unitCount} units)",
                style = MaterialTheme.typography.bodySmall.copy(color = TeaTextSecondary)
            )

            Text(
                text = "Total Quantity: ${String.format(Locale.US, "%.2f", order.totalKg)} KG",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaGreenPrimary
                )
            )

            Text(
                text = "Delivery to: ${order.address}, ${order.city}",
                style = MaterialTheme.typography.bodySmall.copy(color = TeaTextSecondary),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onReorder,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = TeaGreenPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reorder", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onTrack,
                    modifier = Modifier.weight(0.9f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TeaGold)
                ) {
                    Icon(
                        imageVector = Icons.Default.TrackChanges,
                        contentDescription = null,
                        tint = TeaGreenDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Track", color = TeaGreenDark, fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = onShareWhatsApp,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF25D366).copy(alpha = 0.15f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color(0xFF15803D),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
