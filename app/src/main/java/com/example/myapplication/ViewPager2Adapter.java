package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPager2Adapter extends FragmentStateAdapter {

    public ViewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SensingFragment();
            case 1:
                return new ConnectionFragment();
            case 2:
                return new WebViewFragment();
            case 3:
                return new MapFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 4;       // 페이지 수
    }
}
