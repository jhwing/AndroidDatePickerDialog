package com.stark.datepicker

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import org.joda.time.DateTime

class TimePickerDialog : DialogFragment() {

    var mGravity = Gravity.BOTTOM
    var mWidth = WindowManager.LayoutParams.MATCH_PARENT
    var mHeight = WindowManager.LayoutParams.WRAP_CONTENT

    private lateinit var timePicker: TimePickerView
    private var mBeginDate: DateTime = DateTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DatePicker_Base_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.time_picker_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timePicker = view.findViewById(R.id.timePickerView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        timePicker.setCurrentTime(mBeginDate)

        val window = dialog.window
        window!!.setGravity(mGravity)
        val params = window.attributes
        params.height = mHeight
        params.width = mWidth
        window.attributes = params
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.decorView.setPadding(0, 0, 0, 0)
    }
}