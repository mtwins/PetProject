package com.mheredia.petproject.ui.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.PetPicture
import com.mheredia.petproject.ui.contacts.ContactsFragment
import com.mheredia.petproject.ui.utils.SwipeToDeleteCallback

class GalleryFragment : Fragment() {

    private val galleryViewModel: GalleryViewModel by activityViewModels()
    private lateinit var fab: FloatingActionButton

    interface GalleryInterface {
        fun setGalleryImage(context: Context)
    }

    companion object {
        fun newInstance() = ContactsFragment()
    }


    override fun onResume() {
        super.onResume()
        galleryViewModel.getPictures()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_list, container, false)
        galleryViewModel.getPictures()
        fab = root.findViewById(R.id.add_items)
        fab.setOnClickListener { view ->
            openGallaryDialog()
        }

        var pet_pictures_list = root.findViewById<RecyclerView>(R.id.list_items)
        galleryViewModel.pictures.observe(viewLifecycleOwner, Observer { result ->
            galleryViewModel.galleryAdapter =
                GalleryAdapter(
                    result.toMutableList(),
                    requireContext()
                )
            pet_pictures_list.apply {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = galleryViewModel.galleryAdapter
            }
            val itemTouchHelper =
                ItemTouchHelper(SwipeToDeleteCallback(galleryViewModel::deletePictureFromDb))
            itemTouchHelper.attachToRecyclerView(pet_pictures_list)
        })
        return root
    }

    private fun openGallaryDialog(picture: PetPicture= PetPicture()) {
        galleryViewModel.galleryDialogFragment= GalleryDialogFragment(picture, requireContext(), galleryViewModel, this.requireActivity())
        activity?.supportFragmentManager?.let { galleryViewModel.galleryDialogFragment.show(it, "") }
        }





}