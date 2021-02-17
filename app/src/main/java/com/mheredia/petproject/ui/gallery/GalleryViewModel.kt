package com.mheredia.petproject.ui.gallery

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.data.model.PetPicture
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class GalleryViewModel : ViewModel() {


    lateinit var petPictureId: String
    lateinit var  galleryAdapter: GalleryAdapter
    lateinit var  galleryDialogFragment: GalleryDialogFragment

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

//    fun writePictureToDb(petPicture: PetPicture){
//        if(petPicture.pictureId.isEmpty()){
//            addPictureToDb(petPicture)
//        }else{
//            updatePictureToDb(petPicture)
//        }
//    }

    fun addPictureToDb(petPicture: PetPicture, picturesRef:DocumentReference) {
        picturesRef
            .set(petPicture)
            .addOnSuccessListener {
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
            .addOnSuccessListener {
                galleryAdapter.editPicture(petPicture)
                Log.d("ContactDone", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w("ContactError", "Error writing document", e) }
    }

    fun setGalleryImage(
        activity: FragmentActivity,
        context: Context
    ) {
        var mCallback = activity as GalleryFragment.GalleryInterface
        mCallback.setGalleryImage(context)
    }

    fun deletePictureFromDb(position: Int){
        var picture= galleryAdapter.result[position]
        viewModelScope.launch {
            Firebase.firestore.collection("pictures")
                .document(picture.pictureId)
                .delete().await()
            galleryAdapter.deletePicture(position)
        }

    }


}