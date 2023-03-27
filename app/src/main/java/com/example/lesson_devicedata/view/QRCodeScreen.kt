package com.example.lesson_devicedata.view

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException


@Composable
fun QRCodeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val bitmap = generateQRCode()
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "QR Code",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize()
        )
    }
}

private fun generateQRCode(): Bitmap {
    val multiFormatWriter = MultiFormatWriter()
    try {
        val bitMatrix = multiFormatWriter.encode("Hello", BarcodeFormat.QR_CODE, 200, 200)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                val color = if (bitMatrix[x, y]) Color.Black else Color.White
                bitmap.setPixel(x, y, color.toArgb())
            }
        }
        return bitmap
    } catch (e: WriterException) {
        e.printStackTrace()
        throw RuntimeException("Failed to generate QR Code", e)
    }
}