package ru.cocovella.mynotebook.model.entities

// entity
class Note {
    var title: String
    var body: String
    var color: Int


    constructor(title: String, body: String) {
        this.title = title
        this.body = body
        this.color = 0
    }

    constructor(title: String, body: String, color: Int) {
        this.title = title
        this.body = body
        this.color = color
    }

    override fun toString(): String {
        return "{Note '" + title + '\'' +
                ", body='" + body + '\'' +
                '}'
    }

}
