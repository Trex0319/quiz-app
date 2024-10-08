package com.example.mob.ui.login

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mob.core.utils.UserRole
import com.example.mob.ui.base.BaseFragment
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun getLayoutResource() = R.layout.fragment_login

    override fun onBindView(view: View) {
        super.onBindView(view)

        binding?.btnRegister?.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginToRegister()
            )
        }

        binding?.btnLogin?.setOnClickListener {
            val email = binding?.etEmail?.text.toString()
            val password = binding?.etPassword?.text.toString()
            viewModel.login(email, password)
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        lifecycleScope.launch {
            viewModel.success.collect {role ->
                when(role) {
                    UserRole.Teacher ->findNavController().navigate(
                        LoginFragmentDirections.actionLoginToDashboard()
                    )
                    UserRole.Student ->  findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                    )
                }
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    binding?.loadingOverlay?.isVisible = true
                } else {
                    binding?.loadingOverlay?.isVisible = false
                }
            }
        }
    }
}