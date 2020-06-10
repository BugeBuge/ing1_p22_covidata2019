package com.example.mycoviddata

data class DatedSituation(val Country: String, val Cases: Int, val Status: String, val Date: String)

data class Global(val TotalConfirmed: Int, val TotalDeaths: Int, val TotalRecovered: Int)

data class Country(val Country : String, val Slug : String, val TotalConfirmed : Int, val TotalDeaths : Int)

data class CountryName(val Country: String, val Slug: String)

data class GlobalSummary(val Global : Global, val Countries : List<Country>)