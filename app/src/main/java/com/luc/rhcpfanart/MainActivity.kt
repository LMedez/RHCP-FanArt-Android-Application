package com.luc.rhcpfanart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import com.luc.common.resource.NetworkStatus
import com.luc.presentation.viewmodel.MainActivityViewModel
import com.luc.presentation.viewmodel.MusicPlayerViewModel
import com.luc.resources.animation.slideDown
import com.luc.resources.animation.slideUp
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var appUpdateInfoTask: Task<AppUpdateInfo>

    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityViewModel.onConnectionFailed.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    is NetworkStatus.Error -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> { }
                }
            }
        }

        mainActivityViewModel.onNetworkError.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    is NetworkStatus.Error -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> { }
                }
            }
        }


        appUpdateManager = AppUpdateManagerFactory.create(requireNotNull(this.applicationContext))
        appUpdateInfoTask = appUpdateManager.appUpdateInfo

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener(onDestinationChanged)
        registerListener()
        onUpdateInfoTask()
    }

    /* on destination changes, the fragment will be hid if the destiny isn't HomeFragment */
    private val onDestinationChanged =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.homeFragment && destination.id != R.id.allSongs && destination.id != R.id.favFragment) {
                supportFragmentManager.findFragmentById(R.id.mediaPlayerFragment)
                    ?.let { fragment ->
                        fragment.view?.slideDown()
                    }

            } else {
                supportFragmentManager.findFragmentById(R.id.mediaPlayerFragment)
                    ?.let {
                        it.view?.slideUp()
                    }
            }
        }

    private fun onUpdateInfoTask() {
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE /* &&
                (appUpdateInfo.clientVersionStalenessDays() ?: -1) >= DAYS_FOR_FLEXIBLE_UPDATE*/) {

                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    REQUEST_CODE)

            }
        }
    }

    private fun showUpdateDownloadedSnackBar() {
        Snackbar.make(findViewById(R.id.contentContainer),
            getString(R.string.upadate_downloaded),
            Snackbar.LENGTH_INDEFINITE).setAction("Install") {
            appUpdateManager.completeUpdate()
        }.show()
    }

    private fun registerListener() {
        appUpdateManager.registerListener { installState ->
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                showUpdateDownloadedSnackBar()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.installStatus() == InstallStatus.DOWNLOADED) {
                showUpdateDownloadedSnackBar()
            }
        }
    }

}

private const val DAYS_FOR_FLEXIBLE_UPDATE: Int = 6
private const val REQUEST_CODE: Int = 123
