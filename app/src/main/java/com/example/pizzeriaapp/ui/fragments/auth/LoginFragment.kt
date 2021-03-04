package com.example.pizzeriaapp.ui.fragments.auth

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.databinding.LoginFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.ui.fragments.sharedfood.NamedFragment
import com.example.pizzeriaapp.utils.views.gone
import com.example.pizzeriaapp.utils.views.show
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

class LoginFragment(
    private val navigationCallback: () -> Unit
) : BaseFragment(R.layout.login_fragment), NamedFragment {

    private lateinit var binding: LoginFragmentBinding

    override fun getName(): String {
        return "Logowanie"
    }

    private val email: String
        get() = binding.etEmailLogin.text.toString()

    private val password: String
        get() = binding.etPasswordLogin.text.toString()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.setLoginChannel()
        binding = LoginFragmentBinding.bind(view)
        binding.btnLogin.setOnClickListener { validateLogin() }
        observeLoginRequest()
    }

    private fun observeLoginRequest() {
        lifecycleScope.launchWhenStarted {
            authViewModel.loginRequestState.consumeAsFlow().collect {
                when (it) {
                    is Resource.Loading -> showLoading()
                    is Resource.Success -> {
                        hideLoading()
                        mainActivity.shortSnackbar("Zalogowano")
                        navigationCallback()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        dispatchError(it)
                    }
                }
            }
        }
    }

    private fun dispatchError(resp: Resource.Error) {
        if (resp.error !== null) {
            if (resp.error.contains("Not authorized")) {
                mainActivity.shortSnackbar("Wprowadzono niepoprawne dane")
            }
        } else {
            mainActivity.shortSnackbar("Coś poszło nie tak")
        }
    }

    private fun showLoading() {
        binding.pbLogin.show()
    }

    private fun hideLoading() {
        binding.pbLogin.gone()
    }

    private fun validateLogin() {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            authViewModel.login(email, password)
        } else {
            mainActivity.shortSnackbar("Wprowadź dane")
        }
    }
}