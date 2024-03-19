package com.base.hilt.bottom_sheets

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.base.hilt.BuildConfig
import com.base.hilt.R
import com.base.hilt.databinding.DialogUploadImgSelectionBinding
import com.base.hilt.utils.DebugLog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File

class ProfileImageBottomSheetDialog(
    private val listener: ItemClickListener,
    private val isDeleteVisible: Boolean
) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogUploadImgSelectionBinding
    private var latestTmpUri: Uri? = null
    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_upload_img_selection,
            container,
            false
        )
        val view = binding.root
        when {
            isDeleteVisible -> binding.txtDelete.visibility = View.VISIBLE
            else -> binding.txtDelete.visibility = View.GONE
        }

        binding.txtCamera.setOnClickListener {
            //dismiss()
            lifecycleScope.launchWhenCreated {
                generateCameraUri().let { uri ->
                    latestTmpUri = uri
                    takeImageResult.launch(uri)
                }
            }
        }
        binding.txtGallery.setOnClickListener {
            //dismiss()
            galleryImageResult.launch("image/*")
        }

        binding.txtDelete.setOnClickListener {
            dismiss()
            listener.itemClick("", true)
        }

        binding.txtCancel1.setOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            behavior = BottomSheetBehavior.from(sheet!!)
            behavior.isHideable = false
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            sheet.setBackgroundResource(android.R.color.transparent)
        }

        return dialog
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    DebugLog.d("System out Image path:$uri")
                    dismiss()
                    listener.itemClick(getFileFromUri(latestTmpUri!!).absolutePath, false)
                }
            }
        }

    private val galleryImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                DebugLog.d("System out Image Uri:$uri")
                dismiss()
                listener.itemClick(getFileFromUri(uri).absolutePath, false)
            }
        }

    interface ItemClickListener {
        fun itemClick(imagePath: String, isDeleteImage: Boolean)
    }

    private fun getFileFromUri(uri: Uri): File {
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file =
            File.createTempFile("Img_", ".png", storageDir)
        file.outputStream().use {
            requireContext().contentResolver.openInputStream(uri)?.copyTo(it)
        }

        return file
    }

    private fun generateCameraUri(): Uri {
        val tmpFile = getTempFile()
        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun getTempFile(): File {
        val fileName = "Img_" + System.currentTimeMillis().toString()
        // Create an image file name
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            fileName, /// prefix /
            ".png", // suffix /
            storageDir // directory /
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
//            actualFilePath = absolutePath
        }
    }
}