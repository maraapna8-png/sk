package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE orderNumber = :orderNumber LIMIT 1")
    suspend fun getOrderByNumber(orderNumber: String): OrderEntity?

    @Query("SELECT * FROM orders WHERE mobileNumber = :mobileNumber ORDER BY timestamp DESC")
    fun getOrdersByMobile(mobileNumber: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    suspend fun getOrderById(id: Long): OrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Query("UPDATE orders SET status = :newStatus, updatedTimestamp = :updatedTime WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, newStatus: String, updatedTime: Long)

    @Query("SELECT COUNT(*) FROM orders")
    fun getOrderCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM orders WHERE status = :status")
    fun getCountByStatus(status: String): Flow<Int>

    @Query("SELECT SUM(totalKg) FROM orders WHERE status != 'Cancelled'")
    fun getTotalKgOrdered(): Flow<Double?>
}
