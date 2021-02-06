package com.mheredia.petproject.ui.contacts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.R
import com.mheredia.petproject.ui.registration.RegistrationFragment

class AddEditContactsFragment : Fragment() {

    private lateinit var contactsViewModel: ContactsViewModel
    private val db = Firebase.firestore
    companion object {
        fun newInstance() = AddEditContactsFragment()
    }

    interface AddEditContactInterface {
        fun goToContacts()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contactsViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_add_contacts, container, false)
        val name: EditText = root.findViewById(R.id.contact_name_edit_text)
        val email: EditText = root.findViewById(R.id.contact_email_edit_text)
        val phoneNumber: EditText = root.findViewById(R.id.contact_phone_number_edit_text)
        val notes: EditText = root.findViewById(R.id.contact_notes_edit_text)
        val submitButton: Button = root.findViewById(R.id.add_contact_button)

        submitButton.setOnClickListener {
            val contact = hashMapOf(
                "name" to name.text.toString(),
                "email" to email.text.toString(),
                "phoneNumber" to phoneNumber.text.toString(),
                "notes" to notes.text.toString(),
                "userId" to Firebase.auth.currentUser?.uid.toString()
            )
            addContactToDb(contact)

        }

        return root
    }


    private fun addContactToDb(contact: HashMap<String, String>) {
        var mCallback = activity as AddEditContactInterface
        db.collection("contacts")
            .add(contact)
            .addOnSuccessListener {
                mCallback.goToContacts()
                Log.d("ContactDone", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
    }

}