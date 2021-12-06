package fi.lauriari.crypto_condition_android.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import fi.lauriari.crypto_condition_android.databinding.FragmentMainMenuBinding
import fi.lauriari.crypto_condition_android.R
import fi.lauriari.crypto_condition_android.viewmodels.CryptoConditionViewModel
import java.util.*

class MainMenuFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentMainMenuBinding

    private val cryptoConditionViewModel: CryptoConditionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)

        binding.selectDateBtn.setOnClickListener {

            DatePickerDialog(
                requireContext(),
                R.style.SpinnerDatePickerStyle,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH) + 1,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            ).also { dateSpinner ->
                dateSpinner.datePicker.maxDate = Calendar.getInstance().timeInMillis
                dateSpinner.setTitle("Select start date")
            }.show()

        }

        return binding.root
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        Toast.makeText(requireContext(), "$dayOfMonth ${month + 1} $year", Toast.LENGTH_SHORT)
            .show()
    }
}