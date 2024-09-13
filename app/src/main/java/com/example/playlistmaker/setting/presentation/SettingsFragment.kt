package com.example.playlistmaker.setting.presentation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.setting.presentation.viewModel.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private val viewModelSetting by viewModel<SettingViewModel>()

    private var _binding : FragmentSettingsBinding? = null
    val binding : FragmentSettingsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backButton = binding.toolbar
        val switchTheme = binding.switcherTheme
        val supportButton = binding.textViewSupport
        val agreementButton = binding.textViewAgreement
        val shareButton = binding.textViewShare


        switchTheme.isChecked = viewModelSetting.getTheme()
        switchTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModelSetting.editTheme(checked)
            viewModelSetting.switchTheme(checked)
        }


        backButton.setNavigationOnClickListener{
            parentFragmentManager.popBackStack()
        }

        supportButton.setOnClickListener{
            viewModelSetting.supportSend()
        }

        agreementButton.setOnClickListener{
            viewModelSetting.openTerm()
        }

        shareButton.setOnClickListener{
            viewModelSetting.shareApp()
        }
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}
