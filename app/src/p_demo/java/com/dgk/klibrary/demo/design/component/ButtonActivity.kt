package com.dgk.klibrary.demo.design.component

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.OvershootInterpolator
import com.dgk.R
import com.dgk.common.util.log.KLogi
import com.dgk.common.util.ToastManager
import com.dgk.klibrary.main.app.KApplication
import kotlinx.android.synthetic.main.demo_activity_button.*

class ButtonActivity : AppCompatActivity() {

    var isExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_button)

        setSupportActionBar(toolbar)

        btn.setOnClickListener {
            ToastManager.toast(KApplication.getCtx(), "Button")
        }

        img_btn.setOnClickListener {
            ToastManager.toast(KApplication.getCtx(), "ImageButton")
        }

        sw.setOnCheckedChangeListener { buttonView, isChecked ->
            KLogi(this@ButtonActivity, "开关状态变为：$isChecked")
        }


        /*
            Floating action bar 浮动操作按钮
            - 点击该按钮，可以弹出一个选择列表，表示本应用或者本页面的主要操作
            - 该按钮的功能应该进行比较积极的操作，如创建、收藏、分享、浏览和探索。
            - 正常情况下，建议一个页面最多一个该按钮

            两种尺寸：
            - 默认尺寸：适用于多数应用情况
            - 迷你尺寸：仅用于创建与其他屏幕元素视觉的连续性

            外边距：浮动操作按钮应至少放在距手机边缘 16dp 或电脑/台式机边缘 24dp 的地方，用来显示阴影效果
         */
        fab.setOnClickListener {
//            // 点击浮动操作按钮，弹出一个Snackbar
//            Snackbar.make(fab, "基地受到不明生物攻击!", Snackbar.LENGTH_LONG)
//                    .setAction("OK", object : View.OnClickListener {
//                        override fun onClick(v: View?) {
//                            // 点击Snackbar的按钮，给出一个提示
//                            ToastManager.toast(KApplication.getCtx(), "正在前往支援")
//                        }
//                    })
//                    .show()

            if (!isExpanded) {
                showFabList()
            } else {
                hideFabList()
            }
        }
    }

    /**
     *  显示列表
     */
    private fun showFabList() {

        isExpanded = true

        fab.setImageResource(R.mipmap.demo_design_icon_cross_64)

        loadObjectAnimator(ll_item_03)
        loadObjectAnimator(ll_item_02)
        loadObjectAnimator(ll_item_01)
    }

    /**
     * 隐藏列表
     */
    private fun hideFabList() {

        isExpanded = false

        fab.setImageResource(R.mipmap.demo_design_icon_add_64)

        ll_item_01.visibility = View.GONE
        ll_item_02.visibility = View.GONE
        ll_item_03.visibility = View.GONE
    }

    /**
     * 执行属性动画
     * @param view 执行动画的目标View
     */
    private fun loadObjectAnimator(view: View) {
//        val rotation = ObjectAnimator.ofFloat(view, "rotation", -90F, 0F)
        // 透明度动画
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 0F, 0.2F, 0.4F, 0.6F, 0.8F,1F)
        // 位移动画
        val translationY = ObjectAnimator.ofFloat(view, "translationY", -100F, 0F)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationY, alpha)
        animatorSet.duration = 500
        animatorSet.interpolator = OvershootInterpolator()
        animatorSet.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                view.visibility = View.VISIBLE
            }
        })
        animatorSet.start()
    }
}