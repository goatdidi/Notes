package cn.edu.notes.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.edu.notes.Entities.Notes
import cn.edu.notes.R
import kotlinx.android.synthetic.main.item_notes.view.*

class NotesAdapter(val arrList:List<Notes>)
    :RecyclerView.Adapter<NotesAdapter.NotesViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notes,parent,false)
        )
    }


    override fun getItemCount(): Int {
        return arrList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.itemView.tvTitle.text=arrList[position].title
        holder.itemView.tvDesc.text=arrList[position].noteText
        holder.itemView.tvDateTime.text=arrList[position].dateTime
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

    }
    class NotesViewHolder(view:View):RecyclerView.ViewHolder(view){

    }
}