package com.mheredia.petproject.ui.reminders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.Reminder
import com.mheredia.petproject.ui.utils.SwipeToDeleteCallback
import java.util.*


class ReminderFragment : Fragment() {

    companion object {
        fun newInstance() = ReminderFragment()
    }
    interface ReminderInterface {
        fun sendReminder(message: String, id: String, calendar: Calendar)
    }
    private lateinit var reminderViewModel: ReminderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      

        reminderViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)
        reminderViewModel.getReminders()
        val reminderList = root.findViewById<RecyclerView>(R.id.list_items)
        reminderViewModel.reminderInfo.observe(viewLifecycleOwner, Observer { result ->
            reminderViewModel.reminderAdapter= ReminderAdapter(result.toMutableList(),
                ::openReminderDialog, ::displayNoInfoToShowMessage, root)
            displayNoInfoToShowMessage(result, root)
            reminderList.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = reminderViewModel.reminderAdapter
            }
            val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(reminderViewModel::deleteToDb))
            itemTouchHelper.attachToRecyclerView(reminderList)
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button: FloatingActionButton = view.findViewById(R.id.add_items)
        button.setOnClickListener {
            openReminderDialog()
        }
    }

    private fun openReminderDialog(
        reminder: Reminder = Reminder()
    ) {
        activity?.supportFragmentManager?.let {
            ReminderDialogFragment(reminder, reminderViewModel).show(it, "")
        }
    }

    private fun displayNoInfoToShowMessage(
        list: List<Reminder>,
        root: View
    ) {
        val message = root.findViewById<TextView>(R.id.no_items_message)
        if (list.isNotEmpty()) {
            message.visibility = View.GONE
        }else{
            message.visibility = View.VISIBLE
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reminderViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)
    }


}