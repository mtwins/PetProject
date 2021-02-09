package com.mheredia.petproject.ui.reminders

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.mheredia.petproject.R
import java.text.SimpleDateFormat
import java.util.*


class ReminderDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout


            val view = inflater.inflate(R.layout.dialog_reminder, null)
            val time: EditText = view.findViewById(R.id.reminder_time)
            val date: EditText = view.findViewById(R.id.reminder_date)
            val datePickerButton: Button = view.findViewById(R.id.reminder_date_picker)
            val timePickerButton: Button = view.findViewById(R.id.reminder_time_picker)

            datePickerButton.setOnClickListener {
                var datePickerDialog = DatePickerDialog(this.requireContext())

                datePickerDialog.setOnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                    date.setText( "$month/$day/$year")
                }
                datePickerDialog.show()
            }
            timePickerButton.setOnClickListener {
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        time.setText( SimpleDateFormat("HH:mm a").format(cal.time).toString())
                    }
                    TimePickerDialog(this.requireContext(), timeSetListener, 0, 0, false).show()
            }

            builder.setView(view)
                .setTitle("Add Reminder")
                // Add action buttons
                .setPositiveButton("Save",
                    DialogInterface.OnClickListener { dialog, id ->

                    })

                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })


            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun timePicker() {

    }
}