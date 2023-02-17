package com.sport.lotsofballs.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sport.lotsofballs.R
import com.sport.lotsofballs.databinding.FragmentSettingBinding
import com.sport.lotsofballs.service.SoundService
import com.sport.lotsofballs.util.*
import com.sport.lotsofballs.util.Constants.EN
import com.sport.lotsofballs.util.Constants.KEY_LANGUAGE
import com.sport.lotsofballs.util.Constants.KEY_SOUND
import com.sport.lotsofballs.util.Constants.KEY_VIBRATION
import com.sport.lotsofballs.util.Constants.RU
import dev.b3nedikt.app_locale.AppLocale
import dev.b3nedikt.reword.Reword
import java.util.*


class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val binding by viewBinding { FragmentSettingBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        soundIcon(getBoolean(KEY_SOUND))
        vibrationIcon(getBoolean(KEY_VIBRATION))
        languageIcon(getString(KEY_LANGUAGE) == EN)

        with(binding) {

            btnMenu.setOnClickListener {
                if (getBoolean(KEY_VIBRATION)) {
                    vibrator()
                    requireActivity().onBackPressed()
                } else {
                    requireActivity().onBackPressed()
                }
            }

            btnSound.setOnClickListener {
                if (getBoolean(KEY_VIBRATION)) {
                    vibrator()
                    if (!getBoolean(KEY_SOUND)) {
                        saveBoolean(KEY_SOUND, true)
                        soundIcon(true)
                        requireActivity().startService(Intent(requireContext(),
                            SoundService::class.java))
                    } else {
                        saveBoolean(KEY_SOUND, false)
                        soundIcon(false)
                        requireActivity().stopService(Intent(requireContext(),
                            SoundService::class.java))

                    }
                } else {
                    if (!getBoolean(KEY_SOUND)) {
                        saveBoolean(KEY_SOUND, true)
                        soundIcon(true)
                        requireActivity().startService(Intent(requireContext(),
                            SoundService::class.java))
                    } else {
                        saveBoolean(KEY_SOUND, false)
                        soundIcon(false)
                        requireActivity().stopService(Intent(requireContext(),
                            SoundService::class.java))

                    }
                }
            }

            btnVibration.setOnClickListener {
                if (getBoolean(KEY_VIBRATION)) {
                    vibrator()
                    saveBoolean(KEY_VIBRATION, false)
                    vibrationIcon(false)
                } else {
                    saveBoolean(KEY_VIBRATION, true)
                    vibrationIcon(true)
                }
            }

            btnEn.setOnClickListener {
                if (getBoolean(KEY_VIBRATION)) {
                    vibrator()
                    saveString(KEY_LANGUAGE, EN)
                    languageIcon(true)
                    AppLocale.desiredLocale = Locale.ENGLISH
                    Reword.reword(binding.root)
                } else {
                    saveString(KEY_LANGUAGE, EN)
                    languageIcon(true)
                    AppLocale.desiredLocale = Locale.ENGLISH
                    Reword.reword(binding.root)
                }
            }

            btnRu.setOnClickListener {
                if (getBoolean(KEY_VIBRATION)) {
                    vibrator()
                    saveString(KEY_LANGUAGE, RU)
                    languageIcon(false)
                    AppLocale.desiredLocale = Locale(RU)
                    Reword.reword(binding.root)
                } else {
                    saveString(KEY_LANGUAGE, RU)
                    languageIcon(false)
                    AppLocale.desiredLocale = Locale(RU)
                    Reword.reword(binding.root)
                }
            }
        }

    }

    private fun soundIcon(isOn: Boolean) {
        val icon = if (isOn) R.drawable.ic_open else R.drawable.ic_closed
        binding.btnSound.setImageResource(icon)
    }

    private fun vibrationIcon(isOn: Boolean) {
        val icon = if (isOn) R.drawable.ic_open else R.drawable.ic_closed
        binding.btnVibration.setImageResource(icon)
    }

    private fun languageIcon(language: Boolean) {
        val enIcon = if (language) R.drawable.en_selected else R.drawable.en_non_selected
        val ruIcon = if (language) R.drawable.ru_non_selected else R.drawable.ru_selected
        binding.apply {
            btnEn.setImageResource(enIcon)
            btnRu.setImageResource(ruIcon)
        }
    }

}