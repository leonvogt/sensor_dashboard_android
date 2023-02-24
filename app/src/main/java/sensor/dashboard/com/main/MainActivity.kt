package sensor.dashboard.com.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.hotwire.turbo.activities.TurboActivity
import dev.hotwire.turbo.delegates.TurboActivityDelegate
import sensor.dashboard.com.R
import sensor.dashboard.com.databinding.ActivityMainBinding
import sensor.dashboard.com.util.DEVICES_URL
import sensor.dashboard.com.util.HOME_URL

class MainActivity : AppCompatActivity(), TurboActivity {
    override lateinit var delegate: TurboActivityDelegate
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
}
