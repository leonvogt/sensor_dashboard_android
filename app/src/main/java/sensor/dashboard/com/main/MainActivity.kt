package sensor.dashboard.com.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import dev.hotwire.turbo.activities.TurboActivity
import dev.hotwire.turbo.delegates.TurboActivityDelegate
import sensor.dashboard.com.BuildConfig
import sensor.dashboard.com.R
import sensor.dashboard.com.databinding.ActivityMainBinding
import sensor.dashboard.com.util.DEVICES_URL
import sensor.dashboard.com.util.HOME_URL


class MainActivity : AppCompatActivity(), TurboActivity {
    override lateinit var delegate: TurboActivityDelegate
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askNotificationPermission()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        delegate = TurboActivityDelegate(this, R.id.main_nav_host)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                delegate.navigate(HOME_URL)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_devices -> {
                delegate.navigate(DEVICES_URL)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                delegate.navigate(HOME_URL)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            println("PushNotification: Permission granted")
        } else {
            println("PushNotification: Permission NOT granted")
        }
    }

    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("PushNotification: Fetching FCM registration token failed")
                return@OnCompleteListener
            }
            saveServerCommunicationSettings(task.result)
        })
    }
    @SuppressLint("HardwareIds")
    private fun saveServerCommunicationSettings(notificationToken: String?) {
        val sharedPreferences = applicationContext.getSharedPreferences("MobileAppInfos", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("pushNotificationToken", notificationToken)
        editor.putString("uniqueMobileId", Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
        editor.putString("platform", "android")
        editor.putString("appVersion", BuildConfig.VERSION_NAME)
        editor.apply()
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                println("PushNotification: Permission granted")
                getFirebaseToken()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                println("PushNotification: Permission NOT granted")
            } else {
                println("PushNotification: Ask for permission")
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
