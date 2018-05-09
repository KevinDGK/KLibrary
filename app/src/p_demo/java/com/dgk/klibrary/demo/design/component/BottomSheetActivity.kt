package com.dgk.klibrary.demo.design.component

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.dgk.R
import com.dgk.common.util.log.KLogi
import kotlinx.android.synthetic.main.demo_activity_bottom_sheet.*

class BottomSheetActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_bottom_sheet)


        val behavior = BottomSheetBehavior.from(bottom_sheet)

        // 设置是否可以滑动隐藏，true-下滑可以隐藏掉该控件，可以在xml中设置该属性
        behavior.isHideable = true

        // 设置peek区域的高度，可以在xml中设置该属性
//        behavior.peekHeight = 100

        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> KLogi("STATE_EXPANDED")   // 展开，全部显示
                    BottomSheetBehavior.STATE_HIDDEN -> KLogi("STATE_HIDDEN")       // 隐藏
                    BottomSheetBehavior.STATE_COLLAPSED -> KLogi("STATE_COLLAPSED") // 折叠，仅显示peek区域
                    BottomSheetBehavior.STATE_DRAGGING -> KLogi("STATE_DRAGGING")   // 拖拽
                    BottomSheetBehavior.STATE_SETTLING -> KLogi("STATE_SETTLING")   // 松开手指后，自动滑动到最终位置的过程
                    else -> KLogi("unknown state: $newState")
                }
            }
        })

        // 通过按钮控制该控件的显示和隐藏，效果类似于底部的对话框
        btn_bottom_sheet.setOnClickListener {
            val isSelect = bottom_sheet.isSelected
            KLogi("点击 btn_bottom_sheet 按钮，the select state of the bottom sheet is :$isSelect")
            if (isSelect) {
                behavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            // 实际是通过改变bottom_sheet的isSelect状态来实现隐藏和显示
            bottom_sheet.isSelected = !isSelect
        }
    }
}