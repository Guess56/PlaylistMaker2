package com.example.playlistmaker.playList.presentation.playListViewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgument
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.RedactorPlayListBinding
import com.example.playlistmaker.databinding.TabMediaFragmentBinding
import com.example.playlistmaker.playList.data.db.entity.PlayListEntity
import com.example.playlistmaker.playList.presentation.PlaylistInfoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class RedactorPlayListFragment: Fragment() {

    companion object {
        const val PLAYLIST_ID = "PLAYLISTID"
        fun newInstance(playListId: Int) = RedactorPlayListFragment().apply {
            arguments = bundleOf(PLAYLIST_ID to playListId)
        }
    }

    private var _binding: RedactorPlayListBinding? = null
    private val viewModel by viewModel<PlayListInfoViewModel>()
    private val viewModelCreate by viewModel<CreatePlayListViewModel>()
    var textInputName = ""
    var textDescriptor = ""
    var filePathImage=""


    val binding: RedactorPlayListBinding
        get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = RedactorPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var playList: PlayListEntity
        val playListId = requireArguments().getInt(PLAYLIST_ID)

        viewModel.getPlayList(playListId)
        viewModelCreate.checkBottom(binding.userName.toString())

        viewModel.getPlayListState().observe(viewLifecycleOwner){ state ->
            when(state){
                is PlayListIdState.Content -> {
                    playList = state.data
                    viewModel.setPlayList(playList)
                    binding.userName.setText(playList.namePlayList)
                    binding.description.setText(playList.description)


                    Glide.with(requireContext())
                        .load(playList.filePath)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.imagePlayList)
                }

                is PlayListIdState.Error -> Log.d("Sprint 23", "Нет данных")
            }
        }

        viewModelCreate.getBottomState().observe(viewLifecycleOwner){state ->
            when (state) {
                false -> {
                    binding.bCreate.isEnabled = false
                }

                true -> {
                    binding.bCreate.isEnabled = true
                }
            }
        }
        val textNameListWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                textInputName = p0.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                viewModelCreate.checkBottom(textInputName)
            }
        }
        val textDescriptionWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                textDescriptor = p0.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                viewModelCreate.checkBottom(textInputName)
            }
        }

        binding.userName.addTextChangedListener(textNameListWatcher)
        binding.description.addTextChangedListener(textDescriptionWatcher)



        binding.toolbarSearch.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.bCreate.setOnClickListener {
           val itemPlayList = viewModel.sharePlayList(playListId)
            viewModel.updatePlayList(itemPlayList,textInputName,textDescriptor,filePathImage)
            findNavController().navigate(R.id.action_redactorPlayListFragment_to_mediaFragment)
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
            filePathImage = filePath.toString().plus("/" + "$currentDate")
            viewModelCreate.saveFilePath(filePath.toString().plus("/" + "$currentDate"))
        }
    }
    fun Float.dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
        ).toInt()
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
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).isVisible = false
    }
}