package tcg.pocket.dex.remote.service

import android.util.Log
import io.ktor.client.plugins.logging.Logger
import org.json.JSONArray
import org.json.JSONObject

object PrettyLogger : Logger {
    private const val TAG = "KtorLogger"

    override fun log(message: String) {
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                val prettyJson =
                    when {
                        message.startsWith("{") -> JSONObject(message).toString(4)
                        message.startsWith("[") -> JSONArray(message).toString(4)
                        else -> message
                    }
                Log.d(TAG, prettyJson)
            } catch (e: Exception) {
                Log.d(TAG, message) // Log as is if formatting fails
            }
        } else {
            Log.d(TAG, message) // Regular log
        }
    }
}
