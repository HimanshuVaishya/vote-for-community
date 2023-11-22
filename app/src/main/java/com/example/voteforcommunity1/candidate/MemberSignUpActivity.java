package com.example.voteforcommunity1.candidate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.voteforcommunity1.databinding.ActivityMemberSignUpBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MemberSignUpActivity extends AppCompatActivity {
    ActivityMemberSignUpBinding binding;
    public static final String UPLOAD_URL = "http://filepath/VoteForCommunityAPIs/MemberSignUp.php";
    private String comCode, memCode, name, email, password, cnfrmpassword, encodedImage;
    private ImageView img;
    private static final int STORAGE_PERMISSION_CODE = 4655;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filepath;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        img = binding.imageView;
//        requestStoragePermission();
        binding.gotosignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MemberSignInActivity.class));
                finish();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowFileChooser();
            }
        });

        binding.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comCode = binding.communityCodeTxt.getText().toString().trim();
                memCode = binding.memberCode.getText().toString().trim();
                name = binding.name.getText().toString().trim();
                email = binding.email.getText().toString().trim();
                password = binding.passwordTxt.getText().toString().trim();
                cnfrmpassword = binding.cnfrmPasswordTxt.getText().toString().trim();

                validate(comCode, name, memCode, email, password, cnfrmpassword);
            }
        });

    }

//    private void requestStoragePermission() {
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//            return;
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            //If the user has denied the permission previously your code will come to this block
//            //Here you can explain why you need this permission
//            //Explain here why you need this permission
//        }
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private void ShowFileChooser() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        //intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                imageStore(bitmap);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] imageBytes = stream.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }



    private void uploadImage(final String comCode, final String name, final String memCode, final String email, final String pass) {
        //String path = getPath(filepath);
        //try {

            StringRequest request = new StringRequest(Request.Method.POST, UPLOAD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(MemberSignUpActivity.this, response, Toast.LENGTH_SHORT).show();
                            if(response.equals("account created")){
                                SharedPreferences sp = getSharedPreferences("memberStorage", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("comcode", comCode);
                                editor.putString("mememail", email);
                                editor.putString("memcode", memCode);
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(), MemberDashboardActivity.class));
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MemberSignUpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("upload", encodedImage);
                    params.put("comcode", comCode);
                    params.put("name", name);
                    params.put("memcode", memCode);
                    params.put("email", email);
                    params.put("pass", pass);
                    params.put("upload", encodedImage);
                    return params;
                }
            };

        RequestQueue requestQueue = Volley.newRequestQueue(MemberSignUpActivity.this);
        requestQueue.add(request);

//            String uploadId = UUID.randomUUID().toString();
//            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
//                    .addFileToUpload(path, "upload")
//                    .addParameter("comcode", comCode)
//                    .addParameter("name", name)
//                    .addParameter("memcode", memCode)
//                    .addParameter("email", email)
//                    .addParameter("pass", pass)
//                    .setNotificationConfig(new UploadNotificationConfig())
//                    .setMaxRetries(3)
//                    .startUpload();
//
//            SharedPreferences sp = getSharedPreferences("memberStorage", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putString("comcode", comCode);
//                    editor.putString("mememail", email);
//                    editor.putString("memcode", memCode);
//                    editor.commit();
//                    startActivity(new Intent(getApplicationContext(), MemberDashboardActivity.class));
//        } catch (Exception ex) {
//
//        }

    }

    public void validate(final String comCode, final String name, final String memCode, final String email, final String password, final String cnfrmpassword){
        if( comCode.equals("") || name.equals("") || memCode.equals("") || email.equals("") || password.equals("") || cnfrmpassword.equals("") || filepath.toString().equals("")){
            Toast.makeText(MemberSignUpActivity.this, "fill all the details !!", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(cnfrmpassword)){
            binding.cnfrmPasswordTxt.setError("password is not matching !!");
//            Toast.makeText(MemberSignUpActivity.this, "password is not matching !!", Toast.LENGTH_SHORT).show();
        }
        else if(!email.contains("@")){
            binding.email.setError("enter valid email !!");
        }
        else {
            uploadImage(comCode, name, memCode, email, password);
        }
    }

//    private void register_user(final String comCode, final String name, final String memCode, final String email, final String pass){
//        Call<responsemodel> call = apicontroller
//                .getInstance()
//                .getapi()
//                .membersignup(comCode, name, memCode, email, pass);
//
//        call.enqueue(new Callback<responsemodel>() {
//            @Override
//            public void onResponse(Call<responsemodel> call, retrofit2.Response<responsemodel> response) {
//                responsemodel obj = response.body();
//                String output = obj.getMessage();
//                if(output.equals("account created")){
//                    SharedPreferences sp = getSharedPreferences("memberStorage", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putString("comcode", comCode);
//                    editor.putString("mememail", email);
//                    editor.putString("memcode", memCode);
//                    editor.commit();
//                    startActivity(new Intent(getApplicationContext(), MemberDashboardActivity.class));
//                }else if(output.equals("email already")){
//                    Toast.makeText(MemberSignUpActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
//                }else if (output.equals("code already")){
//                    Toast.makeText(MemberSignUpActivity.this, "Code already exists", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(MemberSignUpActivity.this, output.substring(0, output.indexOf('<')), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<responsemodel> call, Throwable t) {
//
//            }
//        });
//    }

}