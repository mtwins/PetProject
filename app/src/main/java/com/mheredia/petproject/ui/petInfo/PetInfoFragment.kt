package com.mheredia.petproject.ui.petInfo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.PetInfo
import com.mheredia.petproject.data.model.Vaccine
import com.mheredia.petproject.ui.petInfo.medical.MedicalHomeActivity
import com.mheredia.petproject.ui.petInfo.vaccine.VaccineHomeActivity
import com.mheredia.petproject.ui.utils.SwipeToDeleteCallback

class PetInfoFragment : Fragment() {

    private val petInfoViewModel: PetInfoViewModel by activityViewModels()

    private lateinit var fab: FloatingActionButton

    interface PetImageInterface {
        fun setPetImage(petId: String, index: Int)
        fun sharePetInfo(context: Context, petInfo: PetInfo)
    }

    override fun onResume() {
        super.onResume()
        petInfoViewModel.getPetInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_list, container, false)
        petInfoViewModel.getPetInfo()
        fab = root.findViewById(R.id.add_items)
        fab.setOnClickListener { view ->
            openPetInfoDialog()
        }

        var pet_list = root.findViewById<RecyclerView>(R.id.list_items)
        petInfoViewModel.petInfo.observe(viewLifecycleOwner, Observer { result ->
            petInfoViewModel.petInfoAdapter =
                PetInfoAdapter(
                    result.toMutableList(),
                    ::openPetInfoDialog,
                    ::selectPetProfile,
                    ::startVaccineActivity,
                    ::startMedicineActivity,
                    petInfoViewModel,
                    this.requireContext(),
                    this.requireActivity(),
                    this::displayNoInfoToShowMessage,
                    root
                )
            displayNoInfoToShowMessage(result,root)

            pet_list.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = petInfoViewModel.petInfoAdapter
            }
            val itemTouchHelper =
                ItemTouchHelper(SwipeToDeleteCallback(petInfoViewModel::deletePetFromDb))
            itemTouchHelper.attachToRecyclerView(pet_list)
        })
        return root
    }
    private fun displayNoInfoToShowMessage(
        list: List<PetInfo>,
        root: View
    ) {
        val message = root.findViewById<TextView>(R.id.no_items_message)
        if (list.isNotEmpty()) {
            message.visibility = View.GONE
        }else{
            message.visibility = View.VISIBLE
        }
    }

    private fun openPetInfoDialog(petInfo: PetInfo = PetInfo() ){
        activity?.supportFragmentManager?.let {
            PetDialogFragment(petInfo, petInfoViewModel).show(it, "")
        }
    }

    private fun startVaccineActivity(petId: String){
        startActivity(VaccineHomeActivity.newIntent(this.requireContext(), petId))
    }

    private fun startMedicineActivity(petId: String){
        startActivity(MedicalHomeActivity.newIntent(this.requireContext(), petId))
    }

    private fun selectPetProfile(petId:String, index: Int){
        petInfoViewModel.setPetProfile(this.requireActivity(), petId , index)
    }


}