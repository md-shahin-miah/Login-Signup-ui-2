package com.shahin.loginsignupui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class RegisterActivity extends AppCompatActivity {

    private ImageButton back;
    private MaterialButton buttonRegister;
    private Button buttonFacebook, buttonGoogle;
    private TextInputEditText et_email, et_password, et_name, et_phone, et_cpassword;
    private CircularImageView profile_image;
    private String imagePath = "";
    private Uri imageUri;

    private static final int IMAGE_REQUEST_CODE = 100;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        back =  findViewById(R.id.back);
        buttonRegister = findViewById(R.id.button_signup);
        et_email = findViewById(R.id.registerEmail);
        et_password =  findViewById(R.id.registerPassword);
        et_cpassword =  findViewById(R.id.registerConfirmPassword);
        et_phone = findViewById(R.id.registerPhone);
        et_name = findViewById(R.id.registerName);
        profile_image = findViewById(R.id.profile_image);
        profile_image.setImageResource(R.drawable.user);
//        Glide.with(this).load(R.drawable.user).into(profile_image);
        buttonGoogle = findViewById(R.id.buttonGoogle);
        buttonFacebook =  findViewById(R.id.buttonFacebook);
        buttonGoogle.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.gbutton), null, null, null);
        buttonFacebook.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.fbbg), null, null, null);
        assert profile_image != null;

        buttonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isConnected(RegisterActivity.this)) {
                    Utility.showToast(RegisterActivity.this, "Handle Facebook Login" );
                }
                else {
                    Utility.showNetworkAlert(RegisterActivity.this, R.string.nointernet);
                }
            }
        });

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isConnected(RegisterActivity.this)) {
                    Utility.showToast(RegisterActivity.this, "Handle Google SignIn" );
                }
                else {
                    Utility.showNetworkAlert(RegisterActivity.this, R.string.nointernet);
                }
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this,
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 200);
                }
                else {
                    loadImage();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerManually();
            }
        });


        (findViewById(R.id.tv_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults.length > 0) {
                loadImage();
            }
        }
    }

    private void loadImage(){
        try {
            File photoFile = createImageFile();
            imageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        List<Intent> cameraIntents = new ArrayList<>();
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo info : listCam){
            String packageName = info.activityInfo.packageName;
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(packageName, info.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(intent);
        }

        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        Intent chooserIntent = Intent.createChooser(gallery, "Select Source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        startActivityForResult(chooserIntent, IMAGE_REQUEST_CODE);
    }

    private File createImageFile() throws IOException {
        String imageFileName = Utility.getUniqueImageFileName();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imagePath = imageFileName;
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IMAGE_REQUEST_CODE:
                boolean isCamera;
                if (data == null) isCamera = true;
                else {
                    String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }
                Uri filePath;
                if (isCamera) {
                    filePath = imageUri;
                } else {
                    filePath = data.getData();
                }
                profile_image.setImageURI(filePath);
                break;
        }
    }

    private void registerManually() {
        if (Utility.isConnected(this)){
            Boolean flag = false;
            final String email, cpassword, password, name, phone;

            email = et_email.getText().toString().trim();
            password = et_password.getText().toString().trim();
            cpassword = et_cpassword.getText().toString().trim();
            name = et_name.getText().toString().trim();
            phone = et_phone.getText().toString().trim();

            if (!isValidEmail(email)){
                assert et_email != null;
                et_email.setError(getResources().getString(R.string.emailerror));
                flag = true;
            }
            if (password.length() < 6){
                et_password.setError(getResources().getString(R.string.passworderror));
                flag = true;
            }
            if (cpassword.length() < 6) {
                et_cpassword.setError(getResources().getString(R.string.passworderror));
                flag = true;
            }
//            if (phone.length() < 6) {
//                et_phone.setError(getResources().getString(R.string.validationerror));
//                flag = true;
//            }
            if (name.length() <= 0){
                et_name.setError(getResources().getString(R.string.validationerror));
                flag = true;
            }
            if (password.equals(cpassword)) {
                Utility.showToast(this, "Passwords are not matching");
                flag = true;
            }
            if (!flag) {
                Utility.showToast(this, "Handle Register Manual");
            }
            else{
                Utility.showToast(this, "Please fill up all the required fields");
            }
        }
        else
            Utility.showNetworkAlert(this, R.string.nointernet);
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
        return matcher.matches();
    }
//
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }
}
