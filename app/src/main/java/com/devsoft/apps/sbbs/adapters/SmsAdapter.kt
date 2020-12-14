package com.devsoft.apps.sbbs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devsoft.apps.sbbs.ExcelDataActivity
import com.devsoft.apps.sbbs.R
import com.devsoft.apps.sbbs.models.SMS

class SmsAdapter(private val smsDataset: ExcelDataActivity, val context: List<SMS>?) :
    RecyclerView.Adapter<SmsAdapter.SmsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.sms_details_card, null)
        return SmsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        val smsCard = smsDataset[position]
        holder.smsName.text = smsCard.rowNo // change to Name
        holder.smsNumber.text = smsCard.mobileNo
        holder.smsText.text = smsCard.smsText
    }

    override fun getItemCount(): Int {
        return smsDataset.size
    }

    class SmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val smsName: TextView = itemView.findViewById(R.id.sms_name)
        val smsNumber: TextView = itemView.findViewById(R.id.sms_mobile_number)
        val smsText: TextView = itemView.findViewById(R.id.sms_text)
    }
}