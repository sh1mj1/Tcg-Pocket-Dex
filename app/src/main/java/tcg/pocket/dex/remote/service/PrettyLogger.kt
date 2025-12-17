package tcg.pocket.dex.remote.service

import io.ktor.client.plugins.logging.Logger
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber

object PrettyLogger : Logger {
    override fun log(message: String) {
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                val prettyJson =
                    when {
                        message.startsWith("{") -> JSONObject(message).toString(4)
                        message.startsWith("[") -> JSONArray(message).toString(4)
                        else -> message
                    }
                Timber.d(prettyJson)
            } catch (e: Exception) {
                Timber.d(message) // Log as is if formatting fails
            }
        } else {
            Timber.d(message) // Regular log
        }
    }
}
