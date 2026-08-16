package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val shopName: String,
    val mobileNumber: String,
    val address: String,
    val city: String,
    val totalOrders: Int = 1,
    val totalKgOrdered: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val lastOrderTimestamp: Long = System.currentTimeMillis()
)
