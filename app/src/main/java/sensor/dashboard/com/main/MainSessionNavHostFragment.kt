package sensor.dashboard.com.main

import androidx.fragment.app.Fragment
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import dev.hotwire.turbo.BuildConfig
import dev.hotwire.turbo.config.TurboPathConfiguration
import dev.hotwire.turbo.session.TurboSessionNavHostFragment
import sensor.dashboard.com.features.web.WebBottomSheetFragment
import sensor.dashboard.com.util.HOME_URL
import sensor.dashboard.com.util.initDayNightTheme
import sensor.dashboard.com.features.web.WebFragment
import sensor.dashboard.com.features.web.WebHomeFragment
import sensor.dashboard.com.features.web.WebModalFragment
import sensor.dashboard.com.util.PATH_CONFIGURATION_URL
import kotlin.reflect.KClass

class MainSessionNavHostFragment : TurboSessionNavHostFragment() {
    override val sessionName = "main"

    override val startLocation = HOME_URL

    override val registeredActivities: List<KClass<out AppCompatActivity>>
        get() = listOf()

    override val registeredFragments: List<KClass<out Fragment>>
        get() = listOf(
            WebFragment::class,
            WebHomeFragment::class,
            WebModalFragment::class,
            WebBottomSheetFragment::class,
            )

    override val pathConfigurationLocation: TurboPathConfiguration.Location
        get() = TurboPathConfiguration.Location(
            assetFilePath = "json/configuration.json",
            remoteFileUrl = PATH_CONFIGURATION_URL,
        )

    override fun onSessionCreated() {
        super.onSessionCreated()
        session.webView.settings.userAgentString = customUserAgent(session.webView)
        session.webView.initDayNightTheme()

        if (BuildConfig.DEBUG) {
            session.setDebugLoggingEnabled(true)
            WebView.setWebContentsDebuggingEnabled(true)
        }
    }

    private fun customUserAgent(webView: WebView): String {
        return "Turbo Native Android ${webView.settings.userAgentString}"
    }
}