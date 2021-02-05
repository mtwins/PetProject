package com.mheredia.petproject.ui.login

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.mheredia.petproject.MainActivity
import com.mheredia.petproject.R
import com.mheredia.petproject.model.CallResult
import com.mheredia.petproject.ui.Utils
import com.mheredia.petproject.ui.registration.RegistrationFragment

class LoginFragment() : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var password: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var registrationButton: Button
    private fun getEmailText(): String = email.text.toString()
    private fun getPasswordText(): String = password.text.toString()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginButton = view.findViewById(R.id.login_button)
        registrationButton = view.findViewById(R.id.registration_button)
        password = view.findViewById(R.id.password_edit_text)
        email = view.findViewById(R.id.email_edit_text)
        loginButton.setOnClickListener { view -> loginUser(view) }
        registrationButton.setOnClickListener { startFragment(RegistrationFragment.newInstance()) }
    }

    private fun startFragment(registrationFragment: Fragment) {
        getActivity()?.getSupportFragmentManager()?.beginTransaction()
            ?.replace(R.id.fragment_place, registrationFragment)
            ?.addToBackStack(null)
            ?.commit();
    }

    private fun loginUser(view: View) {
        val observer = LoginActivity.getObserver(view, requireContext())
        viewModel.userLogInResult.observe(viewLifecycleOwner, observer)
        viewModel.emailSignIn(getEmailText(), getPasswordText())
        viewModel.userLogInResult.removeObserver(observer)
    }

}