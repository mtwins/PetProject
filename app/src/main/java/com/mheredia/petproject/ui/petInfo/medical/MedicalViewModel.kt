package com.mheredia.petproject.ui.petInfo.medical

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.data.model.MedicalInfo
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MedicalViewModel : ViewModel() {
    lateinit var medicalAdapter: MedicalAdapter
    var medicalInfo = MutableLiveData<MutableList<MedicalInfo>>()
    private val db = Firebase.firestore
    private val _text = MutableLiveData<String>().apply {
        value = "This is pet info Fragment"
    }

    fun getMedicalInfo(petId: String) {
        viewModelScope.launch {
            medicalInfo.value = getMedicalInfoForPet(petId)
        }
    }

    suspend fun getMedicalInfoForPet(petId: String): MutableList<MedicalInfo> =
        Firebase.firestore.collection("medicalInfo")
            .whereEqualTo("petId", petId)
            .get()
            .await()
            .toObjects(MedicalInfo::class.java)

    fun writeMedicalInfoToDb(medicalInfo: MedicalInfo) {
        viewModelScope.launch {
            if (medicalInfo.medicalId.isBlank()) {
                addMedicalInfoToDb(medicalInfo)
            } else {
                updateMedicalInfoToDb(medicalInfo)
            }
        }
    }

    private suspend fun addMedicalInfoToDb(medicalInfo: MedicalInfo) {
        val medicalInfoRef = db.collection("medicalInfo").document()
        medicalInfo.medicalId = medicalInfoRef.id
        medicalInfoRef.set(medicalInfo).await()
        medicalAdapter.addMedicalInfo(medicalInfo)
        this.medicalInfo.value?.add(medicalInfo)
    }

    private suspend fun updateMedicalInfoToDb(medicalInfo: MedicalInfo) {
        val petInfoRef = db.collection("medicalInfo")
        petInfoRef
            .document(medicalInfo.medicalId)
            .set(medicalInfo).await()
        medicalAdapter.editMedicalInfo(medicalInfo)
    }

    fun deleteMedicalInfoFromDb(position: Int){
        var medicalInfo= medicalAdapter.result[position]
        this.medicalInfo.value?.removeAt(position)
        viewModelScope.launch {
            Firebase.firestore.collection("medicalInfo")
                .document(medicalInfo.medicalId)
                .delete().await()
            medicalAdapter.deleteMedicalInfo(position)
        }

    }


}