package com.vungn.backvietlibrary.ui.activity.cart

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.ActivityCartBinding
import com.vungn.backvietlibrary.db.entity.BookEntity
import com.vungn.backvietlibrary.network.NetworkState
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity.Companion.RESULT_BUNDLE_KEY
import com.vungn.backvietlibrary.ui.activity.cart.contract.impl.CartViewModelImpl
import com.vungn.backvietlibrary.ui.base.BaseActivity
import com.vungn.backvietlibrary.ui.borrow.BorrowFragmentDirections
import com.vungn.backvietlibrary.ui.component.CheckoutLoadingDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityCartBinding, CartViewModelImpl>(),
    CheckoutLoadingDialogFragment.NoticeDialogListener {
    private val startLoginActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        activityResultCallback()
    )

    override fun getViewBinding(): ActivityCartBinding = ActivityCartBinding.inflate(layoutInflater)

    override fun getViewModelClass(): Class<CartViewModelImpl> = CartViewModelImpl::class.java

    override fun setupListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.networkState.collect {
                    if (it is NetworkState.UNAUTHORIZED) {
                        val intent = Intent(this@CartActivity, AuthActivity::class.java)
                        startLoginActivity.launch(intent)
                    }
                }
            }
        }
    }

    override fun setupViews() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navHostController = navHostFragment.navController
        navHostController.setGraph(R.navigation.cart_navigation, intent.extras)
        val bundle = intent.extras
        val startDestination = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getParcelable(START_DESTINATION_BUNDLE_KEY, StartDestination::class.java)
                ?: StartDestination.CART
        } else {
            bundle?.getSerializable(START_DESTINATION_BUNDLE_KEY) as? StartDestination
                ?: StartDestination.CART
        }
        when (startDestination) {
            StartDestination.CART -> {}
            StartDestination.ADD_TO_CART -> {
                val bookEntity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle?.getParcelable(BOOK_BUNDLE_KEY, BookEntity::class.java)
                } else {
                    bundle?.getParcelable(BOOK_BUNDLE_KEY)
                }
                if (bookEntity == null) {
                    finish()
                    return
                }
                navHostController.navigate(
                    BorrowFragmentDirections.actionBorrowFragmentToAddToCartFragment(
                        bookEntity
                    )
                )
            }
        }
    }

    private fun activityResultCallback() = ActivityResultCallback<ActivityResult> {
        if (it.resultCode == RESULT_OK) {
            val bundle = it.data?.extras
            val isLoginSuccess = bundle?.getBoolean(RESULT_BUNDLE_KEY, false)
            if (isLoginSuccess != true) {
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.startWorker(this)
    }

    @Parcelize
    enum class StartDestination : Parcelable {
        CART,
        ADD_TO_CART
    }

    override fun onDialogGoHomeClick(dialog: DialogFragment) {
        lifecycleScope.launch(Dispatchers.IO) {
            delay(300)
            finish()
        }
    }

    companion object {
        const val START_DESTINATION_BUNDLE_KEY = "start_destination_bundle_key"
        const val BOOK_BUNDLE_KEY = "book_bundle_key"
    }
}