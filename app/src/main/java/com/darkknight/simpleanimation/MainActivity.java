package com.darkknight.simpleanimation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Rect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;

import android.view.ViewTreeObserver;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.darkknight.simpleanimation.kotlin.CircularHorizontalMode;
import com.darkknight.simpleanimation.kotlin.CircularRecyclerView;


public class MainActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener, onItemClickListener {

    private TextView tv1, tv2, tv3, tv4;
    private Rect rect, rect1, rect2, rect3, rect4;
    private RelativeLayout layout;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);

        CircularRecyclerView myCustomRecycler = findViewById(R.id.recycler);
        myCustomRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        myCustomRecycler.setViewMode(new CircularHorizontalMode());
        myCustomRecycler.setAdapter(new CustomAdapter(this));

        layout = findViewById(R.id.layout);
        ViewTreeObserver viewTreeObserver = layout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(this);

    }


    private void animateText(final Rect rectq, final TextView tv, final TextView tv1) {
        PropertyValuesHolder holders1 = PropertyValuesHolder.ofFloat("scaleX", 2, 1);
        PropertyValuesHolder holders2 = PropertyValuesHolder.ofFloat("scaleY", 2, 1);
        tv.setText(tv1.getText());
        int[] a = new int[2];
        int[] b = new int[2];
        tv1.getLocationInWindow(a);
        tv.getLocationInWindow(b);
        Log.e("abcd", "x :" + b[0] + "   y :" + b[1]);
        Log.e("abcd", "x :" + a[0] + "   y :" + a[1]);
        tv.setVisibility(View.VISIBLE);
        Log.e("center", rectq.toString());
//        PropertyValuesHolder holderx = PropertyValuesHolder.ofFloat("x", rect.exactCenterX(), rectq.exactCenterX());
//        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("y", rect.exactCenterY(), rectq.exactCenterY());
        PropertyValuesHolder holderx = PropertyValuesHolder.ofFloat("translationX", rectq.left + tv1.getWidth() / 2, 0);
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("translationY", rectq.top - tv1.getHeight() / 2, 0);
        ObjectAnimator.ofPropertyValuesHolder(tv, holders1, holders2, holderx, holderY).setDuration(1000).start();
    }

    @Override
    public void onGlobalLayout() {
        layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        rect1 = new Rect();
        rect2 = new Rect();
        rect3 = new Rect();
        rect4 = new Rect();
        tv1.getGlobalVisibleRect(rect1);
        tv2.getGlobalVisibleRect(rect2);
        tv3.getGlobalVisibleRect(rect3);
        tv4.getGlobalVisibleRect(rect4);
    }

    @Override
    public void onClick(final View v) {
        count += 1;
        rect = new Rect();
        v.getGlobalVisibleRect(rect);
        Log.e("coordinates", rect.toString());
        switch (count) {
            case 1:
                animateText(rect1, tv1, (TextView) v);
                break;

            case 2:
                animateText(rect3, tv3, (TextView) v);
                break;

            case 3:
                animateText(rect4, tv4, (TextView) v);
                break;

            case 4:
                animateText(rect2, tv2, (TextView) v);
                break;

            default:
                break;
        }

    }
}





