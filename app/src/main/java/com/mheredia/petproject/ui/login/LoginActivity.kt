package com.mheredia.petproject.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.mheredia.petproject.MainActivity
import com.mheredia.petproject.R
import com.mheredia.petproject.model.CallResult
import com.mheredia.petproject.ui.Utils


class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var password: TextInputEditText
    private lateinit var email: TextInputEditText
    private fun getEmailText(): String = email.text.toString()
    private fun getPasswordText(): String = password.text.toString()

    private fun getLoginObserver(view: View): Observer<CallResult> =
        Observer { result ->
            if (result.isSuccess) MainActivity.route(this)
            else Utils().sendMessage(view, result.errorMessage)

        }

    private fun getRegistrationObserver(view: View): Observer<CallResult> =
        Observer { result ->
            if (result.isSuccess) MainActivity.route(this)
            else Utils().sendMessage(view, result.errorMessage)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val loginButton: Button = findViewById(R.id.login_button)
        val registrationButton: Button = findViewById(R.id.registration_button)
        email = findViewById(R.id.email_edit_text)
        password = findViewById(R.id.password_edit_text)
        loginButton.setOnClickListener { view -> loginUser(view) }
        registrationButton.setOnClickListener { view ->
            registerUser(view)
//            val fm: FragmentManager = supportFragmentManager
//            val editNameDialogFragment =
//                EditNameDialogFragment.newInstance()
//            editNameDialogFragment.show(fm, "fragment_register")
        }
    }

    private fun registerUser(view: View) {
        val observer = getRegistrationObserver(view)
        viewModel.userRegistrationResult.observe(this, observer)
        viewModel.registerUser(getEmailText(), getPasswordText())
        viewModel.userRegistrationResult.removeObserver(observer)

    }

    private fun loginUser(view: View) {
        val observer = getLoginObserver(view)
        viewModel.userLogInResult.observe(this, observer)
        viewModel.emailSignIn(getEmailText(), getPasswordText())
        viewModel.userLogInResult.removeObserver(observer)
    }
}


