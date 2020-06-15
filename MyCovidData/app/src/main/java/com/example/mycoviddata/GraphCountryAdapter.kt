package com.example.mycoviddata

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GraphCountryAdapter(val context: Context, val data: List<CountryName>,
                    private val countryListener: View.OnClickListener)
    : RecyclerView.Adapter<GraphCountryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.countryNameGraph)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater
            .from(context)
            .inflate(R.layout.item_graph_list_country, parent, false)

        rowView.setOnClickListener(countryListener)
        return ViewHolder(rowView)
    }

    override fun getItemCount(): Int {
        return data.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]

        holder.textView.text = currentItem.Country

        holder.itemView.tag = position
    }

}