package com.example.schedulingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPass extends AppCompatActivity {

    EditText inputEmail, inputPassword, inputConfirmpassword;
    Button resetbtn;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmpassword = findViewById(R.id.inputConfirmpassword);
        resetbtn = findViewById(R.id.resetbtn);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performreset();
            }
        });
    }

    private void performreset() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmpassword = inputConfirmpassword.getText().toString();

        if (!email.matches(emailPattern)) {
            inputEmail.setError("Enter Valid Email");
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter Valid Password");
        } else if (!password.equals(confirmpassword)) {
            inputConfirmpassword.setError("Password not match");
        } else {
            progressDialog.setMessage("Please Wait While Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.confirmPasswordReset(email, password).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(ForgotPass.this, "Reset Password Successful", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPass.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void sendUserToNextActivity() {
        Intent intent = new Intent(ForgotPass.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}