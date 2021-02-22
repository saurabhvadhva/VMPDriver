package app.vmp.driver.utils;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class VerticalPageTransformer implements ViewPager.PageTransformer {
    private float MIN_SCALE = 0.90F;

    public VerticalPageTransformer() {
    }

//    @Override
//    public void transformPage(View view, float position) {
//        float alpha = 0;
//        if (0 <= position && position <= 1) {
//            alpha = 1 - position;
//        } else if (-1 < position && position < 0) {
//            alpha = position + 1;
//        }
//        view.setAlpha(alpha);
//        view.setTranslationX(view.getWidth() * -position);
//        float yPosition = position * view.getHeight();
//        view.setTranslationY(yPosition);
//    }

//    @Override
//    public void transformPage(View page, float position) {
//        page.setTranslationX(page.getWidth() * -position);
//        page.setTranslationY(position < 0 ? position * page.getHeight() : 0f);
//    }

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1.0F) {
            view.setAlpha(0.0F);
        } else {
            float scaleFactor;
            if (position <= 0.0F) {
                view.setAlpha(1.0F);
                view.setScaleX(1.0F);
                view.setScaleY(1.0F);
                scaleFactor = position * (float)view.getHeight();
                view.setTranslationY(scaleFactor);
                view.setTranslationX((float)(-1 * view.getWidth()) * position);
            } else if (position <= 1.0F) {
                view.setAlpha(1.0F - position);
                view.setTranslationX((float)(-1 * view.getWidth()) * position);
                scaleFactor = this.MIN_SCALE + (1.0F - this.MIN_SCALE) * (1.0F - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setAlpha(0.0F);
            }
        }

    }
}
