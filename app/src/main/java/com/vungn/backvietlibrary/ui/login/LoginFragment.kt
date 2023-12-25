package com.vungn.backvietlibrary.ui.login

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.util.TypedValue
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.vungn.backvietlibrary.databinding.FragmentLoginBinding
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.login.contract.impl.LoginViewModelImpl
import com.vungn.backvietlibrary.util.hideSoftKeyboard
import kotlinx.coroutines.launch


class LoginFragment : FragmentBase<FragmentLoginBinding, LoginViewModelImpl>() {
    private lateinit var callbackManager: CallbackManager
    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)

    override fun getViewModelClass(): Class<LoginViewModelImpl> = LoginViewModelImpl::class.java

    override fun setupListener() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.d("Success", "Login")
                    Log.d(TAG, "onSuccess: access token -> ${result.accessToken}")
                    Log.d(TAG, "onSuccess: auth token -> ${result.authenticationToken}")
                }

                override fun onCancel() {
                    Log.d(TAG, "onCancel: ")
                }

                override fun onError(error: FacebookException) {
                    Log.e(TAG, "onError: ")
                }
            })

        binding.close.setOnClickListener {
            findNavController().popBackStack()
        }

        if (view !is EditText) {
            view?.setOnTouchListener { v, event ->
                v.performClick()
                requireActivity().hideSoftKeyboard()
                false
            }
        }

        binding.textFieldUsername.editText?.doOnTextChanged { text, _, _, _ ->
            lifecycleScope.launch {
                viewModel.username.emit(text.toString())
            }
        }

        binding.textFieldPassword.editText?.doOnTextChanged { text, _, _, _ ->
            lifecycleScope.launch {
                viewModel.password.emit(text.toString())
            }
        }

        binding.loginButton.setOnClickListener {
            viewModel.loginWithUsernameAndPassword()
        }
//        binding.facebook.setOnClickListener {
//            LoginManager.getInstance().logInWithReadPermissions(requireActivity(), listOf("email"))
//        }
    }

    override fun setupViews() {
        val typedValue = TypedValue()
        val theme = requireContext().theme
        theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        @ColorInt val colorPrimary = typedValue.data
        theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true)
        @ColorInt val colorBackground = typedValue.data
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(colorBackground, colorPrimary)
        )
        binding.root.background = gradient
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}