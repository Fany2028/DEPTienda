package com.example.com.dep.data.dao

import androidx.room.*
import com.example.com.dep.data.models.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    // Obtener todas las órdenes de un usuario
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY date DESC")
    fun getOrdersByUser(userId: String): Flow<List<Order>>

    // Obtener orden por ID
    @Query("SELECT * FROM orders WHERE id = :orderId")
    fun getOrderById(orderId: String): Flow<Order?>

    // Insertar nueva orden
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    // Actualizar orden
    @Update
    suspend fun updateOrder(order: Order)

    // Eliminar orden
    @Delete
    suspend fun deleteOrder(order: Order)

    // Obtener historial de órdenes
    @Query("SELECT * FROM orders WHERE userId = :userId AND status = :status")
    fun getOrdersByStatus(userId: String, status: String): Flow<List<Order>>
}