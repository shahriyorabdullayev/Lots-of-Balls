package com.sport.lotsofballs.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sport.lotsofballs.R
import com.sport.lotsofballs.databinding.FragmentMenuBinding
import com.sport.lotsofballs.util.OutlineSpan
import com.sport.lotsofballs.util.viewBinding


class MenuFragment : Fragment(R.layout.fragment_menu) {

    private val binding by viewBinding { FragmentMenuBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            val outlineSpan = OutlineSpan(
                strokeColor = Color.BLACK,
                strokeWidth = 6f
            )
            val startText = resources.getString(R.string.start) + " "
            val recordText = resources.getString(R.string.record) + " "
            val settingText = resources.getString(R.string.setting) + " "
            val spannableStartText = SpannableString(startText)
            val spannableRecordText = SpannableString(recordText)
            val spannableSettingText = SpannableString(settingText)
            spannableStartText.setSpan(outlineSpan,
                0,
                startText.length - 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableRecordText.setSpan(outlineSpan,
                0,
                recordText.length - 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableSettingText.setSpan(outlineSpan,
                0,
                settingText.length - 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            btnStart.text = spannableStartText
            btnRecord.text = spannableRecordText
            btnSetting.text = spannableSettingText

            btnStart.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_gameFragment)
            }
            btnRecord.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_recordFragment)
            }
            btnSetting.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_settingFragment)
            }
        }
    }

}