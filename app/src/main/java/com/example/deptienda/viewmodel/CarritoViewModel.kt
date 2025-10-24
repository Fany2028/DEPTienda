package com.example.deptienda.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CarritoViewModel : ViewModel() {

    private val _cartTotal = MutableStateFlow(0.0)
    val cartTotal: StateFlow<Double> = _cartTotal

    fun updateTotal(total: Double) {
        _cartTotal.value = total
    }
}