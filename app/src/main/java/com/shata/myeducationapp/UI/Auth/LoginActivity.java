package com.shata.myeducationapp.UI.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.shata.myeducationapp.MainActivity;
import com.shata.myeducationapp.R;
import com.shata.myeducationapp.databinding.ActivityLoginBinding;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginBinding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        loginBinding.btnForgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            finish();
        });

        loginBinding.btnRegisterNow.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        loginBinding.btnSignin.setOnClickListener(v -> {
            loginBinding.progressCircular.setVisibility(View.VISIBLE);
            String emailAdd = loginBinding.email.getText().toString();
            if (emailAdd.isEmpty()) {
                loginBinding.email.setError("" + getResources().getString(R.string.email_required));
                loginBinding.email.setFocusable(true);
                loginBinding.email.requestFocus();
                loginBinding.progressCircular.setVisibility(View.GONE);
                return;
            }

            String passwords = loginBinding.password.getText().toString();
            if (passwords.isEmpty()) {
                loginBinding.password.setError("" + getResources().getString(R.string.password_required));
                loginBinding.password.setFocusable(true);
                loginBinding.password.requestFocus();
                loginBinding.progressCircular.setVisibility(View.GONE);
                return;
            }
            mAuth.signInWithEmailAndPassword(emailAdd, passwords)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Toasty.success(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            loginBinding.progressCircular.setVisibility(View.GONE);
                            finish();
                        } else {
                            Toasty.error(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}