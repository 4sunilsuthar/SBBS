package com.devsoft.apps.sbbs.models

import com.google.gson.annotations.SerializedName

class SMS(mobileNo: String, rowNo: String, smsText: String) {
    @SerializedName("SMSText")
    val smsText = smsText

    @SerializedName("rowNo")
    val rowNo = rowNo

    @SerializedName("PhoneNo")
    val mobileNo = mobileNo
    override fun toString(): String {
        return super.toString()
    }
}
