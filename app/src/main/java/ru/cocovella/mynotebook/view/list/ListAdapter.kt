package ru.cocovella.mynotebook.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import ru.cocovella.mynotebook.R
import ru.cocovella.mynotebook.model.Note
import ru.cocovella.mynotebook.model.Note.Color


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

            cardView.setCardBackgroundColor(ContextCompat.getColor(context,
                    when(note.color){
                        Color.WHITE -> R.color.white
                        Color.YELLOW -> R.color.yellow
                        Color.GREEN -> R.color.green
                        Color.BLUE -> R.color.blue
                        Color.RED -> R.color.red
                        Color.VIOLET -> R.color.violet
                        Color.PINK -> R.color.pink
                    }))

            setOnClickListener { onItemViewClick.invoke(note) }

        }
    }

}
