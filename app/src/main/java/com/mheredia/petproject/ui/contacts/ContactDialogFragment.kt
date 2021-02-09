package com.mheredia.petproject.ui.contacts

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
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
    var  contactViewModel: ContactsViewModel
) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_add_contacts, null)

            val nameTextBox: TextView = view.findViewById(R.id.contact_name_edit_text)
            val emailTextBox: TextView = view.findViewById(R.id.contact_email_edit_text)
            val phoneTextBox: TextView = view.findViewById(R.id.contact_phone_number_edit_text)
            val notesTextBox: TextView = view.findViewById(R.id.contact_notes_edit_text)

            var title = "Add Contact"
            if (contact.contactId.isNotBlank()) {
                title = "Edit Contact"
                nameTextBox.setText(contact.name)
                emailTextBox.setText(contact.email)
                phoneTextBox.setText(contact.phone)
                notesTextBox.setText(contact.notes)
            }

            builder.setView(view)
                .setTitle(title)
                .setPositiveButton(
                    "Save"
                ) { dialog, id ->
                    contact.name=nameTextBox.text.toString()
                    contact.email=emailTextBox.text.toString()
                    contact.phone=phoneTextBox.text.toString()
                    contact.notes=notesTextBox.text.toString()
                    contact.userId=Firebase.auth.currentUser?.uid.toString()
                    contactViewModel.writeContactToDb(contact)
                }
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })


            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }



}