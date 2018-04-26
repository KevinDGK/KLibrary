package com.dgk.common.util

import android.app.Application
import android.view.Gravity
import android.widget.Toast

/**
 * Created by daigaokai on 2018/4/26.
 */
object ToastManager {

    private var toast: Toast? = null

    fun toast(ctx: Application, content: String) {
        if (toast == null) {
            toast = Toast.makeText(ctx, content, Toast.LENGTH_SHORT)
        }
        // 注意：小米手机如果直接Toast.makeText(ctx)，那么显示的内容包为：应用名称:content，再执行一遍setText()就不会显示应用名称了
        toast?.setText(content)
        toast?.duration = Toast.LENGTH_SHORT
        toast?.setGravity(Gravity.CENTER, 0 ,0)
        toast?.show()
    }
}