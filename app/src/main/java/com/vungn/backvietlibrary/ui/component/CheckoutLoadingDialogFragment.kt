package com.vungn.backvietlibrary.ui.component

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.vungn.backvietlibrary.databinding.FragmentCheckoutLoadingDialogBinding
import com.vungn.backvietlibrary.util.enums.CallApiState

class CheckoutLoadingDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCheckoutLoadingDialogBinding
    internal lateinit var listener: NoticeDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = FragmentCheckoutLoadingDialogBinding.inflate(layoutInflater)
            builder.setView(binding.root).setCancelable(false).create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setApiState(callApiState: CallApiState) {
        when (callApiState) {
            CallApiState.LOADING -> {
                binding.progressBar.show()
            }

            CallApiState.SUCCESS -> {
                Toast.makeText(
                    activity, "Bạn đã thanh toán thành công.", Toast.LENGTH_LONG
                ).show()
                listener.onDialogGoHomeClick(this)
            }

            CallApiState.ERROR -> {
                Toast.makeText(
                    activity, "Thanh toán thất bại. Vui lòng thử lại.", Toast.LENGTH_LONG
                ).show()
                dialog?.cancel()
            }

            CallApiState.NONE -> {}
        }
    }

    interface NoticeDialogListener {
        fun onDialogGoHomeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() + " must implement NoticeDialogListener")
            )
        }
    }
}