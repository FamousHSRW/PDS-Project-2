package com.example.pds_project_2.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.pds_project_2.Consumer;
import com.example.pds_project_2.Producer;
import com.example.pds_project_2.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    private Producer producer;
    private Consumer consumer;

    public SectionsPagerAdapter(Context context, FragmentManager fm, Producer producer, Consumer consumer) {
        super(fm);
        mContext = context;
        this.producer = producer;
        this.consumer = consumer;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderPublishers (defined as a static inner class below).
        position += 1;
        if(position == 1) return PlaceholderPublishers.newInstance(position + 1, producer, consumer);
        else return PlaceholderNews.newInstance(position + 1, producer);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}