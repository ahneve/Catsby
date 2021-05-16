package org.techtown.catsby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.techtown.catsby.community.FragmentCommunity;
import org.techtown.catsby.cattown.FragmentCatTown;
import org.techtown.catsby.home.FragmentHome;
import org.techtown.catsby.notification.NotificationActivity;
import org.techtown.catsby.setting.FragmentSetting;

public class MainActivity extends AppCompatActivity {
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final FragmentHome fragmenthome = new FragmentHome();
    private final FragmentCatTown fragmentcattown = new FragmentCatTown();
    private final FragmentCommunity fragmentcommunity = new FragmentCommunity();
    private final FragmentSetting fragmentsetting = new FragmentSetting();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmenthome).commitAllowingStateLoss();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu) ;
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notice:
                Intent notificateionIntent = new Intent(this, NotificationActivity.class);
                startActivity(notificateionIntent);
            default:
                return super.onOptionsItemSelected(item) ;
        }
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch(menuItem.getItemId())
            {
                case R.id.iconHome:
                    transaction.replace(R.id.frameLayout, fragmenthome).commitAllowingStateLoss();
                    break;
                case R.id.iconCommunity:
                    transaction.replace(R.id.frameLayout, fragmentcommunity).commitAllowingStateLoss();
                    break;
                case R.id.iconCatTown:
                    transaction.replace(R.id.frameLayout, fragmentcattown).commitAllowingStateLoss();
                    break;
                case R.id.iconSetting:
                    transaction.replace(R.id.frameLayout, fragmentsetting).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}