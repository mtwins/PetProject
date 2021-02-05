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
    companion object {
        private const val invalidPasswordOrEmail = "Invalid Email or Password"
        private const val mismatchConfirmPassword = "Password and Confirm Password Should match"
    }

    private var auth = Firebase.auth
    val userRegistrationResult: MutableLiveData<CallResult> by lazy {
        MutableLiveData<CallResult>()
    }

    fun registerUser(email: String, password: String, confirmPassword: String) {
        //move this to the isValidEmailPassword method , as this is common
        if (!isValidEmailAndPassword(email, password, confirmPassword)) {
            setUserRegistrationResult(CallResult(false, invalidPasswordOrEmail))
        } else if (!confirmPassword.equals(password)) {
            setUserRegistrationResult(CallResult(false, mismatchConfirmPassword))
        } else {
            viewModelScope.launch {
                registerUserWithFirebase(email, password)
            }
        }
    }

    private suspend fun registerUserWithFirebase(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            setUserRegistrationResult(CallResult(true))
        } catch (ex: Exception) {
            userRegistrationResult.value = CallResult(false, ex.localizedMessage.toString())
        }
    }

    private fun setUserRegistrationResult(result: CallResult) {
        userRegistrationResult.value = result
    }

    private fun isValidEmailAndPassword(emailText: String, passwordText:
        String, confirmPassword: String): Boolean =
        !(isRequiredFieldEmpty(emailText) || isRequiredFieldEmpty(passwordText)
                || isRequiredFieldEmpty(confirmPassword))

    private fun isRequiredFieldEmpty(text: String): Boolean = text.isEmpty() || text.isBlank()


}


