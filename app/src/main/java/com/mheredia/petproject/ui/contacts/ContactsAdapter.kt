package com.mheredia.petproject.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.Contact

class ContactsAdapter(
    var result: MutableList<Contact>,
    var openEmail: (email: String) -> Unit,
    var openPhone: (phone: String) -> Unit,
    var openDialog: (contact: Contact) -> Unit,
    var noItemsMessage: (list: List<Contact>, root: View) -> Unit,
    var root: View
) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    fun addContact(contact: Contact) {
        result.add(contact)
        noItemsMessage(result, root)
        notifyDataSetChanged()
    }

    fun editContact(contact: Contact) {
        var position = 0
        for (item in result) {
            if (item.contactId == contact.contactId) {
                result[position] = contact
                break
            }
            position++
        }
        notifyDataSetChanged()
    }

    fun deleteContact(index: Int) {
        result.removeAt(index)
        noItemsMessage(result, root)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = result[position].name
        holder.subTitle.text = result[position].notes

        holder.emailImage.setOnClickListener {
            openEmail(result[position].email)
        }
        holder.phoneImage.setOnClickListener {
            openPhone(result[position].phone)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var subTitle: TextView
        var emailImage: ImageView
        var phoneImage: ImageView

        init {

            title = itemView.findViewById(R.id.card_title)
            subTitle = itemView.findViewById(R.id.card_subtitle)
            emailImage = itemView.findViewById(R.id.email_image)
            phoneImage = itemView.findViewById(R.id.phone_image)

            itemView.setOnClickListener {
                var position: Int = getAdapterPosition()
                with(result[position]) {
                    val contact = Contact(
                        Firebase.auth.currentUser?.uid.toString(),
                        name,
                        phone,
                        email,
                        notes,
                        contactId
                    )
                    openDialog(contact)
                }
            }

        }
    }


}



