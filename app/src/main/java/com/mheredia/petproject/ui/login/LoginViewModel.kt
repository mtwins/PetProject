package com.mheredia.petproject.ui.login

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.MainActivity
import com.mheredia.petproject.ui.Utils
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class LoginViewModel : ViewModel() {
    private var auth = Firebase.auth


    fun emailSignIn(email: String, password: String, view: View, context: Context) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                MainActivity.route(context)

            } catch (ex: Exception) {
                Utils().sendSnackbarMessage(view, ex.localizedMessage.toString())
            }

        }
    }

    fun registerUser(email: String, password: String, view: View, context: Context) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                MainActivity.route(context)

            } catch (ex: Exception) {
                Utils().sendSnackbarMessage(view, ex.localizedMessage.toString())
            }

        }
    }

    fun isValidEmailAndPassword(emailText: String, passwordText: String, view: View): Boolean {
        return !(isRequiredFieldEmpty(emailText) || isRequiredFieldEmpty(passwordText))
    }

    private fun isRequiredFieldEmpty(text: String): Boolean {
        return text.isEmpty() || text.isBlank()
    }


}