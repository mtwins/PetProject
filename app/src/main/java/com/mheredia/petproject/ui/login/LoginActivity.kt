package com.mheredia.petproject.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.mheredia.petproject.R
import com.mheredia.petproject.ui.Utils


class LoginActivity : AppCompatActivity() {
    private lateinit var password: TextInputEditText
    private lateinit var email: TextInputEditText
    private fun getEmailText(): String = email.text.toString()
    private fun getPasswordText(): String = password.text.toString()
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val loginButton: Button = findViewById(R.id.login_button)
        val registrationButton: Button = findViewById(R.id.registration_button)
        email = findViewById(R.id.email_edit_text)
        password = findViewById(R.id.password_edit_text)
        loginButton.setOnClickListener { view -> loginUser(view) }
        registrationButton.setOnClickListener { view -> registerUser(view)
//            val fm: FragmentManager = supportFragmentManager
//            val editNameDialogFragment =
//                EditNameDialogFragment.newInstance()
//            editNameDialogFragment.show(fm, "fragment_register")
        }
    }

    private fun registerUser(view: View) {
        if (viewModel.isValidEmailAndPassword(getEmailText(), getPasswordText(), view)) {
            viewModel.registerUser(getEmailText(), getPasswordText(), view, this)
        } else {
            Utils().sendSnackbarMessage(view, "Invalid email or password")
        }
    }

    private fun loginUser(view: View) {
        if (viewModel.isValidEmailAndPassword(getEmailText(), getPasswordText(), view)) {
            viewModel.emailSignIn(getEmailText(), getPasswordText(), view, this)
        } else {
            Utils().sendSnackbarMessage(view, "Invalid email or password")
        }
    }
}


