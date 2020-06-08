package com.example.mycoviddata

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CountryListAd(val context: Context, val data: List<CountryStatData>)
    : RecyclerView.Adapter<CountryListAd.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val paysView: TextView = itemView.findViewById(R.id.ItemPays)
        val nbcas: TextView = itemView.findViewById(R.id.ItempNombre)
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
        holder.paysView.text = currentItem.country
        holder.nbcas.text = currentItem.cas.toString()
    }
}