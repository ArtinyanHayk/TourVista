package com.example.destination.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.destination.Fragments.NetworkFragment;
import com.example.destination.Fragments.ProfileFragment;

//public class ViewPagerAdapter extends FragmentStatePagerAdapter {
//    int noOfTabes;
//
//    public ViewPagerAdapter(@NonNull FragmentManager fm, int noOfTabes) {
//        super(fm);
//        this.noOfTabes = noOfTabes;
//    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        //texapoxvelu xndir
//
//        switch (position){
//            //NetworkFragment ---> Home
//            case 0:
//                return new NetworkFragment();
//
//            case 1:
//                return new Add_location_Fragment2();
//
//            case 2:
//                return new ProfileFragment();
//            default:
//                return null;
//        }
//
//
//    }
//
//    @Override
//    public int getCount() {
//        return noOfTabes;
//    }
//}
//