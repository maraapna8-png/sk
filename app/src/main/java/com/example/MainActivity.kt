package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.SktBrandLogo
import com.example.ui.components.launchWhatsApp
import com.example.ui.components.makePhoneCall
import com.example.ui.model.COMPANY_CONTACT
import com.example.ui.model.NavTab
import com.example.ui.screens.AboutContactScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderHistoryScreen
import com.example.ui.screens.PlaceOrderScreen
import com.example.ui.screens.ProductsScreen
import com.example.ui.screens.TrackOrderScreen
import com.example.ui.theme.SktTeaTheme
import com.example.ui.theme.TeaCreamBg
import com.example.ui.theme.TeaGold
import com.example.ui.theme.TeaGoldContainer
import com.example.ui.theme.TeaGreenDark
import com.example.ui.theme.TeaGreenPrimary
import com.example.ui.theme.TeaTextPrimary
import com.example.ui.viewmodel.SktViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SktViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SktTeaTheme {
                SktTeaAppRoot(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SktTeaAppRoot(viewModel: SktViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val stats by viewModel.dashboardStats.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { viewModel.navigate(NavTab.Home) }
                    ) {
                        SktBrandLogo(size = 32, showText = false)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "SK TEA",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = "Quality Tea, Trusted Service",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TeaGold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                },
                actions = {
                    // Quick Call Action
                    IconButton(
                        onClick = { makePhoneCall(context, COMPANY_CONTACT.ownerPhone) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call SK Tea",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Quick WhatsApp Action
                    IconButton(
                        onClick = {
                            launchWhatsApp(
                                context,
                                "Hello SK Tea Company, I would like to inquire about wholesale tea pricing."
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "WhatsApp SK",
                            tint = TeaGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Admin Portal Shortcut
                    IconButton(
                        onClick = { viewModel.navigate(NavTab.Admin) },
                        modifier = Modifier.testTag("topbar_admin_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Portal",
                            tint = if (currentTab is NavTab.Admin) TeaGold else Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TeaGreenDark,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            SktBottomNavBar(
                currentTab = currentTab,
                newOrdersCount = stats.newOrders,
                onTabSelected = { viewModel.navigate(it) }
            )
        },
        floatingActionButton = {
            if (currentTab !is NavTab.Admin) {
                FloatingActionButton(
                    onClick = {
                        launchWhatsApp(
                            context = context,
                            message = "Hello SK Tea Company, I would like to place an order or ask a question."
                        )
                    },
                    containerColor = Color(0xFF25D366),
                    contentColor = Color.White,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(6.dp),
                    modifier = Modifier.testTag("floating_whatsapp_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "WhatsApp Order Line",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(TeaCreamBg)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { tab ->
                when (tab) {
                    is NavTab.Home -> {
                        HomeScreen(
                            onNavigate = { viewModel.navigate(it) },
                            onSelectBlendToOrder = { blendName ->
                                viewModel.updateSelectedBlend(blendName)
                                viewModel.navigate(NavTab.PlaceOrder)
                            }
                        )
                    }

                    is NavTab.Products -> {
                        ProductsScreen(
                            onSelectBlendAndOrder = { blend, size, units ->
                                viewModel.updateSelectedBlend(blend)
                                viewModel.updatePackageQty(size, units)
                                viewModel.navigate(NavTab.PlaceOrder)
                            }
                        )
                    }

                    is NavTab.PlaceOrder -> {
                        PlaceOrderScreen(
                            viewModel = viewModel,
                            onNavigate = { viewModel.navigate(it) }
                        )
                    }

                    is NavTab.TrackOrder -> {
                        TrackOrderScreen(
                            viewModel = viewModel,
                            onNavigate = { viewModel.navigate(it) }
                        )
                    }

                    is NavTab.History -> {
                        OrderHistoryScreen(
                            viewModel = viewModel,
                            onNavigate = { viewModel.navigate(it) }
                        )
                    }

                    is NavTab.Admin -> {
                        AdminDashboardScreen(
                            viewModel = viewModel
                        )
                    }

                    is NavTab.ContactAbout -> {
                        AboutContactScreen(
                            onNavigate = { viewModel.navigate(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SktBottomNavBar(
    currentTab: NavTab,
    newOrdersCount: Int,
    onTabSelected: (NavTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        // Home
        NavigationBarItem(
            selected = currentTab is NavTab.Home,
            onClick = { onTabSelected(NavTab.Home) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TeaGreenDark,
                selectedTextColor = TeaGreenPrimary,
                indicatorColor = TeaGoldContainer
            )
        )

        // Products / Blends
        NavigationBarItem(
            selected = currentTab is NavTab.Products,
            onClick = { onTabSelected(NavTab.Products) },
            icon = { Icon(Icons.Default.Eco, contentDescription = "Tea Blends") },
            label = { Text("Blends", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TeaGreenDark,
                selectedTextColor = TeaGreenPrimary,
                indicatorColor = TeaGoldContainer
            )
        )

        // Order Now (Prominent CTA)
        NavigationBarItem(
            selected = currentTab is NavTab.PlaceOrder,
            onClick = { onTabSelected(NavTab.PlaceOrder) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (currentTab is NavTab.PlaceOrder) TeaGold else TeaGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalMall,
                        contentDescription = "Order",
                        tint = if (currentTab is NavTab.PlaceOrder) TeaGreenDark else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            },
            label = {
                Text(
                    "Order",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentTab is NavTab.PlaceOrder) TeaGreenPrimary else TeaTextPrimary
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )

        // Track Order
        NavigationBarItem(
            selected = currentTab is NavTab.TrackOrder,
            onClick = { onTabSelected(NavTab.TrackOrder) },
            icon = { Icon(Icons.Default.TrackChanges, contentDescription = "Track") },
            label = { Text("Track", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TeaGreenDark,
                selectedTextColor = TeaGreenPrimary,
                indicatorColor = TeaGoldContainer
            )
        )

        // Orders History
        NavigationBarItem(
            selected = currentTab is NavTab.History,
            onClick = { onTabSelected(NavTab.History) },
            icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "History") },
            label = { Text("History", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TeaGreenDark,
                selectedTextColor = TeaGreenPrimary,
                indicatorColor = TeaGoldContainer
            )
        )

        // Admin Portal
        NavigationBarItem(
            selected = currentTab is NavTab.Admin,
            onClick = { onTabSelected(NavTab.Admin) },
            icon = {
                if (newOrdersCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(containerColor = Color(0xFFDC2626)) {
                                Text("$newOrdersCount", color = Color.White, fontSize = 9.sp)
                            }
                        }
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin")
                    }
                } else {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin")
                }
            },
            label = { Text("Admin", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TeaGreenDark,
                selectedTextColor = TeaGreenPrimary,
                indicatorColor = TeaGoldContainer
            )
        )
    }
}
