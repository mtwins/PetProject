package com.mheredia.petproject.ui.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mheredia.petproject.model.CallResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class RegistrationViewModel : ViewModel() {
    companion object {
        private const val invalidPasswordOrEmail = "Invalid Email or Password"
        private const val mismatchConfirmPassword = "Password and Confirm Password should match"
    }

    val user = Firebase.auth.currentUser

    private var auth = Firebase.auth
    val userRegistrationResult: MutableLiveData<CallResult> by lazy {
        MutableLiveData<CallResult>()
    }

    fun registerUser(
        email: String,
        password: String,
        confirmPassword: String,
        name: String
    ) {
        //move this to the isValidEmailPassword method , as this is common
        //Can make a seprate class for validation (objecct for register request))but this might be
        if (!isValidEmailAndPassword(email, password, confirmPassword)) {
            setUserRegistrationResult(CallResult(false, invalidPasswordOrEmail))
        } else if (!confirmPassword.equals(password)) {
            setUserRegistrationResult(CallResult(false, mismatchConfirmPassword))
        } else {
            var updatedProfile = UserProfileChangeRequest.Builder().setDisplayName(name).build()
            viewModelScope.launch {
                registerUserWithFirebase(email, password, updatedProfile)
            }
        }
    }

    private suspend fun registerUserWithFirebase(
        email: String,
        password: String,
        updatedProfile: UserProfileChangeRequest
    ) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            user?.updateProfile(updatedProfile)?.await()
            setUserRegistrationResult(CallResult(true))
        } catch (ex: Exception) {
            userRegistrationResult.value = CallResult(false, ex.localizedMessage.toString())
        }
    }

    private fun setUserRegistrationResult(result: CallResult) {
        userRegistrationResult.value = result
    }

    private fun isValidEmailAndPassword(
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean =
        !(isReqFieldEmpty(email) || isReqFieldEmpty(password) || isReqFieldEmpty(confirmPassword))

    private fun isReqFieldEmpty(text: String): Boolean = text.isEmpty() || text.isBlank()


}


