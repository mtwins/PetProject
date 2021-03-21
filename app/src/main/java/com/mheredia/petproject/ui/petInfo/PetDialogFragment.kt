package com.mheredia.petproject.ui.petInfo

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.PetInfo


class PetDialogFragment(
    var petInfo: PetInfo,
    var petInfoViewModel: PetInfoViewModel
) :
    DialogFragment() {
    lateinit var nameTextBox: TextView
    lateinit var petTypeTextBox: TextView
    lateinit var petBreedTextBox: TextView
    lateinit var petAgeTextBox: TextView
    lateinit var saveButton: Button
    lateinit var cancelButton: ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog: Dialog = activity?.let {
            val builder = AlertDialog.Builder(it, R.style.FullScreenDialog)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_add_pet_info, null)

            nameTextBox = view.findViewById(R.id.pet_name_edit_text)
            petTypeTextBox = view.findViewById(R.id.pet_type_edit_text)
            val petTypeTextContainer: TextInputLayout = view.findViewById(R.id.pet_type_textbox)
            val items = listOf("Cat", "Dog", "Bird", "Reptile", "Fish", "Other")
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            (petTypeTextContainer.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            petBreedTextBox = view.findViewById(R.id.pet_breed_edit_text)
            petAgeTextBox = view.findViewById(R.id.pet_age_text)
            petAgeTextBox = view.findViewById(R.id.pet_age_text)
            saveButton = view.findViewById(R.id.save_pet)
            cancelButton = view.findViewById(R.id.cancel_dialog)

            var title = "Add Pet"
            if (petInfo.petId.isNotBlank()) {
                title = "Edit Pet Info"
                setPetInfoTextBoxes(
                    nameTextBox,
                    petTypeTextContainer,
                    petAgeTextBox,
                    petBreedTextBox
                )
            }

            saveButton.setOnClickListener {
                setPetInfo(nameTextBox, petTypeTextBox, petBreedTextBox, petAgeTextBox)
                petInfoViewModel.writePetInfoToDb(petInfo)
                dialog?.cancel()
            }
            cancelButton.setOnClickListener{
                dialog?.dismiss()
            }

            builder.setView(view)
                .setTitle(title)

            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
        return dialog
    }

    private fun setPetInfoTextBoxes(
        nameTextBox: TextView,
        petTypeTextBox: TextInputLayout,
        petAgeTextBox: TextView,
        petBreedTextBox: TextView
    ) {
        nameTextBox.setText(petInfo.petName)
        (petTypeTextBox.editText as AutoCompleteTextView).setText(petInfo.petType, false)
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