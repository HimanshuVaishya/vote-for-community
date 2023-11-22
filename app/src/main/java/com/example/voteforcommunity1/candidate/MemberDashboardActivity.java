package com.example.voteforcommunity1.candidate;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.voteforcommunity1.R;
import com.example.voteforcommunity1.community.CommunitySignInActivity;
import com.example.voteforcommunity1.model.votecandidatelistmodel;
import com.example.voteforcommunity1.retrofit.apicontroller;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberDashboardActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_dashboard);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = findViewById(R.id.mdToolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.mdrecyclerView);
        sp = getSharedPreferences("memberStorage", MODE_PRIVATE);
        String comCode = sp.getString("comcode", "");
        processData(comCode);
    }

    private void processData(String communityCode) {
        Call<List<votecandidatelistmodel>> call = apicontroller
                .getInstance()
                .getapi()
                .dovotecandidatelist(communityCode);
        call.enqueue(new Callback<List<votecandidatelistmodel>>() {
            @Override
            public void onResponse(Call<List<votecandidatelistmodel>> call, Response<List<votecandidatelistmodel>> response) {
                List<votecandidatelistmodel> data = response.body();
                MemberDoVoteAdapter adapter = new MemberDoVoteAdapter(data, getApplicationContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<votecandidatelistmodel>> call, Throwable t) {
                Toast.makeText(MemberDashboardActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mLogOut){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences sp = getSharedPreferences("memberStorage", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.clear();
        ed.apply();
        ed.commit();
        Intent intent = new Intent(getApplicationContext(), MemberSignInActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}