package com.reham11203.todoapp.ui.home.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import com.reham11203.todoapp.R
import com.reham11203.todoapp.databinding.FragmentSettingsBinding
import com.reham11203.todoapp.ui.util.Constants
import com.reham11203.todoapp.ui.util.applyModeChange

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireContext().getSharedPreferences(Constants.MODE_PREF, Context.MODE_PRIVATE)
        setLanguageDropDownMenuListener()
        setModeDropDownMenuListener()

    }

    private fun initializeUI() {
        setInitialLanguageState()
        setInitialModeState()
    }

    override fun onStart() {
        super.onStart()
        initializeUI()
        setModeDropDownMenu()
        setLanguageDropDownMenu()

    }

    private fun setModeDropDownMenu() {
        val modes = resources.getStringArray(R.array.Modes).toList()
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, modes)
        binding.mode.setAdapter(adapter)
    }

    private fun setLanguageDropDownMenu() {
        val languages = resources.getStringArray(R.array.Languages).toList()
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages)
        binding.language.setAdapter(adapter)
    }

    private fun setModeDropDownMenuListener() {
        binding.mode.setOnItemClickListener { _, _, position, _ ->
            val selectedMode = binding.mode.adapter.getItem(position).toString()
            binding.mode.setText(selectedMode)
            val isDark = selectedMode == getString(R.string.dark)
            applyModeChange(isDark)
            savaModeToSharedPref(isDark)
        }
    }

    private fun savaModeToSharedPref(isDark: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(Constants.MODE_KEY, isDark)
            apply()
        }
    }


    private fun setLanguageDropDownMenuListener() {
        binding.language.setOnItemClickListener { _, _, position, _ ->
            val selectedLanguage = binding.language.adapter.getItem(position).toString()
            binding.language.setText(selectedLanguage)
            val languageCode = when (selectedLanguage) {
                getString(R.string.english) -> Constants.ENGLISH_CODE
                getString(R.string.arabic) -> Constants.ARABIC_CODE
                else -> Constants.ENGLISH_CODE
            }
            applyLanguageChange(languageCode)
        }
    }

    private fun applyLanguageChange(languageCode: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
    }

    private fun setInitialModeState() {
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        val mode = when (currentMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> R.string.dark
            else -> R.string.light

        }
        binding.mode.setText(mode)
    }

    private fun setInitialLanguageState() {
        val currentLanguage = AppCompatDelegate.getApplicationLocales()[0]?.language
            ?: resources.configuration.locales[0].language
        val language = when (currentLanguage) {
            Constants.ENGLISH_CODE -> R.string.english
            Constants.ARABIC_CODE -> R.string.arabic
            else -> R.string.english
        }
        binding.language.setText(language)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}