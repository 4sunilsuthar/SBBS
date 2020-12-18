package com.devsoft.apps.sbbs.api

import com.devsoft.apps.sbbs.models.SMS
import retrofit2.Call
import retrofit2.http.GET

interface API {

    @GET("exec")
    fun getSMSData(): Call<List<SMS>>

}