package com.mheredia.petproject.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.mheredia.petproject.MainActivity
import com.mheredia.petproject.R

class LoginActivity : AppCompatActivity() {
    private lateinit var password: TextInputEditText
    private lateinit var email: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val viewModel: LoginViewModel by viewModels()
        val loginButton: Button = findViewById(R.id.login_button)
        val registrationButton: Button = findViewById(R.id.registration_button)
        email = findViewById(R.id.email_edit_text)
        password = findViewById(R.id.password_edit_text)
        loginButton.setOnClickListener { view ->
            loginUser(viewModel, view)
        }
        registrationButton.setOnClickListener { view ->
            registerUser(viewModel, view)
        }
    }

    private fun registerUser(viewModel: LoginViewModel, view: View) {
        if (viewModel.isValidEmailAndPassword(getEmailText(), getPasswordText(), view)) {
            viewModel.registerUser(getEmailText(), getPasswordText(), view, this)
        } else {
            sendMessage(view, "Invalid email or password")
        }
    }

    private fun loginUser(viewModel: LoginViewModel, view: View) {
        if (viewModel.isValidEmailAndPassword(getEmailText(), getPasswordText(), view)) {
            viewModel.emailSignIn(getEmailText(), getPasswordText(), view, this)
        } else {
            sendMessage(view, "Invalid email or password")
        }
    }

    private fun sendMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    private fun getEmailText(): String {
        return email.text.toString()
    }

    private fun getPasswordText(): String {
        return password.text.toString()
    }


}


