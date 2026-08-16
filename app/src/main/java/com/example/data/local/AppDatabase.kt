package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.CustomerDao
import com.example.data.local.dao.OrderDao
import com.example.data.local.entity.CustomerEntity
import com.example.data.local.entity.OrderEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [OrderEntity::class, CustomerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun customerDao(): CustomerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "skt_tea_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.orderDao(), database.customerDao())
                    }
                }
            }

            private suspend fun populateInitialData(orderDao: OrderDao, customerDao: CustomerDao) {
                val now = System.currentTimeMillis()
                val oneHour = 3600 * 1000L
                val oneDay = 24 * 3600 * 1000L

                // Initial realistic tea shop orders for SK Tea Company
                val sampleCustomers = listOf(
                    CustomerEntity(
                        id = 1,
                        name = "Haji Abdul Rehman",
                        shopName = "Bismillah Tea Stall & General Store",
                        mobileNumber = "0301-4567890",
                        address = "Shop #12, G.T. Road Market, Near Clock Tower",
                        city = "Gujranwala",
                        totalOrders = 3,
                        totalKgOrdered = 45.0,
                        createdAt = now - (5 * oneDay),
                        lastOrderTimestamp = now - (2 * oneHour)
                    ),
                    CustomerEntity(
                        id = 2,
                        name = "Chaudhry Tariq Mehmood",
                        shopName = "Al-Madina Tea Mart",
                        mobileNumber = "0322-7654321",
                        address = "Shop #4, Main Anarkali Wholesale Market",
                        city = "Lahore",
                        totalOrders = 2,
                        totalKgOrdered = 30.0,
                        createdAt = now - (7 * oneDay),
                        lastOrderTimestamp = now - (5 * oneHour)
                    ),
                    CustomerEntity(
                        id = 3,
                        name = "Malik Usman Ali",
                        shopName = "Shandar Chai Khana",
                        mobileNumber = "0333-5551234",
                        address = "Commercial Area, Block B, Satellite Town",
                        city = "Rawalpindi",
                        totalOrders = 1,
                        totalKgOrdered = 15.0,
                        createdAt = now - (2 * oneDay),
                        lastOrderTimestamp = now - (1 * oneDay)
                    ),
                    CustomerEntity(
                        id = 4,
                        name = "Sheikh Nadeem",
                        shopName = "Khyber Hotel & Restaurant",
                        mobileNumber = "0345-7778899",
                        address = "Opposite General Bus Stand, Sargodha Road",
                        city = "Faisalabad",
                        totalOrders = 1,
                        totalKgOrdered = 25.0,
                        createdAt = now - (3 * oneDay),
                        lastOrderTimestamp = now - (2 * oneDay)
                    )
                )

                sampleCustomers.forEach { customerDao.insertCustomer(it) }

                val sampleOrders = listOf(
                    OrderEntity(
                        id = 1,
                        orderNumber = "SK-001042",
                        customerName = "Haji Abdul Rehman",
                        shopName = "Bismillah Tea Stall & General Store",
                        mobileNumber = "0301-4567890",
                        address = "Shop #12, G.T. Road Market, Near Clock Tower",
                        city = "Gujranwala",
                        teaBlend = "SK Royal Danedar Blend",
                        teaSize = "500g",
                        unitCount = 20,
                        totalKg = 10.0,
                        notes = "Please ensure fresh batch packing with gold seal",
                        status = "New",
                        timestamp = now - (2 * oneHour),
                        updatedTimestamp = now - (2 * oneHour)
                    ),
                    OrderEntity(
                        id = 2,
                        orderNumber = "SK-001041",
                        customerName = "Chaudhry Tariq Mehmood",
                        shopName = "Al-Madina Tea Mart",
                        mobileNumber = "0322-7654321",
                        address = "Shop #4, Main Anarkali Wholesale Market",
                        city = "Lahore",
                        teaBlend = "SK Premium Gold Leaf",
                        teaSize = "1kg",
                        unitCount = 15,
                        totalKg = 15.0,
                        notes = "Deliver before 3:00 PM today",
                        status = "Processing",
                        timestamp = now - (5 * oneHour),
                        updatedTimestamp = now - (1 * oneHour)
                    ),
                    OrderEntity(
                        id = 3,
                        orderNumber = "SK-001040",
                        customerName = "Malik Usman Ali",
                        shopName = "Shandar Chai Khana",
                        mobileNumber = "0333-5551234",
                        address = "Commercial Area, Block B, Satellite Town",
                        city = "Rawalpindi",
                        teaBlend = "SK Classic Strong Chai Mixture",
                        teaSize = "500g",
                        unitCount = 30,
                        totalKg = 15.0,
                        notes = "Call upon arrival at main gate",
                        status = "Out for Delivery",
                        timestamp = now - (1 * oneDay),
                        updatedTimestamp = now - (3 * oneHour)
                    ),
                    OrderEntity(
                        id = 4,
                        orderNumber = "SK-001039",
                        customerName = "Sheikh Nadeem",
                        shopName = "Khyber Hotel & Restaurant",
                        mobileNumber = "0345-7778899",
                        address = "Opposite General Bus Stand, Sargodha Road",
                        city = "Faisalabad",
                        teaBlend = "SK Special Hotel Karak Blend",
                        teaSize = "1kg",
                        unitCount = 25,
                        totalKg = 25.0,
                        notes = "Direct delivery to hotel kitchen",
                        status = "Delivered",
                        timestamp = now - (2 * oneDay),
                        updatedTimestamp = now - (1 * oneDay)
                    )
                )

                sampleOrders.forEach { orderDao.insertOrder(it) }
            }
        }
    }
}
