package com.stark.datepicker

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import org.joda.time.DateTime

class TimePickerDialog : DialogFragment() {

    private lateinit var timePicker: TimePickerView
    private var mBeginDate: DateTime = DateTime()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = View.inflate(context, R.layout.time_picker_dialog, null)
        timePicker = dialogView.findViewById(R.id.timePickerView)
        return AlertDialog.Builder(context)
                .setView(dialogView)
                .create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        timePicker.setCurrentTime(mBeginDate)
    }
}