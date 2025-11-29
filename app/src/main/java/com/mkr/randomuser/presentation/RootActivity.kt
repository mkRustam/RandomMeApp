package com.mkr.randomuser.presentation

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.mkr.randomuser.R
import com.mkr.randomuser.core.error.ErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RootActivity : AppCompatActivity() {

    @Inject
    lateinit var errorHandler: ErrorHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        // Correctly find the NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setupWithNavController(navController, appBarConfiguration)

        observeErrorEvents()
    }

    private fun observeErrorEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                errorHandler.errorEvents.collect { errorEvent ->
                    showErrorDialog(errorEvent.message)
                }
            }
        }
    }

    private fun showErrorDialog(message: String) {
        if (!isFinishing && !isDestroyed) {
            AlertDialog.Builder(this)
                .setTitle(R.string.error_dialog_title)
                .setMessage(message)
                .setPositiveButton(R.string.error_dialog_button_ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(true)
                .show()
        }
    }
}