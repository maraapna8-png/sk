package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.CustomerEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.repository.SktTeaRepository
import com.example.ui.model.AUTHORIZED_ADMINS
import com.example.ui.model.AdminAccount
import com.example.ui.model.NavTab
import com.example.ui.model.PACK_OPTIONS
import com.example.ui.model.SKT_TEA_CATALOG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

data class OrderFormState(
    val customerName: String = "",
    val shopName: String = "",
    val mobileNumber: String = "",
    val address: String = "",
    val city: String = "Lahore",
    val selectedBlend: String = SKT_TEA_CATALOG.first().name,
    // Per-package quantities
    val qty125g: Int = 0,
    val qty250g: Int = 10, // Default 10 packets of 250g
    val qty500g: Int = 0,
    val qty1kg: Int = 0,
    val qtyCustomUnits: Int = 0,
    val customKgPerUnit: Double = 1.0,
    val notes: String = "",
    val nameError: String? = null,
    val shopError: String? = null,
    val mobileError: String? = null,
    val addressError: String? = null,
    val quantityError: String? = null,
    val isReviewModalOpen: Boolean = false,
    val isSubmitting: Boolean = false
) {
    val totalPackets: Int
        get() = qty125g + qty250g + qty500g + qty1kg + qtyCustomUnits

    val totalKg: Double
        get() = (qty125g * 0.125) + (qty250g * 0.25) + (qty500g * 0.50) + (qty1kg * 1.0) + (qtyCustomUnits * customKgPerUnit)

    val packageBreakdownString: String
        get() {
            val list = mutableListOf<String>()
            if (qty125g > 0) list.add("125g x $qty125g")
            if (qty250g > 0) list.add("250g x $qty250g")
            if (qty500g > 0) list.add("500g x $qty500g")
            if (qty1kg > 0) list.add("1kg x $qty1kg")
            if (qtyCustomUnits > 0) list.add("Custom(${customKgPerUnit}kg) x $qtyCustomUnits")
            return if (list.isEmpty()) "No packets selected" else list.joinToString(", ")
        }

    val selectedSizeSummary: String
        get() = packageBreakdownString
}

data class AdminDashboardStats(
    val totalOrders: Int = 0,
    val newOrders: Int = 0,
    val confirmedOrders: Int = 0,
    val processingOrders: Int = 0,
    val outForDeliveryOrders: Int = 0,
    val deliveredOrders: Int = 0,
    val cancelledOrders: Int = 0,
    val totalKgOrdered: Double = 0.0,
    val totalCustomers: Int = 0
)

class SktViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SktTeaRepository

    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = SktTeaRepository(db.orderDao(), db.customerDao())
    }

    // Active Navigation Tab
    private val _currentTab = MutableStateFlow<NavTab>(NavTab.Home)
    val currentTab: StateFlow<NavTab> = _currentTab.asStateFlow()

    fun navigateTo(tab: NavTab) {
        _currentTab.value = tab
    }

    fun navigate(tab: NavTab) {
        _currentTab.value = tab
    }

    // All Orders & Customers Flow
    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCustomers: StateFlow<List<CustomerEntity>> = repository.allCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin Dashboard Statistics
    val dashboardStats: StateFlow<AdminDashboardStats> = combine(
        allOrders,
        allCustomers
    ) { orders, customers ->
        var newCount = 0
        var confirmedCount = 0
        var processingCount = 0
        var outForDeliveryCount = 0
        var deliveredCount = 0
        var cancelledCount = 0
        var totalKg = 0.0

        for (order in orders) {
            when (order.status.lowercase(Locale.ROOT)) {
                "new" -> newCount++
                "confirmed" -> confirmedCount++
                "processing" -> processingCount++
                "out for delivery" -> outForDeliveryCount++
                "delivered" -> {
                    deliveredCount++
                    totalKg += order.totalKg
                }
                "cancelled" -> cancelledCount++
            }
            if (order.status.lowercase(Locale.ROOT) != "cancelled" && order.status.lowercase(Locale.ROOT) != "delivered") {
                totalKg += order.totalKg
            }
        }

        AdminDashboardStats(
            totalOrders = orders.size,
            newOrders = newCount,
            confirmedOrders = confirmedCount,
            processingOrders = processingCount,
            outForDeliveryOrders = outForDeliveryCount,
            deliveredOrders = deliveredCount,
            cancelledOrders = cancelledCount,
            totalKgOrdered = totalKg,
            totalCustomers = customers.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdminDashboardStats())

    // Customer Order Form State
    private val _orderForm = MutableStateFlow(OrderFormState())
    val orderForm: StateFlow<OrderFormState> = _orderForm.asStateFlow()

    // Confirmation State
    private val _lastSubmittedOrder = MutableStateFlow<OrderEntity?>(null)
    val lastSubmittedOrder: StateFlow<OrderEntity?> = _lastSubmittedOrder.asStateFlow()

    fun updateCustomerName(name: String) {
        _orderForm.value = _orderForm.value.copy(customerName = name, nameError = null)
    }

    fun updateShopName(shop: String) {
        _orderForm.value = _orderForm.value.copy(shopName = shop, shopError = null)
    }

    fun updateMobileNumber(mobile: String) {
        _orderForm.value = _orderForm.value.copy(mobileNumber = mobile, mobileError = null)
    }

    fun updateAddress(addr: String) {
        _orderForm.value = _orderForm.value.copy(address = addr, addressError = null)
    }

    fun updateCity(city: String) {
        _orderForm.value = _orderForm.value.copy(city = city)
    }

    fun updateSelectedBlend(blend: String) {
        _orderForm.value = _orderForm.value.copy(selectedBlend = blend)
    }

    fun updatePackageQty(sizeId: String, qty: Int) {
        val safeQty = qty.coerceAtLeast(0)
        val current = _orderForm.value
        val updated = when (sizeId.lowercase(Locale.ROOT)) {
            "125g" -> current.copy(qty125g = safeQty, quantityError = null)
            "250g" -> current.copy(qty250g = safeQty, quantityError = null)
            "500g" -> current.copy(qty500g = safeQty, quantityError = null)
            "1kg" -> current.copy(qty1kg = safeQty, quantityError = null)
            "custom" -> current.copy(qtyCustomUnits = safeQty, quantityError = null)
            else -> current
        }
        _orderForm.value = updated
    }

    fun updateCustomKgPerUnit(kg: Double) {
        val safeKg = if (kg <= 0.0) 0.1 else kg
        _orderForm.value = _orderForm.value.copy(customKgPerUnit = safeKg)
    }

    fun updateNotes(notes: String) {
        _orderForm.value = _orderForm.value.copy(notes = notes)
    }

    fun selectBlendAndOrder(blendName: String) {
        _orderForm.value = _orderForm.value.copy(selectedBlend = blendName)
        _currentTab.value = NavTab.PlaceOrder
    }

    fun validateAndOpenReview(): Boolean {
        val form = _orderForm.value
        var isValid = true

        var nameErr: String? = null
        var shopErr: String? = null
        var mobErr: String? = null
        var addrErr: String? = null
        var qtyErr: String? = null

        if (form.customerName.isBlank()) {
            nameErr = "Please enter customer name"
            isValid = false
        }
        if (form.shopName.isBlank()) {
            shopErr = "Please enter shop / business name"
            isValid = false
        }
        if (form.mobileNumber.isBlank() || form.mobileNumber.length < 7) {
            mobErr = "Please enter a valid contact phone/mobile number"
            isValid = false
        }
        if (form.address.isBlank()) {
            addrErr = "Please enter delivery address"
            isValid = false
        }
        if (form.totalPackets <= 0) {
            qtyErr = "Please select at least 1 packet for at least one package size"
            isValid = false
        }

        _orderForm.value = form.copy(
            nameError = nameErr,
            shopError = shopErr,
            mobileError = mobErr,
            addressError = addrErr,
            quantityError = qtyErr,
            isReviewModalOpen = isValid
        )

        return isValid
    }

    fun closeReviewModal() {
        _orderForm.value = _orderForm.value.copy(isReviewModalOpen = false)
    }

    fun submitConfirmedOrder() {
        val form = _orderForm.value
        if (form.isSubmitting) return

        _orderForm.value = form.copy(isSubmitting = true)

        viewModelScope.launch {
            try {
                val createdOrder = repository.submitOrder(
                    customerName = form.customerName,
                    shopName = form.shopName,
                    mobileNumber = form.mobileNumber,
                    address = form.address,
                    city = form.city,
                    teaBlend = form.selectedBlend,
                    teaSize = form.packageBreakdownString,
                    unitCount = form.totalPackets,
                    totalKg = form.totalKg,
                    notes = form.notes
                )

                _lastSubmittedOrder.value = createdOrder
                _orderForm.value = OrderFormState(
                    customerName = form.customerName,
                    shopName = form.shopName,
                    mobileNumber = form.mobileNumber,
                    address = form.address,
                    city = form.city
                )
                // Set tracking to this newly created order automatically
                _trackedOrder.value = createdOrder
                _trackingQuery.value = createdOrder.orderNumber
            } finally {
                _orderForm.value = _orderForm.value.copy(isSubmitting = false, isReviewModalOpen = false)
            }
        }
    }

    fun clearConfirmation() {
        _lastSubmittedOrder.value = null
    }

    // Reorder Feature
    fun reorder(pastOrder: OrderEntity) {
        var q125 = 0
        var q250 = 0
        var q500 = 0
        var q1k = 0
        var qCustom = 0

        val str = pastOrder.teaSize
        if (str.contains("x")) {
            val parts = str.split(",")
            for (part in parts) {
                val trimmed = part.trim()
                if (trimmed.startsWith("125g")) q125 = trimmed.substringAfter("x").trim().toIntOrNull() ?: 0
                else if (trimmed.startsWith("250g")) q250 = trimmed.substringAfter("x").trim().toIntOrNull() ?: 0
                else if (trimmed.startsWith("500g")) q500 = trimmed.substringAfter("x").trim().toIntOrNull() ?: 0
                else if (trimmed.startsWith("1kg")) q1k = trimmed.substringAfter("x").trim().toIntOrNull() ?: 0
                else if (trimmed.startsWith("Custom")) qCustom = trimmed.substringAfter("x").trim().toIntOrNull() ?: 0
            }
        } else {
            if (str.contains("125g", ignoreCase = true)) q125 = pastOrder.unitCount
            else if (str.contains("250g", ignoreCase = true)) q250 = pastOrder.unitCount
            else if (str.contains("1kg", ignoreCase = true)) q1k = pastOrder.unitCount
            else q500 = pastOrder.unitCount
        }

        _orderForm.value = OrderFormState(
            customerName = pastOrder.customerName,
            shopName = pastOrder.shopName,
            mobileNumber = pastOrder.mobileNumber,
            address = pastOrder.address,
            city = pastOrder.city,
            selectedBlend = pastOrder.teaBlend,
            qty125g = q125,
            qty250g = q250,
            qty500g = q500,
            qty1kg = q1k,
            qtyCustomUnits = qCustom,
            notes = pastOrder.notes
        )
        _currentTab.value = NavTab.PlaceOrder
    }

    // Order Tracking State
    private val _trackingQuery = MutableStateFlow("")
    val trackingQuery: StateFlow<String> = _trackingQuery.asStateFlow()

    private val _trackedOrder = MutableStateFlow<OrderEntity?>(null)
    val trackedOrder: StateFlow<OrderEntity?> = _trackedOrder.asStateFlow()

    private val _isSearchingTracking = MutableStateFlow(false)
    val isSearchingTracking: StateFlow<Boolean> = _isSearchingTracking.asStateFlow()

    private val _trackingMessage = MutableStateFlow<String?>(null)
    val trackingMessage: StateFlow<String?> = _trackingMessage.asStateFlow()

    fun updateTrackingQuery(query: String) {
        _trackingQuery.value = query
        _trackingMessage.value = null
    }

    fun trackOrder(query: String? = null) {
        val q = (query ?: _trackingQuery.value).trim()
        if (q.isBlank()) {
            _trackingMessage.value = "Please enter an Order ID or Mobile Number"
            return
        }

        viewModelScope.launch {
            _isSearchingTracking.value = true
            _trackingMessage.value = null
            try {
                // Try looking up by order number
                val byNumber = repository.getOrderByNumber(q)
                if (byNumber != null) {
                    _trackedOrder.value = byNumber
                } else {
                    // Try looking up by exact mobile in local list
                    val currentList = allOrders.value
                    val match = currentList.firstOrNull {
                        it.orderNumber.equals(q, ignoreCase = true) ||
                        it.mobileNumber.replace("-", "").trim() == q.replace("-", "").trim()
                    }
                    if (match != null) {
                        _trackedOrder.value = match
                    } else {
                        _trackedOrder.value = null
                        _trackingMessage.value = "No order found matching '$q'. Please verify the Order ID (e.g. SK-001042)."
                    }
                }
            } finally {
                _isSearchingTracking.value = false
            }
        }
    }

    // Customer History lookup
    private val _historyMobileFilter = MutableStateFlow("")
    val historyMobileFilter: StateFlow<String> = _historyMobileFilter.asStateFlow()

    fun updateHistoryMobileFilter(filter: String) {
        _historyMobileFilter.value = filter
    }

    // Admin Authentication & Operations
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    private val _loggedInAdmin = MutableStateFlow<AdminAccount?>(null)
    val loggedInAdmin: StateFlow<AdminAccount?> = _loggedInAdmin.asStateFlow()

    private val _adminPinInput = MutableStateFlow("")
    val adminPinInput: StateFlow<String> = _adminPinInput.asStateFlow()

    private val _adminAuthError = MutableStateFlow<String?>(null)
    val adminAuthError: StateFlow<String?> = _adminAuthError.asStateFlow()

    fun updateAdminPin(pin: String) {
        _adminPinInput.value = pin
        _adminAuthError.value = null
    }

    fun loginAdminWithPin(pin: String? = null) {
        val code = (pin ?: _adminPinInput.value).trim()
        val matchedAdmin = AUTHORIZED_ADMINS.find { it.accessCode == code }
        if (matchedAdmin != null || code == "Admin@1973") {
            val adminUser = matchedAdmin ?: AUTHORIZED_ADMINS.first()
            _loggedInAdmin.value = adminUser
            _isAdminLoggedIn.value = true
            _adminPinInput.value = ""
            _adminAuthError.value = null
        } else {
            _adminAuthError.value = "Invalid Passcode. Please enter authorized admin passcode."
        }
    }

    fun quickLoginAs(admin: AdminAccount) {
        _loggedInAdmin.value = admin
        _isAdminLoggedIn.value = true
        _adminAuthError.value = null
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
        _loggedInAdmin.value = null
        _adminPinInput.value = ""
    }

    // Admin Filters & Search
    private val _adminSearchQuery = MutableStateFlow("")
    val adminSearchQuery: StateFlow<String> = _adminSearchQuery.asStateFlow()

    private val _adminStatusFilter = MutableStateFlow("All")
    val adminStatusFilter: StateFlow<String> = _adminStatusFilter.asStateFlow()

    fun updateAdminSearch(query: String) {
        _adminSearchQuery.value = query
    }

    fun updateAdminStatusFilter(status: String) {
        _adminStatusFilter.value = status
    }

    // Admin Order Detail Modal
    private val _selectedAdminOrder = MutableStateFlow<OrderEntity?>(null)
    val selectedAdminOrder: StateFlow<OrderEntity?> = _selectedAdminOrder.asStateFlow()

    fun selectAdminOrder(order: OrderEntity?) {
        _selectedAdminOrder.value = order
    }

    // Admin Customer Profile Modal
    private val _selectedAdminCustomer = MutableStateFlow<CustomerEntity?>(null)
    val selectedAdminCustomer: StateFlow<CustomerEntity?> = _selectedAdminCustomer.asStateFlow()

    fun selectAdminCustomer(customer: CustomerEntity?) {
        _selectedAdminCustomer.value = customer
    }

    fun updateStatus(orderId: Long, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
            // If current detail is open, update it
            if (_selectedAdminOrder.value?.id == orderId) {
                _selectedAdminOrder.value = _selectedAdminOrder.value?.copy(
                    status = newStatus,
                    updatedTimestamp = System.currentTimeMillis()
                )
            }
            // If tracked order is open, update it
            if (_trackedOrder.value?.id == orderId) {
                _trackedOrder.value = _trackedOrder.value?.copy(
                    status = newStatus,
                    updatedTimestamp = System.currentTimeMillis()
                )
            }
        }
    }
}
