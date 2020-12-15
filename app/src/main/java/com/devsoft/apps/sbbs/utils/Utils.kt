package com.devsoft.apps.sbbs.utils

import com.devsoft.apps.sbbs.api.API
import okhttp3.OkHttpClient
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory

object Utils {
    //    private const val baseURL: String = "https://script.google.com/macros/s/AKfycby3u08m42uYrGs-K6tnl--Y-gj7WcCcTlSr7XbYRtb_t9MSA7NC/"
    private const val baseURL: String =
        "https://script.google.com/macros/s/AKfycbwREhIfANUOI_M-kuvy5BDlQZlghtRPenA3TMvXsaAfOX2p8lb3/"
//    private const val baseURL: String = "https://script.google.com/macros/s/AKfycby3u08m42uYrGs-K6tnl--Y-gj7WcCcTlSr7XbYRtb_t9MSA7NC/exec"

    fun getApiInstance(): API {
        val retrofit = Builder()
            .baseUrl(baseURL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(API::class.java)
    }

}