package com.example.edgecollisiondetect

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var lastX: Int = 0
    var lastY: Int = 0    //保存手指点下的点的坐标
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //初始设置一个layoutParams
        val layoutParams = RelativeLayout.LayoutParams(100, 100)
        imageView!!.layoutParams = layoutParams
        //设置屏幕触摸事件
        imageView!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent?): Boolean {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.rawX.toInt()
                        lastY = event.rawY.toInt()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        //计算出需要移动的距离
                        var dx = event.rawX - lastX;
                        var dy = event.rawY - lastY;
                        //将移动距离加上，现在本身距离边框的位置
                        var left = view.getLeft() + dx;
                        var top = view.getTop() + dy;
                        //获取到layoutParams然后改变属性，在设置回去
                        var layoutParams = view.getLayoutParams() as RelativeLayout.LayoutParams
                        layoutParams.height = 100;
                        layoutParams.width = 100;
                        layoutParams.leftMargin = left.toInt();
                        layoutParams.topMargin = top.toInt();
                        view.setLayoutParams(layoutParams);
                        //记录最后一次移动的位置
                        lastX = event.rawX.toInt()
                        lastY = event.rawY.toInt()
                    }
                }
                //刷新界面
                view.invalidate()
                isTouchButonEdge(view)
                return true
            }
        })
    }
    fun isTouchButonEdge(mView:View) :Boolean{
        var mButton = button
        //左右判斷
        if(mView.bottom <= button.bottom+mView.height /2 && mView.top >=button.top-mView.height /2) {
            //right
            if (mView.right == button.left && mView.left < button.left) {
                Log.d("touch", "right Touch!")
            }
            //left
            if (mView.left == button.right && mView.right > button.right) {
                Log.d("touch", "left Touch!")
            }
        }
//        上下判斷
        if ((mView.left >= button.left - mView.width /2) &&
            (mView.right  <= button.right +mView.width /2)
        ){
            //bottom
            if (mView.top < button.bottom && mView.bottom == button.top) {
                Log.d("touch", "bottom Touch!")
            }
            //top
            if (mView.top == button.bottom && mView.bottom > button.bottom) {
                Log.d("touch", "top Touch!")
            }
        }
        return true
    }
}
