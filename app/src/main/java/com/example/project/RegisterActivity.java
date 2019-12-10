package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.Console;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author Leif(Yuxiang Yao)
 * @Referenece
 */
public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private EditText nameInput;
    private EditText passwordInput;
    private EditText passwordEnsureInput;
    private EditText EmailInput;
    private RadioGroup genderSelection;
    private RadioButton maleSelection;
    private RadioButton femaleSelection;
    private EditText descriptionInput;

    private String objectID;
    private String userName="";
    private String userPassword="";
    private String userPasswordEnsure="";
    private String userEmail="";
    private String userGender="";
    private String userDescription="";
    int themeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"a68d15cc107400e52c8d4c0da58acc39");
        SharedPreferences sharedPreferences = getSharedPreferences("themePre", MODE_PRIVATE);
        themeID = sharedPreferences.getInt("themeID", -1);
        setTheme(themeID);

        setContentView(R.layout.activity_register);
        initUI();

    }


    private void initUI(){
        register=findViewById(R.id.register);
        nameInput=findViewById(R.id.user_name_register);
        passwordInput=findViewById(R.id.user_password_register);
        passwordEnsureInput=findViewById(R.id.user_password_ensure_register);
        EmailInput=findViewById(R.id.user_email_register);
        genderSelection=findViewById(R.id.user_gender_register);
        maleSelection=findViewById(R.id.male_selection_register);
        femaleSelection=findViewById(R.id.female_selection_register);
        descriptionInput=findViewById(R.id.user_description_register);

        ImageView nickname=findViewById(R.id.image_register_nickname);
        ImageView password=findViewById(R.id.image_register_password);
        ImageView ensure_password=findViewById(R.id.image_register_password_ensure);
        ImageView email=findViewById(R.id.image_register_email);
        ImageView gender=findViewById(R.id.image_register_gender);

        if(themeID==R.style.AppTheme){
            nickname.setImageResource(R.drawable.nickname_orange);
            password.setImageResource(R.drawable.password_orange);
            ensure_password.setImageResource(R.drawable.password_ensure_orange);
            email.setImageResource(R.drawable.email_orange);
            gender.setImageResource(R.drawable.gender_orange);
            register.setBackground(RegisterActivity.this.getResources().getDrawable(R.drawable.button));
        }else if(themeID==R.style.PinkTheme){
            nickname.setImageResource(R.drawable.nickname_pink);
            password.setImageResource(R.drawable.password_pink);
            ensure_password.setImageResource(R.drawable.password_ensure_pink);
            email.setImageResource(R.drawable.email_pink);
            gender.setImageResource(R.drawable.gender_pink);
          register.setBackground(RegisterActivity.this.getResources().getDrawable(R.drawable.button_pink));
        }else if(themeID==R.style.BlueTheme){
            register.setBackground(RegisterActivity.this.getResources().getDrawable(R.drawable.button_blue));
        }else if(themeID==R.style.GrayTheme){
            nickname.setImageResource(R.drawable.nickname_grey);
            password.setImageResource(R.drawable.password_grey);
            ensure_password.setImageResource(R.drawable.password_ensure_grey);
            email.setImageResource(R.drawable.email_grey);
            gender.setImageResource(R.drawable.gender_grey);
            register.setBackground(RegisterActivity.this.getResources().getDrawable(R.drawable.button_grey));
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPersonalInformationInput();
                int feedback=checkLegal();
                if(feedback==0){
                    checkUserNameRegistered();
                }else{
                    switch (feedback){
                        case 1:
                            Toast.makeText(RegisterActivity.this, "User Name cannot be Empty", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(RegisterActivity.this, "User Email cannot be empty", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(RegisterActivity.this, "User Password cannot be empty", Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            Toast.makeText(RegisterActivity.this, "User Gender cannot be empty", Toast.LENGTH_SHORT).show();
                            break;
                        case 5:
                            Toast.makeText(RegisterActivity.this, "Ensuring Password cannot be empty", Toast.LENGTH_SHORT).show();
                            break;
                        case 6:
                            Toast.makeText(RegisterActivity.this, "Password does not match ensuring password", Toast.LENGTH_SHORT).show();
                            break;
                        case 7:
                            Toast.makeText(RegisterActivity.this, "User Email must have '@'", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(RegisterActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });

    }

    private void getPersonalInformationInput(){
        userName=nameInput.getText().toString();
        userPassword=passwordInput.getText().toString();
        userPasswordEnsure=passwordEnsureInput.getText().toString();
        userEmail=EmailInput.getText().toString();
        genderSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.male_selection_register){
                    userGender="male";
                }else if(i==R.id.female_selection_register){
                    userGender="female";
                }
                else{
                    userGender="";
                }
            }
        });
        userDescription=descriptionInput.getText().toString();
    }

    private int checkLegal(){
        if("".equals(userName)){
            return 1;
        }
        else if("".equals(userEmail)){
            return 2;
        }
        else if("".equals(userPassword)){
            return 3;
        }
        else if("".equals(userGender)){
            return 4;
        }
        else if("".equals(userPasswordEnsure)){
            return 5;
        }
        else if(!userPassword.equals(userPasswordEnsure)){
            return 6;
        }
        else if(!userEmail.contains("@")){
            return 7;
        }
        return 0;
    }

    private void checkUserNameRegistered(){
        BmobQuery<User> bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("userName",userName);
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    if(list.size()==0){
                        savePersonalInformation();
                    }else{
                        Toast.makeText(RegisterActivity.this, "This username has been registered", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(RegisterActivity.this, "Error Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void savePersonalInformation(){
        User user=new User();
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setUserEmail(userEmail);
        user.setUserGender(userGender);
        user.setUserDescription(userDescription);
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(RegisterActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();
                    saveUserInformationLocally(s);
                    Intent intent=new Intent(RegisterActivity.this, PersonalInformationActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this, "Error Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void saveUserInformationLocally(String objectID){
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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(RegisterActivity.this,FunctionListActivity.class);
        startActivity(intent);
    }
}
