package neha.matchmaker.view.ui

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import neha.matchmaker.R
import neha.matchmaker.viewmodel.shadiusers.ShadiUsersViewModel
import android.arch.lifecycle.Observer
import neha.matchmaker.injection.ViewModelFactory

class ShadiUsers : AppCompatActivity() {
    private lateinit var binding:neha.matchmaker.databinding.ShadiUsersActivityBinding
    private lateinit var viewModel: ShadiUsersViewModel
    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.shadi_users_activity);
        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(ShadiUsersViewModel::class.java)
        viewModel.errorMessage.observe(this, Observer {
            errorMessage -> if(errorMessage != null) showError(errorMessage.toInt()) else hideError()
        })

        binding.viewModel = viewModel


    } private fun showError(@StringRes errorMessage:Int){
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError(){
        errorSnackbar?.dismiss()
    }
}