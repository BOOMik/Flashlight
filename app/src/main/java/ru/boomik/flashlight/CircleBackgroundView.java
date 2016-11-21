package ru.boomik.flashlight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * Created by boomv on 15.05.2016.
 */
public class CircleBackgroundView extends View {

    private Paint mBackPaint;
    private int s;
    private int cy;
    private int cx;

    public CircleBackgroundView(Context context) {
        this(context, null, 0);
    }

    public CircleBackgroundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }
    public CircleBackgroundView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    Scroller mScroller;
    boolean mShowed;

    private void init() {
        mBackPaint = new Paint();
        mBackPaint.setColor(0xffb5b5b5);
        mBackPaint.setAntiAlias(true);
        mScroller = new Scroller(this.getContext(), new DecelerateInterpolator());
    }

    public void show() {
        setState(true);
    }

    public void hide() {
        setState(false);
    }

    public void setState(boolean b) {
        mShowed = b;
        mScroller.startScroll(0,0,1000,1000,300);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            int w = getWidth();
            int h = getHeight();
            s = Math.max(w, h);
            cx = w /2;
            cy = h /2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mScroller.computeScrollOffset ();
        float pos = mScroller.getCurrX() / 1000f;
        float r;
        if (!mShowed) r =s*(1- pos); else r =s* pos;
        r = Math.max(1, r);
        mBackPaint.setColor(0xffb5b5b5);
        canvas.drawCircle(cx,cy, r,mBackPaint);
        if (!mScroller.isFinished()) invalidate();
    }

    public boolean isShowed() {
        return mShowed;
    }
}
