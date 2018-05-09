package ru.leberamai.sstest.profile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import ru.leberamai.sstest.R;
import ru.leberamai.sstest.main.MainActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText numberEditText;
    private EditText nameEditText;
    private TextInputLayout emailLayout;
    private TextInputLayout numberLayout;
    private TextInputLayout nameLayout;
    private RoundedImageView avatarView;
    private FirebaseUser user;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference().child("users");;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        emailEditText = findViewById(R.id.emailEdit);
        numberEditText = findViewById(R.id.numberEdit);
        nameEditText = findViewById(R.id.nameEdit);
        emailLayout = findViewById(R.id.emailTextInputLayout);
        numberLayout = findViewById(R.id.numberTextInputLayout);
        nameLayout = findViewById(R.id.nameTextInputLayout);
        avatarView = findViewById(R.id.avatar);

        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
                else {
                    dispatchTakePictureIntent();
                }
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            usersRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User t = dataSnapshot.getValue(User.class);
                    nameEditText.setText(t.getName());
                    numberEditText.setText(t.getNumber());
                    emailEditText.setText(t.getEmail());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(ProfileActivity.this, "Ошибка",
                            Toast.LENGTH_SHORT).show();
                }
            });


            Button saveBtn = findViewById(R.id.save_button);

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String email = emailEditText.getText().toString();
                    String name = nameEditText.getText().toString();
                    String number = numberEditText.getText().toString();

                    if (!isValidEmail(email)){
                        emailLayout.setError("Некорректно введен Email");
                    }
                    else if(!isValidName(name)){
                        nameLayout.setError("Строка не может быть пустой");
                    }
                    else if(!isValidNumber(number)){
                        numberLayout.setError("Неверно введен номер");
                    }
                    else {

                        User us = new User(name, number, email);
                        usersRef.child(user.getUid()).setValue(us);

                        Toast.makeText(ProfileActivity.this, "Cохранено.",
                                Toast.LENGTH_SHORT).show();

                        Context context = ProfileActivity.this;
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }

                }
            });

        }
    }

    public boolean isValidEmail(String email){
        return email.length() > 3;
    }

    public boolean isValidName(String name){
        return name.length() != 0;
    }

    public boolean isValidNumber(String num){
        return android.util.Patterns.PHONE.matcher(num).matches();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            avatarView.setImageBitmap(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (permissions.length == 1 &&
                        permissions[0].equals(Manifest.permission.CAMERA) &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
            }
        }
    }
}
