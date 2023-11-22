package com.example.voteforcommunity1.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.voteforcommunity1.databinding.ActivityCommunitySignUpBinding;
import com.example.voteforcommunity1.model.responsemodel;
import com.example.voteforcommunity1.retrofit.apicontroller;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunitySignUpActivity extends AppCompatActivity {
    ActivityCommunitySignUpBinding binding;
    private String name, code, email, password, cnfrmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.gotosignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CommunitySignInActivity.class));
                finish();
            }
        });

        binding.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 name = binding.communityNameTxt.getText().toString().trim();
                 code = binding.communityCodeTxt.getText().toString().trim();
                 email = binding.email.getText().toString().trim();
                 password = binding.passwordTxt.getText().toString().trim();
                 cnfrmpassword = binding.cnfrmPasswordTxt.getText().toString().trim();

                if( name.equals("") || code.equals("") || email.equals("") || password.equals("") || cnfrmpassword.equals("")){
                    Toast.makeText(CommunitySignUpActivity.this, "fill all the details !!", Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(cnfrmpassword)){
                    Toast.makeText(CommunitySignUpActivity.this, "password is not matching !!", Toast.LENGTH_SHORT).show();
                }
                else {
                    register_user(code, name, email, password);
                }

            }
        });

    }

    public void register_user(final String code, final String name, final String email, final String password){
        Call<responsemodel> call = apicontroller
                .getInstance()
                .getapi()
                .registercommunity(code, name, email, password);

        call.enqueue(new Callback<responsemodel>() {
            @Override
            public void onResponse(Call<responsemodel> call, Response<responsemodel> response) {
                responsemodel obj = response.body();
                String output = obj.getMessage();
                if(output.equals("email")){
                    Toast.makeText(CommunitySignUpActivity.this, "email already exist", Toast.LENGTH_SHORT).show();
                }
                else if(output.equals("code")){
                    Toast.makeText(CommunitySignUpActivity.this, "code already used", Toast.LENGTH_SHORT).show();
                }
                else if(output.equals("created")){
                    SharedPreferences sp = getSharedPreferences("communityCredentials", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("code", code);
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.commit();
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), CommunityDashboardActivity.class));
                    finish();
                }else{
                    Toast.makeText(CommunitySignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<responsemodel> call, Throwable t) {
                Toast.makeText(CommunitySignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}