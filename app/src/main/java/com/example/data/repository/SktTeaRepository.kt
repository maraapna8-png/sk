package com.example.data.repository

import com.example.data.local.dao.CustomerDao
import com.example.data.local.dao.OrderDao
import com.example.data.local.entity.CustomerEntity
import com.example.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import kotlin.random.Random

class SktTeaRepository(
    private val orderDao: OrderDao,
    private val customerDao: CustomerDao
) {
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()
    val allCustomers: Flow<List<CustomerEntity>> = customerDao.getAllCustomers()
    val totalOrderCount: Flow<Int> = orderDao.getOrderCount()
    val totalKgSum: Flow<Double?> = orderDao.getTotalKgOrdered()

    fun getCountForStatus(status: String): Flow<Int> = orderDao.getCountByStatus(status)

    fun getOrdersByMobile(mobile: String): Flow<List<OrderEntity>> = orderDao.getOrdersByMobile(mobile)

    suspend fun getOrderByNumber(orderNumber: String): OrderEntity? {
        val cleanNumber = orderNumber.trim().uppercase(Locale.ROOT)
        return orderDao.getOrderByNumber(cleanNumber)
    }

    suspend fun submitOrder(
        customerName: String,
        shopName: String,
        mobileNumber: String,
        address: String,
        city: String,
        teaBlend: String,
        teaSize: String,
        unitCount: Int,
        totalKg: Double,
        notes: String
    ): OrderEntity {
        val randomDigits = Random.nextInt(100100, 999900)
        val generatedOrderNumber = "SK-$randomDigits"
        val currentTime = System.currentTimeMillis()

        val newOrder = OrderEntity(
            orderNumber = generatedOrderNumber,
            customerName = customerName.trim(),
            shopName = shopName.trim(),
            mobileNumber = mobileNumber.trim(),
            address = address.trim(),
            city = city.trim(),
            teaBlend = teaBlend,
            teaSize = teaSize,
            unitCount = unitCount,
            totalKg = totalKg,
            notes = notes.trim(),
            status = "New",
            timestamp = currentTime,
            updatedTimestamp = currentTime
        )

        val insertedId = orderDao.insertOrder(newOrder)
        val savedOrder = newOrder.copy(id = insertedId)

        // Update or insert Customer record
        val existingCustomer = customerDao.getCustomerByMobile(mobileNumber.trim())
        if (existingCustomer != null) {
            val updatedCustomer = existingCustomer.copy(
                name = customerName.trim(),
                shopName = shopName.trim(),
                address = address.trim(),
                city = city.trim(),
                totalOrders = existingCustomer.totalOrders + 1,
                totalKgOrdered = existingCustomer.totalKgOrdered + totalKg,
                lastOrderTimestamp = currentTime
            )
            customerDao.updateCustomer(updatedCustomer)
        } else {
            val freshCustomer = CustomerEntity(
                name = customerName.trim(),
                shopName = shopName.trim(),
                mobileNumber = mobileNumber.trim(),
                address = address.trim(),
                city = city.trim(),
                totalOrders = 1,
                totalKgOrdered = totalKg,
                createdAt = currentTime,
                lastOrderTimestamp = currentTime
            )
            customerDao.insertCustomer(freshCustomer)
        }

        return savedOrder
    }

    suspend fun updateOrderStatus(orderId: Long, newStatus: String) {
        orderDao.updateOrderStatus(orderId, newStatus, System.currentTimeMillis())
    }
}
