package ru.cocovella.mortalenemies.view.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.cocovella.mortalenemies.R
import ru.cocovella.mortalenemies.data.errors.NoAuthException

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {
    companion object{ const val REQUEST_CODE = 665 }
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let { setContentView(it) }
        viewModel.baseLiveData().observe(this, object : Observer<S> {
            override fun onChanged(viewState: S?) {
                viewState ?: return
                viewState.error?.let {
                    renderError(it)
                    return
                }
                renderData(viewState.data)
            }
        })
    }

    private fun renderError(error: Throwable) {
        when(error){
            is NoAuthException -> startLogin()
            else -> error.message?.let { showError(it)
            }
        }
    }

    private fun startLogin() {
        val providers = listOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setLogo(R.drawable.android_robot)
                        .setTheme(R.style.LoginStyle)
                        .setAvailableProviders(providers)
                        .build(), REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode != Activity.RESULT_OK) {
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showError(error: String) {
        val snackBar = Snackbar.make(recyclerView, error, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(R.string.button) { snackBar.dismiss() }.show()
    }

    abstract fun renderData(data: T)

}
