package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.PACK_OPTIONS
import com.example.ui.model.SKT_TEA_CATALOG
import com.example.ui.model.TeaBlend
import com.example.ui.theme.TeaCreamBg
import com.example.ui.theme.TeaGold
import com.example.ui.theme.TeaGoldContainer
import com.example.ui.theme.TeaGreenContainer
import com.example.ui.theme.TeaGreenDark
import com.example.ui.theme.TeaGreenLight
import com.example.ui.theme.TeaGreenPrimary
import com.example.ui.theme.TeaSurfaceVariant
import com.example.ui.theme.TeaTextPrimary
import com.example.ui.theme.TeaTextSecondary
import java.util.Locale

@Composable
fun ProductsScreen(
    onSelectBlendAndOrder: (blendName: String, size: String, units: Int) -> Unit
) {
    var calcSelectedBlend by remember { mutableStateOf(SKT_TEA_CATALOG.first().name) }
    var calcSelectedSize by remember { mutableStateOf("500g") }
    var calcUnits by remember { mutableIntStateOf(10) }
    var calcCustomKgInput by remember { mutableStateOf("5.0") }

    val calculatedTotalKg = when (calcSelectedSize) {
        "250g" -> calcUnits * 0.25
        "500g" -> calcUnits * 0.50
        "1kg" -> calcUnits * 1.00
        else -> (calcCustomKgInput.toDoubleOrNull() ?: 1.0)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TeaCreamBg)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Screen Header
        item {
            Column {
                Text(
                    text = "SKT TEA CATALOG",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TeaGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                )
                Text(
                    text = "Premium Blends & Quantities",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Choose from predefined 250g, 500g, 1KG packs or custom bulk kilograms tailored for retail shops, cafes, and commercial distributors.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TeaTextSecondary)
                )
            }
        }

        // 2. Interactive Live Quantity & Weight Calculator Tool
        item {
            LiveQuantityCalculatorCard(
                selectedBlend = calcSelectedBlend,
                onBlendChange = { calcSelectedBlend = it },
                selectedSize = calcSelectedSize,
                onSizeChange = { calcSelectedSize = it },
                units = calcUnits,
                onUnitsChange = { calcUnits = it },
                customKgInput = calcCustomKgInput,
                onCustomKgChange = { calcCustomKgInput = it },
                calculatedTotalKg = calculatedTotalKg,
                onProceedToOrder = {
                    onSelectBlendAndOrder(calcSelectedBlend, calcSelectedSize, calcUnits)
                }
            )
        }

        // 3. Section Title for Product List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 20.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(TeaGold)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Available Tea Blends",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaTextPrimary
                    )
                )
            }
        }

        // 4. Product Blends Catalog
        items(SKT_TEA_CATALOG) { blend ->
            ProductDetailCard(
                blend = blend,
                onOrderClicked = { size, count ->
                    onSelectBlendAndOrder(blend.name, size, count)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LiveQuantityCalculatorCard(
    selectedBlend: String,
    onBlendChange: (String) -> Unit,
    selectedSize: String,
    onSizeChange: (String) -> Unit,
    units: Int,
    onUnitsChange: (Int) -> Unit,
    customKgInput: String,
    onCustomKgChange: (String) -> Unit,
    calculatedTotalKg: Double,
    onProceedToOrder: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, TeaGold.copy(alpha = 0.6f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Calculator Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(TeaGoldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = TeaGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Tea Quantity & Weight Calculator",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TeaGreenPrimary
                        )
                    )
                    Text(
                        text = "Live automatic conversion to Total Kilograms (KG)",
                        style = MaterialTheme.typography.bodySmall.copy(color = TeaTextSecondary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFEFE8DE))
            Spacer(modifier = Modifier.height(14.dp))

            // Step 1: Package Size Selector (Geometric Balance 2x2 Grid)
            Text(
                text = "1. Choose Package Size:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            val calcSizeOptions = listOf(
                Triple("250g", "STANDARD", "250g"),
                Triple("500g", "MEDIUM", "500g"),
                Triple("1kg", "LARGE", "1 KG"),
                Triple("custom", "CUSTOM", "Other")
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    calcSizeOptions.take(2).forEach { (id, tag, label) ->
                        val isSelected = selectedSize == id
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSizeChange(id) },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) TeaGreenContainer else Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) TeaGreenPrimary else Color(0xFFE7E5E4)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TeaGold else Color(0xFFA8A29E),
                                        letterSpacing = 1.sp,
                                        fontSize = 9.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TeaGreenPrimary else TeaTextPrimary,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    calcSizeOptions.drop(2).forEach { (id, tag, label) ->
                        val isSelected = selectedSize == id
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSizeChange(id) },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) TeaGreenContainer else Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) TeaGreenPrimary else Color(0xFFE7E5E4)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TeaGold else Color(0xFFA8A29E),
                                        letterSpacing = 1.sp,
                                        fontSize = 9.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TeaGreenPrimary else TeaTextPrimary,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 2: Units or Custom KG Input
            if (selectedSize != "custom") {
                Text(
                    text = "2. Number of Packets / Units:",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

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
                            onClick = { if (units > 1) onUnitsChange(units - 1) },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(TeaCreamBg)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease Units", tint = TeaGreenPrimary)
                        }

                        Surface(
                            modifier = Modifier.width(64.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF9F7F3),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TeaGold.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "$units",
                                modifier = Modifier.padding(vertical = 8.dp),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = TeaTextPrimary
                                )
                            )
                        }

                        IconButton(
                            onClick = { onUnitsChange(units + 1) },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(TeaCreamBg)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase Units", tint = TeaGreenPrimary)
                        }
                    }

                    // Quick increment presets
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(5, 10, 20, 50).forEach { preset ->
                            Surface(
                                modifier = Modifier.clickable { onUnitsChange(preset) },
                                shape = RoundedCornerShape(6.dp),
                                color = if (units == preset) TeaGold else TeaCreamBg,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD6CEBF))
                            ) {
                                Text(
                                    text = "+$preset",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (units == preset) TeaGreenDark else TeaTextPrimary
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "2. Enter Required Quantity in KG:",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = customKgInput,
                    onValueChange = onCustomKgChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Total Kilograms (KG)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = { Text("KG", modifier = Modifier.padding(end = 12.dp), fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 3: Calculation Result Highlight Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = TeaGreenContainer,
                border = androidx.compose.foundation.BorderStroke(1.dp, TeaGreenPrimary.copy(alpha = 0.3f))
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
                            text = "ORDER CALCULATION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TeaGreenDark,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        if (selectedSize != "custom") {
                            Text(
                                text = "Size: $selectedSize  •  Units: $units packets",
                                style = MaterialTheme.typography.bodySmall.copy(color = TeaTextSecondary)
                            )
                        } else {
                            Text(
                                text = "Custom Bulk Volume",
                                style = MaterialTheme.typography.bodySmall.copy(color = TeaTextSecondary)
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${String.format(Locale.US, "%.2f", calculatedTotalKg)} KG",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TeaGreenPrimary
                            )
                        )
                        Text(
                            text = "Total Weight",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TeaGreenDark,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Order with this calculation CTA
            Button(
                onClick = onProceedToOrder,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = TeaGold),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocalMall,
                    contentDescription = null,
                    tint = TeaGreenDark,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Apply Calculation to Order Form",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenDark
                    )
                )
            }
        }
    }
}

@Composable
private fun ProductDetailCard(
    blend: TeaBlend,
    onOrderClicked: (size: String, count: Int) -> Unit
) {
    var selectedPack by remember { mutableStateOf("500g") }
    var unitCount by remember { mutableIntStateOf(10) }

    val currentTotalKg = when (selectedPack) {
        "250g" -> unitCount * 0.25
        "500g" -> unitCount * 0.50
        "1kg" -> unitCount * 1.00
        else -> unitCount * 0.50
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DFD4))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title & Grade
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
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
                    color = TeaCreamBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TeaGold.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "100% PURE",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TeaGreenDark,
                            fontWeight = FontWeight.Bold,
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

            // Flavor profile
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = TeaSurfaceVariant
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Flavor Profile: ${blend.flavorProfile}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TeaTextPrimary
                        )
                    )
                    Text(
                        text = "Recommended: ${blend.recommendedFor}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TeaTextSecondary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Pack Selector Row
            Text(
                text = "Select Pack Size:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TeaTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("250g" to "250g", "500g" to "500g", "1kg" to "1 KG", "Custom" to "Custom").forEach { (id, label) ->
                    val isSelected = selectedPack == id
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedPack = id },
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSelected) TeaGreenPrimary else TeaCreamBg,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) TeaGreenPrimary else Color.LightGray
                        )
                    ) {
                        Text(
                            text = label,
                            modifier = Modifier.padding(vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TeaTextPrimary,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Unit count and instant Order Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total: ${String.format(Locale.US, "%.2f", currentTotalKg)} KG",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TeaGreenPrimary
                    )
                )

                Button(
                    onClick = { onOrderClicked(selectedPack, unitCount) },
                    colors = ButtonDefaults.buttonColors(containerColor = TeaGreenPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalMall,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Place Order", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
