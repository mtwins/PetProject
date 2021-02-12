package com.mheredia.petproject.ui.reminders

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.data.model.Reminder
import com.mheredia.petproject.ui.contacts.ContactsFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class ReminderViewModel : ViewModel() {
    val reminderInfo = MutableLiveData<List<Reminder>>()
    lateinit var reminderAdapter: ReminderAdapter
    private val db = Firebase.firestore

    fun getReminders() {
        viewModelScope.launch {
            reminderInfo.value =
                getRemindersForCurrentUser(Firebase.auth.currentUser?.uid.toString())
        }
    }

    private suspend fun getRemindersForCurrentUser(userid: String): List<Reminder> =
        Firebase.firestore.collection("reminders")
            .whereEqualTo("userId", userid)
            .get()
            .await()
            .toObjects(Reminder::class.java)

    fun editToDb(reminder: Reminder) {
        db.collection("reminders").document(reminder.reminderId).set(reminder)
            .addOnSuccessListener { documentReference ->
                reminderAdapter.editContact(reminder)
            }
            .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
    }

    fun addToDb(
        reminder: Reminder,
        reminderDoc: DocumentReference
    ) {
        reminderDoc
            .set(reminder)
            .addOnSuccessListener { documentReference ->
                reminderAdapter.addReminder(reminder)

            }
            .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
    }

    fun getReminderDoc(): DocumentReference = db.collection("reminders").document()

    fun sendReminder(activity: FragmentActivity, message: String, id: String, calendar: Calendar) {
        var mCallback = activity as ReminderFragment.ReminderInterface
        mCallback.sendReminder(message, id, calendar)
    }

    fun writeReminderToDb(reminder: Reminder, calender: Calendar, activity: FragmentActivity) =
        if (reminder.reminderId.isBlank()) {
            var reminderDoc = getReminderDoc()
            reminder.reminderId = reminderDoc.id
            sendReminder(activity, reminder.name, reminder.reminderId, calender)
            addToDb(reminder, reminderDoc)
        } else {
            sendReminder(activity, reminder.name, reminder.reminderId, calender)
            editToDb(reminder)
        }

    fun deleteToDb(position: Int) {
        var reminder = reminderAdapter.result[position]
        Firebase.firestore.collection("reminders")
            .document(reminder.reminderId)
            .delete()
            .addOnSuccessListener {
                reminderAdapter.deleteReminder(position)
            }
    }

}