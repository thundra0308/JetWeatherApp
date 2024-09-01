package com.example.jetweatherapp.widgets

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Preview
@Composable
fun TemperatureText(
    modifier: Modifier = Modifier,
    modifier1: Modifier = Modifier,
    modifier2: Modifier = Modifier,
    style1: TextStyle = TextStyle(),
    style2: TextStyle = TextStyle(),
    temperature: Int = 0,
    unit: String = "C"
) {
    val temperatureSymbol = when (unit.uppercase()) {
        "C" -> "°C"
        "F" -> "°F"
        "K" -> "K"
        else -> ""
    }
    ConstraintLayout(
        modifier = modifier,
    ) {
        val (tvTemp,tvUnit) = createRefs()
        Text(
            modifier = modifier1
                .constrainAs(tvTemp){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
            text = "$temperature",
            style = style1,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = modifier2
                .constrainAs(tvUnit){
                    start.linkTo(tvTemp.end)
                    top.linkTo(tvTemp.top)
                }
                .offset(y = 0.dp),
            text = "°",
            style = style2,
            textAlign = TextAlign.Center
        )
    }
}