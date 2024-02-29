package com.vungn.backvietlibrary.ui.activity.main

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.ActivityMainBinding
import com.vungn.backvietlibrary.ui.activity.account.AccountActivity
import com.vungn.backvietlibrary.ui.activity.main.contract.MainActivityViewModel
import com.vungn.backvietlibrary.ui.activity.main.contract.impl.MainActivityViewModelImpl
import com.vungn.backvietlibrary.ui.activity.search.SearchActivity
import com.vungn.backvietlibrary.util.data.Category
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private val viewModel: MainActivityViewModel by viewModels<MainActivityViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupUI()
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.account -> {
                        val intent = Intent(this@MainActivity, AccountActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.item_search -> {
                        val intent = Intent(this@MainActivity, SearchActivity::class.java)
                        startActivity(intent)
                    }

                    else -> {}
                }
                true
            }
        }
    }

    private fun setupUI() {
        setupTopBar()
        setupBottomBar()
        repeat(Category.entries.size) {
            binding.tabLayout.addTab(
                binding.tabLayout.newTab().setIcon(Category.entries[it].resource)
                    .setText(Category.entries[it].title)
            )
        }
    }

    private fun setupBottomBar() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationBar: BottomNavigationView = binding.bottomNavigation
        bottomNavigationBar.setupWithNavController(navController)
        bottomNavigationBar.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(bottom = 0)
            insets
        }
    }

    private fun setupTopBar() {
        lifecycleScope.launch {
            viewModel.avatar.collect { url ->
                if (url == null) {
                    binding.toolbar.menu.getItem(1).setIcon(R.drawable.round_account_circle_24)
                    return@collect
                }
                Glide.with(this@MainActivity).asDrawable().load(url).circleCrop()
                    .into(object :
                        CustomTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            binding.toolbar.menu.getItem(1).setIcon(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            binding.toolbar.menu.getItem(1).setIcon(placeholder)
                        }
                    })
            }
        }
    }

    fun setTopBarTitle(title: String) {
        binding.toolbar.title = title
    }

    fun setupTabLayout(setupListener: (TabLayout) -> Unit) {
        setupListener(binding.tabLayout)
    }

    fun showTabLayout() {
        binding.tabLayout.visibility = View.VISIBLE
    }

    fun hideTabLayout() {
        binding.tabLayout.visibility = View.GONE
    }
}