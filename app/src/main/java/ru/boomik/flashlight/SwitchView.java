package ru.boomik.flashlight;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * Created by zhanglei on 15/6/27.
 */
public class SwitchView extends View {

    private Paint mBackPaint;
    private static final float mStrokeWidth = 5;
    public static final int CHECK_COLOR = 0xFF333333;
    public static final int UNCHECK_COLOR = 0xffb5b5b5;


    private boolean isChecked = false;

    Scroller mScroller;

    private OnCheckedChangeListener mOnCheckedListener;


    public SwitchView(Context context) {
        this(context, null, 0);
    }

    public SwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    final float[] from = new float[3],
            to =   new float[3];

    private void init() {
        mBackPaint = new Paint();
        mBackPaint.setColor(0xffffffff);
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setStrokeWidth(mStrokeWidth);
        mScroller = new Scroller(this.getContext(), new DecelerateInterpolator());
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(!isChecked,true);
            }
        });

        Color.colorToHSV(CHECK_COLOR, from);   // from white
        Color.colorToHSV(UNCHECK_COLOR, to);     // to red

        setChecked(true,false);


    }

    private void initAnimator() {
        mScroller.startScroll(0,0,1000,1000,300);
        invalidate();
    }

    public void setChecked(boolean check, boolean animated) {
        if (!mScroller.isFinished()) return;
        if (mOnCheckedListener !=null && check!=isChecked) {
            mOnCheckedListener.onCheckedChanged(this, check);
        }
        isChecked = check;
        if (animated) initAnimator();
    }

    // interface to get isChecked state
    public boolean isChecked() {
        return isChecked;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedListener = listener;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            w = getWidth();
            h = getHeight();
            r = w>h ? h/2f : w/2f; //100
            o = r/6f; //16.66
            ri = (r-o); //83.33
        }
    }

    int w; //200
    int h; //300
    float r; //100
    float o; //16.66
    float ri; //83.33
    float pos;

    float start;
    float end; // 183

    float maxH; //100

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mScroller.computeScrollOffset ();
        pos = mScroller.getCurrX() / 1000f;
        mBackPaint.setColor(getCurColor(to,from,pos));
        canvas.drawRoundRect(0,0,w,h,r,r,mBackPaint);

        mBackPaint.setColor(getCurColor(from,to, pos));
        start =  o;
        end = (o+ri*2f); // 183
        maxH = (h-o*2f-ri*2f); //100


        if (mScroller.isFinished()) {
            start = start*1;
        }

        if (!isChecked) {
            if (pos<=0.5) {
                end += maxH*pos*2f;
            } else {
                start += maxH*((pos-0.5f)*2f);
                end += maxH;
            }
        } else {
            if (pos<=0.5) {
                start = start + maxH - (maxH*(pos*2f));
                end =  end + maxH;
            } else {
                end = end + maxH - maxH* ((pos-0.5f)*2f);
            }
        }
       canvas.drawRoundRect(o,start,w-o, end, ri, ri, mBackPaint);

        if (!mScroller.isFinished()) invalidate();
    }

    final float[] hsv  = new float[3];
    private int getCurColor(float[] from, float[] to, double ratio) {
        if (!mScroller.isFinished()) {
            ratio = isChecked ? ratio : 1 - ratio;
            hsv[0] = (float) (from[0] + (to[0] - from[0])*ratio);
            hsv[1] = (float) (from[1] + (to[1] - from[1])*ratio);
            hsv[2] = (float) (from[2] + (to[2] - from[2])*ratio);
        } else {
            return isChecked ? Color.HSVToColor(to) : Color.HSVToColor(from);
        }
        return Color.HSVToColor(hsv);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(View view, boolean isChecked);
    }
}
