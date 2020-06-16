package com.example.mycoviddata

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_data_all.*
import kotlinx.android.synthetic.main.activity_data_country.*
import kotlinx.android.synthetic.main.activity_graph.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate.parse
import java.util.*

class DataCountryActivity : AppCompatActivity() {

    fun setCountryInfoByDate(country: String, status: String, date: Date) {
        val baseURL = "https://api.covid19api.com/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()
        val service: WebService = retrofit.create(WebService::class.java)
        val activity: DataCountryActivity = this

        val callback: Callback<List<DatedSituation>> = object : Callback<List<DatedSituation>> {
            override fun onFailure(call: Call<List<DatedSituation>>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("TAG", "WebService call failed")
            }

            override fun onResponse(call: Call<List<DatedSituation>>, response: Response<List<DatedSituation>>) {
                if (response.code() == 200) {
                    val wsdata = response.body()!!
                    if (wsdata.isEmpty()) {
                        return
                    }
                    val refDate: String =
                        SimpleDateFormat("yyyy-MM-dd").format(date)
                    if (date.time >= activity_data_country_calendar.date || date.before(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(wsdata[0].Date))) {
                        activity_data_country_txt_confirmed_nb.text = "0"
                        activity_data_country_txt_deaths_nb.text = "0"
                        activity_data_country_txt_recovered_nb.text = "0"
                    } else {
                        for (i in wsdata) {
                            if (refDate.equals(i.Date.substring(0, 10))) {
                                if (status.equals("confirmed"))
                                    activity_data_country_txt_confirmed_nb.text = i.Cases.toString()
                                else if (status.equals("deaths"))
                                    activity_data_country_txt_deaths_nb.text = i.Cases.toString()
                                else
                                    activity_data_country_txt_recovered_nb.text = i.Cases.toString()
                                break;
                            }
                        }
                    }
                } else {
                    throw Exception()
                }
            }
        }

        service.evolution(country, status).enqueue(callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_country)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activity_data_country_txt_country.text = intent.getStringExtra("COUNTRY")!!
        val country = intent.getStringExtra("COUNTRY_SLUG")!!
        val curDate = Date(activity_data_country_calendar.date)
        setCountryInfoByDate(country, "confirmed", curDate)
        setCountryInfoByDate(country, "deaths", curDate)
        setCountryInfoByDate(country, "recovered", curDate)

        activity_data_country_calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val newDate = Date(year - 1900, month, dayOfMonth);
            setCountryInfoByDate(country, "confirmed", newDate)
            setCountryInfoByDate(country, "deaths", newDate)
            setCountryInfoByDate(country, "recovered", newDate)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id : Int = item.itemId
        if (id == android.R.id.home)
            this.finish()
        return super.onOptionsItemSelected(item)
    }
}