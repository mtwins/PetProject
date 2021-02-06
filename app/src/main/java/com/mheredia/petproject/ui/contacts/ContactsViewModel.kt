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

    fun addContactToDb(contact: HashMap<String, String>, activity: FragmentActivity) {
        var mCallback = activity as AddEditContactsFragment.AddEditContactInterface
        db.collection("contacts")
            .add(contact)
            .addOnSuccessListener {
                mCallback.goToContacts()
                ContactsFragment.contactsAdapter.addContact(Contact(
                    contact.get("userId").toString(),
                    contact.get("name").toString(),
                    contact.get("phone").toString(),
                    contact.get("email").toString(),
                    contact.get("notes").toString()
                ))
                Log.d("ContactDone", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
    }


}