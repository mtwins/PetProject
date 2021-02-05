package com.mheredia.petproject.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mheredia.petproject.MainActivity
import com.mheredia.petproject.R
import com.mheredia.petproject.model.CallResult
import com.mheredia.petproject.ui.Utils


class LoginActivity : AppCompatActivity() {


    private lateinit var viewModel: ViewModel

    companion object {
        fun getObserver(view: View, context: Context): Observer<CallResult> =
            Observer { result ->
                if (result.isSuccess) MainActivity.route(context)
                else Utils().sendMessage(view, result.errorMessage)

            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val login: Fragment = LoginFragment()
        startFragment(login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

    }

    private fun startFragment(fr: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_place, fr)
        fragmentTransaction.commit()
    }


}


