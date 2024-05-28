package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

import androidx.annotation.NonNull;

public class MyWebView extends WebView {
    private Boolean intercept =false;
    private float slideBorder = 0.1f;
    public MyWebView( Context context) {
        super(context);
    }

    public MyWebView( Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView( Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            intercept = (event.getX() > getWidth() *slideBorder && event.getX() < getWidth() *(1-slideBorder));
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            getParent().requestDisallowInterceptTouchEvent(intercept);
        }
        Log.i("touch", String.valueOf(event.getX()));
        return super.dispatchTouchEvent(event);
    }
}
