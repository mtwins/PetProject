package com.mheredia.petproject.ui.petInfo

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.PetInfo


class PetDialogFragment(
    var petInfo: PetInfo,
    var petInfoViewModel: PetInfoViewModel
) :
    DialogFragment() {
    lateinit var  nameTextBox: TextView
    lateinit var  petTypeTextBox: TextView
    lateinit var  petBreedTextBox: TextView
    lateinit var  petAgeTextBox: TextView

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_add_pet_info, null)

             nameTextBox = view.findViewById(R.id.pet_name_edit_text)
             petTypeTextBox = view.findViewById(R.id.pet_type_edit_text)
             petBreedTextBox = view.findViewById(R.id.pet_breed_edit_text)
             petAgeTextBox = view.findViewById(R.id.pet_age_text)

            var title = "Add Pet"
            if (petInfo.petId.isNotBlank()) {
                title = "Edit Pet Info"
                setPetInfoTextBoxes(nameTextBox, petTypeTextBox, petAgeTextBox, petBreedTextBox)
            }

            builder.setView(view)
                .setTitle(title)
                .setPositiveButton(
                    "Save"
                ) { _, _ ->
                    setPetInfo(nameTextBox, petTypeTextBox, petBreedTextBox, petAgeTextBox)
                    petInfoViewModel.writePetInfoToDb(petInfo)
                }
                .setNegativeButton("Cancel") { _, _ -> dialog?.cancel() }
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setPetInfoTextBoxes(
        nameTextBox: TextView,
        petTypeTextBox: TextView,
        petAgeTextBox: TextView,
        petBreedTextBox: TextView
    ) {
        nameTextBox.setText(petInfo.petName)
        petTypeTextBox.setText(petInfo.petType)
        petAgeTextBox.setText(petInfo.petAge)
        petBreedTextBox.setText(petInfo.petBreed)
    }

    private fun setPetInfo(
        nameTextBox: TextView,
        petTypeTextBox: TextView,
        petBreedTextBox: TextView,
        petAgeTextBox: TextView
    ) {
        petInfo.petName = nameTextBox.text.toString()
        petInfo.petType = petTypeTextBox.text.toString()
        petInfo.petBreed = petBreedTextBox.text.toString()
        petInfo.petAge = petAgeTextBox.text.toString()
        petInfo.userId = Firebase.auth.currentUser?.uid.toString()
    }


}