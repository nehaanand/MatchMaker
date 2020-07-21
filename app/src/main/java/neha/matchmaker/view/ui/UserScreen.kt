package neha.matchmaker.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import neha.matchmaker.R
import neha.matchmaker.databinding.UsersScreenBinding
import neha.matchmaker.injection.ViewModelFactory
import neha.matchmaker.viewmodels.UsersViewModel

class UserScreen : AppCompatActivity() {
    private lateinit var binding: UsersScreenBinding
    private lateinit var viewModel: UsersViewModel
    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.users_screen)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(UsersViewModel::class.java)
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })

        binding.viewModel = viewModel

    }

    private fun showError(errorMessage: String) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}