package com.dgk.klibrary.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dgk.R
import com.dgk.common.util.getFormatDate
import com.dgk.common.util.log.KLogi
import com.dgk.common.util.log.UploadLogIntentService
import com.dgk.klibrary.main.app.CONFIG_SERVER_URL
import kotlinx.android.synthetic.main.demo_activity_test.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_test)

        setSupportActionBar(toolbar)

        btn_upload_log.setOnClickListener {
            KLogi(this@TestActivity, "点击日志上传按钮")

            val today = getFormatDate(pattern = "yyyyMMdd")

            UploadLogIntentService.launch(this@TestActivity, today, "$CONFIG_SERVER_URL/uploadFile")
        }
    }
}