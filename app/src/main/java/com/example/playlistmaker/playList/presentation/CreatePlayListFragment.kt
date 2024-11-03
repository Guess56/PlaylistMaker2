package com.example.playlistmaker.playList.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.CreatePlayListBinding
import com.example.playlistmaker.playList.domain.db.model.PlayList
import com.example.playlistmaker.playList.presentation.playListViewModel.CreatePlayListViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.TrackAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.flow.subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


class CreatePlayListFragment: Fragment() {
    companion object {
        fun newInstance() = CreatePlayListFragment()
    }
    private var _binding: CreatePlayListBinding? = null
    val binding: CreatePlayListBinding
        get() = _binding!!

    private val viewModel by viewModel<CreatePlayListViewModel>()
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

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

        viewModel.getBottomState().observe(viewLifecycleOwner) {state ->
            when(state) {
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
        




        backButton.setNavigationOnClickListener {
            val image = binding.imagePlayList.drawable
            val drawableImage =ContextCompat.getDrawable(requireContext(),R.drawable.image_bottom_sheet )

            if ((textInputName.isBlank()) && (textDescriptor.isBlank()) && (image.constantState == drawableImage?.constantState) )   {
                parentFragmentManager.popBackStack()
            } else {
                confirmDialog.show()
            }
            //parentFragmentManager.popBackStack()
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("«Завершить создание плейлиста?»\n«Все несохраненные данные будут потеряны»?")
            .setNegativeButton("Отмена") { dialog, which ->
            }.setPositiveButton("Завершить") { dialog, which ->
                //savePlayList()
                parentFragmentManager.popBackStack()
            }



        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null){
                binding.imagePlayList.setImageURI(uri)
                saveImageToPrivateStorage(uri)
            } else {
                Log.d("PhotoPicker","No media file")
            }
        }

        binding.imagePlayList.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
            val file = File(filePath,"first_cover.jpg")
            binding.imagePlayList.setImageURI(file.toUri())
            viewModel.saveFilePath(filePath.toString())
        }


        binding.bCreate.setOnClickListener {
            Toast.makeText(requireContext(), "Плейлист $textInputName успешно создан!", Toast.LENGTH_LONG).show()
           parentFragmentManager.popBackStack()
            viewModel.saveName(textInputName)
            viewModel.saveDescription(textDescriptor)
            viewModel.savePlayList()
        }

    }
    private fun saveImageToPrivateStorage(uri: Uri) {
        val contentResolver = requireActivity().applicationContext.contentResolver
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val file = File(filePath, "first_cover.jpg")
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }
}




