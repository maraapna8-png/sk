package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Scale
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.OrderEntity
import com.example.ui.components.OrderReviewDialog
import com.example.ui.components.SktBrandLogo
import com.example.ui.components.SktStatusBadge
import com.example.ui.components.formatTimestamp
import com.example.ui.components.launchWhatsApp
import com.example.ui.model.NavTab
import com.example.ui.model.SKT_TEA_CATALOG
import com.example.ui.theme.StatusDelivered
import com.example.ui.theme.TeaBorder
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
import com.example.ui.theme.TeaTextTertiary
import com.example.ui.viewmodel.OrderFormState
import com.example.ui.viewmodel.SktViewModel
import java.util.Locale

@Composable
fun PlaceOrderScreen(
    viewModel: SktViewModel,
    onNavigate: (NavTab) -> Unit
) {
    val formState by viewModel.orderForm.collectAsStateWithLifecycle()
    val lastOrder by viewModel.lastSubmittedOrder.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // If an order was just submitted, show Order Confirmation Screen
    if (lastOrder != null) {
        OrderConfirmationView(
            order = lastOrder!!,
            onTrackOrder = {
                viewModel.clearConfirmation()
                viewModel.trackOrder(lastOrder!!.orderNumber)
                onNavigate(NavTab.TrackOrder)
            },
            onWhatsAppSupport = {
                val whatsappMsg = """
                    *SKT Tea Order*
                    Order ID: ${lastOrder!!.orderNumber}
                    Customer Name: ${lastOrder!!.customerName}
                    Shop Name: ${lastOrder!!.shopName}
                    Mobile: ${lastOrder!!.mobileNumber}
                    Tea Blend: ${lastOrder!!.teaBlend}
                    Tea Size: ${lastOrder!!.teaSize} (${lastOrder!!.unitCount} units)
                    Total KG: ${String.format(Locale.US, "%.2f", lastOrder!!.totalKg)} KG
                    Delivery Address: ${lastOrder!!.address}, ${lastOrder!!.city}
                    Notes: ${lastOrder!!.notes.ifBlank { "N/A" }}
                """.trimIndent()
                launchWhatsApp(context, whatsappMsg)
            },
            onBackToHome = {
                viewModel.clearConfirmation()
                onNavigate(NavTab.Home)
            },
            onPlaceAnotherOrder = {
                viewModel.clearConfirmation()
            }
        )
        return
    }

    // Order Review Modal
    if (formState.isReviewModalOpen) {
        OrderReviewDialog(
            customerName = formState.customerName,
            shopName = formState.shopName,
            mobileNumber = formState.mobileNumber,
            address = formState.address,
            city = formState.city,
            teaBlend = formState.selectedBlend,
            teaSize = formState.selectedSize,
            unitCount = formState.unitCount,
            totalKg = formState.totalKg,
            notes = formState.notes,
            isSubmitting = formState.isSubmitting,
            onConfirm = { viewModel.submitConfirmedOrder() },
            onDismiss = { viewModel.closeReviewModal() }
        )
    }

    // Main Customer Order Form
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TeaCreamBg)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Form Title - Geometric Balance Theme Headline
        item {
            Column {
                Text(
                    text = "PREMIUM SELECTION",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TeaGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.4.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Pure quality ",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = TeaGreenPrimary
                        )
                    )
                    Text(
                        text = "in every leaf.",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TeaGold,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select your preferred tea blend and quantity for direct delivery to your shop.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TeaTextSecondary)
                )
            }
        }

        // Section 1: Tea Selection & Pack Size (Geometric 2x2 Grid)
        item {
            TeaSelectionCard(
                formState = formState,
                onBlendChange = { viewModel.updateSelectedBlend(it) },
                onSizeChange = { viewModel.updateSelectedSize(it) },
                onUnitCountChange = { viewModel.updateUnitCount(it) },
                onCustomKgChange = { viewModel.updateCustomKgPerUnit(it) }
            )
        }

        // Section 2: Customer & Shop Delivery Information
        item {
            CustomerInfoCard(
                formState = formState,
                onNameChange = { viewModel.updateCustomerName(it) },
                onShopChange = { viewModel.updateShopName(it) },
                onMobileChange = { viewModel.updateMobileNumber(it) },
                onAddressChange = { viewModel.updateAddress(it) },
                onCityChange = { viewModel.updateCity(it) },
                onNotesChange = { viewModel.updateNotes(it) }
            )
        }

        // Geometric Balance Signature Summary & Review CTA
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = TeaGreenPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "SELECTED UNITS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.75f),
                                    letterSpacing = 1.2.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                IconButton(
                                    onClick = { if (formState.unitCount > 1) viewModel.updateUnitCount(formState.unitCount - 1) },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF2D5A47))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Decrease",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Text(
                                    text = "${formState.unitCount}",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )

                                IconButton(
                                    onClick = { viewModel.updateUnitCount(formState.unitCount + 1) },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF2D5A47))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Increase",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "TOTAL KG",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TeaGold,
                                    letterSpacing = 1.2.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = String.format(Locale.US, "%.1f", formState.totalKg),
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "KG",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = TeaGoldLight
                                    ),
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = { viewModel.validateAndOpenReview() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("review_order_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TeaGold,
                            contentColor = TeaGreenDark
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Review & Place Order",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TeaGreenDark
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = TeaGreenDark,
                            modifier = Modifier.size(20.dp)
                        )
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
private fun TeaSelectionCard(
    formState: OrderFormState,
    onBlendChange: (String) -> Unit,
    onSizeChange: (String) -> Unit,
    onUnitCountChange: (Int) -> Unit,
    onCustomKgChange: (Double) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(TeaGoldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text("1", fontWeight = FontWeight.Bold, color = TeaGreenDark, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Tea Blend & Quantity",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Blend Selector Chips
            Text(
                text = "Select Blend *",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(6.dp))

            SKT_TEA_CATALOG.forEach { blend ->
                val isSelected = formState.selectedBlend == blend.name
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onBlendChange(blend.name) },
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) TeaGreenContainer else TeaCreamBg,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) TeaGreenPrimary else Color(0xFFDCD6CA)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) TeaGreenPrimary else Color.Transparent)
                                .border(1.5.dp, if (isSelected) TeaGreenPrimary else Color.Gray, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = blend.name,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) TeaGreenDark else TeaTextPrimary
                                )
                            )
                            Text(
                                text = blend.grade,
                                style = MaterialTheme.typography.labelSmall.copy(color = TeaTextSecondary)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Package Size Options (Geometric Balance 2x2 Grid)
            Text(
                text = "Package Size *",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            val sizeOptions = listOf(
                Triple("250g", "STANDARD", "250g"),
                Triple("500g", "MEDIUM", "500g"),
                Triple("1kg", "LARGE", "1 KG"),
                Triple("Custom", "CUSTOM", "Other")
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    sizeOptions.take(2).forEach { (id, tag, displayLabel) ->
                        val isSelected = formState.selectedSize.equals(id, ignoreCase = true)
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSizeChange(id) },
                            shape = RoundedCornerShape(24.dp),
                            color = if (isSelected) TeaGreenContainer else Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) TeaGreenPrimary else TeaBorder
                            ),
                            shadowElevation = if (isSelected) 2.dp else 1.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TeaGold else TeaTextTertiary,
                                        letterSpacing = 1.sp,
                                        fontSize = 10.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = displayLabel,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TeaGreenPrimary else TeaTextPrimary,
                                        fontSize = 18.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    sizeOptions.drop(2).forEach { (id, tag, displayLabel) ->
                        val isSelected = formState.selectedSize.equals(id, ignoreCase = true)
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSizeChange(id) },
                            shape = RoundedCornerShape(24.dp),
                            color = if (isSelected) TeaGreenContainer else Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) TeaGreenPrimary else TeaBorder
                            ),
                            shadowElevation = if (isSelected) 2.dp else 1.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TeaGold else TeaTextTertiary,
                                        letterSpacing = 1.sp,
                                        fontSize = 10.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = displayLabel,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TeaGreenPrimary else TeaTextPrimary,
                                        fontSize = 18.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Unit Multiplier
            Text(
                text = "Number of Units / Packets *",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { if (formState.unitCount > 1) onUnitCountChange(formState.unitCount - 1) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(TeaCreamBg)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease Units")
                    }

                    Surface(
                        modifier = Modifier.width(68.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF9F7F3),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TeaGold.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "${formState.unitCount}",
                            modifier = Modifier.padding(vertical = 8.dp),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = TeaTextPrimary
                            )
                        )
                    }

                    IconButton(
                        onClick = { onUnitCountChange(formState.unitCount + 1) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(TeaCreamBg)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase Units")
                    }
                }

                // Quick Increment presets
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(5, 10, 20, 50).forEach { preset ->
                        Surface(
                            modifier = Modifier.clickable { onUnitCountChange(preset) },
                            shape = RoundedCornerShape(6.dp),
                            color = if (formState.unitCount == preset) TeaGold else TeaCreamBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD6CEBF))
                        ) {
                            Text(
                                text = "+$preset",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (formState.unitCount == preset) TeaGreenDark else TeaTextPrimary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomerInfoCard(
    formState: OrderFormState,
    onNameChange: (String) -> Unit,
    onShopChange: (String) -> Unit,
    onMobileChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onNotesChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(TeaGoldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text("2", fontWeight = FontWeight.Bold, color = TeaGreenDark, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Customer & Delivery Details",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Customer Name
            OutlinedTextField(
                value = formState.customerName,
                onValueChange = onNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_customer_name"),
                label = { Text("Customer Name *") },
                isError = formState.nameError != null,
                supportingText = formState.nameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = TeaGreenPrimary) },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Shop Name
            OutlinedTextField(
                value = formState.shopName,
                onValueChange = onShopChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_shop_name"),
                label = { Text("Shop / Business / Hotel Name *") },
                isError = formState.shopError != null,
                supportingText = formState.shopError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                leadingIcon = { Icon(Icons.Default.Storefront, contentDescription = null, tint = TeaGreenPrimary) },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mobile Number
            OutlinedTextField(
                value = formState.mobileNumber,
                onValueChange = onMobileChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_mobile_number"),
                label = { Text("Mobile Number (WhatsApp) *") },
                placeholder = { Text("e.g. 0300-1234567") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = formState.mobileError != null,
                supportingText = formState.mobileError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = TeaGreenPrimary) },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // City / Area
            OutlinedTextField(
                value = formState.city,
                onValueChange = onCityChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("City / Commercial Area *") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = TeaGreenPrimary) },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Complete Address
            OutlinedTextField(
                value = formState.address,
                onValueChange = onAddressChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_address"),
                label = { Text("Complete Shop Delivery Address *") },
                placeholder = { Text("Shop #, Market/Plaza Name, Street / Road landmark") },
                minLines = 2,
                isError = formState.addressError != null,
                supportingText = formState.addressError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = TeaGreenPrimary) },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Special Notes
            OutlinedTextField(
                value = formState.notes,
                onValueChange = onNotesChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Additional Notes (Optional)") },
                placeholder = { Text("e.g. Call before delivery, deliver in morning batch") },
                leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null, tint = TeaGreenPrimary) },
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@Composable
fun OrderConfirmationView(
    order: OrderEntity,
    onTrackOrder: () -> Unit,
    onWhatsAppSupport: () -> Unit,
    onBackToHome: () -> Unit,
    onPlaceAnotherOrder: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TeaCreamBg)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(TeaGreenContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = StatusDelivered,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        item {
            Text(
                text = "Order Submitted Successfully!",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaGreenPrimary
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Thank you for choosing SKT Tea Company. Your tea order has been recorded and scheduled for preparation.",
                style = MaterialTheme.typography.bodyMedium.copy(color = TeaTextSecondary),
                textAlign = TextAlign.Center
            )
        }

        // Order ID Badge
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = TeaGoldContainer,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, TeaGold)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "UNIQUE ORDER ID",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4A3408),
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

                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(order.orderNumber))
                            android.widget.Toast.makeText(context, "Order ID Copied: ${order.orderNumber}", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Order ID",
                            tint = TeaGreenDark
                        )
                    }
                }
            }
        }

        // Complete Order Details Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ORDER SUMMARY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGold,
                            letterSpacing = 1.2.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ConfirmationRow("Order Date", formatTimestamp(order.timestamp))
                    ConfirmationRow("Customer Name", order.customerName)
                    ConfirmationRow("Shop Name", order.shopName)
                    ConfirmationRow("Mobile Number", order.mobileNumber)
                    ConfirmationRow("Delivery Address", "${order.address}, ${order.city}")

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color(0xFFEFE8DE)
                    )

                    ConfirmationRow("Tea Selection", order.teaBlend, isBold = true)
                    ConfirmationRow("Package Size", order.teaSize)
                    ConfirmationRow("Units Ordered", "${order.unitCount} packets")
                    ConfirmationRow(
                        "Total Volume",
                        "${String.format(Locale.US, "%.2f", order.totalKg)} KG",
                        isHighlight = true
                    )
                    if (order.notes.isNotBlank()) {
                        ConfirmationRow("Special Notes", order.notes)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Initial Status",
                            style = MaterialTheme.typography.bodySmall.copy(color = TeaTextSecondary)
                        )
                        SktStatusBadge(status = order.status)
                    }
                }
            }
        }

        // Action Buttons
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onWhatsAppSupport,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("whatsapp_order_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Contact SKT on WhatsApp",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Button(
                    onClick = onTrackOrder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("track_order_confirmation_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = TeaGreenPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TrackChanges,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Track This Order Live",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onPlaceAnotherOrder,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Place Another Order")
                    }

                    OutlinedButton(
                        onClick = onBackToHome,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Back to Home")
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfirmationRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(color = TeaTextSecondary),
            modifier = Modifier.weight(0.42f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isHighlight || isBold) FontWeight.Bold else FontWeight.Normal,
                color = if (isHighlight) TeaGreenPrimary else TeaTextPrimary,
                textAlign = TextAlign.End
            ),
            modifier = Modifier.weight(0.58f)
        )
    }
}
