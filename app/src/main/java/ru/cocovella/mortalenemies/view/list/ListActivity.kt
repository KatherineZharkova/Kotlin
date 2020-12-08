package ru.cocovella.mortalenemies.view.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.koin.android.viewmodel.ext.android.viewModel
import ru.cocovella.mortalenemies.R
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.view.base.BaseActivity
import ru.cocovella.mortalenemies.view.note.NoteActivity
import ru.cocovella.mortalenemies.view.splash.SplashActivity


@ExperimentalCoroutinesApi
class ListActivity : BaseActivity<List<Note>?>() {
    override val model: ListViewModel by viewModel()
    override val layoutRes = R.layout.activity_main
    private lateinit var adapter: ListAdapter

    companion object{
        fun start(context: Context) = context.startActivity<ListActivity>()
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
                R.id.logout -> showLogoutDialog().let { true }
                else -> false
            }

    private fun showLogoutDialog() {
        alert {
            titleResource = R.string.logout_title
            messageResource = R.string.logout_message
            positiveButton(R.string.positive_btn) { onLogout() }
            negativeButton(R.string.negative_btn) { dialog -> dialog.dismiss() }
        }.show()
    }

    private fun onLogout() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener{
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }
    }

}
