package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String,
    val customerName: String,
    val shopName: String,
    val mobileNumber: String,
    val address: String,
    val city: String,
    val teaBlend: String,
    val teaSize: String, // "250g", "500g", "1kg", "Custom"
    val unitCount: Int,
    val totalKg: Double,
    val notes: String = "",
    val status: String = "New", // New, Confirmed, Processing, Out for Delivery, Delivered, Cancelled
    val timestamp: Long = System.currentTimeMillis(),
    val updatedTimestamp: Long = System.currentTimeMillis()
)
