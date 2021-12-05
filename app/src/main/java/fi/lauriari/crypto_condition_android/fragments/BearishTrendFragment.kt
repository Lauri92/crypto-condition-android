package fi.lauriari.crypto_condition_android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import fi.lauriari.crypto_condition_android.R
import fi.lauriari.crypto_condition_android.databinding.FragmentBearishTrendBinding
import fi.lauriari.crypto_condition_android.viewmodels.CryptoConditionViewModel


class BearishTrendFragment : Fragment() {

    private lateinit var binding: FragmentBearishTrendBinding
    private val cryptoConditionViewModel: CryptoConditionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bearish_trend, container, false)
        return binding.root
    }
}