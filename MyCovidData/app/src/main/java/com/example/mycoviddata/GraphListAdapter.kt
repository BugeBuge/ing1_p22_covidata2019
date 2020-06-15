package com.example.mycoviddata

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GraphListAdapter(val context: Context, val data: List<DateGraphSample>)
        : RecyclerView.Adapter<GraphListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateView: TextView = itemView.findViewById(R.id.dateGraph)
        val progress: ProgressBar = itemView.findViewById(R.id.progressBar)
        var prct: TextView = itemView.findViewById(R.id.prct_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(context)
            .inflate(R.layout.item_graph_list_view, parent, false)

        return ViewHolder(rowView)
    }

    override fun getItemCount(): Int {
        return data.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]

        holder.dateView.text = currentItem.date
        holder.progress.progress = currentItem.progress
        holder.progress.max = currentItem.max
        val pr : Float = (holder.progress.progress.toFloat() / holder.progress.max.toFloat()) * 100f
        holder.prct.text = pr.toInt().toString() + "%";
    }
}