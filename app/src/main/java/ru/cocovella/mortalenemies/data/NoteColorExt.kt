package ru.cocovella.mortalenemies.data

import android.content.Context
import androidx.core.content.ContextCompat
import ru.cocovella.mortalenemies.R
import ru.cocovella.mortalenemies.data.Note.Color

fun Color.getColorInt(context: Context) : Int =
    ContextCompat.getColor(context, when(this) {
        Color.WHITE -> R.color.white
        Color.PINK -> R.color.pink
        Color.RED -> R.color.red
        Color.ORANGE -> R.color.orange
        Color.YELLOW -> R.color.yellow
        Color.GREEN -> R.color.green
        Color.BLUE -> R.color.blue
        Color.VIOLET -> R.color.violet
    })

fun Color.getColorRes() : Int = when (this) {
    Color.WHITE -> R.color.white
    Color.PINK -> R.color.pink
    Color.RED -> R.color.red
    Color.ORANGE -> R.color.orange
    Color.YELLOW -> R.color.yellow
    Color.GREEN -> R.color.green
    Color.BLUE -> R.color.blue
    Color.VIOLET -> R.color.violet
}

