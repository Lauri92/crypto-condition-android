package fi.lauriari.crypto_condition_android.fragments

import android.app.AlertDialog
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
import fi.lauriari.crypto_condition_android.models.CryptoCondition
import fi.lauriari.crypto_condition_android.viewmodels.CryptoConditionViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.text.SimpleDateFormat
import java.util.Locale

class MainMenuFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentMainMenuBinding
    private lateinit var datePickerDialog: DatePickerDialog
    private var setStartdate = false
    private var setEnddate = false
    private var startDateSeconds: Long? = null
    private var endDateSeconds: Long? = null
    private var savedStartDate: Triple<Int, Int, Int>? = null
    private var savedEndDate: Triple<Int, Int, Int>? = null

    private val cryptoConditionViewModel: CryptoConditionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)

        datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.SpinnerDatePickerStyle,
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH) + 1,
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
        ).also {
            it.datePicker.maxDate = Calendar.getInstance().timeInMillis
            it.datePicker.minDate = 1367107200000
        }

        initClickListeners()
        setObserver()

        return binding.root
    }

    private fun setObserver() {
        cryptoConditionViewModel.cryptoConditionInfo.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                binding.progressBar.visibility = View.GONE
                val message = response.body()
                message ?: return@observe
                Log.d("cryptocondition", message.toString())

                setTradingVolume(message)

                setBearishTrend(message)

                setTimeMachine(message)

            } else {
                Toast.makeText(requireContext(), response.code(), Toast.LENGTH_SHORT).show()
            }
        })

        cryptoConditionViewModel.requestFail.observe(viewLifecycleOwner, { errorValue ->
            if (errorValue) {
                displayErrorInFetchAlertDialog()
            }
        })

    }

    private fun setTimeMachine(message: CryptoCondition) {
        if (message.timeMachine.bestDayToBuy.date != null) {
            binding.hasBestDatesCl.visibility = View.VISIBLE
            binding.hasNoBestDatesCl.visibility = View.GONE
            binding.bestDatesIv.visibility = View.VISIBLE
            binding.bestDatesNotAvailableIv.visibility = View.GONE

            val buyDate = convertMillisToDate(message.timeMachine.bestDayToBuy.date)
            val buyPrice = message.timeMachine.bestDayToBuy.price?.toInt().toString()
            val sellDate = convertMillisToDate(message.timeMachine.bestDayToSell.date!!)
            val sellPrice = message.timeMachine.bestDayToSell.price?.toInt().toString()

            binding.buyDateTv.text = buyDate
            binding.buyPriceTv.text = getString(R.string.buysell_price, buyPrice)
            binding.sellDateTv.text = sellDate
            binding.sellPriceTv.text = getString(R.string.buysell_price, sellPrice)
        } else {
            binding.hasBestDatesCl.visibility = View.GONE
            binding.hasNoBestDatesCl.visibility = View.VISIBLE
            binding.bestDatesIv.visibility = View.GONE
            binding.bestDatesNotAvailableIv.visibility = View.VISIBLE
        }
    }

    private fun setBearishTrend(message: CryptoCondition) {
        val bearishTrendLength = message.bearishTrend.length.toString()
        val bearishTrendStart = convertMillisToDate(message.bearishTrend.startDate)
        val bearishTrendEnd = convertMillisToDate(message.bearishTrend.endDate)
        binding.bearishTrendLengthTv.text = bearishTrendLength
        binding.bearishTrendDaysText.visibility = View.VISIBLE
        binding.bearishTrendTextTv.visibility = View.GONE

        when {
            bearishTrendStart != bearishTrendEnd -> {
                binding.bearishTrendDate.text = getString(
                    R.string.bearish_trend_dates_string,
                    bearishTrendStart,
                    bearishTrendEnd
                )
            }
            bearishTrendStart == bearishTrendEnd && message.bearishTrend.length > 0 -> {
                binding.bearishTrendDate.text =
                    getString(R.string.bearish_trend_date, bearishTrendStart)
            }
            else -> {
                binding.bearishTrendDate.text = getString(R.string.no_bearish_trend_detected)
            }
        }


    }

    private fun setTradingVolume(message: CryptoCondition) {
        val tradingVolumeBillions: BigDecimal =
            BigDecimal(message.highestVolume.volume / 1_000_000_000).setScale(
                3,
                RoundingMode.HALF_EVEN
            )
        val tradingVolumeMillions: BigDecimal =
            BigDecimal(message.highestVolume.volume / 1_000_000).setScale(
                3, RoundingMode.HALF_EVEN
            )

        val tradingVolumeDate = convertMillisToDate(message.highestVolume.date)
        binding.redBoxPlaceholderTv.visibility = View.GONE
        binding.sellTv.visibility = View.VISIBLE
        binding.buyTv.visibility = View.VISIBLE

        when {
            message.highestVolume.volume == 0.0 -> {
                binding.tradingVolumeVolume.text = getString(R.string.volume_not_available)
                binding.tradingVolumeDate.text = ""
            }
            message.highestVolume.volume > 1_000_000_000 -> {
                binding.tradingVolumeVolume.text =
                    getString(
                        R.string.trading_volume_volume_string,
                        tradingVolumeBillions.toString()
                    )
                binding.tradingVolumeDate.text =
                    getString(R.string.trading_volume_date_string, tradingVolumeDate)
            }
            else -> {
                binding.tradingVolumeVolume.text = getString(
                    R.string.trading_volume_millions_volume_string,
                    tradingVolumeMillions.toString()
                )
                binding.tradingVolumeDate.text =
                    getString(R.string.trading_volume_date_string, tradingVolumeDate)
            }
        }
    }

    private fun displayErrorInFetchAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Server down")
        builder.setMessage("The server is probably sleeping at the moment, request has been sent to wake it up so please try again in ~3 minutes!????")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Ok") { _, _ ->
            cryptoConditionViewModel.requestFail.value = false
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        binding.progressBar.visibility = View.GONE
    }

    private fun initClickListeners() {
        binding.selectStartDateTv.setOnClickListener {
            setStartdate = true
            setEnddate = false

            if (savedStartDate != null) {
                datePickerDialog.updateDate(
                    savedStartDate!!.first,
                    savedStartDate!!.second,
                    savedStartDate!!.third
                )
            }

            datePickerDialog.setTitle("Select start date")
            datePickerDialog.show()
        }

        binding.selectEndDateTv.setOnClickListener {
            setStartdate = false
            setEnddate = true

            if (savedEndDate != null) {
                datePickerDialog.updateDate(
                    savedEndDate!!.first,
                    savedEndDate!!.second,
                    savedEndDate!!.third
                )
            }

            datePickerDialog.setTitle("Select end date")
            datePickerDialog.show()
        }

        binding.getDataBtn.setOnClickListener {
            if (startDateSeconds != null && endDateSeconds != null) {
                if (endDateSeconds!! > startDateSeconds!!) {
                    Log.d("dates", "start: $startDateSeconds end: $endDateSeconds")
                    cryptoConditionViewModel.getCryptoCondition(
                        startDateSeconds!!,
                        endDateSeconds!!
                    )
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    Toast.makeText(
                        requireContext(), getString(R.string.Date_picker_alert), Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(), getString(R.string.check_dates), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun convertMillisToDate(milliseconds: Long): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds
        return formatter.format(calendar.time)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val formatter = SimpleDateFormat("dd MM yyyy", Locale.getDefault()).also {
            it.timeZone = TimeZone.getTimeZone("UTC")
        }

        if (setStartdate) {
            binding.selectStartDateTv.text = getString(
                R.string.set_tv_text, dayOfMonth.toString(), (month + 1).toString(), year.toString()
            )
            val calendarTime = formatter.parse("$dayOfMonth ${month + 1} $year")
            startDateSeconds = calendarTime?.time!! / 1000

            savedStartDate = Triple(year, month, dayOfMonth)

        } else {
            binding.selectEndDateTv.text = getString(
                R.string.set_tv_text, dayOfMonth.toString(),
                (month + 1).toString(),
                year.toString()
            )
            val calendarTime = formatter.parse("$dayOfMonth ${month + 1} $year")
            endDateSeconds = calendarTime?.time!! / 1000

            savedEndDate = Triple(year, month, dayOfMonth)

        }
    }
}