package ru.cocovella.mortalenemies.view.list

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.cocovella.mortalenemies.R


class LogoutFragment : DialogFragment() {
    companion object{
        val TAG = "${LogoutFragment::class.java.name} :"
        fun createInstance() = LogoutFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = AlertDialog.Builder(context)
            .setTitle(getString(R.string.logout_title))
            .setMessage(getString(R.string.logout_message))
            .setPositiveButton(getString(R.string.positive_btn)) { _, _ ->  (activity as LogoutListener).onLogout()}
            .setNegativeButton(getString(R.string.negative_btn)) { _, _ ->  dismiss() }
            .create()

    interface LogoutListener {
        fun onLogout()
    }

}
