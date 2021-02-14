package com.mheredia.petproject.ui.petInfo

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.PetInfo
import com.mheredia.petproject.ui.utils.SwipeToDeleteCallback

class PetInfoFragment : Fragment() {

    private lateinit var petInfoViewModel: PetInfoViewModel

    private lateinit var fab: FloatingActionButton

    interface PetImageInterface {
        fun setPetImage(petId: String, image:ImageView)
    }

    override fun onResume() {
        super.onResume()
        petInfoViewModel.getPetInfo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        petInfoViewModel = ViewModelProvider(this).get(PetInfoViewModel::class.java)
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
                    ::selectPetProfile
                )
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

    private fun openPetInfoDialog(petInfo: PetInfo = PetInfo() ){
        activity?.supportFragmentManager?.let {
            PetDialogFragment(petInfo, petInfoViewModel).show(it, "")
        }
    }
    private fun selectPetProfile(petId:String, imageView: ImageView){
        petInfoViewModel.setPetProfile(this.requireActivity(), petId , imageView)
    }


}