package com.yzz.editlibrary.edit_box

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import kotlin.math.max

class MyFrameLayout : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var moveX = 0f
    private var moveY = 0f

    //判断我们是否点击到了子控件，如果没有点击到子控件就返回false，那么就把子控件放大
    var mIsMove = false
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val childView = getChildAt(0)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mIsMove =
                    !((event.x !in childView.x..(childView.x + childView.width)) || (event.y !in childView.y..childView.y + childView.height))
                moveX = event.x
                moveY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (!mIsMove) {
                    return true
                }
                val layoutParams = childView.layoutParams
                val xCha = event.x - childView.x
                val yCha = event.y - childView.y
                val maxN = max(xCha, yCha).toInt()
                if (maxN < 100) {
                    return true
                }
                layoutParams.width = maxN
                layoutParams.height = maxN
                childView.layoutParams = layoutParams
            }
        }
        return true
    }


}