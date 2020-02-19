package ru.cocovella.mortalenemies.view.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import ru.cocovella.mortalenemies.R
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.view.base.BaseActivity
import ru.cocovella.mortalenemies.view.note.NoteActivity
import ru.cocovella.mortalenemies.view.splash.SplashActivity


class MainActivity : BaseActivity<List<Note>?, ListViewState>(), LogoutFragment.LogoutListener {
    override val viewModel: ListViewModel by lazy { ViewModelProvider(this).get(ListViewModel::class.java) }
    override val layoutRes = R.layout.activity_main
    lateinit var adapter: ListAdapter

    companion object{
        fun start(context: Context) = Intent(context, MainActivity::class.java).apply{
            context.startActivity(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        adapter = ListAdapter { NoteActivity.start(this, it.id) }
        recyclerView.adapter = adapter

        fab.setOnClickListener { NoteActivity.start(this) }
    }

    override fun renderData(data: List<Note>?) {
        data?.let { adapter.notes = it }
    }

    override fun onCreateOptionsMenu(menu: Menu?) =
            MenuInflater(this).inflate(R.menu.main, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.logout -> showLogoutDialog()?.let { true }
                else -> false
            }

    private fun showLogoutDialog() {
        supportFragmentManager.findFragmentByTag(LogoutFragment.TAG) ?:
        LogoutFragment.createInstance().show(supportFragmentManager, LogoutFragment.TAG)
    }

    override fun onLogout() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener{
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                }
    }
}
