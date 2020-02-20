package ru.cocovella.mortalenemies.view.splash

import android.os.Handler
import org.koin.android.viewmodel.ext.android.viewModel
import ru.cocovella.mortalenemies.view.base.BaseActivity
import ru.cocovella.mortalenemies.view.list.MainActivity


class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {
    override val model: SplashViewModel by viewModel()
    override val layoutRes: Int? = null

    override fun onResume() {
        super.onResume()
        Handler().postDelayed( { model.requestUser() }, 1000)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let { startMainActivity() }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }
}
