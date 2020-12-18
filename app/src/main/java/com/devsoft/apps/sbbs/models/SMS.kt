package com.devsoft.apps.sbbs.models

import com.google.gson.annotations.SerializedName

class SMS(rowNo: String, groupNo : String, name: String, codeNo: String, totalMembers: String, mobileNo: String, month: String, amount: String, smsText: String) {

    @SerializedName("rowNo")
    val rowNo = rowNo

    @SerializedName("GroupNo")
    val groupNo = groupNo

    @SerializedName("Name")
    val name = name

    @SerializedName("CodeNo")
    val codeNo = codeNo

    @SerializedName("TotalMembers")
    val totalMembers = totalMembers

    @SerializedName("MobileNo")
    val mobileNo = mobileNo

    @SerializedName("Month")
    val month = month

    @SerializedName("Amount")
    val amount = amount

    @SerializedName("SMSText")
    val smsText = smsText
}
