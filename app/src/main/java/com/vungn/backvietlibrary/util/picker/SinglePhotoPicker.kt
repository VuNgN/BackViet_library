package com.vungn.backvietlibrary.util.picker

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class SinglePhotoPicker(builder: Builder) {
    /**
     * Pick visual media
     */
    private val pickVisualMedia =
        when (builder.type) {
            Builder.Type.ACTIVITY -> builder.activity.registerForActivityResult(
                ActivityResultContracts.PickVisualMedia(),
                builder.listener::onPhotoPicked
            )

            Builder.Type.FRAGMENT -> builder.fragment.registerForActivityResult(
                ActivityResultContracts.PickVisualMedia(),
                builder.listener::onPhotoPicked
            )
        }

    /**
     * Start activity for result
     */
    private val startActivityResult =
        when (builder.type) {
            Builder.Type.ACTIVITY -> builder.activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == AppCompatActivity.RESULT_OK) {
                    val uri = it.data?.data
                    builder.listener.onPhotoPicked(uri)
                }
            }

            Builder.Type.FRAGMENT -> builder.fragment.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == AppCompatActivity.RESULT_OK) {
                    val uri = it.data?.data
                    builder.listener.onPhotoPicked(uri)
                }
            }
        }

    /**
     * Request permission
     */
    private val requestPermission = when (builder.type) {
        Builder.Type.ACTIVITY -> builder.activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                pickPhoto()
            }
        }

        Builder.Type.FRAGMENT -> builder.fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                pickPhoto()
            }
        }
    }

    /**
     * Execute
     */
    fun execute() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
            pickPhoto()
        } else {
            requestPermission()
        }
    }

    /**
     * Pick a photo from the gallery
     */
    private fun pickPhoto() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            startActivityResult.launch(intent)
            return
        } else {
            pickVisualMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    /**
     * Unregister
     */
    fun unregister() {
        pickVisualMedia.unregister()
        startActivityResult.unregister()
    }

    private fun requestPermission() {
        requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    /**
     * Builder class
     */
    class Builder {
        lateinit var activity: AppCompatActivity
        lateinit var fragment: Fragment
        var type: Type

        constructor(activity: AppCompatActivity) {
            this.activity = activity
            type = Type.ACTIVITY
        }

        constructor(fragment: Fragment) {
            this.fragment = fragment
            type = Type.FRAGMENT
        }

        private lateinit var _listener: OnPhotoPickerListener
        val listener: OnPhotoPickerListener
            get() = _listener

        fun setListener(listener: OnPhotoPickerListener) = apply {
            _listener = listener
        }

        fun build() = SinglePhotoPicker(this)

        enum class Type {
            FRAGMENT, ACTIVITY
        }
    }

    /**
     * On photo picker listener
     */
    interface OnPhotoPickerListener {
        fun onPhotoPicked(uri: Uri?)
    }
}