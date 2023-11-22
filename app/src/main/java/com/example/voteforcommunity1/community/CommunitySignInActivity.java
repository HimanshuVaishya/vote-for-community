package com.example.voteforcommunity1.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.voteforcommunity1.databinding.ActivityCommunitySignInBinding;
import com.example.voteforcommunity1.model.responsemodel;
import com.example.voteforcommunity1.retrofit.apicontroller;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunitySignInActivity extends AppCompatActivity {

    ActivityCommunitySignInBinding binding;
    private String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        goToDashboard();
        //checkLogOut();
        binding.gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CommunitySignUpActivity.class));
                finish();
            }
        });
        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.email.getText().toString().trim();
                password = binding.passwordTxt.getText().toString().trim();
                login_user(email, password);
            }
        });
        
    }
    private void login_user(final String email, final String password) {
        Call<responsemodel> call = apicontroller
                .getInstance()
                .getapi()
                .verifycommunity(email, password);

        call.enqueue(new Callback<responsemodel>() {
            @Override
            public void onResponse(Call<responsemodel> call, Response<responsemodel> response) {
                responsemodel obj = response.body();
                String output = obj.getMessage();
                if(output.equals("failed")){
                    binding.email.setText("");
                    binding.passwordTxt.setText("");
                    Toast.makeText(CommunitySignInActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
                else if(output.contains("_exist")){
                    String code = output.split("_")[0];
                    String name = output.split("_")[1];
                    SharedPreferences sp = getSharedPreferences("communityCredentials", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("code", code);
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.commit();
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), CommunityDashboardActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<responsemodel> call, Throwable t) {
                Toast.makeText(CommunitySignInActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void goToDashboard(){
        SharedPreferences sp = getSharedPreferences("communityCredentials", MODE_PRIVATE);
        if (sp.contains("email")) {
            startActivity(new Intent(getApplicationContext(), CommunityDashboardActivity.class));
            finish();
        }
    }
//    public void checkLogOut(){
//        SharedPreferences sp = getSharedPreferences("communityCredentials", MODE_PRIVATE);
//        if(sp.contains("msg")) {
//            SharedPreferences.Editor ed = sp.edit();
//            ed.remove("msg");
//            ed.commit();
//            ed.apply();
//            Toast.makeText(this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
//        }
//    }
}