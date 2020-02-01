package ru.cocovella.mynotebook.model.entities

import android.annotation.SuppressLint

// entity
class Note (var title: String, var body: String, var color: Int) {

    @SuppressLint("DefaultLocale")
    override fun toString(): String = "{\"${title.toUpperCase()}\": \"$body\"}"

}
