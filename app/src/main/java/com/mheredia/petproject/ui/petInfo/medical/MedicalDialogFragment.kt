package com.mheredia.petproject.ui.petInfo.medical

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.MedicalInfo


class MedicalDialogFragment(
    var medicalInfo: MedicalInfo,
    var medicalViewModel: MedicalViewModel,
    var petId:String
) :
    DialogFragment() {
    lateinit var conditionNameTextBox: TextView
    lateinit var medicineTextBox: TextView
    lateinit var careInstructionsTextBox: TextView
    lateinit var saveButton: Button
    lateinit var cancelButton: ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog: Dialog = activity?.let {
            val builder = AlertDialog.Builder(it, R.style.FullScreenDialog)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_add_medical_condition, null)

            conditionNameTextBox = view.findViewById(R.id.medical_condition_name_edit_text)
            medicineTextBox = view.findViewById(R.id.medicine_edit_text)
            careInstructionsTextBox = view.findViewById(R.id.medical_care_edit_text)
            saveButton = view.findViewById(R.id.save_medical_info)
            cancelButton = view.findViewById(R.id.cancel_dialog)

            var title = "Add Medical Condition"
            if (medicalInfo.medicalId.isNotBlank()) {
                title = "Edit Medical Condition"
                conditionNameTextBox.text = medicalInfo.condition
                medicineTextBox.text = medicalInfo.medicines
                careInstructionsTextBox.text = medicalInfo.careInstructions
            }
            saveButton.setOnClickListener {
                setMedicalInfo(
                    conditionNameTextBox,
                    medicineTextBox,
                    careInstructionsTextBox
                )
                medicalViewModel.writeMedicalInfoToDb(medicalInfo)
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



    private fun setMedicalInfo(
        nameTextBox: TextView,
        medicinesTextBox: TextView,
        careInstructionsTextBox: TextView
    ) {
        medicalInfo.condition = nameTextBox.text.toString()
        medicalInfo.medicines = medicinesTextBox.text.toString()
        medicalInfo.careInstructions = careInstructionsTextBox.text.toString()
        medicalInfo.petId = petId
    }



}