package com.dgk.klibrary.demo.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.dgk.R
import kotlinx.android.synthetic.main.demo_dialog_01.*

/**
 * 标准对话框 01
 */
class Dialog01(val ctx: Context, val listener: OnClickListener) : Dialog(ctx, R.style.DefaultDialogStyle), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_dialog_01)

//        val lp = window.attributes

        /*
            设置对话框的高度和宽度:对话框的宽高需要：
            - 代码设置：window.attributes
            - XML设置：设置给子控件，不要设置给rootView，那样不起作用。
            所以说，如果XML中的rootView有子控件，可以将宽高设置给子控件，否则用代码设置。
         */
//        lp.width = AutoUtils.getPercentWidthSize(660)
//        lp.height = AutoUtils.getPercentHeightSize(420)
//        lp.alpha = 1.0f

        /*
         * lp.x与lp.y表示相对于边缘的偏移.
         * lp.gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |Gravity.CENTER_VERTICAL，此时lp.x与lp.y的值被忽略。
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时,对话框水平居中,lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时,对话框垂直居中,lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
        */

        // 设置对话框显示在底部，并且向上偏移46px
//        lp.gravity = Gravity.BOTTOM
//        lp.y = AutoUtils.getPercentHeightSize(46)

        // 将LayoutParams参数设置给window
//        window.attributes = lp

        // 设置点击对话框外部区域不消失
        setCanceledOnTouchOutside(false)

        // 设置点击返回键不可以取消
        setCancelable(false)


        btn_left.setOnClickListener(this)
        btn_right.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_left -> {
                listener.onLeft()
                this.dismiss()
            }
            R.id.btn_right -> {
                listener.onRight()
                this.dismiss()
            }
        }
    }

    override fun dismiss() {
        super.dismiss()

    }

    interface OnClickListener {
        /**
         * 左侧按钮
         */
        fun onLeft()

        /**
         * 右侧按钮
         */
        fun onRight()
    }
}