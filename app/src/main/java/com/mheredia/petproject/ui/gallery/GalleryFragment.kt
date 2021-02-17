package com.mheredia.petproject.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.Contact
import com.mheredia.petproject.data.model.PetPicture
import com.mheredia.petproject.ui.contacts.ContactDialogFragment
import com.mheredia.petproject.ui.contacts.ContactsFragment

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var fab: FloatingActionButton

    interface GalleryInterface {

    }

    companion object {
        fun newInstance() = ContactsFragment()
    }


    override fun onResume() {
        super.onResume()
        galleryViewModel.getPictures()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
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
//            val itemTouchHelper =
//                ItemTouchHelper(SwipeToDeleteCallback(galleryViewModel::deletePicturesFromDb))
          //  itemTouchHelper.attachToRecyclerView(pet_pictures_list)
        })
        return root
    }

    private fun openGallaryDialog(picture: PetPicture= PetPicture()) {
        activity?.supportFragmentManager?.let {
            GalleryDialogFragment(picture, requireContext(), galleryViewModel).show(it, "")
        }
    }




}