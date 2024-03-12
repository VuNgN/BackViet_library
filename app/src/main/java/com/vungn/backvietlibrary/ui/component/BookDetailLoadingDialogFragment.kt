package com.vungn.backvietlibrary.ui.component

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.vungn.backvietlibrary.databinding.FragmentBookDetailLoadingDialogBinding

class BookDetailLoadingDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentBookDetailLoadingDialogBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = FragmentBookDetailLoadingDialogBinding.inflate(layoutInflater)
            builder.setView(binding.root).setCancelable(false).create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}