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

    fun addContactToDb( contact: Contact, activity: FragmentActivity) {
        var mCallback = activity as AddEditContactsFragment.AddEditContactInterface
        var contactsRef= db.collection("contacts").document()
        contact.contactId=contactsRef.id
        contactsRef
            .set(contact)
            .addOnSuccessListener { documentReference ->
                mCallback.goToContacts()
                ContactsFragment.contactsAdapter.addContact(contact)
                Log.d("ContactDone", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
    }
    fun updateContactToDb(contact: Contact, activity: FragmentActivity) {
        var mCallback = activity as AddEditContactsFragment.AddEditContactInterface
        var contactsRef= db.collection("contacts")
        contactsRef
            .document(contact.contactId)
            .set(contact)
            .addOnSuccessListener { documentReference ->
                mCallback.goToContacts()
                ContactsFragment.contactsAdapter.editContact(contact)
                Log.d("ContactDone", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
    }
    fun goToEditContactFragment(activity: FragmentActivity, contact: Contact) {
        var mCallback = activity as ContactsFragment.ContactInterface
        mCallback.editContactButtonClicked(contact)
    }

    fun addNewContactNavigation(activity: FragmentActivity) {
        var mCallback = activity as ContactsFragment.ContactInterface
        mCallback.addContactButtonClicked()
    }

    fun openEmail(activity: FragmentActivity, email: String) {
        var mCallback = activity as ContactsFragment.ContactInterface
        mCallback.openEmail(email)
    }

    fun openPhone(activity: FragmentActivity, phone:String) {
        var mCallback = activity as ContactsFragment.ContactInterface
        mCallback.openPhone(phone)
    }
}