package com.vungn.backvietlibrary.ui.activity.mybook

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.ActivityMyBookBinding
import com.vungn.backvietlibrary.network.NetworkState
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity
import com.vungn.backvietlibrary.ui.activity.auth.AuthActivity.Companion.RESULT_BUNDLE_KEY
import com.vungn.backvietlibrary.ui.activity.mybook.contract.impl.MyBookActivityViewModelImpl
import com.vungn.backvietlibrary.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyBookActivity : BaseActivity<ActivityMyBookBinding, MyBookActivityViewModelImpl>() {
    private val startLoginActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), activityResultCallback()
    )

    override fun getViewBinding(): ActivityMyBookBinding =
        ActivityMyBookBinding.inflate(layoutInflater)

    override fun getViewModelClass(): Class<MyBookActivityViewModelImpl> =
        MyBookActivityViewModelImpl::class.java

    override fun setupListener() {
    }

    override fun setupViews() {
        setupNavHost()
        setupUpdateView()
    }

    private fun setupUpdateView() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.networkState.collect {
                    if (it is NetworkState.UNAUTHORIZED) {
                        val intent = Intent(this@MyBookActivity, AuthActivity::class.java)
                        startLoginActivity.launch(intent)
                    }
                }
            }
        }
    }

    private fun setupNavHost() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navHostController = navHostFragment.navController
        navHostController.setGraph(R.navigation.my_book_navigation, intent.extras)
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
}