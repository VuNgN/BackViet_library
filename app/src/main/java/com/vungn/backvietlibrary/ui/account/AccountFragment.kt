package com.vungn.backvietlibrary.ui.account

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.appbar.AppBarLayout
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.FragmentAccountBinding
import com.vungn.backvietlibrary.ui.account.contract.impl.AccountViewModelImpl
import com.vungn.backvietlibrary.ui.base.FragmentBase
import com.vungn.backvietlibrary.ui.component.ChangeAvatarBottomSheetFragment
import com.vungn.backvietlibrary.util.enums.CallApiState
import com.vungn.backvietlibrary.util.extension.startAlphaAnimation
import com.vungn.backvietlibrary.util.extension.startBackgroundAnimation
import com.vungn.backvietlibrary.util.extension.toAvatarUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs


@AndroidEntryPoint
class AccountFragment : FragmentBase<FragmentAccountBinding, AccountViewModelImpl>(),
    AppBarLayout.OnOffsetChangedListener {
    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true

    private lateinit var mLargeTitleContainer: LinearLayout
    private lateinit var mSmallTitleContainer: LinearLayout
    private lateinit var mAppBarLayout: AppBarLayout
    private lateinit var mToolbar: Toolbar

    @ColorInt
    private var vibrantColor: Int = Color.TRANSPARENT
    private lateinit var bottomSheetFragment: ChangeAvatarBottomSheetFragment
    override fun getViewBinding(): FragmentAccountBinding {
        return FragmentAccountBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<AccountViewModelImpl> {
        return AccountViewModelImpl::class.java
    }

    override fun setupListener() {
        binding.mainToolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }
        binding.mainButtonLogout.setOnClickListener {
            viewModel.logout(object : AccountViewModelImpl.OnLogoutListener {
                override fun onLogoutSuccess() {
                    Toast.makeText(requireContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity().finish()
                }

                override fun onLogoutFailed() {
                    Toast.makeText(
                        requireContext(),
                        "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
        binding.mainButtonChangeAvatar.setOnClickListener {
            val fragmentTag = ChangeAvatarBottomSheetFragment.TAG
            val existingFragment = childFragmentManager.findFragmentByTag(fragmentTag)

            if (existingFragment == null || !existingFragment.isAdded) {
                bottomSheetFragment.show(childFragmentManager, ChangeAvatarBottomSheetFragment.TAG)
            }
        }
        binding.mainButtonEditProfile.setOnClickListener {
            navController.navigate(R.id.action_accountFragment_to_editProfileFragment)
        }
        bottomSheetFragment.onPhotoPickerListener =
            object : ChangeAvatarBottomSheetFragment.OnPhotoPickerListener {
                override fun onPhotoPicked(path: String?) {
                    Log.d(TAG, "onPhotoPicked: $path")
                    bottomSheetFragment.dismiss()
                    if (path != null) {
                        val action =
                            AccountFragmentDirections.actionAccountFragmentToConfirmAvatarFragment(
                                path
                            )
                        navController.navigate(action)
                    }
                }
            }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.callApiState.collect { state ->
                    when (state) {
                        CallApiState.LOADING -> {
                            binding.mainButtonChangeAvatar.isEnabled = false
                            binding.mainButtonEditProfile.isEnabled = false
                            binding.mainButtonLogout.isEnabled = false
                        }

                        CallApiState.SUCCESS -> {
                            binding.mainButtonChangeAvatar.isEnabled = true
                            binding.mainButtonEditProfile.isEnabled = true
                            binding.mainButtonLogout.isEnabled = true
                            binding.mainFramelayoutProgress.visibility = View.GONE
                            binding.mainLinearlayoutLargeTitle.visibility = View.VISIBLE
                        }

                        CallApiState.ERROR -> {
                            binding.mainFramelayoutProgress.visibility = View.GONE
                            binding.mainLinearlayoutLargeTitle.visibility = View.VISIBLE
                        }

                        CallApiState.NONE -> {}
                    }
                }
            }
        }
    }

    override fun setupViews() {
        bottomSheetFragment = ChangeAvatarBottomSheetFragment()
        mToolbar = binding.mainToolbar
        mAppBarLayout = binding.mainAppbar
        mSmallTitleContainer = binding.mainLinearlayoutSmallTitle
        mLargeTitleContainer = binding.mainLinearlayoutLargeTitle
        mAppBarLayout.addOnOffsetChangedListener(this)
        mSmallTitleContainer.startAlphaAnimation(0, View.INVISIBLE)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { user ->
                    binding.mainTextviewSmallTitle.text = user?.displayName
                    binding.mainTextviewLargeTitle.text = user?.displayName
                    Glide.with(requireContext()).asBitmap().load(user?.avatar?.toAvatarUrl())
                        .circleCrop()
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap, transition: Transition<in Bitmap>?
                            ) {
                                binding.mainImageviewLargeAvatar.setImageBitmap(resource)
                                binding.mainImageviewSmallAvatar.setImageBitmap(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                binding.mainImageviewLargeAvatar.setImageDrawable(placeholder)
                                binding.mainImageviewSmallAvatar.setImageDrawable(placeholder)
                            }
                        })
                }
            }
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, offset: Int) {
        val maxScroll = appBarLayout!!.totalScrollRange
        val percentage = abs(offset).toFloat() / maxScroll.toFloat()

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                mSmallTitleContainer.startAlphaAnimation(ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                binding.mainToolbar.startBackgroundAnimation(
                    ALPHA_ANIMATIONS_DURATION, Color.TRANSPARENT, vibrantColor
                )
                mIsTheTitleVisible = true
            }
        } else {
            if (mIsTheTitleVisible) {
                mSmallTitleContainer.startAlphaAnimation(ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                binding.mainToolbar.startBackgroundAnimation(
                    ALPHA_ANIMATIONS_DURATION, vibrantColor, Color.TRANSPARENT
                )
                mIsTheTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                mLargeTitleContainer.startAlphaAnimation(ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                mIsTheTitleContainerVisible = false
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                mLargeTitleContainer.startAlphaAnimation(ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                mIsTheTitleContainerVisible = true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUser()
    }

    companion object {
        private const val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.6f
        private const val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
        private const val ALPHA_ANIMATIONS_DURATION = 200L
        private const val TAG = "AccountFragment"
    }
}