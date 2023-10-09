package com.shata.myeducationapp.UI.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.shata.myeducationapp.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText email;
    Button resetPassword, register;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        email = findViewById(R.id.email);

        resetPassword = findViewById(R.id.btn_forget_password);
        register = findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(v -> {
            Intent intent = new Intent(ForgetPasswordActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        resetPassword.setOnClickListener(v -> {

            String emailAdd = email.getText().toString();
            if (emailAdd.isEmpty()) {
                email.setError("" + getResources().getString(R.string.email_required));
                email.setFocusable(true);
                email.requestFocus();
                return;
            }

            auth.sendPasswordResetEmail(emailAdd).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgetPasswordActivity.this, "Reset Password Link Sent to Your Email", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(ForgetPasswordActivity.this, "Reset Password Failed "+message, Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}