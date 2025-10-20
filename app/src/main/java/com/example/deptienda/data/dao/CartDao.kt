package com.example.com.dep.data.dao

import androidx.room.*
import com.example.com.dep.data.models.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    // Obtener todos los items del carrito para un usuario
    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun getCartItems(userId: String): Flow<List<CartItem>>

    // Obtener un item específico del carrito
    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId AND selectedSize = :size AND selectedColor = :color")
    fun getCartItem(userId: String, productId: String, size: String, color: String): Flow<CartItem?>

    // Contar items en el carrito
    @Query("SELECT COUNT(*) FROM cart_items WHERE userId = :userId")
    fun getCartItemCount(userId: String): Flow<Int>

    // Insertar o reemplazar item en el carrito
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    // Actualizar item del carrito
    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    // Eliminar item específico del carrito
    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    // Eliminar item por IDs
    @Query("DELETE FROM cart_items WHERE userId = :userId AND productId = :productId AND selectedSize = :size AND selectedColor = :color")
    suspend fun deleteCartItemByIds(userId: String, productId: String, size: String, color: String)

    // Limpiar todo el carrito de un usuario
    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: String)

    // Obtener el total del carrito
    @Query("SELECT SUM(ci.quantity * p.price) FROM cart_items ci INNER JOIN products p ON ci.productId = p.id WHERE ci.userId = :userId")
    fun getCartTotal(userId: String): Flow<Double?>

    // Actualizar cantidad de un item
    @Query("UPDATE cart_items SET quantity = :quantity WHERE userId = :userId AND productId = :productId AND selectedSize = :size AND selectedColor = :color")
    suspend fun updateQuantity(userId: String, productId: String, size: String, color: String, quantity: Int)
}