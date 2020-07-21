package neha.matchmaker.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import neha.matchmaker.R
import neha.matchmaker.injection.ViewModelFactory
import neha.matchmaker.model.database.UsersDao
import neha.matchmaker.viewmodels.SplashScreenViewModel

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: neha.matchmaker.databinding.ActivitySplashScreenBinding
    private lateinit var viewModel: SplashScreenViewModel
    private var errorSnackbar: Snackbar? = null
    private var userDao: UsersDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(SplashScreenViewModel::class.java)

        viewModel.liveData.observe(this, Observer {
            when (it) {
                is SplashScreenViewModel.SplashState.ShadiUsers -> {
                    goToMainActivity()
                }
            }
        })
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })
        binding.viewModel = viewModel


    }

    private fun showError(errorMessage: String) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    private fun goToMainActivity() {

        finish()
        startActivity(Intent(this, UserScreen::class.java))
    }

}
