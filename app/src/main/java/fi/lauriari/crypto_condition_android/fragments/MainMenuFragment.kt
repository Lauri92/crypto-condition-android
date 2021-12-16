package fi.lauriari.crypto_condition_android.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import fi.lauriari.crypto_condition_android.databinding.FragmentMainMenuBinding
import fi.lauriari.crypto_condition_android.R
import fi.lauriari.crypto_condition_android.viewmodels.CryptoConditionViewModel
import java.util.*
import java.text.SimpleDateFormat
import java.util.Locale

class MainMenuFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentMainMenuBinding
    private var setStartdate = false
    private var setEnddate = false
    private var startDate: Long? = null
    private var endDate: Long? = null

    private val cryptoConditionViewModel: CryptoConditionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)

        initClickListeners()
        setObserver()

        return binding.root
    }

    private fun setObserver() {
        cryptoConditionViewModel.cryptoConditionInfo.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                val message = response.body()
                message ?: return@observe
                Log.d("cryptocondition", message.toString())
                val tradingVolume: Int =
                    (message.highestVolume.volume / 1_000_000_000).toInt()
                val tradingVolumeDate = convertMillisToDate(message.highestVolume.date)
                binding.tradingVolumeVolume.text =
                    getString(R.string.trading_volume_volume_string, tradingVolume.toString())
                binding.tradingVolumeDate.text =
                    getString(R.string.trading_volume_date_string, tradingVolumeDate)
            } else {
                Toast.makeText(activity, response.code(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initClickListeners() {
        binding.selectStartDateTv.setOnClickListener {
            setStartdate = true
            setEnddate = false
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

        binding.selectEndDateTv.setOnClickListener {
            setStartdate = false
            setEnddate = true
            DatePickerDialog(
                requireContext(),
                R.style.SpinnerDatePickerStyle,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH) + 1,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            ).also { dateSpinner ->
                dateSpinner.datePicker.maxDate = Calendar.getInstance().timeInMillis
                dateSpinner.setTitle("Select end date")
            }.show()
        }

        binding.getDataBtn.setOnClickListener {
            if (startDate != null && endDate != null) {
                Toast.makeText(requireContext(), "$startDate $endDate", Toast.LENGTH_SHORT).show()
                cryptoConditionViewModel.getCryptoCondition(startDate!!, endDate!!)
            }
        }
    }

    private fun convertMillisToDate(milliseconds: Long): String {
        val formatter = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds
        return formatter.format(calendar.time)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val formatter = SimpleDateFormat("dd MM yyyy", Locale.getDefault())

        if (setStartdate) {
            binding.selectStartDateTv.text = getString(
                R.string.set_tv_text,
                dayOfMonth.toString(),
                (month + 1).toString(),
                year.toString()
            )
            val calendarTime = formatter.parse("$dayOfMonth ${month + 1} $year")
            startDate = calendarTime?.time!! / 1000

        } else {
            binding.selectEndDateTv.text = getString(
                R.string.set_tv_text, dayOfMonth.toString(),
                (month + 1).toString(),
                year.toString()
            )
            val calendarTime = formatter.parse("$dayOfMonth ${month + 1} $year")
            endDate = calendarTime?.time!! / 1000
        }
    }
}