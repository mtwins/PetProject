package com.mheredia.petproject.ui.petInfo.vaccine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.Vaccine
import com.mheredia.petproject.ui.petInfo.PetInfoAdapter
import com.mheredia.petproject.ui.reminders.ReminderDialogFragment
import com.mheredia.petproject.ui.utils.SwipeToDeleteCallback

class ViewVaccineFragment(var petId: String) : Fragment() {
    private val vaccineViewModel: VaccineViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_first, container, false)
        var vaccine_list = root.findViewById<RecyclerView>(R.id.list_items)
        vaccineViewModel.vaccineInfo.observe(viewLifecycleOwner, Observer { result ->
            vaccineViewModel.vaccineAdapter =
                VaccineAdapter(
                    result.toMutableList(),
                    this::openDialog
                )
            displayNoVaccinesToShowMessage(vaccine_list, root)
            vaccine_list.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = vaccineViewModel.vaccineAdapter
            }
            val itemTouchHelper =
                ItemTouchHelper(SwipeToDeleteCallback(vaccineViewModel::deleteVaccineFromDb))
            itemTouchHelper.attachToRecyclerView(vaccine_list)
        })
        return root
    }

    private fun displayNoVaccinesToShowMessage(
        vaccine_list: RecyclerView,
        root: View
    ) {
        if (vaccine_list.size == 0) {
            var vaccineMessage = root.findViewById<TextView>(R.id.no_vaccine_message)
            vaccineMessage.visibility = View.GONE
        }
    }


    override fun onResume() {
        super.onResume()
        vaccineViewModel.getVaccineInfo(petId)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.button_first).setOnClickListener {
            openDialog()
        }


    }

    private fun openDialog(vaccine: Vaccine = Vaccine()) {
        activity?.supportFragmentManager?.let {
            VaccineDialogFragment(vaccine, vaccineViewModel, petId).show(it, "")
        }
    }
}