package com.sport.lotsofballs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.ViewPumpAppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sport.lotsofballs.databinding.ActivityMainBinding
import com.sport.lotsofballs.service.SoundService
import com.sport.lotsofballs.util.Constants.KEY_SOUND
import com.sport.lotsofballs.util.SharedPref
import dev.b3nedikt.app_locale.AppLocale

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController

        if (SharedPref(this).getBoolean(KEY_SOUND)) {
            startService(Intent(this, SoundService::class.java))
        }

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.gameFragment -> {
                    window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar_color2)
                }
                else -> {
                    window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar_color1)
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, SoundService::class.java))

    }

    private val appCompatDelegate: AppCompatDelegate by lazy {
        ViewPumpAppCompatDelegate(
            baseDelegate = super.getDelegate(),
            baseContext = this,
            wrapContext = AppLocale::wrap
        )
    }

    override fun getDelegate(): AppCompatDelegate {
        return appCompatDelegate
    }
}