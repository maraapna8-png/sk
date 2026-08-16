package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.CustomerEntity
import com.example.data.local.entity.OrderEntity
import com.example.ui.components.SktBrandLogo
import com.example.ui.components.SktStatCard
import com.example.ui.components.SktStatusBadge
import com.example.ui.components.formatTimestamp
import com.example.ui.components.launchWhatsApp
import com.example.ui.components.makePhoneCall
import com.example.ui.model.AUTHORIZED_ADMINS
import com.example.ui.model.AdminAccount
import com.example.ui.theme.StatusCancelled
import com.example.ui.theme.StatusConfirmed
import com.example.ui.theme.StatusDelivered
import com.example.ui.theme.StatusNew
import com.example.ui.theme.StatusOutForDelivery
import com.example.ui.theme.StatusProcessing
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
fun AdminDashboardScreen(
    viewModel: SktViewModel
) {
    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsStateWithLifecycle()
    val loggedInAdmin by viewModel.loggedInAdmin.collectAsStateWithLifecycle()
    val pinInput by viewModel.adminPinInput.collectAsStateWithLifecycle()
    val authError by viewModel.adminAuthError.collectAsStateWithLifecycle()

    val stats by viewModel.dashboardStats.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val allCustomers by viewModel.allCustomers.collectAsStateWithLifecycle()

    val searchQuery by viewModel.adminSearchQuery.collectAsStateWithLifecycle()
    val statusFilter by viewModel.adminStatusFilter.collectAsStateWithLifecycle()
    val selectedOrder by viewModel.selectedAdminOrder.collectAsStateWithLifecycle()
    val selectedCustomer by viewModel.selectedAdminCustomer.collectAsStateWithLifecycle()

    var adminSectionTab by remember { mutableIntStateOf(0) } // 0: Dashboard, 1: Orders, 2: Customers

    // If NOT logged in, show secure login prompt
    if (!isAdminLoggedIn) {
        AdminLoginScreen(
            pinInput = pinInput,
            authError = authError,
            onPinChange = { viewModel.updateAdminPin(it) },
            onLogin = { viewModel.loginAdminWithPin() },
            onQuickSelect = { viewModel.quickLoginAs(it) }
        )
        return
    }

    // Modal: Admin Order Detail & Status Modifier Dialog
    if (selectedOrder != null) {
        AdminOrderDetailDialog(
            order = selectedOrder!!,
            onStatusChange = { newStatus ->
                viewModel.updateStatus(selectedOrder!!.id, newStatus)
            },
            onDismiss = { viewModel.selectAdminOrder(null) }
        )
    }

    // Modal: Customer Profile & Order History Dialog
    if (selectedCustomer != null) {
        val customerOrders = allOrders.filter {
            it.mobileNumber.replace("-", "").trim() == selectedCustomer!!.mobileNumber.replace("-", "").trim()
        }
        AdminCustomerDetailDialog(
            customer = selectedCustomer!!,
            orders = customerOrders,
            onDismiss = { viewModel.selectAdminCustomer(null) },
            onOpenOrder = { order ->
                viewModel.selectAdminCustomer(null)
                viewModel.selectAdminOrder(order)
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TeaCreamBg)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Admin Top Bar & Session Info
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = TeaGreenDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(TeaGoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = TeaGreenDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = loggedInAdmin?.name ?: "Muhammad Azam",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "${loggedInAdmin?.role ?: "Owner"} • SK Admin Portal",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TeaGold,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = { viewModel.logoutAdmin() },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Sub-navigation Tabs (Dashboard | Orders | Customers)
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Dashboard", "Orders (${allOrders.size})", "Customers (${allCustomers.size})").forEachIndexed { index, title ->
                        val isSelected = adminSectionTab == index
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { adminSectionTab = index },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) TeaGreenPrimary else Color.Transparent
                        ) {
                            Text(
                                text = title,
                                modifier = Modifier.padding(vertical = 10.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TeaTextPrimary,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }

        // TAB 0: Real-Time Business Dashboard
        if (adminSectionTab == 0) {
            item {
                Text(
                    text = "Live Order Statistics & Volume",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenPrimary
                    )
                )
            }

            // Stat Cards Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SktStatCard(
                            title = "Total Orders",
                            value = "${stats.totalOrders}",
                            subtitle = "${String.format(Locale.US, "%.1f", stats.totalKgOrdered)} KG Tea",
                            icon = Icons.Default.ReceiptLong,
                            accentColor = TeaGreenPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        SktStatCard(
                            title = "New Orders",
                            value = "${stats.newOrders}",
                            subtitle = "Action Needed",
                            icon = Icons.Default.NewReleases,
                            accentColor = StatusNew,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SktStatCard(
                            title = "Confirmed",
                            value = "${stats.confirmedOrders}",
                            subtitle = "Verified",
                            icon = Icons.Default.Verified,
                            accentColor = StatusConfirmed,
                            modifier = Modifier.weight(1f)
                        )
                        SktStatCard(
                            title = "Processing",
                            value = "${stats.processingOrders}",
                            subtitle = "Packing",
                            icon = Icons.Default.HourglassEmpty,
                            accentColor = StatusProcessing,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SktStatCard(
                            title = "Out for Delivery",
                            value = "${stats.outForDeliveryOrders}",
                            subtitle = "On Route",
                            icon = Icons.Default.DeliveryDining,
                            accentColor = StatusOutForDelivery,
                            modifier = Modifier.weight(1f)
                        )
                        SktStatCard(
                            title = "Delivered",
                            value = "${stats.deliveredOrders}",
                            subtitle = "Completed",
                            icon = Icons.Default.DoneAll,
                            accentColor = StatusDelivered,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SktStatCard(
                            title = "Total Tea (KG)",
                            value = "${String.format(Locale.US, "%.1f", stats.totalKgOrdered)} KG",
                            subtitle = "Gross Volume",
                            icon = Icons.Default.Scale,
                            accentColor = TeaGold,
                            modifier = Modifier.weight(1f)
                        )
                        SktStatCard(
                            title = "Client Shops",
                            value = "${stats.totalCustomers}",
                            subtitle = "Registered",
                            icon = Icons.Default.People,
                            accentColor = TeaGreenPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Recent Orders Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Incoming Orders",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaTextPrimary
                        )
                    )
                    OutlinedButton(
                        onClick = { adminSectionTab = 1 },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("View All", fontSize = 12.sp)
                    }
                }
            }

            // Recent Orders Preview
            items(allOrders.take(4)) { order ->
                AdminOrderCard(order = order, onClick = { viewModel.selectAdminOrder(order) })
            }
        }

        // TAB 1: Complete Order Management (Search, Status Filter, Details & Change)
        if (adminSectionTab == 1) {
            item {
                AdminOrderSearchAndFilter(
                    searchQuery = searchQuery,
                    onSearchChange = { viewModel.updateAdminSearch(it) },
                    selectedFilter = statusFilter,
                    onFilterChange = { viewModel.updateAdminStatusFilter(it) }
                )
            }

            val filteredOrders = allOrders.filter { order ->
                val matchesSearch = searchQuery.isBlank() ||
                    order.orderNumber.contains(searchQuery, ignoreCase = true) ||
                    order.customerName.contains(searchQuery, ignoreCase = true) ||
                    order.shopName.contains(searchQuery, ignoreCase = true) ||
                    order.mobileNumber.contains(searchQuery)

                val matchesStatus = statusFilter == "All" ||
                    order.status.equals(statusFilter, ignoreCase = true)

                matchesSearch && matchesStatus
            }

            if (filteredOrders.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No Orders Match Filter Criteria",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TeaTextPrimary
                                )
                            )
                        }
                    }
                }
            } else {
                items(filteredOrders) { order ->
                    AdminOrderCard(order = order, onClick = { viewModel.selectAdminOrder(order) })
                }
            }
        }

        // TAB 2: Customer Management
        if (adminSectionTab == 2) {
            item {
                Text(
                    text = "Customer & Shop Directory",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenPrimary
                    )
                )
            }

            items(allCustomers) { customer ->
                AdminCustomerCard(
                    customer = customer,
                    onClick = { viewModel.selectAdminCustomer(customer) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AdminLoginScreen(
    pinInput: String,
    authError: String?,
    onPinChange: (String) -> Unit,
    onLogin: () -> Unit,
    onQuickSelect: (AdminAccount) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(TeaGreenDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = TeaGold,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SK Admin Portal",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaGreenPrimary
                )
            )

            Text(
                text = "Authorized Access Only",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TeaTextSecondary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Quick login presets for Owner and Manager
            Text(
                text = "Select Authorized Profile:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AUTHORIZED_ADMINS.forEach { admin ->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onQuickSelect(admin) },
                        shape = RoundedCornerShape(10.dp),
                        color = TeaCreamBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, TeaGold.copy(alpha = 0.6f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = admin.name,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TeaTextPrimary,
                                    textAlign = TextAlign.Center
                                )
                            )
                            Text(
                                text = admin.role,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TeaGreenPrimary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = Color(0xFFEFE8DE))
            Spacer(modifier = Modifier.height(18.dp))

            // PIN entry
            OutlinedTextField(
                value = pinInput,
                onValueChange = onPinChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_pin_input"),
                label = { Text("Enter 4-Digit Access Code") },
                placeholder = { Text("e.g. 7860 or 1122") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                keyboardActions = KeyboardActions(onDone = { onLogin() }),
                isError = authError != null,
                supportingText = authError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TeaGreenPrimary) },
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("admin_login_submit_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = TeaGreenPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Authenticate & Enter Portal", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AdminOrderSearchAndFilter(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedFilter: String,
    onFilterChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search by Order #, Shop, Name, or Mobile") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TeaGreenPrimary) },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Filter Status:",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaTextSecondary
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("All", "New", "Confirmed", "Processing", "Out for Delivery", "Delivered", "Cancelled").forEach { status ->
                    val isSelected = selectedFilter == status
                    Surface(
                        modifier = Modifier.clickable { onFilterChange(status) },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) TeaGreenPrimary else TeaCreamBg,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) TeaGreenPrimary else Color(0xFFD6CEBF)
                        )
                    ) {
                        Text(
                            text = status,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TeaTextPrimary
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminOrderCard(
    order: OrderEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.orderNumber,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenPrimary
                    )
                )

                SktStatusBadge(status = order.status)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${order.shopName} • ${order.customerName}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TeaTextPrimary
                )
            )

            Text(
                text = "${order.teaBlend} (${order.teaSize}) • ${order.unitCount} units = ${String.format(Locale.US, "%.2f", order.totalKg)} KG",
                style = MaterialTheme.typography.bodySmall.copy(color = TeaTextSecondary)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${order.city} • ${formatTimestamp(order.timestamp)}",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                )

                Text(
                    text = "Manage Status >",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGold
                    )
                )
            }
        }
    }
}

@Composable
private fun AdminCustomerCard(
    customer: CustomerEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
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
                    text = customer.shopName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaTextPrimary
                    )
                )
                Text(
                    text = "Contact: ${customer.name} (${customer.mobileNumber})",
                    style = MaterialTheme.typography.bodySmall.copy(color = TeaTextSecondary)
                )
                Text(
                    text = "Address: ${customer.address}, ${customer.city}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                    maxLines = 1
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = TeaGreenContainer
                ) {
                    Text(
                        text = "${customer.totalOrders} Orders",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGreenDark
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${String.format(Locale.US, "%.1f", customer.totalKgOrdered)} KG Total",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGold
                    )
                )
            }
        }
    }
}

@Composable
private fun AdminOrderDetailDialog(
    order: OrderEntity,
    onStatusChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val statuses = listOf("New", "Confirmed", "Processing", "Out for Delivery", "Delivered", "Cancelled")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Manage Order",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGreenPrimary
                        )
                    )
                    Text(
                        text = order.orderNumber,
                        style = MaterialTheme.typography.labelMedium.copy(color = TeaGold)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = TeaCreamBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Customer: ${order.customerName}", fontWeight = FontWeight.Bold)
                            Text("Shop: ${order.shopName}")
                            Text("Mobile: ${order.mobileNumber}")
                            Text("Address: ${order.address}, ${order.city}")
                            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                            Text("Tea: ${order.teaBlend}", fontWeight = FontWeight.Bold)
                            Text("Size: ${order.teaSize} (${order.unitCount} units)")
                            Text("Total Volume: ${String.format(Locale.US, "%.2f", order.totalKg)} KG", color = TeaGreenPrimary, fontWeight = FontWeight.Bold)
                            if (order.notes.isNotBlank()) {
                                Text("Notes: ${order.notes}", color = Color.DarkGray)
                            }
                        }
                    }
                }

                // Quick Call / WhatsApp buttons
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { makePhoneCall(context, order.mobileNumber) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = TeaGreenPrimary),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call Shop", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val msg = "Hello ${order.customerName}, regarding your SK Tea order ${order.orderNumber} (${order.totalKg} KG):"
                                launchWhatsApp(context, msg, order.mobileNumber)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("WhatsApp", fontSize = 12.sp)
                        }
                    }
                }

                // Status Change Section
                item {
                    Text(
                        text = "Update Order Status:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaTextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        statuses.forEach { st ->
                            val isSelected = order.status.equals(st, ignoreCase = true)
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onStatusChange(st) },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) TeaGreenContainer else Color.White,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) TeaGreenPrimary else Color(0xFFE5DFD4)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = st,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) TeaGreenDark else TeaTextPrimary
                                        )
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = TeaGreenPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TeaGreenPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Done")
            }
        }
    )
}

@Composable
private fun AdminCustomerDetailDialog(
    customer: CustomerEntity,
    orders: List<OrderEntity>,
    onDismiss: () -> Unit,
    onOpenOrder: (OrderEntity) -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = customer.shopName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGreenPrimary
                        )
                    )
                    Text(
                        text = "Customer Profile",
                        style = MaterialTheme.typography.labelSmall.copy(color = TeaGold)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = TeaCreamBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Name: ${customer.name}", fontWeight = FontWeight.Bold)
                            Text("Mobile: ${customer.mobileNumber}")
                            Text("Address: ${customer.address}, ${customer.city}")
                            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                            Text("Total Orders: ${customer.totalOrders}", fontWeight = FontWeight.SemiBold)
                            Text("Total Volume: ${String.format(Locale.US, "%.1f", customer.totalKgOrdered)} KG", color = TeaGreenPrimary, fontWeight = FontWeight.Bold)
                            Text("Registered: ${formatTimestamp(customer.createdAt)}", color = Color.Gray, fontSize = 11.sp)
                        }
                    }
                }

                // Call & WhatsApp shortcuts
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { makePhoneCall(context, customer.mobileNumber) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = TeaGreenPrimary),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call Shop", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val msg = "Hello ${customer.name} (${customer.shopName}), this is SK Tea Company management."
                                launchWhatsApp(context, msg, customer.mobileNumber)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("WhatsApp", fontSize = 12.sp)
                        }
                    }
                }

                item {
                    Text(
                        text = "Customer Orders History (${orders.size}):",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaTextPrimary
                        )
                    )
                }

                items(orders) { order ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenOrder(order) },
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(order.orderNumber, fontWeight = FontWeight.Bold, color = TeaGreenPrimary)
                                Text("${order.teaBlend} (${order.totalKg} KG)", fontSize = 12.sp, color = TeaTextSecondary)
                            }
                            SktStatusBadge(status = order.status)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TeaGreenPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Close")
            }
        }
    )
}
