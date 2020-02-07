package ru.cocovella.mortalenemies.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.cocovella.mortalenemies.R

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        viewModel.getViewState().observe(this, object : Observer<S> {
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
        error.message?.let { showError(it) }
    }

    private fun showError(error: String) {
        val snackBar = Snackbar.make(recyclerView, error, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(R.string.button) { snackBar.dismiss() }.show()
    }

    abstract fun renderData(data: T)

}
