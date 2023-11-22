package com.example.voteforcommunity1.candidate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.voteforcommunity1.databinding.ActivityMemberSignInBinding;
import com.example.voteforcommunity1.model.responsemodel;
import com.example.voteforcommunity1.retrofit.apicontroller;

import retrofit2.Call;
import retrofit2.Callback;

public class MemberSignInActivity extends AppCompatActivity {

    ActivityMemberSignInBinding binding;
    private String comCode, memCode, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        goToDashboard();

        binding.gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MemberSignUpActivity.class));
                finish();
            }
        });
        //checkLogOut();
        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comCode = binding.communityCodeTxt.getText().toString().trim();
                memCode = binding.memberCode.getText().toString().trim();
                password = binding.passwordTxt.getText().toString().trim();
                validate(comCode, memCode, password);
            }
        });

    }
    public void validate(final String comCode, final String memCode, final String pass){
        if( comCode.equals("") || memCode.equals("") || password.equals("") ){
            Toast.makeText(MemberSignInActivity.this, "fill all the details !!", Toast.LENGTH_SHORT).show();
        }
        else {
            login_user(comCode, memCode, pass);
        }
    }

    private void login_user(final String comCode, final String memCode, final String pass) {
        Call<responsemodel> call = apicontroller
                .getInstance()
                .getapi()
                .membersignin(comCode, memCode, pass);

        call.enqueue(new Callback<responsemodel>() {
            @Override
            public void onResponse(Call<responsemodel> call, retrofit2.Response<responsemodel> response) {
                responsemodel obj = response.body();
                String output = obj.getMessage();
                if(output.equals("wrong")){
                    binding.communityCodeTxt.setText("");
                    binding.memberCode.setText("");
                    binding.passwordTxt.setText("");
                    Toast.makeText(MemberSignInActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
                else if(output.contains("@")){
                    SharedPreferences sp = getSharedPreferences("memberStorage", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("comcode", comCode);
                    editor.putString("mememail", output);
                    editor.putString("memcode", memCode);
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(), MemberDashboardActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<responsemodel> call, Throwable t) {
                Toast.makeText(MemberSignInActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToDashboard(){
        SharedPreferences sp = getSharedPreferences("memberStorage", MODE_PRIVATE);
        if(sp.contains("mememail")) {
            startActivity(new Intent(getApplicationContext(), MemberDashboardActivity.class));
            finish();
        }
    }

//    public void checkLogOut(){
//        SharedPreferences sp = getSharedPreferences("memberStorage", MODE_PRIVATE);
//        if(sp.contains("msg")) {
//            SharedPreferences.Editor ed = sp.edit();
//            ed.remove("msg");
//            Toast.makeText(this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
//        }
//    }
}