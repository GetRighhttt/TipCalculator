package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                TopHeader()
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

@Preview(showBackground = false)
@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
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
            Text(text = "Total Per Person:", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium)
            Text(text = "$$totalDigits", style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoSomething() {}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipCalculatorTheme {
        MyApp {
            TopHeader()
        }
    }
}