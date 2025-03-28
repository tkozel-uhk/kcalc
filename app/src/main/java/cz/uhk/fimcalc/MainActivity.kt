package cz.uhk.fimcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.uhk.fimcalc.ui.theme.FimCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FimCalcTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Calculator()
                }
            }
        }
    }
}

@Composable
fun Calculator(modifier: Modifier = Modifier) {
    val displayValue = rememberSaveable {
        mutableStateOf("0")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        CalculatorDisplay(displayValue.value)
        CalculatorButtons {
            //TODO C, M, DEL, AC ...
            if (it == "=") {
                displayValue.value = eval(displayValue.value)
            } else {
                displayValue.value += it
            }
        }
    }
}

fun eval(value: String): String {
    val values = value.split("+", "-", "*", "÷")
    val operators = value.filter { it in "+-*/" }

    var result = values[0].toDouble()
    for (i in 1 until values.size) {
        val operator = operators[i - 1]
        val nextValue = values[i].toDouble()
        when (operator) {
            '+' -> result += nextValue
            '-' -> result -= nextValue
            '*' -> result *= nextValue
            '÷' -> result /= nextValue
        }
    }
    return result.toString()
}

@Composable
fun CalculatorButtons(onClick: (text: String) -> Unit) {
    val btLabels = listOf(
        "AC", "⌫", "M", "+",
        "7",  "8", "9", "-",
        "4",  "5", "6", "*",
        "1",  "2", "3", "÷",
        "C",  "0", ".", "="
    )
    val btWin = btLabels.windowed(4, 4, true)
    for (btRow in btWin) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (bt in btRow) {
                CalculatorButton(
                    text = bt, onClick = { onClick(bt) },
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                )
            }
        }
    }
}

@Composable
fun CalculatorButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CalculatorDisplay(value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFC0F0C0)
        ),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                fontSize = 36.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    FimCalcTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Calculator()
        }
    }
}