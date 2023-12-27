package com.vungn.backvietlibrary.util.helper

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.vungn.backvietlibrary.R

class DialogHelper constructor(private val activity: Activity) {
    private lateinit var loadingDialog: AlertDialog
    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val layoutInflater = activity.layoutInflater
        builder.setView(layoutInflater.inflate(R.layout.dialog_loading, null))
        builder.setCancelable(true)

        loadingDialog = builder.create()
        loadingDialog.show()
    }

    fun dismissDialog() {
        loadingDialog.dismiss()
    }
}