package com.mheredia.petproject.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.model.CallResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LoginViewModel : ViewModel() {
    val userLogInResult: MutableLiveData<CallResult> by lazy {
        MutableLiveData<CallResult>()
    }
    companion object{
        const val errorMessage = "Invalid Email or Password"
    }


    fun emailSignIn(email: String, password: String) {
        //move this check to the isValidEmailPassword method, as this is common
        if (!isValidEmailAndPassword(email, password)) {
            setCallRequestResult(CallResult(false, errorMessage))
        }else {
            viewModelScope.launch {
                try {
                    Firebase.auth.signInWithEmailAndPassword(email, password).await()
                    setCallRequestResult(CallResult(true))
                } catch (ex: Exception) {
                    setCallRequestResult(CallResult(false, ex.localizedMessage.toString()))
                }

            }
        }
    }

    private fun setCallRequestResult(callResult: CallResult) {
        userLogInResult.value = callResult
    }

    private fun isValidEmailAndPassword(emailText: String, passwordText: String): Boolean =
        !(isRequiredFieldEmpty(emailText) || isRequiredFieldEmpty(passwordText))

    private fun isRequiredFieldEmpty(text: String): Boolean =  text.isEmpty() || text.isBlank()



}