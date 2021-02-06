package com.mheredia.petproject.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mheredia.petproject.R
import com.mheredia.petproject.ui.login.LoginActivity

class ContactsFragment : Fragment() {

    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var fab: FloatingActionButton

    interface ContactInterface {
        fun addContactButtonClicked()
    }

    companion object {
        fun newInstance() = ContactsFragment()
    }

    override fun onResume() {
        super.onResume()
        contactsViewModel.getContacts()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contactsViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_contacts, container, false)
        contactsViewModel.getContacts()

        fab = root.findViewById(R.id.add_contacts)
        fab.setOnClickListener { view ->
            addNewContactNavigation()
        }

        var contacts_list = root.findViewById<RecyclerView>(R.id.contacts_list)
        contacts_list.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ContactsAdapter(mutableListOf(),this.context)
        }

        contactsViewModel.contactInfo.observe(viewLifecycleOwner, Observer { result ->
            contacts_list.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = ContactsAdapter(result, this@ContactsFragment.requireContext())
            }

        })
        return root
    }

    private fun addNewContactNavigation() {
        var mCallback = activity as ContactInterface
        mCallback.addContactButtonClicked()
    }


}