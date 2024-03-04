package com.vungn.backvietlibrary.ui.activity.search

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.vungn.backvietlibrary.R
import com.vungn.backvietlibrary.databinding.ActivitySearchBinding
import com.vungn.backvietlibrary.ui.activity.search.adapter.RecycleViewAdapter
import com.vungn.backvietlibrary.util.extension.focus

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUi()
        setupListener()
    }

    private fun setupUi() {
        val adapter = RecycleViewAdapter(this)
//        adapter.data = bookResponses
        binding.apply {
            editText.focus()
            recycleView.adapter = adapter
            recycleView.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupListener() {
        binding.apply {
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.cancel -> {
                        finish()
                    }

                    else -> {}
                }
                true
            }
        }
    }
}