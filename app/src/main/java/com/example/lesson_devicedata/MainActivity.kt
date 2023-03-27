package com.example.lesson_devicedata

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
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
                QRCodeScreen()
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

/*
public class MainActivity extends Activity {

  TextView tvEnabledGPS;
  TextView tvStatusGPS;
  TextView tvLocationGPS;
  TextView tvEnabledNet;
  TextView tvStatusNet;
  TextView tvLocationNet;

  private LocationManager locationManager;
  StringBuilder sbGPS = new StringBuilder();
  StringBuilder sbNet = new StringBuilder();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    tvEnabledGPS = (TextView) findViewById(R.id.tvEnabledGPS);
    tvStatusGPS = (TextView) findViewById(R.id.tvStatusGPS);
    tvLocationGPS = (TextView) findViewById(R.id.tvLocationGPS);
    tvEnabledNet = (TextView) findViewById(R.id.tvEnabledNet);
    tvStatusNet = (TextView) findViewById(R.id.tvStatusNet);
    tvLocationNet = (TextView) findViewById(R.id.tvLocationNet);

    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
  }

  @Override
  protected void onResume() {
    super.onResume();
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
        1000 * 10, 10, locationListener);
    locationManager.requestLocationUpdates(
        LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
        locationListener);
    checkEnabled();
  }

  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(locationListener);
  }

  private LocationListener locationListener = new LocationListener() {

    @Override
    public void onLocationChanged(Location location) {
      showLocation(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
      checkEnabled();
    }

    @Override
    public void onProviderEnabled(String provider) {
      checkEnabled();
      showLocation(locationManager.getLastKnownLocation(provider));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
      if (provider.equals(LocationManager.GPS_PROVIDER)) {
        tvStatusGPS.setText("Status: " + String.valueOf(status));
      } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
        tvStatusNet.setText("Status: " + String.valueOf(status));
      }
    }
  };

  private void showLocation(Location location) {
    if (location == null)
      return;
    if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
      tvLocationGPS.setText(formatLocation(location));
    } else if (location.getProvider().equals(
        LocationManager.NETWORK_PROVIDER)) {
      tvLocationNet.setText(formatLocation(location));
    }
  }

  private String formatLocation(Location location) {
    if (location == null)
      return "";
    return String.format(
        "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
        location.getLatitude(), location.getLongitude(), new Date(
            location.getTime()));
  }

  private void checkEnabled() {
    tvEnabledGPS.setText("Enabled: "
        + locationManager
            .isProviderEnabled(LocationManager.GPS_PROVIDER));
    tvEnabledNet.setText("Enabled: "
        + locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
  }

  public void onClickLocationSettings(View view) {
    startActivity(new Intent(
        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
  };

}
*/