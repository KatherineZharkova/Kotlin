package ru.cocovella.mynotebook.model.entities

import android.annotation.SuppressLint

// entity
class Note (var title: String, var body: String, var color: Int) {


    @SuppressLint("DefaultLocale")
    override fun toString(): String = "{\"${title.toUpperCase()}\": \"$body\"}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Note) return false

        if (title != other.title) return false
        if (body != other.body) return false
        if (color != other.color) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + color
        return result
    }

}
