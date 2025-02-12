package com.reham11203.todoapp.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.reham11203.todoapp.R
import com.reham11203.todoapp.databinding.ActivityHomeBinding
import com.reham11203.todoapp.ui.home.fragments.AddTaskFragment
import com.reham11203.todoapp.ui.home.fragments.SettingsFragment
import com.reham11203.todoapp.ui.home.fragments.tasks_fragment.TasksFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    var taskFragment: TasksFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        taskFragment = supportFragmentManager.findFragmentByTag("TASKS_FRAGMENT") as? TasksFragment
            ?: TasksFragment()
        setNavigation()
        setOnFabClick()
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
                    showFragment(taskFragment!!, "TASKS_FRAGMENT")
                    binding.appBarTitle.text = getString(R.string.app_name)
                }

                R.id.settings -> {
                    showFragment(SettingsFragment(), "SETTINGS_FRAGMENT")
                    binding.appBarTitle.text = getString(R.string.settings)
                }
            }
            true
        }
        binding.bottomNavView.selectedItemId = R.id.tasks
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .commit()
    }
}