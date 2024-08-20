package com.example.mob.ui.register

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mob.ui.base.BaseFragment
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override val viewModel: RegisterViewModel by viewModels()
    override fun getLayoutResource() =  R.layout.fragment_register

    override fun onBindView(view: View) {
        super.onBindView(view)
        binding?.btnLogin?.setOnClickListener {
            findNavController().navigate(
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            )
        }

        binding?.run {
            btnRegister.setOnClickListener {
                viewModel.register(
                    name = etName.text.toString(),
                    email = etEmail.text.toString(),
                    password = etPassword.text.toString(),
                    confirmPassword = etConfirmPassword.text.toString()
                )
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { errorMessage ->
                errorMessage?.let {
                    Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.success.collect {
                findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                )
            }
        }
    }
}