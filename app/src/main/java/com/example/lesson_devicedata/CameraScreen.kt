package com.example.lesson_devicedata

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.io.File


@Composable
fun CameraScreen(takeAPictureLauncher: ActivityResultLauncher<Intent>) {
    var photo: Uri? = null

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val file = File(
                Environment.getExternalStorageDirectory(),
                "test.jpg"
            )
            photo = Uri.fromFile(file)
            takeAPictureLauncher.launch(takePictureIntent)
        }) {
            Text(text = "Take a photo")
        }
    }
}
