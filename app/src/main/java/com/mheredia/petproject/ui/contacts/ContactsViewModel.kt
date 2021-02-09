package com.mheredia.petproject.ui.contacts

import android.util.Log
import androidx.fragment.app.FragmentActivity
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
    lateinit var contactsAdapter: ContactsAdapter
    val contactInfo = MutableLiveData<List<Contact>>()
    private val db = Firebase.firestore

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

    fun addContactToDb( contact: Contact) {
        var contactsRef= db.collection("contacts").document()
        contact.contactId=contactsRef.id
        contactsRef
            .set(contact)
            .addOnSuccessListener { documentReference ->
                contactsAdapter.addContact(contact)
                Log.d("ContactDone", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
    }
    fun updateContactToDb(contact: Contact) {
        var contactsRef= db.collection("contacts")
        contactsRef
            .document(contact.contactId)
            .set(contact)
            .addOnSuccessListener { documentReference ->
                contactsAdapter.editContact(contact)
                Log.d("ContactDone", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
    }


    fun openEmail(activity: FragmentActivity, email: String) {
        var mCallback = activity as ContactsFragment.ContactInterface
        mCallback.openEmail(email)
    }

    fun openPhone(activity: FragmentActivity, phone:String) {
        var mCallback = activity as ContactsFragment.ContactInterface
        mCallback.openPhone(phone)
    }

    fun writeContactToDb(contact: Contact) {
        if (contact.contactId.isBlank()) {
            addContactToDb(contact)
        } else {
            updateContactToDb(contact)
        }
    }
    fun deleteContactFromDb(position: Int){
        var contact= contactsAdapter.result[position]
        Firebase.firestore.collection("contacts")
            .document(contact.contactId)
            .delete()
            .addOnSuccessListener {
                contactsAdapter.deleteContact(position)
            }
    }
}