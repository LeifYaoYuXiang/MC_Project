package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author Leif(Yuxiang Yao)
 * @References: http://doc.bmob.cn/data/android/develop_doc/
 */
public class EditAccountActivity extends AppCompatActivity {
    private String id;
    private String userName;
    private String userEmail;
    private String userGender;
    private String userDescription;

    private TextView idEdit;
    private TextView nameEdit;
    private TextView genderEdit;

    private EditText emailEdit;
    private EditText descriptionEdit;

    private Button editionCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"a68d15cc107400e52c8d4c0da58acc39");
        setContentView(R.layout.activity_edit_account);
        initUI();
        getStoredInformation();
    }

    private void initUI(){
        idEdit=findViewById(R.id.user_id_edit);
        nameEdit=findViewById(R.id.user_name_edit);
        genderEdit=findViewById(R.id.user_gender_edit);
        emailEdit=findViewById(R.id.user_email_edit);
        descriptionEdit=findViewById(R.id.user_description_edit);
        editionCommit=findViewById(R.id.edit_commit);
        editionCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail=emailEdit.getText().toString();
                userDescription=descriptionEdit.getText().toString();
                if(checkLegal()){
                    updateRemoteInformation();
                }else{
                    Toast.makeText(EditAccountActivity.this, "Error in User Email(It must have '@')", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getStoredInformation(){
        SharedPreferences sharedPreferences=getSharedPreferences("PersonalInformation",MODE_PRIVATE);
        id=sharedPreferences.getString("objectID","");
        userName=sharedPreferences.getString("userName","");
        userEmail=sharedPreferences.getString("userEmail","");
        userGender=sharedPreferences.getString("userGender","");
        userDescription=sharedPreferences.getString("userDescription","");

        idEdit.setText(id);
        nameEdit.setText(userName);
        emailEdit.setText(userEmail);
        genderEdit.setText(userGender);
        descriptionEdit.setText(userDescription);

    }

    private void updateLocalInformation(){
        SharedPreferences sharedPreferences=getSharedPreferences("PersonalInformation",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("userEmail",userEmail);
        editor.putString("userDescription",userDescription);
        editor.commit();
    }

    private void updateRemoteInformation(){
        User user=new User();
        user.setUserEmail(userEmail);
        user.setUserDescription(userDescription);
        user.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(EditAccountActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                    updateLocalInformation();
                    onBackPressed();
                }else{
                    Toast.makeText(EditAccountActivity.this, "Error Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean checkLegal(){
        if(userEmail.contains("@")){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(EditAccountActivity.this,PersonalInformationActivity.class);
        startActivity(intent);
    }
}
