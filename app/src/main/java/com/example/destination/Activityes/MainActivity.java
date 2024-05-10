package com.example.destination.Activityes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.destination.Fragments.ChatsFragment;
import com.example.destination.Fragments.NetworkFragment;
import com.example.destination.Fragments.NotificationFragment;
import com.example.destination.Fragments.ProfileFragment;
import com.example.destination.R;

import com.example.destination.utils.BaseApplication;
import com.example.destination.utils.FirbaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseApplication {
    BottomNavigationView bottomNavigationView;
    NetworkFragment networkFragment;
    ProfileFragment profileFragment;
    NotificationFragment notificationFragment;
    ChatsFragment chatsFragment;
    Add_location addLocation;


    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static final String READ_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES;
    private static final String READ_MEDIA_VIDEO = Manifest.permission.READ_MEDIA_VIDEO;
    private static final String READ_MEDIA_AUDIO = Manifest.permission.READ_MEDIA_AUDIO;
    private static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int REQUEST_CODE = 11;
    private  DocumentReference reference;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkFragment = new NetworkFragment();
        profileFragment = new ProfileFragment();
        notificationFragment = new NotificationFragment();
        chatsFragment = new ChatsFragment();
        FirbaseUtil.Online(true);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

         reference = FirebaseFirestore.getInstance().collection("users").document(FirbaseUtil.currentUsersId());
        //search_btn = findViewById(R.id.search_btn);
        //search_btn.setOnClickListener(v -> {
        //    Intent intent = new Intent(MainActivity.this,search_Activity.class);
        //    startActivity(intent);
////////////////////////////////////////////////////////////////////////////////////////////////////////


        showPermissionDialog();
////////////////////////////////////////////////////////////////////////////////////////////////////////
        //init();


       // addTabs();
        // if(!getIntent().equals(null)) {
        //     String fragmentToLoad = getIntent().getStringExtra("Fragment");
        //     if (fragmentToLoad.equals("Profile")) {
        //         tabLayout.getTabAt(3);
        //     }
        // }
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.network) {
                     getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, networkFragment).commit();
                }
                if (item.getItemId() == R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, profileFragment).commit();
                }

                if (item.getItemId() == R.id.notifications) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, notificationFragment).commit();
                }
                if (item.getItemId() == R.id.chats){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, chatsFragment).commit();
                }
                if (item.getItemId() == R.id.Add_post) {
                    startActivity(new Intent(MainActivity.this, Add_location.class));
                     }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.network);
        getFCMToken();


    }

    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    String token = task.getResult();
                    Log.i("fcmToken",token);

                    FirbaseUtil.currentUsersDetails().update("fcmToken",token);

                }else{
           //         Toast.makeText(MainActivity.this, "token", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showPermissionDialog() {
        if ((ContextCompat.checkSelfPermission(this, READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, READ_STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED)) {
            // Если все разрешения уже предоставлены, показываем сообщение об этом
           // Toast.makeText(this, "Permission Accepted", Toast.LENGTH_SHORT).show();
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
        Log.d("generate Toast", "1");

        // Проверяем, был ли запрос на разрешения с нашим кодом запроса
        if (requestCode == REQUEST_CODE) {
            Log.d("generate Toast", "2");
            // Проверяем, что результаты запроса не пусты
            if (grantResults.length > 0) {
                Log.d("generate Toast", "3");
                // Проверяем, все ли разрешения были предоставлены
                boolean allPermissionsGranted = true;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Log.d("generate Toast", "4");
                        allPermissionsGranted = false;
                        break;
                    }
                }
                if (allPermissionsGranted) {
                    // Если все разрешения предоставлены, показываем сообщение об этом
                  //  Toast.makeText(this, "Permission Accepted", Toast.LENGTH_SHORT).show();
                } else {
                    // Если какое-либо разрешение было отклонено, показываем сообщение об этом
                //    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("generate Toast", "5");
                // Если результаты запроса пусты, повторно запрашиваем разрешения
                showPermissionDialog();
            }
        }

////////////////////////////////////////////////////////////////////////////////////////////
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirbaseUtil.Online(false);
    }
}
