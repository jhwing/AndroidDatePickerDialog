package com.stark.datepicker

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import cn.carbswang.android.numberpickerview.library.NumberPickerView
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Months
import org.joda.time.Years

/**
 * Created by jihongwen on 2017/12/4.
 */
class TimePickerView : LinearLayout, NumberPickerView.OnValueChangeListener {

    private var minDate = DateTime()
    private var maxDate = DateTime()
    private val selectDay = DateTime()

    private var mOnDateChangeListener: OnDateChangeListener? = null
    private var mOnHourChangeListener: OnHourChangeListener? = null
    private var mOnMinuteChangeListener: OnMinuteChangeListener? = null

    lateinit var pickerYear: NumberPickerView
    lateinit var pickerMonth: NumberPickerView
    lateinit var pickerDay: NumberPickerView

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.time_picker_view, this)
        pickerYear = findViewById(R.id.pickerYear)
        pickerMonth = findViewById(R.id.pickerMonth)
        pickerDay = findViewById(R.id.pickerDay)
        initDate()
    }

    private fun initDate() {
        // 从今天开始
        minDate = minDate.withTime(0, 0, 0, 0)
        Log.d("jihongwen", "initDate minDate: " + minDate.toString("yyyy/MM/dd HH:mm:ss"))
        maxDate = maxDate.withDate(minDate.plusYears(2).year, minDate.monthOfYear, minDate.dayOfMonth)
        maxDate = maxDate.withTime(0, 0, 0, 0)

        val maxYear = Years.yearsBetween(minDate, maxDate).years
        // 年最大，年最小
        pickerYear.minValue = 0
        pickerYear.maxValue = maxYear
        pickerYear.setOnGetDisplayValueListener { index ->
            val date = getDateYear(index)
            date.toString("YYYY年")
        }

        pickerYear.setOnValueChangedListener(this)

        val maxMonth = Months.monthsBetween(minDate, maxDate).months
        pickerMonth.minValue = 0
        pickerMonth.maxValue = maxMonth
        pickerMonth.setOnGetDisplayValueListener { index ->
            val date = getDateMonth(index)
            date.toString("MM月")
        }

        pickerMonth.setOnValueChangedListener(this)

        val maxDay = Days.daysBetween(minDate, maxDate).days
        Log.d("jihongwen", "initDate maxDay: $maxDay")
        pickerDay.minValue = 0
        pickerDay.maxValue = maxDay
        pickerDay.setOnGetDisplayValueListener { index ->
            val date = getDateDay(index)
            date.toString("dd日 E")
        }

        pickerDay.setOnValueChangedListener(this)
        setCurrentTime(selectDay)
    }

    fun setCurrentTime(dateTime: DateTime) {
        pickerYear.value = getPickerDateYearValue(dateTime)
        pickerMonth.value = getPickerDateMonthValue(dateTime)
        pickerDay.value = getPickerDateValue(dateTime)
    }

    private fun getPickerDateValue(dateTime: DateTime): Int {
        return Days.daysBetween(minDate, dateTime).days
    }

    private fun getPickerDateYearValue(dateTime: DateTime): Int {
        return Years.yearsBetween(minDate.withDate(minDate.year, 1, 1), dateTime).years
    }

    private fun getPickerDateMonthValue(dateTime: DateTime): Int {
        return Months.monthsBetween(minDate.withDate(minDate.year, minDate.monthOfYear, 1), dateTime).months
    }

    private fun getDateDay(index: Int): DateTime {
        return minDate.plusDays(index)
    }

    private fun getDateYear(index: Int): DateTime {
        return minDate.plusYears(index)
    }

    private fun getDateMonth(index: Int): DateTime {
        return minDate.plusMonths(index)
    }

    fun setOnDateChangeListener(onDateChangeListener: OnDateChangeListener) {
        this.mOnDateChangeListener = onDateChangeListener
    }

    fun setOnHourChangeListener(onHourChangeListener: OnHourChangeListener) {
        this.mOnHourChangeListener = onHourChangeListener
    }

    fun setOnMinuteChangeListener(onMinuteChangeListener: OnMinuteChangeListener) {
        this.mOnMinuteChangeListener = onMinuteChangeListener
    }

    override fun onValueChange(picker: NumberPickerView?, oldVal: Int, newVal: Int) {
        when {
            picker === pickerYear -> {
                Log.d("jihongwen", "onValueChange hour newVal ")
                val dateTimeYear = getDateYear(newVal)
                val dateTimeMonth = getDateMonth(pickerMonth.value)
                val dateTimeDay = getDateDay(pickerDay.value)
                val dateTime = dateTimeDay.withDate(dateTimeYear.year, dateTimeMonth.monthOfYear, dateTimeDay.dayOfMonth)
                updateMonth(dateTime)
                updateDay(dateTime)
                mOnDateChangeListener?.onDateChange(dateTime)
            }
            picker === pickerMonth -> {
                // updateYear
                var dateTimeYear = getDateYear(pickerYear.value)
                val oldDateTimeMonth = getDateMonth(oldVal)
                val dateTimeMonth = getDateMonth(newVal)
                var dateTimeDay = getDateDay(pickerDay.value)

                if (oldDateTimeMonth.year < dateTimeMonth.year) {
                    dateTimeYear = dateTimeYear.plusYears(1)
                } else if (oldDateTimeMonth.year > dateTimeMonth.year) {
                    dateTimeYear = dateTimeYear.minusYears(1)
                }

                if (oldDateTimeMonth.monthOfYear < dateTimeMonth.monthOfYear) {
                    dateTimeDay = dateTimeDay.plusMonths(1)
                    Log.d("jihongwen", "onValueChange day newVal " + dateTimeDay.toString("yyyy-MM-dd"))
                } else if (oldDateTimeMonth.monthOfYear > dateTimeMonth.monthOfYear) {
                    dateTimeDay = dateTimeDay.minusMonths(1)
                    Log.d("jihongwen", "onValueChange day newVal " + dateTimeDay.toString("yyyy-MM-dd"))
                }

                val dateTime = dateTimeDay.withDate(dateTimeYear.year, dateTimeMonth.monthOfYear, dateTimeDay.dayOfMonth)
                updateYear(dateTime)
                updateDay(dateTime)
                Log.d("jihongwen", "onValueChange day newVal " + dateTime.toString("yyyy-MM-dd"))
                mOnDateChangeListener?.onDateChange(dateTime)
            }
            picker === pickerDay -> {
                // updateYear, updateMonth
                val dateTimeYear = getDateYear(pickerYear.value)
                val dateTimeMonth = getDateMonth(pickerMonth.value)
                val dateTimeDay = getDateDay(newVal)
                val dateTime = dateTimeDay.withDate(dateTimeDay.year, dateTimeDay.monthOfYear, dateTimeDay.dayOfMonth)
                updateYear(dateTime)
                updateMonth(dateTime)
                Log.d("jihongwen", "onValueChange day newVal " + dateTime.toString("yyyy-MM-dd"))
                mOnDateChangeListener?.onDateChange(dateTime)

            }
        }
    }

    private fun updateDay(dateTime: DateTime) {
        var index = getPickerDateValue(dateTime)
        Log.d("jihongwen", "updateMonth index $index")
        if (index < pickerDay.minValue) {
            index = pickerDay.minValue
        } else if (index > pickerDay.maxValue) {
            index = pickerDay.maxValue
        }
        pickerDay.value = index
    }

    private fun updateMonth(dateTime: DateTime) {
        var index = getPickerDateMonthValue(dateTime)
        Log.d("jihongwen", "updateMonth index $index")
        if (index < pickerMonth.minValue) {
            index = pickerMonth.minValue
        } else if (index > pickerMonth.maxValue) {
            index = pickerMonth.maxValue
        }
        pickerMonth.value = index
    }

    private fun updateYear(dateTime: DateTime) {
        var index = getPickerDateYearValue(dateTime)
        Log.d("jihongwen", "updateMonth index $index")
        if (index < pickerYear.minValue) {
            index = pickerYear.minValue
        } else if (index > pickerYear.maxValue) {
            index = pickerYear.maxValue
        }
        pickerYear.value = index
    }

    interface OnDateChangeListener {
        fun onDateChange(dateTime: DateTime)
    }

    interface OnHourChangeListener {
        fun onHourChange(hour: Int)
    }

    interface OnMinuteChangeListener {
        fun onMinuteChange(minute: Int)
    }

}