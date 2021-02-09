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

class ContactsFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    interface ContactInterface {
        fun addContactButtonClicked()
        fun openEmail(email:String)
        fun openPhone(phone:String)
        fun editContactButtonClicked(contact:Contact)
    }
    companion object {

        fun newInstance() = ContactsFragment()
        lateinit var contactsViewModel: ContactsViewModel
        lateinit var contactsAdapter: ContactsAdapter
    }

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

        val root = inflater.inflate(R.layout.fragment_contacts, container, false)
        contactsViewModel.getContacts()
        fab = root.findViewById(R.id.add_contacts)
        fab.setOnClickListener { view ->
            contactsViewModel.addNewContactNavigation(this.requireActivity())
        }

        var contacts_list = root.findViewById<RecyclerView>(R.id.contacts_list)
        contacts_list.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ContactsAdapter(mutableListOf(), this.context, this@ContactsFragment.requireActivity())
        }


        contactsViewModel.contactInfo.observe(viewLifecycleOwner, Observer { result ->
            contactsAdapter =
                ContactsAdapter(result.toMutableList(), this@ContactsFragment.requireContext(), this.requireActivity())
            contacts_list.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = contactsAdapter
            }
            val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallbackContact(contactsAdapter))
            itemTouchHelper.attachToRecyclerView(contacts_list)
        })
        return root
    }




}