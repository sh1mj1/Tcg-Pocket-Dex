package tcg.pocket.dex

import android.app.Application
import timber.log.Timber

class TcgPocketDexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
