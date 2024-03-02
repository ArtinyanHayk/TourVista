package com.example.destination.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.destination.Add_location_Fragment;
import com.example.destination.NetworkFragment;
import com.example.destination.ProfileFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int noOfTabes;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int noOfTabes) {
        super(fm);
        this.noOfTabes = noOfTabes;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //texapoxvelu xndir

        switch (position){
            //NetworkFragment ---> Home
            case 0:
                return new NetworkFragment();

            case 1:
                return new Add_location_Fragment();

            case 2:
                return new ProfileFragment();
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return noOfTabes;
    }
}
