package com.mheredia.petproject.ui.reminders

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.data.model.Reminder
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReminderViewModel : ViewModel() {
    val reminderInfo = MutableLiveData<List<Reminder>>()
    lateinit var adapter: ReminderAdapter
    private val db = Firebase.firestore

    fun getReminders() {
        viewModelScope.launch {
            reminderInfo.value=getRemindersForCurrentUser(Firebase.auth.currentUser?.uid.toString())
        }
    }
        private suspend fun getRemindersForCurrentUser(userid: String): List<Reminder> =
            Firebase.firestore.collection("reminders")
                .whereEqualTo("userId", userid)
                .get()
                .await()
                .toObjects(Reminder::class.java)


    fun writeReminderToDb(reminder: Reminder) {

        if (reminder.reminderId.isBlank()) {
            addToDb(reminder)
        } else {
            editToDb(reminder)
        }
    }

    private fun editToDb(reminder: Reminder) {
        db.collection("reminders").document(reminder.reminderId).set(reminder)
            .addOnSuccessListener { documentReference ->
                adapter.editContact(reminder)
            }
            .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
    }

    private fun addToDb(reminder: Reminder) {
        var reminderRef = db.collection("reminders").document()
        reminder.reminderId = reminderRef.id
        reminderRef
            .set(reminder)
            .addOnSuccessListener { documentReference ->
                adapter.addReminder(reminder)
            }
            .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
    }

}