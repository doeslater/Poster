package com.example.poster.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poster.data.model.Article
import com.example.poster.data.model.weather.WeatherResponse
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale


@Composable
fun WallCanvas(
    modifier: Modifier = Modifier,
    headlineNewsState: MutableState<ArrayList<Article>>,
    weatherState: MutableState<WeatherResponse>,
    onRefresh: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {
        if (weatherState.value.weather == null || weatherState.value.weather?.isEmpty() == true || headlineNewsState.value.isEmpty()) {
            MainPopup(
                popupType = PopupType.NO_DATA,
                onRefresh = onRefresh
            )
        } else {
            Card(
                modifier = modifier
                    .wrapContentSize()
                    .padding(6.dp),
                border = BorderStroke(2.dp, color = Color.Black),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.White,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.White,
                )
            ) {
                WeatherSection(weatherState = weatherState)
            }
            NewsSection(headlineNewsState = headlineNewsState)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AccessTime,
                contentDescription = "Time",
                tint = Color.Black
            )
            SafeTimestampDisplay(timestampMillis = System.currentTimeMillis())
        }
    }
}


@Composable
fun WeatherSection(
    weatherState: MutableState<WeatherResponse>
) {
    val data = weatherState.value
    Column(modifier = Modifier.padding(10.dp)) {
        Text(
            text = "${data.main?.temp?.toInt()} °C",
            fontSize = 30.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        data.weather?.let { weatherList ->
            weatherList.forEachIndexed { index, weatherItem ->
                Text(text = weatherItem?.description?.replaceFirstChar { it.uppercase() }
                    ?: "", fontSize = 18.sp, color = Color.Black)
            }
        }
        Text(
            modifier = Modifier.padding(end = 10.dp),
            text = "${data.name ?: ""}, ${getCountryName(data.sys?.country) ?: ""}",
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NewsSection(headlineNewsState: MutableState<ArrayList<Article>>) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        headlineNewsState.value.forEachIndexed { index, article ->
            if (index < 3) {
                if (index == 0) {
                    article.source?.name?.let {
                        Text(
                            it,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
                Text(
                    text = article.title ?: "",
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = article.description ?: "",
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}

@Composable
fun SafeTimestampDisplay(timestampMillis: Long) {
    val instant = remember(timestampMillis) {
        Instant.ofEpochMilli(timestampMillis)
    }

    val formatter = remember {
        DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.FULL)
            .withLocale(Locale.UK)
            .withZone(ZoneId.systemDefault())
    }

    val formattedDateTime = remember(instant, formatter) {
        formatter.format(instant)
    }

    Text(text = formattedDateTime, fontWeight = FontWeight.Bold)
}

fun getCountryName(countryCode: String?): String? {
    if (countryCode == null || countryCode.length != 2) {
        return null // Invalid country code
    }

    val locale = Locale("", countryCode)
    return locale.getDisplayCountry(Locale.ENGLISH)
}


enum class PopupType { NO_INTERNET, NO_DATA }

@Composable
fun MainPopup(
    popupType: PopupType,
    onWifiSettings: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()

    var iconImageVector = Icons.Filled.Spa
    var popupTitle = ""
    var popupText = ""

    when (popupType) {
        PopupType.NO_INTERNET -> {
            iconImageVector = Icons.Filled.WifiOff
            popupTitle = "No Internet Connection"
            popupText = "Please check your Wi-Fi settings and try again."
        }

        PopupType.NO_DATA -> {
            iconImageVector = Icons.Filled.ErrorOutline
            popupTitle = "No Data"
            popupText = "Please refresh the page and try again."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = iconImageVector,
            contentDescription = popupTitle,
            tint = Color.Black,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = popupTitle,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
        Text(
            text = popupText,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            onClick = {
                coroutineScope.launch {
                    if (popupType == PopupType.NO_INTERNET) {
                        onWifiSettings.invoke()
                    } else if (popupType == PopupType.NO_DATA) {
                        onRefresh.invoke()
                    }
                }
            },
        ) {
            Text(
                "Settings",
                color = Color.White,
                fontSize = 16.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
        }
    }
}