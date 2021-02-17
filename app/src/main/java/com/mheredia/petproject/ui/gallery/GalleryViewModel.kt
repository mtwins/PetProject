package com.mheredia.petproject.ui.gallery

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.data.model.PetPicture
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class GalleryViewModel : ViewModel() {


    lateinit var  galleryAdapter: GalleryAdapter

    val pictures = MutableLiveData<List<PetPicture>>()

    private val db = Firebase.firestore

    fun getPictures(){
        viewModelScope.launch {
            pictures.value=getPicturesForCurrentUser(Firebase.auth.currentUser?.uid?:"")
        }
    }

    suspend fun getPicturesForCurrentUser(userid: String): List<PetPicture> =
        Firebase.firestore.collection("pictures")
            .whereEqualTo("userId", userid)
            .get()
            .await()
            .toObjects(PetPicture::class.java)

    fun writePictureToDb(petPicture: PetPicture){
        if(petPicture.pictureId.isEmpty()){
            addPictureToDb(petPicture)
        }else{
            updatePictureToDb(petPicture)
        }
    }

    fun addPictureToDb(petPicture: PetPicture) {
        var picturesRef= db.collection("pictures").document()
        petPicture.pictureId=picturesRef.id
        picturesRef
            .set(petPicture)
            .addOnSuccessListener { documentReference ->
                galleryAdapter.addPicture(petPicture)
                Log.d("ContactDone", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
    }
    fun updatePictureToDb(petPicture: PetPicture) {
        var contactsRef= db.collection("pictures")
        contactsRef
            .document(petPicture.pictureId)
            .set(petPicture)
            .addOnSuccessListener { documentReference ->
                galleryAdapter.editPicture(petPicture)
                Log.d("ContactDone", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
    }



}