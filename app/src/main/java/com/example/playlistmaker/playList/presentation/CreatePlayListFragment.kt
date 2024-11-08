package com.example.playlistmaker.playList.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.CreatePlayListBinding
import com.example.playlistmaker.playList.presentation.playListViewModel.CreatePlayListViewModel
import com.example.playlistmaker.player.presentation.MediaPlayer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date


class CreatePlayListFragment: Fragment() {
    companion object {
        fun newInstance() = CreatePlayListFragment()
    }

    private var _binding: CreatePlayListBinding? = null
    val binding: CreatePlayListBinding
        get() = _binding!!

    private val viewModel by viewModel<CreatePlayListViewModel>()
    lateinit var confirmDialog: MaterialAlertDialogBuilder


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CreatePlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val backButton = binding.toolbarSearch
        val editNameList = binding.etNamePlayList
        val description = binding.etDescription
        val bottomCreate = binding.bCreate

        var textInputName = ""
        var textDescriptor = ""

        viewModel.getBottomState().observe(viewLifecycleOwner) { state ->
            when (state) {
                false -> {
                    bottomCreate.isEnabled = false
                }

                true -> {
                    bottomCreate.isEnabled = true
                }
            }
        }
        viewModel.checkBottom(textInputName)

        val textNameListWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                textInputName = p0.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.checkBottom(textInputName)
            }
        }
        val textDescriptionWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                textDescriptor = p0.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.checkBottom(textInputName)
            }
        }

        binding.userName.addTextChangedListener(textNameListWatcher)
        binding.description.addTextChangedListener(textDescriptionWatcher)

        val requester = PermissionRequester.instance()
        lifecycleScope.launch {
            requester.request(Manifest.permission.READ_MEDIA_IMAGES)
                .collect { result ->
                    when (result) {
                        is PermissionResult.Granted -> {}
                        is PermissionResult.Denied -> {}
                        is PermissionResult.Denied.NeedsRationale -> {
                            Toast.makeText(
                                requireContext(),
                                "Разрешение на использование изображений",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        is PermissionResult.Denied.DeniedPermanently -> {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.data = Uri.fromParts("package", context?.packageName, null)
                            context?.startActivity(intent)
                        }

                        is PermissionResult.Cancelled -> {}
                    }
                }
        }




        backButton.setNavigationOnClickListener {
            val image = binding.imagePlayList.drawable
            val drawableImage =
                ContextCompat.getDrawable(requireContext(), com.example.playlistmaker.R.drawable.image_bottom_sheet)

            if ((textInputName.isBlank()) && (textDescriptor.isBlank()) && (image.constantState == drawableImage?.constantState)) {
                parentFragmentManager.popBackStack()
            } else {
                confirmDialog.show()
            }
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("«Завершить создание плейлиста?»\n«Все несохраненные данные будут потеряны»?")
            .setNegativeButton("Отмена") { dialog, which ->
            }.setPositiveButton("Завершить") { dialog, which ->
                parentFragmentManager.popBackStack()
            }
        val formatNameImage = SimpleDateFormat("dd.yyyy hh:mm:ss")
        val currentDate = formatNameImage.format(Date())


        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(requireContext())
                        .load(uri)
                        .placeholder(R.drawable.placeholder)
                        .transform(CenterCrop(), RoundedCorners(8f.dpToPx(requireContext())))
                        .into(binding.imagePlayList)
                    binding.imagePlayList.setImageURI(uri)
                    saveImageToPrivateStorage(uri, currentDate)
                } else {
                    Log.d("PhotoPicker", "No media file")
                }
            }


        binding.imagePlayList.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            val filePath = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "myalbum"
            )
            val file = File(filePath, binding.imagePlayList.toString())
            binding.imagePlayList.setImageURI(file.toUri())
            viewModel.saveFilePath(filePath.toString().plus("/" + "$currentDate"))
        }


        binding.bCreate.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Плейлист $textInputName успешно создан!",
                Toast.LENGTH_LONG
            ).show()
            parentFragmentManager.popBackStack()
            viewModel.saveName(textInputName)
            viewModel.saveDescription(textDescriptor)
            viewModel.savePlayList()
        }

    }

    private fun saveImageToPrivateStorage(uri: Uri, name: String) {
        val contentResolver = requireActivity().applicationContext.contentResolver
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${name.toString()}")
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is MediaPlayer) {
            requireActivity().findViewById<ScrollView>(R.id.scroll).isVisible = false
            requireActivity().findViewById<LinearLayout>(R.id.standard_bottom_sheet).isVisible = false

        } else {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).isVisible = false
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (activity is MediaPlayer) {
            requireActivity().findViewById<ScrollView>(R.id.scroll).isVisible = true
        } else {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).isVisible = true
        }
    }
    fun Float.dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
        ).toInt()
    }
}





