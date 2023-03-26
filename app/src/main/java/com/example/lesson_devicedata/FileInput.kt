package com.example.lesson_devicedata

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.io.*

@Composable
fun FileInput(context: Context) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val fileName = remember { mutableStateOf("") }
        val fileData = remember { mutableStateOf("") }

        OutlinedTextField(value = fileName.value, onValueChange = { fileName.value = it }, label = {
            Text(
                text = "Type a file name"
            )
        })
        OutlinedTextField(value = fileData.value, onValueChange = { fileData.value = it }, label = {
            Text(
                text = "Type a file data"
            )
        })
        Button(onClick = {
            val file: String = fileName.value
            val data: String = fileData.value
            val fileOutputStream: FileOutputStream
            try {
                fileOutputStream = context.openFileOutput(file, Context.MODE_PRIVATE)
                fileOutputStream.write(data.toByteArray())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Toast.makeText(context, "data save", Toast.LENGTH_LONG).show()
            fileName.value = ""
            fileData.value = ""
        }) { Text(text = "Save") }
        Button(onClick = {
            val filename = fileName.value
            if (filename.trim() != "") {
                val fileInputStream = context.openFileInput(filename)
                val inputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder: StringBuilder = StringBuilder()
                var text: String?
                while (run {
                        text = bufferedReader.readLine()
                        text
                    } != null) {
                    stringBuilder.append(text)
                }
                fileData.value = stringBuilder.toString()
            } else {
                Toast.makeText(context, "file name cannot be blank", Toast.LENGTH_LONG).show()
            }
        }) { Text(text = "View") }
    }
}