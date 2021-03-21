package com.mheredia.petproject.ui.petInfo.vaccine

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.data.model.PetInfo
import com.mheredia.petproject.data.model.Vaccine
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class VaccineViewModel : ViewModel() {
    lateinit var vaccineAdapter: VaccineAdapter
    var vaccineInfo = MutableLiveData<List<Vaccine>>()
    private val db = Firebase.firestore
    private val _text = MutableLiveData<String>().apply {
        value = "This is pet info Fragment"
    }

    fun getVaccineInfo(petId: String) {
        viewModelScope.launch {
            vaccineInfo.value = getVaccineForPet(petId)
        }
    }

    suspend fun getVaccineForPet(petId: String): List<Vaccine> =
        Firebase.firestore.collection("vaccines")
            .whereEqualTo("petId", petId)
            .get()
            .await()
            .toObjects(Vaccine::class.java)

    fun writeVaccineInfoToDb(vaccine: Vaccine) {
        viewModelScope.launch {
            if (vaccine.vaccineId.isBlank()) {
                addVaccineToDb(vaccine)
            } else {
                updateVaccineToDb(vaccine)
            }
        }
    }

    private suspend fun addVaccineToDb(vaccine: Vaccine) {
        val vaccineRef = db.collection("vaccines").document()
        vaccine.vaccineId = vaccineRef.id
        vaccineRef.set(vaccine).await()
        vaccineAdapter.addVaccine(vaccine)


    }

    private suspend fun updateVaccineToDb(vaccine: Vaccine) {
        val petInfoRef = db.collection("vaccines")
        petInfoRef
            .document(vaccine.vaccineId)
            .set(vaccine).await()
        vaccineAdapter.editVaccine(vaccine)
    }

    fun deleteVaccineFromDb(position: Int){
        var vaccine= vaccineAdapter.result[position]
        viewModelScope.launch {
            Firebase.firestore.collection("vaccines")
                .document(vaccine.vaccineId)
                .delete().await()
            vaccineAdapter.deleteContact(position)
        }

    }


}