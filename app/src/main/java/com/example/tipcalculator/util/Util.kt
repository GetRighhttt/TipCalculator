package com.example.tipcalculator.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun calculateTipTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill > 1 && totalBill.toString().isNotEmpty()) {
        (totalBill * tipPercentage) / 100
    } else {
        0.0
    }
}

fun calculateTotalPerPerson(totalBill: Double, splitBy: Int, tipPercentage: Int): Double {
    val bill = calculateTipTotalTip(totalBill, tipPercentage) + totalBill
    return (bill / splitBy)
}

operator fun Int.invoke(): Dp = dp
