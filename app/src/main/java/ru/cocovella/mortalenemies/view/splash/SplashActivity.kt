package ru.cocovella.mortalenemies.view.splash

import android.os.Handler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import ru.cocovella.mortalenemies.view.base.BaseActivity
import ru.cocovella.mortalenemies.view.list.ListActivity


@ExperimentalCoroutinesApi
class SplashActivity : BaseActivity<Boolean?>() {
    override val model: SplashViewModel by viewModel()
    override val layoutRes: Int? = null

    override fun onResume() {
        super.onResume()
        Handler().postDelayed( { model.requestUser() }, 3000)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let { startListActivity() }
    }

    private fun startListActivity() {
        ListActivity.start(this)
        finish()
    }
}
