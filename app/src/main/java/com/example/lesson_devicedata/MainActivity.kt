package com.example.lesson_devicedata

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.example.lesson_devicedata.ui.theme.LessonDeviceDataTheme
import com.example.lesson_devicedata.view.LocationScreen
import com.example.lesson_devicedata.view.QRCodeScreen
import java.util.*


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

    // Location
    private lateinit var locationManager: LocationManager
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(p0: Location) {
            showLocation(p0)
        }

        override fun onProviderDisabled(provider: String) {
            checkEnabled()
        }

        override fun onProviderEnabled(provider: String) {
            checkEnabled()
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            showLocation(locationManager.getLastKnownLocation(provider))
        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            when (status) {
                LocationProvider.AVAILABLE -> {
                    // Do something when the provider becomes available.
                }
                LocationProvider.OUT_OF_SERVICE -> {
                    // Do something when the provider goes out of service.
                }
                LocationProvider.TEMPORARILY_UNAVAILABLE -> {
                    // Do something when the provider becomes temporarily unavailable.
                }
            }
        }
    }
    private val locationGPS = mutableStateOf("")
    private val locationNet = mutableStateOf("")
    private val enabledGPS = mutableStateOf("")
    private val enabledNet = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        setContent {
            LessonDeviceDataTheme {
                val context = LocalContext.current

                //CameraScreen(takeAPictureLauncher = takeAPictureLauncher)
                //FileInputScreen(context = context)
                //LocationScreen(
                //    locationGPS = locationGPS,
                //    locationNet = locationNet,
                //    enabledGPS = enabledGPS,
                //    enabledNet = enabledNet
                //)
                //QRCodeScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            10000L, 10F, locationListener
        )
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, 10000L, 10F,
            locationListener
        )
        checkEnabled()
    }

    private fun showLocation(location: Location?) {
        if (location == null) return
        if (location.provider == LocationManager.GPS_PROVIDER) {
            locationGPS.value = formatLocation(location)
        } else if (location.provider ==
            LocationManager.NETWORK_PROVIDER
        ) {
            locationNet.value = formatLocation(location)
        }
    }

    private fun formatLocation(location: Location?): String {
        return if (location == null) "" else String.format(
            "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3\$tF %3\$tT",
            location.latitude, location.longitude, Date(
                location.time
            )
        )
    }

    private fun checkEnabled() {
        enabledGPS.value = (
                "Enabled: "
                        + locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)
                )
        enabledNet.value = (
                ("Enabled: "
                        + locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                )
    }
}