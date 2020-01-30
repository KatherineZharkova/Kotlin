package ru.cocovella.mynotebook

// entity
class Note(val title: String, val body: String) {

    override fun toString(): String {
        return "{Note '" + title + '\'' +
                ", body='" + body + '\'' +
                '}'
    }
}
