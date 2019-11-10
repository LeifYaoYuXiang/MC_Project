package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PersonalInformationActivity extends AppCompatActivity {
    private String id;
    private String userName;
    private String userEmail;
    private String userGender;
    private String userDescription;

    private TextView idTextView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView genderTextView;
    private TextView descriptionTextView;

    private Button editPersonalInformationButton;
    private Button logOutAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        SharedPreferences sharedPreferences=getSharedPreferences("PersonalInformation",MODE_PRIVATE);
        id=sharedPreferences.getString("objectID","");
        userName=sharedPreferences.getString("userName","");
        userEmail=sharedPreferences.getString("userEmail","");
        userGender=sharedPreferences.getString("userGender","");
        userDescription=sharedPreferences.getString("userDescription","");

        initUI();

    }


    private void initUI(){
        idTextView=findViewById(R.id.user_id_display);
        nameTextView=findViewById(R.id.user_name_display);
        emailTextView=findViewById(R.id.user_email_display);
        genderTextView=findViewById(R.id.user_gender_display);
        descriptionTextView=findViewById(R.id.user_description_display);

        idTextView.setText(id);
        nameTextView.setText(userName);
        emailTextView.setText(userEmail);
        genderTextView.setText(userGender);
        if(userDescription.equals("")){
            descriptionTextView.setText("You haven't describe yourself!");
        }else{
            descriptionTextView.setText(userDescription);
        }

        editPersonalInformationButton=findViewById(R.id.edit_personal_information);
        editPersonalInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PersonalInformationActivity.this,EditAccountActivity.class);
                startActivity(intent);
            }
        });
        logOutAccountButton=findViewById(R.id.log_out_account);
        logOutAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutAccount();
                Intent intent=new Intent(PersonalInformationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void logOutAccount(){
        SharedPreferences sharedPreferences=getSharedPreferences("PersonalInformation",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("objectID","");
        editor.putString("userName","");
        editor.putString("userPassword","");
        editor.putString("userEmail","");
        editor.putString("userGender","");
        editor.putString("userDescription","");
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(PersonalInformationActivity.this,FunctionListActivity.class);
        startActivity(intent);
        super.onStop();
    }
}
