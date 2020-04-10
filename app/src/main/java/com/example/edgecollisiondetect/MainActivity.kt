package com.example.edgecollisiondetect

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var lastX: Int = 0
    var lastY: Int = 0    //保存手指点下的点的坐标
    lateinit var ballList: MutableList<Ball>
    lateinit var ballHandler : Handler
    var mThread: HandlerThread = HandlerThread("BallThread")
    lateinit var viewList:MutableList<View>
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewList = mutableListOf<View>()
        viewList.add(button)
        viewList.add(imageView)
        mThread.start()
        ballHandler = Handler(mThread.getLooper())
        ballList = mutableListOf<Ball>()
        var mBall = Ball(this@MainActivity, ballHandler,viewList)
        ballList.add(mBall)
        for (i in 0 until ballList.size) {
            ballList[i].start()
        }
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
//                ItemEdgeDetect(view)
                return true
            }
        })
    }
    fun isTouchButonEdge(mView:View,BeHitObject : View) :Boolean{
        var mBeHitObject = BeHitObject
        //左右判斷
        if(mView.bottom <= mBeHitObject.bottom+mView.height /2 && mView.top >=mBeHitObject.top-mView.height /2) {
            //right
            if (mView.right == mBeHitObject.left && mView.left < mBeHitObject.left) {
                Log.d("touch", "right Touch!")
            }
            //left
            if (mView.left == mBeHitObject.right && mView.right > mBeHitObject.right) {
                Log.d("touch", "left Touch!")
            }
        }
//        上下判斷
        if ((mView.left >= mBeHitObject.left - mView.width /2) &&
            (mView.right  <= mBeHitObject.right +mView.width /2)
        ){
            //bottom
            if (mView.top < mBeHitObject.bottom && mView.bottom == mBeHitObject.top) {
                Log.d("touch", "bottom Touch!")
            }
            //top
            if (mView.top == mBeHitObject.bottom && mView.bottom > mBeHitObject.bottom) {
                Log.d("touch", "top Touch!")
            }
        }
        return true
    }
    fun killBalls(mBall: Ball) {
        var index = ballList.indexOfFirst { it == mBall }
        var relative = findViewById<RelativeLayout>(R.id.activity_main_relativelayout)
        relative!!.removeView(ballList[index].imageView)
        ballList.remove(mBall)
//        showBallCount()
    }
    companion object {
        @kotlin.jvm.JvmField
        var MOVE_IMAGE: Int = 1
    }
}
