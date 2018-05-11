package ru.leberamai.sstest.login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ru.leberamai.sstest.R;
import ru.leberamai.sstest.main.MainActivity;
import ru.leberamai.sstest.profile.User;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailtxt;
    private EditText passwordtxt;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference().child("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailtxt = findViewById(R.id.email);
        passwordtxt = findViewById(R.id.password);
        emailLayout = findViewById(R.id.emailInput);
        passwordLayout = findViewById(R.id.passwordInput);
        Button registerBtn = findViewById(R.id.registrButton);
        Button loginBtn = findViewById(R.id.loginButton);




        mAuth = FirebaseAuth.getInstance();

        // по java code conventions так условия писать не стоит
        // русские слова вынести в ресурсы
        // слишком много действий оnClickListener'e
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailtxt.getText().toString();
                String password = passwordtxt.getText().toString();
                if(!isValidEmail(email)) emailLayout.setError("Неправильно введен email");
                else if(!isValidPassword(password)) passwordLayout.setError("Пароль должен содержать больше 7 символов");
                else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        usersRef.child(user.getUid()).child("email").setValue(user.getEmail());
                                        Log.d(TAG, "createUserWithEmail:success");
                                        updateUI(user);
                                    } else {
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegistrationActivity.this, "Ошибка аутентификации.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validator here
                String email = emailtxt.getText().toString();
                String password = passwordtxt.getText().toString();

                // много дублирующегося кода с логином
                if(!isValidEmail(email)) emailLayout.setError("Неправильно введен email");
                else if(!isValidPassword(password)) passwordLayout.setError("Пароль должен содержать больше 7 символов");
                else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        User u = new User("Имя", "Номер", "Email");
                                        usersRef.child(user.getUid()).setValue(u);
                                        Log.d(TAG, "createUserWithEmail:success");
                                        updateUI(user);
                                    } else {
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegistrationActivity.this, "Ошибка аутентификации.",
                                                Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    // скобки нужно расставлять единообразно
    public void updateUI(FirebaseUser user)
    {
        if(user != null) {
            // достаточно просто this
            Context context = RegistrationActivity.this;
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
    }

    // достаточно просто email.length() > 3
    public boolean isValidEmail(String email){
        if(email.length()>3) return true;
        return false;
    }

    // здесь тоже
    public boolean isValidPassword(String password){
        if(password.length()>7) return true;
        return false;
    }

}
