package com.example.mycoviddata

data class Global(val TotalConfirmed: Int, val TotalDeaths: Int, val TotalRecovered: Int)

data class Country(val Country : String, val TotalConfirmed : Int, val TotalDeaths : Int)

data class GlobalSummary(val Global : Global, val Countries : List<Country>)