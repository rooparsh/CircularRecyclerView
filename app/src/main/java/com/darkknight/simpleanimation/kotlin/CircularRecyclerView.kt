package com.darkknight.simpleanimation.kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

class CircularRecyclerView : RecyclerView {

    private var mIsForceCentering = false
    private val mCenterRunnable: CircularRunnable by lazy { CircularRunnable() }
    private var mCircularHorizontalMode: CircularHorizontalMode? = null


    constructor(context: Context) : super(context)

    constructor(context: Context,
                attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context,
                attributeSet: AttributeSet,
                defStyle: Int) : super(context, attributeSet, defStyle)


    inner class CircularRunnable : Runnable {

        private lateinit var mView: WeakReference<View?>

        fun setView(view: View?) {
            mView = WeakReference(view)
        }

        override fun run() {
            smoothScrollToView(mView.get())
            mIsForceCentering = true
        }

    }

    private fun findViewAtCenter(): View? {
        layoutManager?.let { manager ->
            return when {
                manager.canScrollVertically() -> {
                    findViewAt(0, height / 2)
                }
                manager.canScrollHorizontally() -> {
                    findViewAt(width / 2, 0)
                }
                else -> {
                    null
                }
            }
        } ?: kotlin.run { return null }
    }

    private fun findViewAt(x: Int, y: Int): View? {
        val count = childCount
        if (count > 3) {
            (0..count).forEach { i ->
                val view = getChildAt(i)
                view?.let {
                    val x0: Int = view.left
                    val y0: Int = view.top
                    val x1: Int = view.width + x0
                    val y1: Int = view.height + y0
                    if (x in x0..x1 && y >= y0 && y <= y1) {
                        return view
                    }
                }

            }
        } else {
            return getChildAt(0)
        }
        return null
    }


    fun smoothScrollToView(view: View?) {
        var distance = 0
        if (layoutManager is LinearLayoutManager) {
            view?.let { v ->
                val x: Float = v.x + v.width * 0.5f
                val halfWidth = width * 0.5f
                distance = (x - halfWidth).toInt()
            }
        } else throw IllegalArgumentException("CircleRecyclerView just support T extend LinearLayoutManager!")
        smoothScrollBy(distance, distance)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        removeCallbacks(mCenterRunnable)
        mIsForceCentering = false
        return super.onTouchEvent(e)
    }

    override fun onScrollStateChanged(state: Int) {
        if (state == SCROLL_STATE_IDLE) {
            if (!mIsForceCentering) {
                mIsForceCentering = true
                mCenterRunnable.setView(findViewAtCenter())
                this.postOnAnimation(mCenterRunnable)
            }
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        mCircularHorizontalMode?.let { circularMode ->
            val count = childCount
            (0 until count).map { getChildAt(it) }
                    .forEach {
                        circularMode.applyToView(it, this)
                    }
        }
    }

    fun setViewMode(mode: CircularHorizontalMode?) {
        mCircularHorizontalMode = mode
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        val layoutManager = layoutManager as LinearLayoutManager?
        if (layoutManager!!.canScrollHorizontally()) {
            setPadding(width / 2, 0, width / 2, 0)
        } else if (layoutManager.canScrollVertically()) {
            setPadding(0, height / 2, 0, height / 2)
        }
        clipToPadding = false
        clipChildren = false
        smoothScrollToView(findViewAtCenter())
    }

    override fun requestLayout() {
        super.requestLayout()
        if (layoutManager != null && mCircularHorizontalMode != null) {
            val count = layoutManager!!.childCount
            for (i in 0 until count) {
                val v = getChildAt(i)
                mCircularHorizontalMode!!.applyToView(v, this)
            }
        }
    }
}