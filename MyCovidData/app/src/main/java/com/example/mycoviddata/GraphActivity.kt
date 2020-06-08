package com.example.mycoviddata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_graph.*

class GraphActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        val data : MutableList<DateGraphSample> = arrayListOf()
        for (i in 1..100) {
            data.add(DateGraphSample("date $i", i))
        }

        GraphRecyclerView.setHasFixedSize(true)
        GraphRecyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        GraphRecyclerView.adapter = GraphListAdapter(this, data)
    }
}
