package sensor.dashboard.com.features.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.hotwire.turbo.nav.TurboNavGraphDestination
import sensor.dashboard.com.R

@TurboNavGraphDestination(uri = "turbo://fragment/web/home")
class WebHomeFragment : WebFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sendCommunicationSettingsToServer()
        return inflater.inflate(R.layout.fragment_web_home, container, false)
    }

    @SuppressLint("InflateParams")
    override fun createErrorView(statusCode: Int): View {
        return layoutInflater.inflate(R.layout.error_web_home, null)
    }

    override fun shouldObserveTitleChanges(): Boolean {
        return false
    }

    private fun sendCommunicationSettingsToServer() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("MobileAppInfos", Context.MODE_PRIVATE)
        val settings = sharedPreferences.all.map { (key, value) -> key to value.toString() }.toMap()

        // Create a JSON string from the settings
        val params = settings.entries.joinToString(",") { (key, value) -> "\"$key\":\"$value\"" }

        // Send the settings to the server via the JS Bridge
        val script = "window.bridge.registerMobileApp({$params})"
        session.webView.evaluateJavascript(script, null)
    }
}