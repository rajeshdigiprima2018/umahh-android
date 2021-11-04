package Adapter;

import android.app.Activity;
import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.ummah.R;


/**
 * Created by RonakJain on 01-02-2017.
 */

public class WalkThroughPagerAdapter extends PagerAdapter {

    public Context mContext;
    public Activity mActivity;
    String[] headerTextArray;
    int[] sliderImageArray;
    String[] imageTextArray;

    View imageLayout;

    public WalkThroughPagerAdapter(Context mContext, Activity mActivity, String[] headerTextArray, int[] sliderImageArray, String[] imageTextArray) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.headerTextArray = headerTextArray;
        this.sliderImageArray = sliderImageArray;
        this.imageTextArray = imageTextArray;

    }

    @Override
    public int getCount() {
        return sliderImageArray.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {

        LayoutInflater inflater = mActivity
                .getLayoutInflater();

        imageLayout = inflater.inflate(R.layout.adapter_walk_through, view, false);
        TextView headerTextView = (TextView) imageLayout.findViewById(R.id.headerTxt);
        ImageView adapterImageView = (ImageView) imageLayout.findViewById(R.id.sliderImageView);
        TextView imageTextView = (TextView) imageLayout.findViewById(R.id.descripationTxt);
        adapterImageView.setBackgroundResource(sliderImageArray[position]);
        imageTextView.setText(imageTextArray[position]);

        headerTextView.setText(headerTextArray[position]);
        Log.v("header", "" + position);
        view.addView(imageLayout, 0);
        return imageLayout;
    }

}
