package fi.lauriari.crypto_condition_android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import fi.lauriari.crypto_condition_android.databinding.FragmentMainMenuBinding
import fi.lauriari.crypto_condition_android.R

class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)

        binding.navigateToBearishTrendBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_bearishTrendFragment)
        }
        binding.navigateToTradingVolumeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_tradingVolumeFragment)
        }
        binding.navigateToTimeMachineBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_timeMachineFragment)
        }

        return binding.root
    }
}