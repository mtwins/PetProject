package com.mheredia.petproject.ui.contacts

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.Contact
import com.mheredia.petproject.data.model.Reminder
import java.text.SimpleDateFormat
import java.util.*


class ContactDialogFragment(
    var contact: Contact,
    var contactViewModel: ContactsViewModel
) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.FullScreenDialog)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_contacts, null)

            val nameTextBox: TextView = view.findViewById(R.id.contact_name_edit_text)
            val emailTextBox: TextView = view.findViewById(R.id.contact_email_edit_text)
            val phoneTextBox: TextView = view.findViewById(R.id.contact_phone_number_edit_text)
            val notesTextBox: TextView = view.findViewById(R.id.contact_notes_edit_text)
            val saveButton: Button = view.findViewById(R.id.save_contact)
            val cancelButton: ImageView = view.findViewById(R.id.cancel_dialog)

            var title = "Add Contact"
            if (contact.contactId.isNotBlank()) {
                title = "Edit Contact"
                nameTextBox.setText(contact.name)
                emailTextBox.setText(contact.email)
                phoneTextBox.setText(contact.phone)
                notesTextBox.setText(contact.notes)
            }
            saveButton.setOnClickListener {
                saveContact(nameTextBox, emailTextBox, phoneTextBox, notesTextBox)
                getDialog()?.cancel()
            }
            cancelButton.setOnClickListener {
                getDialog()?.cancel()
            }
            builder.setView(view)
                .setTitle(title)
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun saveContact(nameTextBox: TextView, emailTextBox: TextView, phoneTextBox: TextView, notesTextBox: TextView) {
        contact.name = nameTextBox.text.toString()
        contact.email = emailTextBox.text.toString()
        contact.phone = phoneTextBox.text.toString()
        contact.notes = notesTextBox.text.toString()
        contact.userId = Firebase.auth.currentUser?.uid.toString()
        contactViewModel.writeContactToDb(contact)
    }


}