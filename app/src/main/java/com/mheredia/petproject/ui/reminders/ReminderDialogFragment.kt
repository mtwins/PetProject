package com.mheredia.petproject.ui.reminders

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.Reminder
import java.text.SimpleDateFormat
import java.util.*


class ReminderDialogFragment(
    var reminder: Reminder,
   var reminderViewModel: ReminderViewModel
) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it, R.style.FullScreenDialog)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_reminder, null)

            val nameTextBox: TextView = view.findViewById(R.id.reminder_name)
            val dateTextBox: TextView = view.findViewById(R.id.reminder_date)
            val timeTextBox: TextView = view.findViewById(R.id.reminder_time)
            val saveButton: Button = view.findViewById(R.id.save_reminder)
            val cancelButton: ImageView = view.findViewById(R.id.cancel_dialog)
            var calender= Calendar.getInstance()

            var title = "Add Reminder"
            if (reminder.reminderId.isNotBlank()) {
                title = "Edit Reminder"
                nameTextBox.setText(reminder.name)
                dateTextBox.setText(reminder.date)
                timeTextBox.setText(reminder.time)
            }
            setDateTextOnClickListener(dateTextBox, calender)
            setTimeTextOnClickListener(timeTextBox, calender)
            saveButton.setOnClickListener {
                reminder.name=nameTextBox.text.toString()
                reminder.date= dateTextBox.text.toString()
                reminder.time=timeTextBox.text.toString()
                reminder.userId=Firebase.auth.currentUser?.uid.toString()
                reminderViewModel.writeReminderToDb(reminder, calender, this.requireActivity())
                dialog?.cancel()
            }
            cancelButton.setOnClickListener {
                dialog?.cancel()
            }
            builder.setView(view)
                .setTitle(title)

            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun setDateTextOnClickListener(dateTextBox: TextView, calender: Calendar) {
        dateTextBox.setOnClickListener {
            var datePickerDialog = DatePickerDialog(this.requireContext())
            datePickerDialog.setOnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                calender.set(Calendar.MONTH, month)
                calender.set(Calendar.DAY_OF_MONTH, day)
                calender.set(Calendar.YEAR, year)
                dateTextBox.setText("${month+1}/$day/$year")
            }
            datePickerDialog.show()
        }
    }

    private fun setTimeTextOnClickListener(timeTextBox: TextView, calender: Calendar) {
        timeTextBox.setOnClickListener {
            val timeSetListener =
                TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    calender.set(Calendar.HOUR_OF_DAY, hour)
                    calender.set(Calendar.MINUTE, minute)
                    calender.set(Calendar.SECOND, 0)
                    timeTextBox.setText(SimpleDateFormat("hh:mm a").format(calender.time).toString())
                }
            TimePickerDialog(this.requireContext(), timeSetListener, 0, 0, false).show()
        }
    }




}