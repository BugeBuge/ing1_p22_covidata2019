package com.example.mycoviddata

import android.os.Bundle
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*

class DataCountryActivity : AppCompatActivity() {

    fun setCountryInfoByDate(country: String, status: String) {
        val baseURL = "https://api.covid19api.com/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()
        val service: WebService = retrofit.create(WebService::class.java)
        val data: MutableList<DateGraphSample> = arrayListOf()
        val activity: DataCountryActivity = this

        val callback: Callback<List<DatedSituation>> = object : Callback<List<DatedSituation>> {
            override fun onFailure(call: Call<List<DatedSituation>>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("TAG", "WebService call failed")
            }

            override fun onResponse(
                call: Call<List<DatedSituation>>,
                response: Response<List<DatedSituation>>
            ) {
                if (response.code() == 200) {
                    val wsdata = response.body()!!
                    if (wsdata.isEmpty()) {
                        return
                    }
                    val refDate: String =
                        SimpleDateFormat("yyyy-MM-dd").format(activity_data_country_calendar.date)
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
                    if (activity_data_country_txt_confirmed_nb.text.isEmpty())
                        activity_data_country_txt_confirmed_nb.text = "0"
                        activity_data_country_txt_deaths_nb.text = "0"
                        activity_data_country_txt_recovered_nb.text = "0"
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

        activity_data_country_txt_country.text = intent.getStringExtra("COUNTRY")!!
        val country = intent.getStringExtra("COUNTRY_SLUG")!!
        setCountryInfoByDate(country, "confirmed")
        setCountryInfoByDate(country, "deaths")
        setCountryInfoByDate(country, "recovered")

        activity_data_country_calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            activity_data_country_calendar.date = 0
            setCountryInfoByDate(country, "confirmed")
            setCountryInfoByDate(country, "deaths")
            setCountryInfoByDate(country, "recovered")
        }
    }
}