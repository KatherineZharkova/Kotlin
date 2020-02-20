package ru.cocovella.mortalenemies.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import ru.cocovella.mortalenemies.R
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.getColorInt

class ListAdapter(val onItemViewClick : (Note) -> Unit) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(notes[position])



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) = with(itemView) {
            title.text = note.title
            body.text = note.body
            cardView.setCardBackgroundColor(note.color.getColorInt(context))
            setOnClickListener { onItemViewClick.invoke(note) }
        }
    }

}
