package ru.cocovella.mortalenemies.view.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import ru.cocovella.mortalenemies.R
import ru.cocovella.mortalenemies.data.errors.NoAuthException
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity<T> : AppCompatActivity(), CoroutineScope {
    companion object{ const val REQUEST_CODE = 665 }
    @ExperimentalCoroutinesApi
    abstract val model: BaseViewModel<T>
    abstract val layoutRes: Int?
    private val job = Job()
    private lateinit var dataJob: Job
    private lateinit var errorJob: Job
    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Main + job }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let { setContentView(it) }
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        dataJob = launch { model.viewStateChannel().consumeEach { renderData(it) } }
        errorJob = launch { model.errorChannel().consumeEach { renderError(it)} }
    }

    override fun onStop() {
        super.onStop()
        dataJob.cancel()
        errorJob.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode != Activity.RESULT_OK) {
            finish()
        }
    }

    abstract fun renderData(data: T)

    private fun renderError(error: Throwable) {
        when(error){
            is NoAuthException -> startLogin()
            else -> error.message?.let { showError(it)
            }
        }
    }

    private fun showError(error: String) {
        val snackBar = Snackbar.make(recyclerView, error, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(R.string.on_error_button) { snackBar.dismiss() }.show()
    }

    private fun startLogin() {
        val providers = listOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setLogo(R.drawable.splash)
                        .setTheme(R.style.LoginStyle)
                        .setAvailableProviders(providers)
                        .build(), REQUEST_CODE
        )
    }

}
