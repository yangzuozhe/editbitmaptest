package com.yzz.editlibrary

import android.graphics.Bitmap
import android.graphics.Matrix

class BitmapUtils {
    companion object {
        /**
         * 根据给定的宽和高进行拉伸
         *
         * @param origin    原图
         * @param newWidth  新图的宽
         * @param newHeight 新图的高
         * @return new Bitmap
         */
        fun scaleBitmap(origin: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
            if (origin == null) {
                return null
            }
            val height: Int = origin.getHeight()
            val width: Int = origin.getWidth()
            val scaleWidth = newWidth.toFloat() / width
            val scaleHeight = newHeight.toFloat() / height
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight) // 使用后乘
            val newBM: Bitmap = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
            recycleBitmap(origin)
            return newBM
        }

        /**
         * 按比例缩放图片
         *
         * @param origin 原图
         * @param ratio  比例
         * @return 新的bitmap
         */
        fun scaleBitmap(origin: Bitmap?, ratio: Float): Bitmap? {
            if (origin == null) {
                return null
            }
            val width: Int = origin.getWidth()
            val height: Int = origin.getHeight()
            val matrix = Matrix()
            matrix.preScale(ratio, ratio)
            val newBM: Bitmap = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
            if (newBM.equals(origin)) {
                return newBM
            }
            recycleBitmap(origin)
            return newBM
        }

        /**
         * 裁剪
         *
         * @param bitmap 原图
         * @return 裁剪后的图像
         */
        fun cropBitmap(bitmap: Bitmap): Bitmap {
            val w: Int = bitmap.getWidth() // 得到图片的宽，高
            val h: Int = bitmap.getHeight()
            var cropWidth = if (w >= h) h else w // 裁切后所取的正方形区域边长
            cropWidth /= 2
            val cropHeight = (cropWidth / 1.2).toInt()
            return Bitmap.createBitmap(bitmap, w / 3, 0, cropWidth, cropHeight, null, false)
        }

        /**
         * 旋转图片
         *
         * @param origin 原图
         * @param alpha  旋转角度，可正可负
         * @return 旋转后的图片
         */
        fun rotateBitmap(origin: Bitmap?, alpha: Float): Bitmap? {
            if (origin == null) {
                return null
            }
            val width: Int = origin.width
            val height: Int = origin.height
            val matrix = Matrix()
            matrix.setRotate(alpha)
            // 围绕原地进行旋转
            val newBM: Bitmap = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
            if (newBM.equals(origin)) {
                return newBM
            }
            recycleBitmap(origin)
            return newBM
        }

        /**
         * 偏移效果
         * @param origin 原图
         * @return 偏移后的bitmap
         */
        fun skewBitmap(origin: Bitmap?): Bitmap? {
            if (origin == null) {
                return null
            }
            val width: Int = origin.getWidth()
            val height: Int = origin.getHeight()
            val matrix = Matrix()
            matrix.postSkew(-0.6f, -0.3f)
            val newBM: Bitmap = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
            if (newBM.equals(origin)) {
                return newBM
            }
            recycleBitmap(origin)
            return newBM
        }

        /**
         * 水平镜像翻转
         */
        fun toHorizontalMirror(origin: Bitmap): Bitmap {
            val w = origin.width
            val h = origin.height
            val matrix = Matrix()
            matrix.postScale(-1f, 1f) //
            val newBitmap = Bitmap.createBitmap(origin, 0, 0, w, h, matrix, true)
            recycleBitmap(origin)
            return newBitmap
        }

        /**
         *  垂直镜像翻转
         */
        fun toVerticalMirror(origin: Bitmap): Bitmap {
            val w = origin.width
            val h = origin.height
            val matrix = Matrix()
            matrix.postScale(1f, -1f) //
            val newBitmap = Bitmap.createBitmap(origin, 0, 0, w, h, matrix, true)
            recycleBitmap(origin)
            return newBitmap
        }

        fun recycleBitmap(bitmap: Bitmap?) {
            if (bitmap != null && !bitmap.isRecycled && bitmap.isMutable) {
                bitmap.recycle()
            }
        }
    }


}