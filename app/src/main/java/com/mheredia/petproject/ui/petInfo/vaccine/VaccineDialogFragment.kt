package com.mheredia.petproject.ui.petInfo.vaccine

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.Vaccine
import java.util.*


class VaccineDialogFragment(
    var vaccineInfo: Vaccine,
    var vaccineViewModel: VaccineViewModel,
    var petId:String
) :
    DialogFragment() {
    lateinit var nameTextBox: TextView
    lateinit var vaccineDateTextBox: TextView
    lateinit var renewDateTextBox: TextView
    lateinit var saveButton: Button
    lateinit var cancelButton: ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog: Dialog = activity?.let {
            val builder = AlertDialog.Builder(it, R.style.FullScreenDialog)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_add_vaccine, null)

            nameTextBox = view.findViewById(R.id.vaccine_name_edit_text)
            vaccineDateTextBox = view.findViewById(R.id.vaccine_date)
            renewDateTextBox = view.findViewById(R.id.renew_vaccine_date)
            saveButton = view.findViewById(R.id.save_vaccine)
            cancelButton = view.findViewById(R.id.cancel_dialog)
            var calender= Calendar.getInstance()

            var title = "Add Vaccine"
            if (vaccineInfo.vaccineId.isNotBlank()) {
                title = "Edit Vaccine"
                nameTextBox.text = vaccineInfo.vaccineName
                vaccineDateTextBox.text = vaccineInfo.vaccineDate
                renewDateTextBox.text = vaccineInfo.renewVaccineDate
            }
            setDateTextOnClickListener(vaccineDateTextBox, calender)
            setDateTextOnClickListener(renewDateTextBox, calender)

            saveButton.setOnClickListener {
                setVaccineInfo(
                    nameTextBox,
                    vaccineDateTextBox,
                    renewDateTextBox
                )
                vaccineViewModel.writeVaccineInfoToDb(vaccineInfo)
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



    private fun setVaccineInfo(
        nameTextBox: TextView,
        vaccineDateTextBox: TextView,
        renewDateTextBox: TextView
    ) {
        vaccineInfo.vaccineName = nameTextBox.text.toString()
        vaccineInfo.vaccineDate = vaccineDateTextBox.text.toString()
        vaccineInfo.renewVaccineDate = renewDateTextBox.text.toString()
        vaccineInfo.petId = petId
    }

    private fun setDateTextOnClickListener(dateTextBox: TextView, calender: Calendar) {
        dateTextBox.setOnClickListener {
            var datePickerDialog = DatePickerDialog(this.requireContext())
            datePickerDialog.setOnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                calender.set(Calendar.MONTH, month)
                calender.set(Calendar.DAY_OF_MONTH, day)
                calender.set(Calendar.YEAR, year)
                dateTextBox.setText("$month/$day/$year")
            }
            datePickerDialog.show()
        }
    }

}