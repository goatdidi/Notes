package cn.edu.notes.adapter

import android.graphics.BitmapFactory


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.edu.notes.Entities.Notes
import cn.edu.notes.R
import kotlinx.android.synthetic.main.item_notes.view.*

class NotesAdapter(val arrList:List<Notes>)
    :RecyclerView.Adapter<NotesAdapter.NotesViewHolder>(), Filterable {
    private var filterlist=arrList
    private val originlist=arrList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notes,parent,false)
        )
    }


    override fun getItemCount(): Int {
        return arrList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        var note:Notes = arrList[position]
        holder.itemView.tvTitle.text=arrList[position].title
        holder.itemView.tvDesc.text=arrList[position].noteText
        holder.itemView.tvDateTime.text=arrList[position].dateTime
        //holder.bind(note)
        if (arrList[position].color!=null){
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor(arrList[position].color))
        }else{
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor("#171C26"))
        }
        if (arrList[position].imgPath != null){
            holder.itemView.imgNote.setImageBitmap(BitmapFactory.decodeFile(arrList[position].imgPath))
            holder.itemView.imgNote.visibility = View.VISIBLE
        }else{
            holder.itemView.imgNote.visibility = View.GONE
        }
        if (arrList[position].webLink != null){
            holder.itemView.tvWebLink.text = arrList[position].webLink
            holder.itemView.tvWebLink.visibility = View.VISIBLE
        }else{
            holder.itemView.tvWebLink.visibility = View.GONE
        }

    }

    class NotesViewHolder(view:View):RecyclerView.ViewHolder(view){
//        var title:TextView=view.findViewById(R.id.tvTitle)
//        var notetext:TextView=view.findViewById(R.id.tvDesc)
//        var datetime:TextView=view.findViewById(R.id.tvDateTime)
//        fun bind(note:Notes,clickListener:OnItemClickListener){
//            title.text=note.title
//            notetext.text=note.noteText
//            datetime.text=note.dateTime
//            itemView.setOnClickListener {
//                clickListener.onItemClick(note)
//            }
//
//        }
    }
//    interface OnItemClickListener{
//        fun onItemClick(note:Notes)
//    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults();
                if (constraint==null||constraint.length<0){
                    filterResults.values=originlist
                }else{
                    val listnew=mutableListOf<Notes>()
                    for(item in originlist){
                        if(item.title!!.contains(constraint) ||item.subTitle!!.contains(constraint)){
                            listnew.add(item)

                        }
                    }
                    filterResults.values=listnew
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterlist=results!!.values as List<Notes>
                notifyDataSetChanged()
            }
        }
    }



}