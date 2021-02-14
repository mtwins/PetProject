package com.mheredia.petproject.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.Contact
import com.mheredia.petproject.ui.utils.SwipeToDeleteCallback

class ContactsFragment : Fragment() {

    private lateinit var fab: FloatingActionButton

    interface ContactInterface {
        fun openEmail(email: String)
        fun openPhone(phone: String)
    }

    companion object {

        fun newInstance() = ContactsFragment()
    }

    lateinit var contactsViewModel: ContactsViewModel

    override fun onResume() {
        super.onResume()
        contactsViewModel.getContacts()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactsViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_list, container, false)
        contactsViewModel.getContacts()
        fab = root.findViewById(R.id.add_items)
        fab.setOnClickListener { view ->
            openContactDialog()
        }

        var contacts_list = root.findViewById<RecyclerView>(R.id.list_items)
        contactsViewModel.contactInfo.observe(viewLifecycleOwner, Observer { result ->
            contactsViewModel.contactsAdapter =
                ContactsAdapter(
                    result.toMutableList(),
                    ::openEmail,
                    ::openPhone,
                    ::openContactDialog
                )
            contacts_list.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = contactsViewModel.contactsAdapter
            }
            val itemTouchHelper =
                ItemTouchHelper(SwipeToDeleteCallback(contactsViewModel::deleteContactFromDb))
            itemTouchHelper.attachToRecyclerView(contacts_list)
        })
        return root
    }

    private fun openContactDialog(contact: Contact = Contact() ){
        activity?.supportFragmentManager?.let {
            ContactDialogFragment(contact, contactsViewModel).show(it, "")
        }
    }

    private fun openEmail(email:String ){
        contactsViewModel.openEmail(this.requireActivity(),email )
    }
    private fun openPhone(phone:String){
        contactsViewModel.openPhone(this.requireActivity(),phone )
    }



}