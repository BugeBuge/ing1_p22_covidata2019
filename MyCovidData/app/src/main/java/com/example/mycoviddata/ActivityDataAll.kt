package com.example.mycoviddata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_data_all.*

class ActivityDataAll : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_all)

        val data : MutableList<CountryStatData> = arrayListOf()
        for (i in 1..50){
            data.add(CountryStatData("Recuperer le pays", i))
        }
        CountrydataRecyclerView.setHasFixedSize(true)
        CountrydataRecyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        CountrydataRecyclerView.adapter = CountryListAd(this, data)
    }
}
