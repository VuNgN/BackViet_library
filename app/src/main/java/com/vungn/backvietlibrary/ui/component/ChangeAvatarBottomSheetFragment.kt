package com.vungn.backvietlibrary.ui.component

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vungn.backvietlibrary.databinding.FragmentBottomSheetChangeAvatarBinding
import com.vungn.backvietlibrary.util.picker.PathUtils
import com.vungn.backvietlibrary.util.picker.SinglePhotoPicker

class ChangeAvatarBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetChangeAvatarBinding
    private lateinit var photoPicker: SinglePhotoPicker
    private var _onPhotoPickerListener: OnPhotoPickerListener? = null
    var onPhotoPickerListener: OnPhotoPickerListener?
        get() = _onPhotoPickerListener!!
        set(value) {
            _onPhotoPickerListener = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoPicker = SinglePhotoPicker.Builder(this)
            .setListener(object : SinglePhotoPicker.OnPhotoPickerListener {
                override fun onPhotoPicked(uri: Uri?) {
                    uri?.let {
                        val path = PathUtils.getPathFromUri(requireContext(), uri)
                        onPhotoPickerListener?.onPhotoPicked(path)
                    }
                }
            }).build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetChangeAvatarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val standardBottomSheet = binding.standardBottomSheet
        val standardBottomSheetBehavior = BottomSheetBehavior.from(standardBottomSheet)
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        setupListener()
    }

    private fun setupListener() {
        binding.takePictureButton.setOnClickListener {

        }
        binding.selectFromGalleryButton.setOnClickListener {
            photoPicker.execute()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        photoPicker.unregister()
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    /**
     * OnPhotoPickerListener
     */
    interface OnPhotoPickerListener {
        fun onPhotoPicked(path: String?)
    }
}