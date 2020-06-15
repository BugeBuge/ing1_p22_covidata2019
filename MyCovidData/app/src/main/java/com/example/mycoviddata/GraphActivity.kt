package com.example.mycoviddata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_graph.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class GraphActivity : AppCompatActivity() {

    fun max(l : List<DatedSituation>) : Int {
        var max : Int = 0
        for (x in l)
            if (x.Cases > max)
                max = x.Cases
        return max
    }

    fun copy_list(l : List<CountryName>) : MutableList<CountryName> {
        val nl : MutableList<CountryName> = arrayListOf()
        for (x in l)
            nl.add(x)
        return nl;
    }

    fun order_country(l : MutableList<CountryName>) {
        val m : Int = l.size - 1
        for (i in 0..m) {
            for (j in i..m) {
                if (l[i].Country > l[j].Country)
                {
                    val mem : CountryName = l[i]
                    l[i] = l[j]
                    l[j] = mem
                }
            }
        }
    }

    fun showCasesForCountry(country: String, status: String) {
        val baseURL = "https://api.covid19api.com/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()
        val service: WebService = retrofit.create(WebService::class.java)
        val data : MutableList<DateGraphSample> = arrayListOf()
        val activity : GraphActivity = this

        val callback : Callback<List<DatedSituation>> = object : Callback<List<DatedSituation>> {
            override fun onFailure(call: Call<List<DatedSituation>>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("TAG", "WebService call failed")
            }

            override fun onResponse(call: Call<List<DatedSituation>>, response: Response<List<DatedSituation>>) {
                if (response.code() == 200) {
                    val total = findViewById<TextView>(R.id.total_value);
                    val wsdata = response.body()!!
                    if (wsdata.isEmpty()) {
                        GraphRecyclerView.adapter = GraphListAdapter(activity, data)
                        total.text = ""
                        return
                    }
                    val max : Int = max(wsdata)

                    for (x in wsdata){
                        data.add(DateGraphSample(x.Date.substring(0, 10), x.Cases, max))
                    }
                    GraphRecyclerView.adapter = GraphListAdapter(activity, data)

                    total.text = data[0].max.toString();

                }
                else {
                    throw Exception()
                }
            }
        }

        service.evolution(country, status).enqueue(callback)
        GraphRecyclerView.setHasFixedSize(true)
        GraphRecyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val baseURL = "https://api.covid19api.com/"
        var country : String = ""
        var status : String = "confirmed"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()
        val service: WebService = retrofit.create(WebService::class.java)
        val data : MutableList<CountryName> = arrayListOf()
        val activity : GraphActivity = this

        val callback : Callback<List<CountryName>> = object : Callback<List<CountryName>> {
            override fun onFailure(call: Call<List<CountryName>>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("TAG", "WebService call failed")
            }

            override fun onResponse(call: Call<List<CountryName>>, response: Response<List<CountryName>>) {
                if (response.code() == 200) {
                    val wsdata : MutableList<CountryName> = copy_list(response.body()!!)
                    order_country(wsdata)
                    if (!wsdata.isEmpty())
                        country = wsdata[0].Slug

                    for (x in wsdata){
                        data.add(CountryName(x.Country, x.Slug))
                    }
                    val countryClickListener = View.OnClickListener {
                        val position = it.tag as Int
                        val clickedItem = data[position]

                        country = clickedItem.Slug
                        showCasesForCountry(country, status)
                    }
                    GraphCountryRecyclerView.adapter = GraphCountryAdapter(activity, data, countryClickListener)

                }
                else {
                    throw Exception()
                }
            }
        }
        service.countries().enqueue(callback)

        GraphCountryRecyclerView.setHasFixedSize(true)
        GraphCountryRecyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)

        val confirmedButton = findViewById<Button>(R.id.GraphButtonConfirmed)
        confirmedButton.setOnClickListener {
            status = "confirmed"
            if (country != "")
                showCasesForCountry(country, status)
        }
        val deathButton = findViewById<Button>(R.id.GraphButtonDeath)
        deathButton.setOnClickListener {
            status = "deaths"
            if (country != "")
                showCasesForCountry(country, status)
        }
        val recoveredButton = findViewById<Button>(R.id.GraphButtonRecovered)
        recoveredButton.setOnClickListener {
            status = "recovered"
            if (country != "")
                showCasesForCountry(country, status)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id : Int = item.itemId
        if (id == android.R.id.home)
            this.finish()
        return super.onOptionsItemSelected(item)
    }
}
