package com.reham11203.todoapp.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.reham11203.todoapp.R
import com.reham11203.todoapp.databinding.ActivityHomeBinding
import com.reham11203.todoapp.ui.home.fragments.AddTaskFragment
import com.reham11203.todoapp.ui.home.fragments.SettingsFragment
import com.reham11203.todoapp.ui.home.fragments.tasks_fragment.TasksFragment
import com.reham11203.todoapp.ui.util.Constants

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var taskFragment: TasksFragment? = null
    private var currentFragmentTag: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        taskFragment =
            supportFragmentManager.findFragmentByTag(Constants.TASKS_FRAGMENT_TAG) as? TasksFragment
            ?: TasksFragment()
        setNavigation()
        setOnFabClick()
        if (savedInstanceState != null) {
            currentFragmentTag = savedInstanceState.getString(Constants.CURRENT_FRAGMENT_TAG)
            currentFragmentTag.let {
                val fragment = supportFragmentManager.findFragmentByTag(currentFragmentTag)
                fragment.let {
                    showFragment(fragment!!, currentFragmentTag!!)
                }
            }
        } else {
            binding.bottomNavView.selectedItemId = R.id.tasks
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.CURRENT_FRAGMENT_TAG, currentFragmentTag)
    }
    private fun setOnFabClick() {
        binding.fab.setOnClickListener {
            val bottomSheet = AddTaskFragment()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            bottomSheet.onTaskAdded = AddTaskFragment.OnTaskAdded { task ->
                // reload data in a recycler view at Task Fragment
                taskFragment?.loadAllTasksOfDate(task.date)

            }
        }
    }


    private fun setNavigation() {
        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.tasks -> {
                    showFragment(taskFragment!!, Constants.TASKS_FRAGMENT_TAG)
                    binding.appBarTitle.text = getString(R.string.app_name)
                }

                R.id.settings -> {
                    showFragment(SettingsFragment(), Constants.SETTINGS_FRAGMENT_TAG)
                    binding.appBarTitle.text = getString(R.string.settings)
                }
            }
            true
        }
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        currentFragmentTag = tag
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }


}