package com.mheredia.petproject.ui.gallery

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.GlideApp
import com.mheredia.petproject.MainActivity
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.PetPicture
import com.mheredia.petproject.ui.utils.Utils


class GalleryDialogFragment(
    var petPicture: PetPicture,
    var fragmentContext: Context,
    var galleryViewModel: GalleryViewModel,
    var activityFragment: FragmentActivity
) :
    DialogFragment() {

    lateinit var petPictureImage: ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.FullScreenDialog )
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_add_picture, null)

            petPictureImage = view.findViewById(R.id.add_pet_image)
            val tag: TextView = view.findViewById(R.id.add_tag)
            val saveButton: Button = view.findViewById(R.id.save_pet_picture)
            val cancelButton: ImageView = view.findViewById(R.id.cancel_dialog)
            lateinit var picturesRef:DocumentReference

            var title = "Add Picture"
            if (petPicture.pictureId.isNotBlank()) {
                title = "Edit Picture"
                tag.setText(petPicture.pictureTag)
                GlideApp.with(this.fragmentContext)
                    .load(MainActivity.storage.reference.child(petPicture.pictureUrl))
                    .error(
                        Glide.with(this.fragmentContext)
                            .load(
                                AppCompatResources.getDrawable(
                                    this.fragmentContext,
                                    R.drawable.ic_menu_camera
                                )
                            )
                    )
                    .into(petPictureImage)
            }
            if (petPicture.pictureId.isBlank()) {
                petPictureImage.setOnClickListener {
                    if (tag.text.toString().isNotEmpty()) {
                        picturesRef = Firebase.firestore.collection("pictures").document()
                        petPicture.pictureId = picturesRef.id
                        galleryViewModel.petPictureId = picturesRef.id
                        galleryViewModel.setGalleryImage(activityFragment, fragmentContext)

                    } else {
                        this.view?.let { it1 ->
                            Utils().sendMessage(
                                it1,
                                "Enter a tag for the image"
                            )
                        }
                    }

                }
            }
            saveButton.setOnClickListener {
                petPicture.pictureTag = tag.text.toString()
                petPicture.userId = Firebase.auth.currentUser?.uid.toString()
                petPicture.pictureUrl = "pet-pictures/${petPicture.pictureId}"
                if(petPicture.pictureId.isEmpty()) {
                    galleryViewModel.addPictureToDb(petPicture, picturesRef)
                }else{
                    galleryViewModel.updatePictureToDb(petPicture)
                }
                dialog?.cancel()
            }
            cancelButton.setOnClickListener {
                dialog?.cancel()
            }

            builder.setView(view)
                .setTitle(title)
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun updatePicture(uri: Uri) {
        GlideApp.with(this)
            .load(uri)
            .error(R.drawable.ic_baseline_close_24)
            .into(petPictureImage);
    }




}