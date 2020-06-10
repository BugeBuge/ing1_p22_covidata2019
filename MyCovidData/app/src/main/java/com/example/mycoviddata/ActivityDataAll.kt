package com.example.mycoviddata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_data_all.*
import kotlinx.android.synthetic.main.activity_graph.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class ActivityDataAll : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_all)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val baseURL = "https://api.covid19api.com/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()
        val service: WebService = retrofit.create(WebService::class.java)
        val data : MutableList<CountryStatData> = arrayListOf()
        val activity : ActivityDataAll = this

        val callback : Callback<GlobalSummary> = object : Callback<GlobalSummary> {
            override fun onFailure(call: Call<GlobalSummary>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("TAG", "WebService call failed")
            }

            override fun onResponse(call: Call<GlobalSummary>, response: Response<GlobalSummary>) {
                if (response.code() == 200) {
                    val wsdata = response.body()!!

                    for (i in wsdata.Countries){
                        data.add(CountryStatData(i.Country, i.Slug, i.TotalConfirmed))
                    }

                    Placeconfirmed.text = wsdata.Global.TotalConfirmed.toString()
                    Placedeath.text = wsdata.Global.TotalDeaths.toString()
                    PlaceRecovered.text = wsdata.Global.TotalRecovered.toString()

                    val countryClickListener = View.OnClickListener {
                        val position = it.tag as Int
                        val intent = Intent(activity, DataCountryActivity::class.java)
                        intent.putExtra("COUNTRY", wsdata.Countries[position].Country)
                        intent.putExtra("COUNTRY_SLUG", wsdata.Countries[position].Slug)
                        startActivity(intent)
                    }
                    CountrydataRecyclerView.adapter = CountryListAd(activity, data, countryClickListener)

                }
                else {
                    throw Exception()
                }
            }
        }

        service.summary().enqueue(callback)
        CountrydataRecyclerView.setHasFixedSize(true)
        CountrydataRecyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id : Int = item.itemId
        if (id == android.R.id.home)
            this.finish()
        return super.onOptionsItemSelected(item)
    }

}
