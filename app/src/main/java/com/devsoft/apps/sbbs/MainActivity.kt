package com.devsoft.apps.sbbs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnExcelActivity: Button = findViewById(R.id.btnExcel)
        btnExcelActivity.setOnClickListener {
            val intent = Intent(applicationContext, ExcelDataActivity::class.java)
            startActivity(intent)
        }
    }
}
    