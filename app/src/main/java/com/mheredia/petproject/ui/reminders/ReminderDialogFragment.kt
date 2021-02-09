package com.mheredia.petproject.ui.reminders

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.Reminder
import java.text.SimpleDateFormat
import java.util.*


class ReminderDialogFragment(var reminder: Reminder) :
    DialogFragment() {
    private val db = Firebase.firestore
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_reminder, null)

            val nameTextBox: TextView = view.findViewById(R.id.reminder_name)
            val dateTextBox: TextView = view.findViewById(R.id.reminder_date)
            val timeTextBox: TextView = view.findViewById(R.id.reminder_time)

            var title = "Add Reminder"
            if (reminder.reminderId.isNotBlank()) {
                title = "Edit Reminder"
                nameTextBox.setText(reminder.name)
                dateTextBox.setText(reminder.date)
                timeTextBox.setText(reminder.time)
            }
            setDateTextOnClickListener(dateTextBox)
            setTimeTextOnClickListener(timeTextBox)

            builder.setView(view)
                .setTitle(title)
                .setPositiveButton(
                    "Save"
                ) { dialog, id ->
                    val reminder = Reminder(
                        nameTextBox.text.toString(),
                        dateTextBox.text.toString(),
                        timeTextBox.text.toString(),
                        userId = Firebase.auth.currentUser?.uid.toString()
                    )
                    writeReminderToDb(reminder)
                }
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })


            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setDateTextOnClickListener(dateTextBox: TextView) {
        dateTextBox.setOnClickListener {
            var datePickerDialog = DatePickerDialog(this.requireContext())

            datePickerDialog.setOnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                dateTextBox.setText("$month/$day/$year")
            }
            datePickerDialog.show()
        }
    }

    private fun setTimeTextOnClickListener(timeTextBox: TextView) {
        timeTextBox.setOnClickListener {
            val timeSetListener =
                TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    timeTextBox.setText(SimpleDateFormat("HH:mm a").format(cal.time).toString())
                }
            TimePickerDialog(this.requireContext(), timeSetListener, 0, 0, false).show()
        }
    }

    private fun writeReminderToDb(reminder: Reminder) {

        if (reminder.reminderId.isBlank()) {
            var reminderRef = db.collection("reminders").document()
            reminder.reminderId = reminderRef.id
            reminderRef
                .set(reminder)
                .addOnSuccessListener { documentReference ->
                    Log.d("ContactDone", "DocumentSnapshot successfully written!")
                    
                }
                .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
        } else {
            db.collection("reminders").document(reminder.reminderId).set(reminder)
                .addOnSuccessListener { documentReference ->
                    Log.d("ContactDone", "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
        }
    }


}