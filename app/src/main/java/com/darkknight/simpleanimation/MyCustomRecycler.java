package com.darkknight.simpleanimation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;




public class MyCustomRecycler extends RecyclerView {

    private boolean mIsForceCentering;
    private CircularHorizontalMode mCircularHorizontalMode;

    private CenterRunnable mCenterRunnable = new CenterRunnable();

    public MyCustomRecycler(final Context context) {
        super(context);
    }

    public MyCustomRecycler(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCustomRecycler(final Context context, @Nullable final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }


    public class CenterRunnable implements Runnable {

        private WeakReference<View> mView;

        public void setView(View v) {
            mView = new WeakReference<View>(v);
        }

        @Override
        public void run() {
            smoothScrollToView(findViewAtCenter());
            mIsForceCentering = true;
        }
    }


    @Override
    public void requestLayout() {
        super.requestLayout();

        if (getLayoutManager() != null && mCircularHorizontalMode != null) {
            int count = getLayoutManager().getChildCount();
            for (int i = 0; i < count; ++i) {
                View v = getChildAt(i);
                mCircularHorizontalMode.applyToView(v, this);
            }
        }
    }

    @Override
    protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
        super.onLayout(changed, l, t, r, b);

        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        if (layoutManager.canScrollHorizontally()) {
            setPadding(getWidth() / 2, 0, getWidth() / 2, 0);
        } else if (layoutManager.canScrollVertically()) {
            setPadding(0, getHeight() / 2, 0, getHeight() / 2);
        }

        setClipToPadding(false);
        setClipChildren(false);

        smoothScrollToView(findViewAtCenter());


    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCircularHorizontalMode != null) {
            final int count = getChildCount();
            for (int i = 0; i < count; ++i) {
                View v = getChildAt(i);
                mCircularHorizontalMode.applyToView(v, this);
            }
        }


    }

    public void setViewMode(CircularHorizontalMode mode) {
        this.mCircularHorizontalMode = mode;
    }

    @Override
    public void onScrollStateChanged(final int state) {
        if (state == SCROLL_STATE_IDLE) {
            if (!mIsForceCentering) {
                mIsForceCentering = true;
                mCenterRunnable.setView(findViewAtCenter());
                ViewCompat.postOnAnimation(this, mCenterRunnable);
            }
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent e) {
        removeCallbacks(mCenterRunnable);
        mIsForceCentering = false;
        return super.onTouchEvent(e);
    }

    public void smoothScrollToView(final View v) {
        int distance = 0;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            if (v != null) {
                final float x = v.getX() + v.getWidth() * 0.5f;
                final float halfWidth = getWidth() * 0.5f;
                distance = (int) (x - halfWidth);
            }
        } else
            throw new IllegalArgumentException("CircleRecyclerView just support T extend LinearLayoutManager!");
        smoothScrollBy((int) distance, distance);
    }

    public View findViewAt(int x, int y) {
        final int count = getChildCount();
        if (count >= 3) {
            for (int i = 0; i < count; ++i) {
                final View v = getChildAt(i);
                final int x0 = v.getLeft();
                final int y0 = v.getTop();
                final int x1 = v.getWidth() + x0;
                final int y1 = v.getHeight() + y0;
                if (x >= x0 && x <= x1 && y >= y0 && y <= y1) {
                    return v;
                }
            }
        } else {
            return getChildAt(0);
        }
        return null;
    }

    public View findViewAtCenter() {
        if (getLayoutManager().canScrollVertically()) {
            return findViewAt(0, getHeight() / 2);
        } else if (getLayoutManager().canScrollHorizontally()) {
            return findViewAt(getWidth() / 2, 0);
        }
        return null;
    }
}
