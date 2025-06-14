package com.jarb_studio.control_financiero

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.NavOptions

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private var shouldHandleNavigation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Configurar el listener para cambios de destino
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (shouldHandleNavigation) {
                when (destination.id) {
                    R.id.ingresosFragment -> {
                        bottomNavigationView.menu.findItem(R.id.ingresosFragment)?.isChecked = true
                    }
                    R.id.gastosFragment -> {
                        bottomNavigationView.menu.findItem(R.id.gastosFragment)?.isChecked = true
                    }
                    R.id.ahorroFragment -> {
                        bottomNavigationView.menu.findItem(R.id.ahorroFragment)?.isChecked = true
                    }
                }
            }
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            if (!shouldHandleNavigation) return@setOnItemSelectedListener false

            Log.d("NavDebug", "Item selected: ${item.title} (ID: ${item.itemId}). Current destination: ${navController.currentDestination?.label}")

            when (item.itemId) {
                R.id.ingresosFragment -> {
                    shouldHandleNavigation = false
                    bottomNavigationView.menu.findItem(R.id.ingresosFragment)?.isChecked = true

                    navController.popBackStack(R.id.ingresosFragment, false)
                    if (navController.currentDestination?.id != R.id.ingresosFragment) {
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.ingresosFragment, false)
                            .setLaunchSingleTop(true)
                            .build()
                        navController.navigate(R.id.ingresosFragment, null, navOptions)
                    }
                    shouldHandleNavigation = true
                    true
                }
                R.id.gastosFragment -> {
                    navigateToFragment(item.itemId, R.id.gastosFragment)
                    true
                }
                R.id.ahorroFragment -> {
                    navigateToFragment(item.itemId, R.id.ahorroFragment)
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.setOnItemReselectedListener { item ->
            Log.d("NavDebug", "Tab re-selected: ${item.title} (ID: ${item.itemId})")
            navController.popBackStack(item.itemId, false)
        }
    }

    private fun navigateToFragment(itemId: Int, fragmentId: Int) {
        shouldHandleNavigation = false
        bottomNavigationView.menu.findItem(itemId)?.isChecked = true

        if (navController.currentDestination?.id != fragmentId) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(navController.graph.startDestinationId, false)
                .setLaunchSingleTop(true)
                .build()
            navController.navigate(fragmentId, null, navOptions)
        }

        shouldHandleNavigation = true
    }
}