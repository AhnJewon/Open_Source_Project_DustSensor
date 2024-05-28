package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.naver.maps.map.MapView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import kotlin.jvm.internal.Intrinsics;

public class MyMapView extends MapView {
    private Boolean intercept =false;
    private float slideBorder = 0.1f;

    public MyMapView( Context context) {
        super(context);
    }

    public MyMapView( Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMapView( Context context,  AttributeSet attrs, int defStyleAttr) {
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
