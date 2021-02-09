package com.mheredia.petproject.ui.reminders

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.Reminder

class ReminderFragment : Fragment() {

    companion object {
        fun newInstance() = ReminderFragment()
    }

    private lateinit var reminderViewModel: ReminderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        reminderViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_reminders, container, false)
        reminderViewModel.getReminders()
        var reminderList = root.findViewById<RecyclerView>(R.id.reminders_list)
        reminderViewModel.reminderInfo.observe(viewLifecycleOwner, Observer { result ->
            reminderViewModel.adapter= ReminderAdapter(result.toMutableList(), ::openReminderDialog)
            reminderList.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = reminderViewModel.adapter
            }
            reminderViewModel.adapter.notifyDataSetChanged()

        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val button: FloatingActionButton = view.findViewById(R.id.add_reminders)
        button.setOnClickListener {
            openReminderDialog()
        }


    }

    private fun openReminderDialog(
        reminder: Reminder = Reminder()
    ) {
        activity?.supportFragmentManager?.let {
            ReminderDialogFragment(reminder).show(it, "")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reminderViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)
    }


}