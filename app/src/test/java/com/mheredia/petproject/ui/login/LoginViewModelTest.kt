package com.mheredia.petproject.ui.login

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
//
//class LoginViewModelTest {
////    private val testCoroutineDispatcher = TestCoroutineDispatcher()
//
//    lateinit var subject: LoginViewModel
////    private val testCoroutineScope =
////        TestCoroutineScope(testCoroutineDispatch
//
//    @Before
//    fun setup(){
////        FirebaseApp.initializeApp(this.)
//        subject= LoginViewModel()
//    }
//    @Test
//    fun testEmailSignInInvalidPassword(){
//        subject.emailSignIn("test@test.com","")
//        assertEquals("", subject.userLogInResult.value?.errorMessage)
//    }
//
//}