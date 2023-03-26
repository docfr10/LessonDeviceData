package com.example.lesson_devicedata

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import com.example.lesson_devicedata.ui.theme.LessonDeviceDataTheme

class MainActivity : ComponentActivity() {
    // Camera
    private var imageView: ImageView? = null
    private val takeAPictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val thumbnailBitmap = result.data?.extras?.get("data") as Bitmap
                imageView?.setImageBitmap(thumbnailBitmap)
            } else {
                Toast.makeText(this, "Failed to take picture", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LessonDeviceDataTheme {
                val context = LocalContext.current

                //CameraScreen(takeAPictureLauncher = takeAPictureLauncher)
                FileInput(context = context)
            }
        }
    }
}