package com.yzz.editlibrary.edit_box

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewParent
import androidx.appcompat.widget.AppCompatImageView
import com.yzz.editlibrary.BitmapUtils
import kotlin.math.abs

class MyImageView : AppCompatImageView {
    private var moveX = 0f
    private var moveY = 0f
    private var mChaX = 0f
    private var mChaY = 0f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawBox(it)
        }
    }

    /**
     * 画出编辑的外框
     */
    private fun drawBox(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL
        canvas.drawCircle(width.toFloat(), height.toFloat(), 50f, paint)
        paint.color = Color.GREEN
        canvas.drawCircle(0f, height.toFloat(), 50f, paint)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                moveX = event.x
                moveY = event.y
                if (moveX in (width.toFloat() - 60)..width.toFloat() && moveY in height.toFloat() - 60..height.toFloat()) {
                    //点击右下角，父控件可以操作
                    return false
                }
                if (moveX in 0f..60f && moveY in height.toFloat() - 60..height.toFloat()) {
                    //点击左下角，子控件翻转
                    bitmapHorizontalMirror()
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount > 1) {
                    //双指缩放
                    twoPointEvent(event)
                } else if (event.pointerCount == 1) {
                    //单指按住移动
                    val newX = x + (event.x - moveX)
                    val newY = y + (event.y - moveY)
                    //设置子view 移动的边界
//                if (setBoundary(newX, newY)) {
//                    return true
//                }
                    // getX 控件距离屏幕左上角的x点
                    x = newX
                    y = newY
                }

            }
            MotionEvent.ACTION_UP -> {
            }
            MotionEvent.ACTION_CANCEL -> {}
        }
        return true
    }

    /**
     * 子控件翻转
     */
    private fun bitmapHorizontalMirror() {
        val drawable: BitmapDrawable = drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val newBitmap = BitmapUtils.toHorizontalMirror(bitmap)
        setImageBitmap(newBitmap)
    }


    /**
     * 双指放大缩小
     */
    private fun twoPointEvent(event: MotionEvent) {
        // 获取指针ID
        val oneId = event.getPointerId(0)
        val twoId = event.getPointerId(1)
        // 使用指针 ID 查找活动指针的索引
        // 并获取它的位置

        val (oneX: Float, oneY: Float) = event.findPointerIndex(oneId)
            .let { pointerIndex ->
                // 获取指针的当前位置
                event.getX(pointerIndex) to event.getY(pointerIndex)
            }
        val (towX: Float, towY: Float) = event.findPointerIndex(twoId)
            .let { pointerIndex ->
                //获取指针的当前位置
                event.getX(pointerIndex) to event.getY(pointerIndex)
            }
        val chaX = abs(oneX - towX)
        val chaY = abs(oneY - towY)
        if (mChaX != 0F && mChaY != 0F) {
            if (mChaX > chaX && mChaY > chaY) {
                //缩小
                scaleImage(false)
            } else if (mChaX < chaX && mChaY < chaY) {
                //放大
                scaleImage(true)
            }
        }
        mChaX = chaX
        mChaY = chaY
    }

    /**
     * 图片的放大缩小
     */
    private fun scaleImage(isBig: Boolean) {
        val layoutParams = layoutParams
        val scale = 10
        if (isBig) {
            layoutParams.height += scale
            layoutParams.width += scale
        } else {
            layoutParams.height -= scale
            layoutParams.width -= scale
        }
        setLayoutParams(layoutParams)
    }


    /**
     * 设置子view 移动的边界
     */
    private fun setBoundary(newX: Float, newY: Float): Boolean {
        //设置移动的左右上下边界
        if (newX < 0) {
            return true
        }
        if (newY < 0) {
            return true
        }
        val parentView: ViewParent = parent
        if (parentView is MyFrameLayout) {
            if (newX + width > parentView.width) {
                return true
            }
            if (newY + height > parentView.height) {
                return true
            }
        }
        return false
    }
}