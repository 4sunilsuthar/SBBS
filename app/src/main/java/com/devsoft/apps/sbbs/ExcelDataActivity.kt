package com.devsoft.apps.sbbs

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excel_data2)
        viewManager = LinearLayoutManager(this)
//        viewAdapter = SmsAdapter(myDataset)

        val btnGetExcel: Button = findViewById(R.id.btnGetExcel)
        btnGetExcel.setOnClickListener {
            Toast.makeText(
                applicationContext,
                "Fetching data from Server",
                Toast.LENGTH_SHORT
            ).show()

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

                viewAdapter = SmsAdapter(this@ExcelDataActivity, response.body())
                recyclerView.adapter = viewAdapter
                //sendSMS(response.body())
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

    private fun sendSMS(smsList: List<SMS>?) {
        // first check for the sms sending permission
        setupPermissions()
        // get single records and send SMS
        val smsManager: SmsManager = SmsManager.getDefault()
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