package com.vungn.backvietlibrary.ui.login

import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.navigation.fragment.findNavController
import com.vungn.backvietlibrary.databinding.FragmentLoginBinding
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.login.contract.impl.LoginViewModelImpl


class LoginFragment : FragmentBase<FragmentLoginBinding, LoginViewModelImpl>() {
    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)

    override fun getViewModelClass(): Class<LoginViewModelImpl> = LoginViewModelImpl::class.java

    override fun setupListener() {
        binding.close.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun setupViews() {
        val typedValue = TypedValue()
        val theme = requireContext().theme
        theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        @ColorInt val colorPrimary = typedValue.data
        theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true)
        @ColorInt val colorBackground = typedValue.data
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(colorBackground, colorPrimary)
        )
        binding.root.background = gradient
    }
}