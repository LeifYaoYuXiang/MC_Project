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

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LogIntoActivity extends AppCompatActivity {
    private EditText userNameTextView;
    private EditText userPasswordTextView;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"a68d15cc107400e52c8d4c0da58acc39");
        setContentView(R.layout.activity_log_into);

        HTextView hTextView=findViewById(R.id.animate_login_text_view);
        hTextView.setAnimateType(HTextViewType.FALL);
        hTextView.animateText("Log into your account");
        initUI();
    }

    private void initUI(){
        userNameTextView=findViewById(R.id.user_name_login);
        userPasswordTextView=findViewById(R.id.user_password_login);
        login=findViewById(R.id.log_into_my_account);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameInput=userNameTextView.getText().toString();
                String passwordInput=userPasswordTextView.getText().toString();
                searchInformation(nameInput,passwordInput);
            }
        });
    }

    private void searchInformation(String username, final String password){
        BmobQuery<User> bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("userName",username);
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    if(list.size()==1){
                        User temp=list.get(0);
                        String correctPassword=temp.getUserPassword();
                        if(!password.equals(correctPassword)){
                            Toast.makeText(LogIntoActivity.this, "Error Input: Password", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LogIntoActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            saveInformationLocally(temp);
                            Intent intent=new Intent(LogIntoActivity.this,PersonalInformationActivity.class);
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(LogIntoActivity.this, "Error Input: User name", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(LogIntoActivity.this, "Error Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void saveInformationLocally(User user){
        String objectID=user.getObjectId();
        String userName=user.getUserName();
        String userPassword=user.getUserPassword();
        String userDescription=user.getUserDescription();
        String userEmail=user.getUserEmail();
        String userGender=user.getUserGender();

        SharedPreferences sharedPreferences=getSharedPreferences("PersonalInformation",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString("objectID",objectID);
        editor.putString("userName",userName);
        editor.putString("userPassword",userPassword);
        editor.putString("userEmail",userEmail);
        editor.putString("userGender",userGender);
        editor.putString("userDescription",userDescription);

        editor.commit();

    }
}