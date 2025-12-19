package com.example.darwinasistan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat

class HistoryAdapter(private val items: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val actionName: TextView = view.findViewById(R.id.txtActionName)
        val description: TextView = view.findViewById(R.id.txtDescription)
        val timestamp: TextView = view.findViewById(R.id.txtTimestamp)
        val icon: ImageView = view.findViewById(R.id.imgActionIcon)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_card, parent, false)
        return HistoryViewHolder(view)
    }
    // Veriyi karta bağla
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.actionName.text = item.actionName
        holder.description.text = item.description
        holder.timestamp.text = item.timestamp


        holder.icon.setImageResource(item.iconResId)

        holder.icon.setColorFilter(ContextCompat.getColor(holder.icon.context, R.color.neon_coral))
    }


    override fun getItemCount() = items.size
}