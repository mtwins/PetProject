package com.mheredia.petproject.ui.registration

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.MainActivity
import com.mheredia.petproject.ui.Utils
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class RegistrationViewModel : ViewModel() {
    fun registerUser(email: String, password: String, view: View, context: Context) {
        viewModelScope.launch {
            try {
                Firebase.auth.createUserWithEmailAndPassword(email, password).await()
                MainActivity.route(context)
                
            } catch (ex: Exception) {
                Utils().sendSnackbarMessage(view, ex.localizedMessage.toString())
            }

        }
    }
}