package com.shahin.loginsignupui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;


public class LoginActivity extends AppCompatActivity {

    private ImageButton back;
    private Button buttonLogin, buttonFacebook, buttonGoogle;
    private TextInputEditText et_email, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back = (ImageButton) findViewById(R.id.back);
        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonFacebook = (Button) findViewById(R.id.buttonFacebook);
        buttonGoogle = (Button) findViewById(R.id.buttonGoogle);
        et_email =  findViewById(R.id.loginEmail);
        et_password =  findViewById(R.id.loginPassword);
        buttonGoogle.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.gbutton), null, null, null);
        buttonFacebook.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.fbbg), null, null, null);

        buttonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isConnected(LoginActivity.this)) {
                    Utility.showToast(LoginActivity.this, "Handle Facebook Login" );
                }
                else {
                    Utility.showNetworkAlert(LoginActivity.this, R.string.nointernet);
                }
            }
        });

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isConnected(LoginActivity.this)) {
                    Utility.showToast(LoginActivity.this, "Handle Google SignIn" );
                }
                else {
                    Utility.showNetworkAlert(LoginActivity.this, R.string.nointernet);
                }
            }
        });

        (findViewById(R.id.tv_signup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        (findViewById(R.id.forgotpassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeForgotCall();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginManually();
            }
        });
    }

    private void loginManually(){
        if (Utility.isConnected(this)){
            Boolean flag = false;
            final String email, password;
            email = et_email.getText().toString().trim();
            password = et_password.getText().toString().trim();
            if (!isValidEmail(email)){
                assert et_email != null;
                et_email.setError(getResources().getString(R.string.emailerror));
                flag = true;
            }
            if (password.length() < 6){
                et_password.setError(getResources().getString(R.string.passworderror));
                flag = true;
            }
            if (!flag) {
                Utility.showToast(LoginActivity.this, "Handle Login Manual");
            }
        }
        else
            Utility.showNetworkAlert(this, R.string.nointernet);
    }

    private void makeForgotCall(){
        final Dialog dialog = new Dialog(this, R.style.AppTheme_Dialog_Full);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_forgot_password);
        ImageButton back = (ImageButton) dialog.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        final TextInputEditText etemail = (TextInputEditText) dialog.findViewById(R.id.resetEmail);
        Button reset = (Button) dialog.findViewById(R.id.button_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidEmail(etemail.getText().toString().trim())){
                    Utility.showToast(LoginActivity.this, "Handle Forgot Password");
                }
                else{
                    etemail.setError(getResources().getString(R.string.emailerror));
                }
            }
        });
        dialog.show();
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
        return matcher.matches();
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }
}
