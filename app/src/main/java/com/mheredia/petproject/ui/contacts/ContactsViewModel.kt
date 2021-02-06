package com.mheredia.petproject.ui.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.data.model.Contact
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ContactsViewModel : ViewModel() {

    val contactInfo = MutableLiveData<List<Contact>>()
    private val db = Firebase.firestore
    private val contactsRef = db.collection("contacts")

    fun getContacts(){
        viewModelScope.launch {
            contactInfo.value=getContactsForCurrentUser(Firebase.auth.currentUser?.uid?:"")
        }
    }

     suspend fun getContactsForCurrentUser(userid: String): List<Contact> =
        Firebase.firestore.collection("contacts")
            .whereEqualTo("userId", userid)
            .get()
            .await()
            .toObjects(Contact::class.java)


}