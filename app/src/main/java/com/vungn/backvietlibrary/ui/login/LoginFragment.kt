package com.vungn.backvietlibrary.ui.login

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.vungn.backvietlibrary.databinding.FragmentLoginBinding
import com.vungn.backvietlibrary.ui.activity.account.AccountActivity
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.login.contract.impl.LoginViewModelImpl
import com.vungn.backvietlibrary.util.extension.hideSoftKeyboard
import com.vungn.backvietlibrary.util.helper.DialogHelper
import com.vungn.backvietlibrary.util.state.LoginState
import kotlinx.coroutines.launch


class LoginFragment : FragmentBase<FragmentLoginBinding, LoginViewModelImpl>() {
    private lateinit var callbackManager: CallbackManager
    private lateinit var dialogHelper: DialogHelper

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
            navController.popBackStack()
        }

        if (view !is EditText) {
            view?.setOnTouchListener { v, _ ->
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
        setupBackground()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect { loginState ->
                    when (loginState) {
                        LoginState.NONE -> {}
                        LoginState.LOADING -> {
                            dialogHelper = DialogHelper(requireActivity())
                            dialogHelper.startLoadingDialog()
                        }

                        LoginState.SUCCESS -> {
                            dialogHelper.dismissDialog()
                            val intent = Intent()
                            val bundle = Bundle()
                            bundle.putBoolean(AccountActivity.RESULT_BUNDLE_KEY, true)
                            intent.putExtras(bundle)
                            (requireActivity() as AuthActivity).setResult(RESULT_OK, intent)
                            (requireActivity() as AuthActivity).onBackPressedMethod()
                            requireActivity().finish()
                        }

                        LoginState.FAIL -> {
                            dialogHelper.dismissDialog()
                        }
                    }
                }
            }
        }
    }

    private fun setupBackground() {
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