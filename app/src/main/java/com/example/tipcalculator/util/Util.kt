package com.example.tipcalculator.util

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