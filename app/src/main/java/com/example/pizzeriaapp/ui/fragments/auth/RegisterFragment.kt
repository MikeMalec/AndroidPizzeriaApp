package com.example.pizzeriaapp.ui.fragments.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pizzeriaapp.R
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.databinding.RegisterFragmentBinding
import com.example.pizzeriaapp.ui.common.BaseFragment
import com.example.pizzeriaapp.ui.fragments.sharedfood.NamedFragment
import com.example.pizzeriaapp.utils.views.gone
import com.example.pizzeriaapp.utils.views.show
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

class RegisterFragment(
    private val navigationCallback: () -> Unit
) :
    BaseFragment(R.layout.register_fragment), NamedFragment {

    private lateinit var binding: RegisterFragmentBinding

    override fun getName(): String {
        return "Rejestracja"
    }

    private val email: String
        get() = binding.etEmailRegister.text.toString()

    private val password: String
        get() = binding.etPasswordRegister.text.toString()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.setRegisterChannel()
        binding = RegisterFragmentBinding.bind(view)
        binding.btnRegister.setOnClickListener { validateRegistration() }
        observeRegisterRequest()
    }

    private fun observeRegisterRequest() {
        lifecycleScope.launchWhenStarted {
            authViewModel.registerRequestState.consumeAsFlow().collect {
                when (it) {
                    is Resource.Loading -> showLoading()
                    is Resource.Success -> {
                        hideLoading()
                        mainActivity.shortSnackbar("Rejestracja powiodła się")
                        navigationCallback()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        if (it.error !== null) {
                            dispatchError(it.error)
                        } else {
                            mainActivity.shortSnackbar("Coś poszło nie tak")
                        }
                    }
                }
            }
        }
    }

    private fun dispatchError(error: String) {
        if (error.contains("Email address already in use!")) {
            mainActivity.shortSnackbar("Address email już jest w użyciu!")
        }
        if (error.contains("Use real email")) {
            mainActivity.shortSnackbar("Użyj poprawnego emaila")
        }
    }

    private fun showLoading() {
        binding.pbRegister.show()
    }

    private fun hideLoading() {
        binding.pbRegister.gone()
    }

    private fun validateRegistration() {
        var canRegister = true
        when (email.isEmpty()) {
            true -> {
                canRegister = false
                binding.tilEmailRegister.error = "Wypełnij Pole"
            }
            false -> binding.tilEmailRegister.error = null
        }

        when (password.isEmpty()) {
            true -> {
                canRegister = false
                binding.tilPasswordRegister.error = "Wypełnij Pole"
            }
            false -> {
                if (password.length >= 6) {
                    binding.tilPasswordRegister.error = null
                } else {
                    canRegister = false
                    binding.tilPasswordRegister.error = "Użyj przynajmniej 6 znaków"
                }
            }
        }
        if (canRegister) {
            authViewModel.register(email, password)
        }
    }
}