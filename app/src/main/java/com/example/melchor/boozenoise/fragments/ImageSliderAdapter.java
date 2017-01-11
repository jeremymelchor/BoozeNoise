package com.example.melchor.boozenoise.fragments;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.melchor.boozenoise.R;

public class ImageSliderAdapter extends PagerAdapter {

    private Context context;
    private int[] sliderImagesId = new int[]{
            R.mipmap.abc1, R.mipmap.abc2, R.mipmap.abc3
    };

    public ImageSliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImagesId.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(sliderImagesId[position]);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}
