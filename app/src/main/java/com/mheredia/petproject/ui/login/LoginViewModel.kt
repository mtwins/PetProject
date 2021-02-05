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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class LoginViewModel : ViewModel() {
    private var auth = Firebase.auth


    fun emailSignIn(email: String, password: String, view: View, context: Context) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await().run{
                MainActivity.route(context)
                }
            } catch (ex: Exception) {
                sendSnackbarMessage(view,ex.localizedMessage.toString())
            }

        }
    }

    fun registerUser(email: String, password: String, view: View, context: Context) {
        viewModelScope.launch {
            try {
               auth.createUserWithEmailAndPassword(email, password).await().run {
                   MainActivity.route(context)
               }
            } catch (ex: Exception) {
                sendSnackbarMessage(view,ex.localizedMessage.toString())
            }

        }
    }


    fun isValidEmailAndPassword(emailText: String, passwordText: String, view: View): Boolean {
        if (isRequiredFieldEmpty(emailText) || isRequiredFieldEmpty(passwordText)) {
            return false
        } else {
            return true;
        }
    }

    private fun sendSnackbarMessage(view: View, message:String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    private fun isRequiredFieldEmpty(text: String): Boolean {
        return text.isEmpty() || text.isBlank()
    }


}