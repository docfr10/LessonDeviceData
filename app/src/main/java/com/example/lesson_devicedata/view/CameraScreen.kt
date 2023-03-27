package com.example.lesson_devicedata.view

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun CameraScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val bitmap = remember { mutableStateOf<Bitmap?>(null) }
        val launcher: ActivityResultLauncher<Void> = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { result ->
            result?.let {
                bitmap.value = it
            }
        }

        bitmap.value?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(400.dp)
            )
        }

        Button(onClick = {
            launcher.launch(null)
        }) {
            Text(text = "Take picture")
        }
    }
}
