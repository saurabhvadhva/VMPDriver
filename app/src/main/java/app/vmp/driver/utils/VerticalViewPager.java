package app.vmp.driver.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

import androidx.core.view.MotionEventCompat;
import androidx.viewpager.widget.ViewPager;


import java.lang.reflect.Field;

public class VerticalViewPager extends ViewPager {
    public VerticalViewPager(Context context) {
        super(context);
        this.init();
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    private void init() {
        this.setPageTransformer(true, new VerticalPageTransformer());
        this.setOverScrollMode(2);

    }

    private ScrollerCustomDuration mScroller = null;

    private void postInitViewPager() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ScrollerCustomDuration(getContext(),
                    (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return true;
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }

    private MotionEvent swapXY(MotionEvent ev) {
        float width = (float)this.getWidth();
        float height = (float)this.getHeight();
        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;
        ev.setLocation(newX, newY);
        return ev;
    }


    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = super.onInterceptTouchEvent(swapXY(event));
        switch (event.getAction() & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                break;
        }
        swapXY(event); // return touch coordinates to original reference frame for any child views
        return intercepted;
//        boolean intercepted = super.onInterceptTouchEvent(this.swapXY(ev));
//        this.swapXY(ev);
//        return intercepted;
//        return false;
    }

    private boolean isSwipeNews = false;

    public void enableClick(){
        isSwipeNews = true;
    }

    float x = 0;
    float mStartDragX = 0;
    private static final float SWIPE_X_MIN_THRESHOLD = 20;

    private static int MAX_THRESHOLD = 130;
    private static int MIN_THRESHOLD = 50;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getAdapter() != null) {
            if (getCurrentItem() >= 0 || getCurrentItem() < getAdapter().getCount()) {
                swapXY(event);
                final int action = event.getAction();
                switch (action & MotionEventCompat.ACTION_MASK) {
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        long duration = event.getEventTime() - event.getDownTime();

                            mStartDragX = event.getX();

                            if (x < mStartDragX
                                    && (mStartDragX - x > SWIPE_X_MIN_THRESHOLD)
                                    && getCurrentItem() > 0) {
                                Log.i("VerticalViewPager", "down " + x + " : " + mStartDragX + " : " + (mStartDragX - x));
                                setCurrentItem(getCurrentItem() - 1, true);
                                return true;
                            } else if (x > mStartDragX
                                    && (x - mStartDragX > SWIPE_X_MIN_THRESHOLD)
                                    && getCurrentItem() < getAdapter().getCount() - 1) {
                                Log.i("VerticalViewPager", "up " + x + " : " + mStartDragX + " : " + (x - mStartDragX));
                                mStartDragX = 0;
                                setCurrentItem(getCurrentItem() + 1, true);
                                return true;
                            } else if (isSwipeNews && duration < MAX_THRESHOLD){
//                                if (getCurrentItem() != (getAdapter().getCount() - 1)){
//                                    ((SwipeNewsAdapter)getAdapter()).handleClick();
//                                } else  {
//                                    try {
//                                        if(x - mStartDragX < SWIPE_X_MIN_THRESHOLD){
//                                            ((SwipeNewsAdapter)getAdapter()).handleClick();
//                                        }
//                                    } catch (Exception e){}
//                                }
                            }

                        break;
                }
            } else {
                mStartDragX = 0;
            }
            swapXY(event);
            return super.onTouchEvent(swapXY(event));
        }
        return false;
//        return true;
//        return super.onTouchEvent(this.swapXY(ev));
//        return super.onTouchEvent(ev);
    }
}
