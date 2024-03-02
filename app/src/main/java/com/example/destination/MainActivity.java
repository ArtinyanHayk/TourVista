package com.example.destination;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.destination.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    //  BottomNavigationView bottomNavigationView;
    //  ImageButton search_btn;
    //  NetworkFragment networkFragment;
    //  ProfileFragment profileFragment;
    //  Add_location_Fragment addLocationFragment;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //networkFragment = new NetworkFragment();
        //profileFragment = new ProfileFragment();
        //addLocationFragment = new Add_location_Fragment();
        //bottomNavigationView = findViewById(R.id.bottom_navigation);
        //search_btn = findViewById(R.id.search_btn);
        //search_btn.setOnClickListener(v -> {
        //    Intent intent = new Intent(MainActivity.this,search_Activity.class);
        //    startActivity(intent);
        init();
        addTabs();

    }

    //       bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
    //           @Override
    //           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    //               if(item.getItemId() == R.id.menu_network){
    //                   getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,networkFragment).commit();
    //               }
    //               if(item.getItemId() == R.id.menu_profile){
    //                   getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,profileFragment).commit();
    //               }
//
    //               if(item.getItemId() == R.id.menu_add){
    //                   getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,addLocationFragment).commit();
    //               }
    //               return true;
    //           }
    //       });
    //       bottomNavigationView.setSelectedItemId(R.id.menu_network);
    //}
    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

    }

    private void addTabs() {
        //??? video
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.map_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.add_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.person_icon));

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.getTabAt(0).setIcon(R.drawable.map_icon);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem((tab.getPosition()));

                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.map_icon);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.add_icon);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.person_icon);
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.map_home_icon);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.add_location_icon);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.account_icon);
                        break;
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}
