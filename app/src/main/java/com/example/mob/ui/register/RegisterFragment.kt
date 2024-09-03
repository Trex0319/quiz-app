package com.example.mob.ui.register

import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mob.core.utils.UserRole
import com.example.mob.ui.base.BaseFragment
import com.example.mob.ui.login.LoginFragmentDirections
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
                RegisterFragmentDirections.actionRegisterToLogin()
            )
        }

        binding?.run {

            val role = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.role_array,
                android.R.layout.simple_dropdown_item_1line
            )

            role.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            stRole.adapter = role

            btnRegister.setOnClickListener {

                viewModel.register(
                    name = etName.text.toString(),
                    email = etEmail.text.toString(),
                    password = etPassword.text.toString(),
                    confirmPassword = etConfirmPassword.text.toString(),
                    role = stRole.selectedItem.toString()
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
            viewModel.success.collect {role ->
                when(role) {
                    UserRole.Teacher ->findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterToDashboard()
                    )
                    UserRole.Student ->  findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                    )
                }
            }
        }
    }
}