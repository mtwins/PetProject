package com.mheredia.petproject.ui.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.model.CallResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegistrationViewModel : ViewModel() {

    private var auth = Firebase.auth
    val userRegistrationResult: MutableLiveData<CallResult> by lazy {
        MutableLiveData<CallResult>()
    }

    fun registerUser(email: String, password: String) {
        //move this to the isValidEmailPassword method , as this is common
        if(!isValidEmailAndPassword(email,password)) {
            userRegistrationResult.value = CallResult(false, "Invalid Email or Password")
            return
        }
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                userRegistrationResult.value = CallResult(true)

            } catch (ex: Exception) {
                userRegistrationResult.value = CallResult(false, ex.localizedMessage.toString())
            }

        }
    }

    fun isValidEmailAndPassword(emailText: String, passwordText: String): Boolean {
        return !(isRequiredFieldEmpty(emailText) || isRequiredFieldEmpty(passwordText))
    }

    private fun isRequiredFieldEmpty(text: String): Boolean {
        return text.isEmpty() || text.isBlank()
    }

}