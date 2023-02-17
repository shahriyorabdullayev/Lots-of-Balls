package com.sport.lotsofballs.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sport.lotsofballs.R
import com.sport.lotsofballs.databinding.FragmentRecordBinding
import com.sport.lotsofballs.util.Constants.KEY_VIBRATION
import com.sport.lotsofballs.util.Constants.RECORD_ONE
import com.sport.lotsofballs.util.Constants.RECORD_THREE
import com.sport.lotsofballs.util.Constants.RECORD_TWO
import com.sport.lotsofballs.util.getBoolean
import com.sport.lotsofballs.util.getScoreString
import com.sport.lotsofballs.util.vibrator
import com.sport.lotsofballs.util.viewBinding


class RecordFragment : Fragment(R.layout.fragment_record) {

    private val binding by viewBinding { FragmentRecordBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnBackRecord.setOnClickListener {
                if (getBoolean(KEY_VIBRATION)) {
                    vibrator()
                    requireActivity().onBackPressed()
                } else {
                    requireActivity().onBackPressed()
                }
            }

            tvRecord1.text = getScoreString(RECORD_ONE).toString()
            tvRecord2.text = getScoreString(RECORD_TWO).toString()
            tvRecord3.text = getScoreString(RECORD_THREE).toString()
        }
    }

}