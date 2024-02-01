package com.example.tipcalculator.views

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.components.DeclareInputField
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import com.example.tipcalculator.util.calculateTipTotalTip
import com.example.tipcalculator.util.calculateTotalPerPerson
import com.example.tipcalculator.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}

// entry point
@Composable
fun MyApp(appContent: @Composable () -> Unit) {
    TipCalculatorTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            appContent()
        }
    }
}

//@Preview
@Composable
fun MainContent() {
    val splitByState = remember { mutableIntStateOf(1) }
    val tipAmountState = remember { mutableDoubleStateOf(0.0) }
    val totalPerPersonState = remember { mutableDoubleStateOf(0.0) }
    val rangeValueSet = IntRange(1, 30)
    val totalBillState = remember { mutableStateOf("") }
    val sliderPositionState = remember { mutableFloatStateOf(0f) }

    BillForm(
        splitByState = splitByState,
        tipAmountState = tipAmountState,
        totalPerPersonState = totalPerPersonState,
        rangeValueSet = rangeValueSet,
        totalBillState = totalBillState,
        sliderPositionState = sliderPositionState
    ) { billAmount ->
        Log.d(TAG, "Main Content: $billAmount")
    }
}

@Composable
fun TopHeaderView(totalPerPerson: Double) {
    Surface(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clip(CircleShape.copy(all = CornerSize(15.dp))),
        // .clip(shape = RounderCornerShape(corner = CornerSize(15.dp)))
        color = Color(0xFFFE9D7D)
    ) {
        Column(
            modifier = Modifier.padding(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // format the number of digits to display
            val totalDigits = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person:", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$$totalDigits", style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/*
Method to hold state and calculate all values.
 */
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    rangeValueSet: IntRange = 1..30,
    splitByState: MutableIntState,
    tipAmountState: MutableDoubleState,
    totalPerPersonState: MutableDoubleState,
    totalBillState: MutableState<String>,
    sliderPositionState: MutableFloatState,
    onValueChanged: (String) -> Unit = {} //callback function
) {
    // state holders
    val validInputState =
        remember(totalBillState.value) { totalBillState.value.trim().isNotEmpty() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val tipPercentage = (sliderPositionState.floatValue * 100).toInt()

    Column(
        modifier = modifier.padding(
            top = 20.dp,
            start = 10.dp,
            end = 10.dp
        )
    ) {
        // call top header before creating bottom layer
        TopHeaderView(totalPerPerson = totalPerPersonState.doubleValue)
        Spacer(modifier = modifier.height(3.dp))

        Surface(
            modifier = modifier
                .padding(2.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(width = 3.dp, color = Color(0xFFFE9D7D))
        ) {
            Column(
                modifier = modifier.padding(10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // method from components package
                DeclareInputField(
                    valueState = totalBillState,
                    labelId = "Enter Amount",
                    enabled = true,
                    isSingleLine = true,
                    onAction = KeyboardActions {
                        if (!validInputState) return@KeyboardActions
                        onValueChanged(totalBillState.value.trim())
                        keyboardController?.hide()
                    }
                )
                if (validInputState) {
                    Row(
                        modifier = modifier.padding(5.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Split",
                            modifier = modifier.align(alignment = Alignment.CenterVertically)
                        )
                        Spacer(modifier = modifier.width(120.dp))

                        Row(
                            modifier = modifier.padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            val isValidSplitInput = splitByState.intValue > 1
                            RoundIconButton(
                                imageVector = Icons.Default.RemoveCircle,
                                onClick = {
                                    //dynamically adjust values
                                    splitByState.intValue =
                                        if (isValidSplitInput) splitByState.intValue - 1 else 1
                                    totalPerPersonState.doubleValue = calculateTotalPerPerson(
                                        totalBillState.value.toDouble(),
                                        splitByState.intValue,
                                        tipPercentage
                                    )
                                },
                            )

                            Text(
                                text = "${splitByState.intValue}",
                                modifier = modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 9.dp, end = 9.dp)
                            )

                            RoundIconButton(
                                imageVector = Icons.Default.AddCircle,
                                onClick = {
                                    //dynamically adjusts values
                                    if (splitByState.intValue < rangeValueSet.last) splitByState.intValue += 1
                                    totalPerPersonState.doubleValue = calculateTotalPerPerson(
                                        totalBillState.value.toDouble(),
                                        splitByState.intValue,
                                        tipPercentage
                                    )
                                }
                            )
                        }
                    }
                    Row(modifier = modifier.padding(6.dp)) {
                        Text(text = "Tip", modifier = modifier.align(Alignment.CenterVertically))
                        Spacer(modifier = modifier.width(182.dp))

                        // set value of tip
                        Text(
                            text = "${tipAmountState.doubleValue}",
                            modifier = modifier.align(Alignment.CenterVertically)
                        )
                    }
                    Column(
                        modifier = modifier.padding(top = 20.dp, start = 5.dp, end = 5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$tipPercentage %")
                        Spacer(modifier = modifier.height(20.dp))
                        // Slider
                        Slider(
                            modifier = modifier.padding(start = 10.dp),
                            value = sliderPositionState.floatValue,
                            onValueChange = { newVal ->
                                // when values is changed calculate slider, tip, and per person value
                                sliderPositionState.floatValue = newVal
                                tipAmountState.doubleValue =
                                    calculateTipTotalTip(
                                        totalBillState.value.toDouble(),
                                        tipPercentage
                                    )
                                totalPerPersonState.doubleValue = calculateTotalPerPerson(
                                    totalBillState.value.toDouble(),
                                    splitByState.intValue,
                                    tipPercentage
                                )
                                Log.d(TAG, "New Value: $newVal")
                            },
                            steps = 12,
                            enabled = true,
                            colors = SliderDefaults.colors(Color(0xFFFE9D7D)),
                            onValueChangeFinished = {
                                Log.d(TAG, "BillForm Method Finished...")
                            }
                        )
                    }
                } else {
                    Box {}
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    TipCalculatorTheme {
        MyApp {
            MainContent()
        }
    }
}