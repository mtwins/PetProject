package com.mheredia.petproject.ui.petInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mheredia.petproject.R

class PetInfoFragment : Fragment() {

    private lateinit var petInfoViewModel: PetInfoViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        petInfoViewModel =
                ViewModelProvider(this).get(PetInfoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_pet_info, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        petInfoViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}