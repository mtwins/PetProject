package com.mheredia.petproject.ui.reminders

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

}