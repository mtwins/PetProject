package com.mheredia.petproject.ui.reminders


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.Reminder

class ReminderAdapter(
    var result: MutableList<Reminder>,
    var openDialog: (reminder: Reminder) -> Unit

) :
    RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {

    fun addReminder(reminder: Reminder) {
        result.add(reminder)
        notifyDataSetChanged()
    }

    fun editContact(reminder: Reminder) {
        var position = 0
        for (item in result) {
            if (item.reminderId == reminder.reminderId) {
                result[position] = reminder
                break
            }
            position++
        }
        notifyDataSetChanged()
    }

    fun deleteReminder(position: Int) {
        result.removeAt(position)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.reminder_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = result[position].name
        holder.subTitle.text = "${result[position].date} ${result[position].time}"

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var subTitle: TextView

        init {

            title = itemView.findViewById(R.id.card_title)
            subTitle = itemView.findViewById(R.id.card_subtitle)

            itemView.setOnClickListener {
                var position: Int = getAdapterPosition()
                val reminder = Reminder(
                    result[position].name,
                    result[position].date,
                    result[position].time,
                    result[position].reminderId,
                    Firebase.auth.currentUser?.uid.toString()
                )
                openDialog(reminder)

            }
        }
    }


}


