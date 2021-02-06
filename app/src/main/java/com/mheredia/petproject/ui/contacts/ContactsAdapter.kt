package com.mheredia.petproject.ui.contacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.Contact

class ContactsAdapter(var result: List<Contact>, var context: Context) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

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
            val mIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_EMAIL, result[position].email)
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(context, mIntent, null)
        }
        holder.phoneImage.setOnClickListener {
            val mIntent = Intent(Intent.ACTION_DIAL).apply {
                setData(Uri.parse(result[position].phone))
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(context, mIntent,null )


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
                val context = itemView.context
                val intent = Intent(context, AddEditContactsFragment::class.java).apply {
                    putExtra("NAME", position)
//                    putExtra("EMAIL", itemKode.text)
//                    putExtra("PHONENUMBER", itemKategori.text)
//                    putExtra("NOTES", itemIsi.text)
//                    putExtra("USERID", itemIsi.text)
                }
//                var mCallback = getActivity(context) from ContactsFragment.ContactInterface.
//                mCallback.addContactButtonClicked()
            }
        }
    }




}