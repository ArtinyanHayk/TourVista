package com.example.destination;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    private static final String READ_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES;
    private static final String READ_MEDIA_VIDEO = Manifest.permission.READ_MEDIA_VIDEO;
    private static final String READ_MEDIA_AUDIO = Manifest.permission.READ_MEDIA_AUDIO;
    private static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int REQUEST_CODE = 11;


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
////////////////////////////////////////////////////////////////////////////////////////////////////////

         showPermissionDialog();
////////////////////////////////////////////////////////////////////////////////////////////////////////
        init();
        addTabs();

    }

    private void showPermissionDialog() {
        if ((ContextCompat.checkSelfPermission(this, READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, READ_STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED)) {
            // Если все разрешения уже предоставлены, показываем сообщение об этом
            Toast.makeText(this, "Permission Accepted", Toast.LENGTH_SHORT).show();
        }
       //else if (ContextCompat.checkSelfPermission(this, READ_STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED  &&
       //    ContextCompat.checkSelfPermission(this, LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED){
       //    Toast.makeText(this, "Permission Accepted", Toast.LENGTH_SHORT).show();
       // }
        else {
            // Если разрешения не предоставлены, запрашиваем их
            ActivityCompat.requestPermissions(this, new String[]{LOCATION_PERMISSION, READ_MEDIA_IMAGES, READ_MEDIA_AUDIO, READ_MEDIA_VIDEO}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("generate Toast","1");

        // Проверяем, был ли запрос на разрешения с нашим кодом запроса
        if (requestCode == REQUEST_CODE) {
            Log.d("generate Toast","2");
            // Проверяем, что результаты запроса не пусты
            if (grantResults.length > 0) {
                Log.d("generate Toast","3");
                // Проверяем, все ли разрешения были предоставлены
                boolean allPermissionsGranted = true;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Log.d("generate Toast","4");
                        allPermissionsGranted = false;
                        break;
                    }
                }
                if (allPermissionsGranted) {
                    // Если все разрешения предоставлены, показываем сообщение об этом
                    Toast.makeText(this, "Permission Accepted", Toast.LENGTH_SHORT).show();
                } else {
                    // Если какое-либо разрешение было отклонено, показываем сообщение об этом
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("generate Toast","5");
                // Если результаты запроса пусты, повторно запрашиваем разрешения
                showPermissionDialog();
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////


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
                        tabLayout.getTabAt(0);
                                //.setIcon(R.drawable.map_icon);
                        break;
                    case 1:
                        tabLayout.getTabAt(1);
                                //.setIcon(R.drawable.add_icon);
                        break;
                    case 2:
                        tabLayout.getTabAt(2);
                                //.setIcon(R.drawable.person_icon);
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.getTabAt(0);
                        //.setIcon(R.drawable.map_home_icon);
                        break;
                    case 1:
                        tabLayout.getTabAt(1);
                        //.setIcon(R.drawable.add_location_icon);
                        break;
                    case 2:
                        tabLayout.getTabAt(2);
                        //.setIcon(R.drawable.account_icon);
                        break;
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}
