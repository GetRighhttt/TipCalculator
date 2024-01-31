package com.example.tipcalculator.views

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import com.example.tipcalculator.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Column(
                    modifier = Modifier.padding(
                        top = 20.dp,
                        start = 10.dp,
                        end = 10.dp
                    )
                ) {
                    TopHeader()
                    Spacer(modifier = Modifier.height(5.dp))
                    MainContent()
                }
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

@Composable
fun TopHeader(totalPerPerson: Double = 140.0) {
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

//@Preview
@Composable
fun MainContent() {
    BillForm() { billAmount ->
        Log.d(TAG, "Main Content: $billAmount")
    }
}

@Preview
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit = {} //callback function
) {
    // state holders
    val totalBillState = remember { mutableStateOf("") }
    val validInputState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val splitByInput = remember { mutableIntStateOf(1) }
    val rangeValueSet = IntRange(1, 30)
    val sliderPositionState = remember { mutableFloatStateOf(0f) }

    // start of view
    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 3.dp, color = Color(0xFFFE9D7D))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
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
//            if (validInputState) {
            Row(
                modifier = Modifier.padding(5.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Split",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(120.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    val isValidSplitInput = splitByInput.intValue > 1
                    RoundIconButton(
                        imageVector = Icons.Default.RemoveCircle,
                        onClick = {
                            splitByInput.intValue =
                                if (isValidSplitInput) splitByInput.intValue - 1 else 1
                        },
                    )

                    Text(
                        text = "${splitByInput.intValue}",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp)
                    )

                    RoundIconButton(
                        imageVector = Icons.Default.AddCircle,
                        onClick = {
                            if(splitByInput.intValue < rangeValueSet.last) splitByInput.intValue += 1
                        }
                    )
                }
            }
            Row(modifier = Modifier.padding(6.dp)) {
                Text(text = "Tip", modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(182.dp))
                Text(text = "$33.00", modifier = Modifier.align(Alignment.CenterVertically))
            }
            Column(
                modifier = Modifier.padding(top = 20.dp, start = 5.dp, end = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "33%")
                Spacer(modifier = Modifier.height(20.dp))
                // Slider
                Slider(
                    modifier = Modifier.padding(start = 10.dp),
                    value = sliderPositionState.floatValue,
                    onValueChange = { newVal ->
                        sliderPositionState.floatValue = newVal
                        Log.d(TAG, "New Value: $newVal")
                    },
                    steps = 5,
                    enabled = true,
                    colors = SliderDefaults.colors(Color(0xFFFE9D7D)),
                    onValueChangeFinished = {
                        Log.d(TAG, "BillForm Method Finished...")
                    }
                )
            }
//            } else {
//                Box {}
//            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TipCalculatorTheme {
//        MyApp {
//            TopHeader()
//        }
//    }
//}