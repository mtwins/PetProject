package com.mheredia.petproject.ui.petInfo.medical

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mheredia.petproject.R
import com.mheredia.petproject.data.model.MedicalInfo
import com.mheredia.petproject.ui.utils.SwipeToDeleteCallback

class MedicalHomeActivity : AppCompatActivity() {
    private val medicalViewModel: MedicalViewModel by viewModels()
    var petId = ""

    companion object {
        fun newIntent(context: Context, petId: String): Intent {
            return Intent(context, MedicalHomeActivity::class.java).apply {
                putExtra("petId", petId)
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_info)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        petId = intent.getStringExtra("petId").toString()
        findViewById<View>(R.id.add_medical_info).setOnClickListener {
            openDialog()
        }
        var medicalList_list = findViewById<RecyclerView>(R.id.list_items)
        medicalViewModel.medicalInfo.observe(this, Observer { result ->
            setNoItemMessage(result)
            medicalViewModel.medicalAdapter =
                MedicalAdapter(
                    result.toMutableList(),
                    this::openDialog,
                    ::setNoItemMessage
                )
            medicalList_list.apply {
                layoutManager = LinearLayoutManager(this@MedicalHomeActivity)
                adapter = medicalViewModel.medicalAdapter
            }
            val itemTouchHelper =
                ItemTouchHelper(SwipeToDeleteCallback(medicalViewModel::deleteMedicalInfoFromDb))
            itemTouchHelper.attachToRecyclerView(medicalList_list)
        })
    }

    private fun setNoItemMessage(result: MutableList<MedicalInfo>) {
        val message = findViewById<TextView>(R.id.no_info_message)
        if (result.size > 0) {
            message.visibility = View.GONE
        } else {
            message.visibility = View.VISIBLE
        }
    }


    override fun onResume() {
        super.onResume()
        medicalViewModel.getMedicalInfo(petId)

    }

    private fun openDialog(medicalInfo: MedicalInfo = MedicalInfo()) {
        supportFragmentManager?.let {
            MedicalDialogFragment(medicalInfo, medicalViewModel, petId).show(it, "")
        }
    }
}