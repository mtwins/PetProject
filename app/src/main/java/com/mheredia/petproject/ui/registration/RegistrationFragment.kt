package com.mheredia.petproject.ui.registration

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.mheredia.petproject.R
import com.mheredia.petproject.ui.login.LoginActivity

class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }
    private lateinit var email: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var password: EditText
    private lateinit var registrationButton: Button
    private lateinit var viewModel: RegistrationViewModel
    private fun getEmailText(): String = email.text.toString()
    private fun getPasswordText(): String = password.text.toString()
    private fun getConfirmPasswordText(): String = confirmPassword.text.toString()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        val observer = LoginActivity.getObserver(requireView(), requireContext())
        viewModel.userRegistrationResult.observe(viewLifecycleOwner, observer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationButton = view.findViewById(R.id.register_user_button)
        password = view.findViewById(R.id.registration_password_edit_text)
        confirmPassword = view.findViewById(R.id.registration_confirm_password_edit_text)
        email = view.findViewById(R.id.registration_email_edit_text)
        registrationButton.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        viewModel.registerUser(getEmailText(), getPasswordText(), getConfirmPasswordText())
    }

}