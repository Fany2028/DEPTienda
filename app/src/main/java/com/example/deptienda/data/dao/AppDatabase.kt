package com.example.com.dep.data.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.com.dep.data.models.Product
import com.example.com.dep.data.models.User
import com.example.com.dep.data.models.CartItem
import com.example.com.dep.data.models.Order

@Database(
    entities = [
        Product::class,
        User::class,
        CartItem::class,
        Order::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dep_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}