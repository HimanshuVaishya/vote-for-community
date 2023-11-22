package com.example.voteforcommunity1.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.voteforcommunity1.R;
import com.google.android.material.navigation.NavigationView;

public class CommunityDashboardActivity extends AppCompatActivity {

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_dashboard);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CommunityCandidateListFragment()).commit();
        navigationView.setCheckedItem(R.id.candidate);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Fragment frame;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.candidate)
                    frame = new CommunityCandidateListFragment();
                else if (item.getItemId() == R.id.create) {
                    frame = new CommunityCreateVoteFragment();
                }else if (item.getItemId() == R.id.history) {
                    frame = new CommunityVoteListFragment();
                }else if(item.getItemId() == R.id.logout){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    logout();
                    return true;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, frame).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void logout(){
        SharedPreferences sp = getSharedPreferences("communityCredentials", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
//        ed.remove("email");
//        ed.remove("code");
//        ed.remove("name");
        ed.clear();
        ed.apply();
        ed.commit();
        Intent intent = new Intent(getApplicationContext(), CommunitySignInActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}