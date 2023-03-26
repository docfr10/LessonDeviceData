package com.example.lesson_devicedata.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LocationScreen(
    locationGPS: MutableState<String>,
    locationNet: MutableState<String>,
    enabledGPS: MutableState<String>,
    enabledNet: MutableState<String>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = enabledGPS.value)
        Text(text = enabledNet.value)
        Text(text = locationGPS.value)
        Text(text = locationNet.value)
    }
}

