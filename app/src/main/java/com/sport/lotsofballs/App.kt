package com.sport.lotsofballs

import android.app.Application
import com.sport.lotsofballs.util.Constants.KEY_LANGUAGE
import com.sport.lotsofballs.util.Constants.RU
import com.sport.lotsofballs.util.SharedPref
import dev.b3nedikt.app_locale.AppLocale
import dev.b3nedikt.reword.RewordInterceptor
import dev.b3nedikt.viewpump.ViewPump
import java.util.*

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        ViewPump.init(RewordInterceptor)

        if (SharedPref(this).getString(KEY_LANGUAGE) == RU) {
            AppLocale.desiredLocale = Locale(RU)
        }
    }
}