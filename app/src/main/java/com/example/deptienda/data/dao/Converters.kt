package com.example.deptienda.data.dao

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.example.deptienda.data.models.CartItem

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value ?: emptyList<String>())
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList<String>()
        return try {
            val listType = object : TypeToken<List<String>>() {}.type
            gson.fromJson(value, listType) ?: emptyList<String>()
        } catch (e: Exception) {
            emptyList<String>()
        }
    }

    @TypeConverter
    fun fromCartItemList(value: List<CartItem>?): String {
        return gson.toJson(value ?: emptyList<CartItem>())
    }

    @TypeConverter
    fun toCartItemList(value: String?): List<CartItem> {
        if (value.isNullOrEmpty()) return emptyList<CartItem>()
        return try {
            val listType = object : TypeToken<List<CartItem>>() {}.type
            gson.fromJson(value, listType) ?: emptyList<CartItem>()
        } catch (e: Exception) {
            emptyList<CartItem>()
        }
    }
}
