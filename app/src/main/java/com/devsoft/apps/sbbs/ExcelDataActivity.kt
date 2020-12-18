package com.devsoft.apps.sbbs

import android.Manifest
import android.R.attr.phoneNumber
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devsoft.apps.sbbs.adapters.SmsAdapter
import com.devsoft.apps.sbbs.models.SMS
import com.devsoft.apps.sbbs.utils.Utils.getApiInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExcelDataActivity : AppCompatActivity() {

    //  Logging and Debugging TAG
    private val mTAG: String = ExcelDataActivity::class.java.simpleName
    private val REQUEST_CODE_ASK_PERMISSIONS = 1234
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
//    private lateinit var smsDataList : List<SMS>
    private lateinit var smsDataList : List<SMS>

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excel_data2)
//        smsDataList = mutableListOf()
        smsDataList = mutableListOf<SMS>()
//      initialize the Recycler View
        recyclerView = findViewById(R.id.sms_list_recycler_view)
        viewManager = LinearLayoutManager(this)
        recyclerView.layoutManager = viewManager
        try {
//            here call the function to get data from the json API
            getSMSData()
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                "Something went Wrong with Server... Please Try Again",
                Toast.LENGTH_SHORT
            ).show()
        }
        // send SMS on the Click of the Button
        val btnGetExcel: Button = findViewById(R.id.btnGetExcel)
        btnGetExcel.setOnClickListener {
            // sendSMS(response.body())
            if(smsDataList.isNotEmpty()) {
                sendSMS(smsDataList)
                Toast.makeText(
                    applicationContext,
                    "Sending Pay Reminders via SMS",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    applicationContext,
                    "No Data Available to Send Text",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getSMSData() {
        val api = getApiInstance()
        // calling for the SMS Data
        val call = api.getSMSData()

        call.enqueue(object : Callback<List<SMS>> {
            override fun onResponse(call: Call<List<SMS>>, response: Response<List<SMS>>) {
                // get the list of SMSs to be sent now send this list to populate the SMS from function
                // call the function & send SMS
                smsDataList = response.body()!!
                viewAdapter = SmsAdapter(this@ExcelDataActivity, smsDataList)
                recyclerView.adapter = viewAdapter
            }

            override fun onFailure(call: Call<List<SMS>>, t: Throwable) {
                Toast.makeText(
                    this@ExcelDataActivity,
                    "Something Went Wrong! Please try again later",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(
                    mTAG,
                    "Error in Fetching SMS Data from Sheet, Error: ${t.message} | StackTrace: ${t.printStackTrace()}"
                )
            }
        })
        /* post call example ---------------------->>>
        // create new object of the RequestBodyModel
        val requestBodyModel = RequestBodyModel(keys)

        val postCall1 = api.postAlarmKey(fullUrl = dynamicUrl, requestBodyModel = RequestBodyModel(keys))

        postCall1.enqueue(object : Callback<TokenResponseModel> {
            override fun onFailure(call: Call<TokenResponseModel>, t: Throwable) {
                Toast.makeText(this@KeypadActivity, getString(R.string.lbl_toast_msg_something_went_wrong_try_again_later), Toast.LENGTH_SHORT).show()
                Log.e("mTAG", "Error in POST (Send) Call, Error: ${t.message} | StackTrace: ${t.printStackTrace()}")
            }

            override fun onResponse(call: Call<TokenResponseModel>, response: Response<TokenResponseModel>) {
//                Log.e("mTAG", "Response: $response")
//                Log.e(mTAG, "Response.code(): ${response.code()}")
//                Log.e(mTAG, "Response.body(): ${response.body().toString()}")
                getAlarmStatus()
            }
        })

         */
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @SuppressLint("NewApi")
    private fun sendSMS(smsList: List<SMS>?) {
        // first check for the sms sending permission
        setupPermissions()

        val simCardList: ArrayList<Int> = ArrayList()
        val subscriptionManager: SubscriptionManager = SubscriptionManager.from(this)
        val subscriptionInfoList = subscriptionManager.activeSubscriptionInfoList
        for (subscriptionInfo in subscriptionInfoList) {
            val subscriptionId = subscriptionInfo.subscriptionId
            simCardList.add(subscriptionId)
        }

        val smsToSendFrom = simCardList[0] //assign your desired sim to send sms, or user selected choice

        /*SmsManager.getSmsManagerForSubscriptionId(smsToSendFrom)
            .sendTextMessage(
                phoneNumber,
                null,
                msg,
                sentPI,
                deliveredPI
            ) //use your phone number, message and pending intents*/

        // get single records and send SMS
//        val smsManager: SmsManager = SmsManager.getDefault()
        val smsManager: SmsManager = SmsManager.getSmsManagerForSubscriptionId(smsToSendFrom)
        for (member in smsList!!) {
            Log.e(
                mTAG,
                "Phone: ${member.mobileNo},  SMSLength:${member.smsText.length},  SMSText:${member.smsText}"
            )
//            divide message in multi parts for the 160 char limit
            val parts: ArrayList<String> = smsManager.divideMessage(member.smsText)
//           send all parts as messages
            smsManager.sendMultipartTextMessage(member.mobileNo, null, parts, null, null)
//            smsManager.sendTextMessage(member.mobileNo, null, member.smsText, null, null)
            Toast.makeText(
                this@ExcelDataActivity,
                "Sending SMS to ${member.mobileNo} in Background",
                Toast.LENGTH_SHORT
            ).show()
//            Log.e(mTAG, "SMS Sent Successfully to ${member.mobileNo}")
//            Log.e(mTAG, "SMS Sent is $parts")
        }
    }

    private fun setupPermissions() {
        // multiple permissions array
        val permissions = arrayOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE
        )
        // request for permissions
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "Please allow permissions for smooth working of the application",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i(mTAG, "SMS Sending Permission has been denied by user")
                } else {
                    Log.i(mTAG, "SMS Sending Permission has been granted by user")
                }
            }
        }

    }
}