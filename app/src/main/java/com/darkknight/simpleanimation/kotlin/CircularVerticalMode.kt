package com.darkknight.simpleanimation.kotlin

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.cos

class CircularVerticalMode() {
    private var mCircleOffset = 500
    private var mDegToRad = 1.0f / 180.0f * Math.PI.toFloat()
    private var mScalingRatio = 0.0001f
    private var mTranslationRatio = 0.15f

    constructor(circleOffset: Int, degToRad: Float, scalingRatio: Float, translationRatio: Float)
            : this() {
        mCircleOffset = circleOffset
        mDegToRad = degToRad
        mScalingRatio = scalingRatio
        mTranslationRatio = translationRatio
    }

    fun applyToView(view: View, recyclerView: RecyclerView) {
        val halfHeight = view.height * 0.5f
        val parentHalfHeight = recyclerView.height * 0.5f

        val y = view.y
        val rot = parentHalfHeight - halfHeight - y

        view.apply {
            pivotY = halfHeight
            pivotX = 0.0f
            translationX =
                    ((-cos(rot * mTranslationRatio * mDegToRad.toDouble()) + 1) * mCircleOffset).toFloat()

            val scale =
                    1.0f - abs(parentHalfHeight - halfHeight - x) * mScalingRatio

            scaleX = scale
            scaleY = scale
        }

    }
}