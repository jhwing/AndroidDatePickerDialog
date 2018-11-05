package com.stark.datepicker.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.stark.datepicker.TimePickerDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun testDialog(v: View) {
        val dialog = TimePickerDialog()
        dialog.show(supportFragmentManager, "time_dialog")
    }
}
