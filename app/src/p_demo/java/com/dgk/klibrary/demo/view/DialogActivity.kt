package com.dgk.klibrary.demo.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dgk.R
import com.dgk.common.util.ToastManager
import com.dgk.klibrary.main.app.KApplication
import kotlinx.android.synthetic.main.demo_activity_dialog.*

class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_dialog)

        btn_dialog_01.setOnClickListener {
            Dialog01(this@DialogActivity, object : Dialog01.OnClickListener{
                override fun onLeft() {
                    ToastManager.toast(KApplication.getCtx(), "Left")
                }

                override fun onRight() {
                    ToastManager.toast(KApplication.getCtx(), "Right")
                }
            }).show()
        }

    }

}