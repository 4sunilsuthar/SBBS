package com.devsoft.apps.sbbs.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devsoft.apps.sbbs.ExcelDataActivity
import com.devsoft.apps.sbbs.R
import com.devsoft.apps.sbbs.models.SMS

class SmsAdapter(private val context: ExcelDataActivity, private val smsDataSet: List<SMS>?) :
    RecyclerView.Adapter<SmsAdapter.SmsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.sms_details_card, null)
        return SmsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        val smsCard = smsDataSet!![position]
        holder.smsCodeNo.text = smsCard.codeNo
        holder.smsName.text = smsCard.name
        holder.smsNumber.text = smsCard.mobileNo
        holder.smsAmount.text = smsCard.amount
        holder.smsText.text = smsCard.smsText
    }

    override fun getItemCount(): Int {
        return smsDataSet!!.size
    }

    class SmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val smsCodeNo: TextView = itemView.findViewById(R.id.sms_code_no)
        val smsName: TextView = itemView.findViewById(R.id.sms_name)
        val smsNumber: TextView = itemView.findViewById(R.id.sms_mobile_number)
        val smsText: TextView = itemView.findViewById(R.id.sms_text)
        val smsAmount: TextView = itemView.findViewById(R.id.sms_amount)
    }
}