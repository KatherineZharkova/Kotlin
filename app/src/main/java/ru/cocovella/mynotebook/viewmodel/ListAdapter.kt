package ru.cocovella.mynotebook.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import ru.cocovella.mynotebook.R
import ru.cocovella.mynotebook.model.entities.Note


class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_note, parent, false)
            )

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(notes[position])



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: Note) = with(itemView) {
            title.text = note.title
            body.text = note.body
            cardView.setCardBackgroundColor(note.color)
        }
    }

}
