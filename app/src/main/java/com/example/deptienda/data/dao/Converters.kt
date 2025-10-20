package com.example.com.dep.data.dao

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return gson.fromJson(value, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun fromCartItemList(value: List<com.example.com.dep.data.models.CartItem>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCartItemList(value: String): List<com.example.com.dep.data.models.CartItem> {
        return gson.fromJson(value, object : TypeToken<List<com.example.com.dep.data.models.CartItem>>() {}.type)
    }
}