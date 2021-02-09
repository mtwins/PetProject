package com.mheredia.petproject.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.Contact

class AddEditContactsFragment : Fragment() {

    private lateinit var contactsViewModel: ContactsViewModel

    companion object {
        fun newInstance() = AddEditContactsFragment()
    }

    interface AddEditContactInterface {
        fun goToContacts()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contactsViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_add_contacts, container, false)
        val name: EditText = root.findViewById(R.id.contact_name_edit_text)
        val email: EditText = root.findViewById(R.id.contact_email_edit_text)
        val phoneNumber: EditText = root.findViewById(R.id.contact_phone_number_edit_text)
        val notes: EditText = root.findViewById(R.id.contact_notes_edit_text)
        val submitButton: Button = root.findViewById(R.id.add_contact_button)
        val bundle = arguments
        name.setText(bundle?.getString("name").orEmpty())
        email.setText(bundle?.getString("email").orEmpty())
        phoneNumber.setText(bundle?.getString("phone").orEmpty())
        notes.setText(bundle?.getString("notes").orEmpty())

        submitButton.setOnClickListener {
            val contactId = bundle?.getString("contactId").toString()
            val contact = Contact(
                Firebase.auth.currentUser?.uid.toString(),
                name.text.toString(),
                phoneNumber.text.toString(),
                email.text.toString(),
                notes.text.toString(),
                contactId
            )
            val activity = this.requireActivity()
            if (contactId=="null")  contactsViewModel.addContactToDb(contact, activity)
            else contactsViewModel.updateContactToDb(contact, activity)

        }

        return root
    }


}