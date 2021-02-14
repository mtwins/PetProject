package com.mheredia.petproject.ui.petInfo

import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.data.model.PetInfo
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PetInfoViewModel : ViewModel() {
    lateinit var petInfoAdapter: PetInfoAdapter
    val petInfo = MutableLiveData<List<PetInfo>>()
    private val db = Firebase.firestore
    private val _text = MutableLiveData<String>().apply {
        value = "This is pet info Fragment"
    }

    fun getPetInfo() {
        viewModelScope.launch {
            petInfo.value = getPetInfoForCurrentUser(Firebase.auth.currentUser?.uid ?: "")
        }
    }

    suspend fun getPetInfoForCurrentUser(userid: String): List<PetInfo> =
        Firebase.firestore.collection("pets")
            .whereEqualTo("userId", userid)
            .get()
            .await()
            .toObjects(PetInfo::class.java)

    fun writePetInfoToDb(petInfo: PetInfo) {
        viewModelScope.launch {
            if (petInfo.petId.isBlank()) {
                addPetToDb(petInfo)
            } else {
                updatePetToDb(petInfo)
            }
        }
    }

    private suspend fun addPetToDb(petInfo: PetInfo) {
        val petInfoRef = db.collection("pets").document()
        petInfo.petId = petInfoRef.id
        petInfoRef.set(petInfo).await()
        petInfoAdapter.addPet(petInfo)


    }

    private suspend fun updatePetToDb(petInfo: PetInfo) {
        val petInfoRef = db.collection("pets")
        petInfoRef
            .document(petInfo.petId)
            .set(petInfo).await()
        petInfoAdapter.editPetInfo(petInfo)
    }

    fun deletePetFromDb(position: Int){
        var contact= petInfoAdapter.result[position]
        viewModelScope.launch {
            Firebase.firestore.collection("pets")
                .document(contact.petId)
                .delete().await()
            petInfoAdapter.deleteContact(position)

        }

    }

    fun setPetProfile(
        activity: FragmentActivity,
        petId: String,
        imageView: ImageView
    ) {
        var mCallback = activity as PetInfoFragment.PetImageInterface
        mCallback.setPetImage( petId, imageView)
    }
}