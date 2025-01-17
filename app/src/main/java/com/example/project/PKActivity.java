package com.example.project;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ldoublem.loadingviewlib.view.LVBlazeWood;
import com.ldoublem.loadingviewlib.view.LVEatBeans;
import com.ldoublem.loadingviewlib.view.LVFunnyBar;
import com.ldoublem.loadingviewlib.view.LVGhost;
import com.vstechlab.easyfonts.EasyFonts;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leif Yao Yuxiang
 * @Reference https://www.jianshu.com/p/3696923aa4f7
 */
public class PKActivity extends AppCompatActivity {
    private BlueToothController blueToothController=new BlueToothController();;
    private DeviceAdapter deviceAdapter;
    private TextView status;
    private ListView listView;
    private Button  button;
    private List<BluetoothDevice> deviceList=new ArrayList<>();
    private List<BluetoothDevice> bondedDeviceList=new ArrayList<>();
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private Handler mUIHandler = new MyHandler();

    private LinearLayout questionLayout;
    private LinearLayout answerLayout;
    private LinearLayout replyLayout;
    private LinearLayout feedbackLayout;

    private boolean asker=false;
    private boolean findServer=false;
    private boolean findClient=false;

    private Button giveQuestion;
    private EditText questionToGiven;

    private TextView questionReceive;
    private EditText answerBack;
    private Button ensureSendBack;

    private TextView questionAsked;
    private TextView answerReceived;
    private RadioGroup correctOrNot;
    private RadioButton correct;
    private RadioButton incorrect;
    private Button sendFeedback;
    private int idCorrect=0;

    private TextView feedbackTextView;

    private boolean choosePartner=false;

    private LVBlazeWood lvBlazeWood;
    private LVGhost lvGhost;
    private LVEatBeans lvEatBeans;
    private LVFunnyBar lvFunnyBar;
    private int themeID;


    private BroadcastReceiver receiver=new BroadcastReceiver() {
        //Useless,since "find-connect" process happens outside the application to avoid complexity
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                //开始查找
                deviceList.clear();
                deviceAdapter.notifyDataSetChanged();
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                //结束查找
            }
            else if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                //查找设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceList.add(device);
                deviceAdapter.notifyDataSetChanged();
                Toast.makeText(context, "Find One", Toast.LENGTH_SHORT).show();
            } else if(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                //设备扫描模式改变
                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0);
                if(scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                    Toast.makeText(context, "Discoverable", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Not Discoverable", Toast.LENGTH_SHORT).show();
                }
            } else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                //绑定状态
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(remoteDevice == null) {
                    Toast.makeText(context, "No Equipment", Toast.LENGTH_SHORT).show();
                    return;
                }
                int status = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
                if(status == BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(context, "Bonded:"+remoteDevice.getName(), Toast.LENGTH_SHORT).show();
                } else if(status == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(context, "Bonding"+remoteDevice.getName(), Toast.LENGTH_SHORT).show();
                } else if(status == BluetoothDevice.BOND_NONE) {
                    Toast.makeText(context, "Not Bond with "+remoteDevice.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int choose=bundle.getInt("choose");
        if(choose==0){
            asker=true;
        }else{
            asker=false;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("themePre", MODE_PRIVATE);
        themeID = sharedPreferences.getInt("themeID", -1);
        setTheme(themeID);

        setContentView(R.layout.activity_pk);
        registerBluetoothReceiver();

        initUI();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blueToothController.enableVisibily(PKActivity.this);
                bondedDeviceList = blueToothController.getBondedDeviceList();
                deviceAdapter.refresh(bondedDeviceList);
                listView.setOnItemClickListener(bondedDeviceClick);
                if( mAcceptThread != null) {
                    mAcceptThread.cancel();
                }
                mAcceptThread = new AcceptThread(blueToothController.getAdapter(), mUIHandler);
                mAcceptThread.start();
                button.setVisibility(View.GONE);
            }
        });


    }

    private void initUI(){
        listView=findViewById(R.id.device_list);
        button=findViewById(R.id.startPK);
        status=findViewById(R.id.showStatus);
        if(asker){
            status.setTypeface(EasyFonts.funRaiser(PKActivity.this));
            status.setText("You are asking");
        }else{
            status.setTypeface(EasyFonts.funRaiser(PKActivity.this));
            status.setText("You are answering");
        }
        deviceAdapter=new DeviceAdapter(deviceList,this);
        listView.setAdapter(deviceAdapter);
        listView.setOnItemClickListener(bondedDeviceClick);


        questionLayout=findViewById(R.id.question_layout);
        answerLayout=findViewById(R.id.answer_layout);
        feedbackLayout=findViewById(R.id.feedback_layout);
        replyLayout=findViewById(R.id.reply_layout);

        giveQuestion=findViewById(R.id.give_question);
        questionToGiven=findViewById(R.id.question_to_give);
        giveQuestion.setOnClickListener(giveQuestionListener);

        questionReceive=findViewById(R.id.question_receive);
        answerBack=findViewById(R.id.answer_back);
        questionReceive.setTypeface(EasyFonts.ostrichBold(PKActivity.this));
        ensureSendBack=findViewById(R.id.ensure_send_back);
        ensureSendBack.setOnClickListener(giveAnswerBack);

        questionAsked=findViewById(R.id.question_asked);
        answerReceived=findViewById(R.id.answer_received);
        questionAsked.setTypeface(EasyFonts.walkwayBlack(PKActivity.this));
        answerReceived.setTypeface(EasyFonts.walkwayObliqueUltraBold(PKActivity.this));
        correctOrNot=findViewById(R.id.answer_correct_or_not);
        correct=findViewById(R.id.correct_answer);
        incorrect=findViewById(R.id.incorrect_answer);
        sendFeedback=findViewById(R.id.give_feedback);
        sendFeedback.setOnClickListener(giveFeedback);
        correctOrNot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                idCorrect=i;
            }
        });

        feedbackTextView=findViewById(R.id.feedback);
        feedbackTextView.setTypeface(EasyFonts.recognition(PKActivity.this));
        if(asker){
            listView.setVisibility(View.GONE);
        }

        lvBlazeWood=findViewById(R.id.animation_blaze);
        lvEatBeans=findViewById(R.id.animation_eatBeans);
        lvFunnyBar=findViewById(R.id.animation_funnyBar);
        lvGhost=findViewById(R.id.animation_ghost);

        if(themeID==R.style.AppTheme){
            button.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button));
            giveQuestion.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button));
            ensureSendBack.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button));
            sendFeedback.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button));
        }else if(themeID==R.style.PinkTheme){
            button.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button_pink));
            giveQuestion.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button_pink));
            ensureSendBack.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button_pink));
            sendFeedback.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button_pink));
        }else if(themeID==R.style.BlueTheme){
            button.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button_blue));
            giveQuestion.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button_blue));
            ensureSendBack.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button_blue));
            sendFeedback.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button_blue));
        }else if(themeID==R.style.GrayTheme){
            button.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button_grey));
            giveQuestion.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button_grey));
            ensureSendBack.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button_grey));
            sendFeedback.setBackground(PKActivity.this.getResources().getDrawable(R.drawable.button_grey));
        }


    }

    private void registerBluetoothReceiver(){
        //Implicit call: an implicit call is one that does not explicitly indicate component information.
        // Instead, filter out the required components
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(receiver,intentFilter);
    }

    private AdapterView.OnItemClickListener bondedDeviceClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            BluetoothDevice device = bondedDeviceList.get(i);
            if (mConnectThread != null) {
                mConnectThread.cancel();
            }
            mConnectThread = new ConnectThread(device, blueToothController.getAdapter(), mUIHandler);
            mConnectThread.start();
            listView.setVisibility(View.GONE);
        }
    };

    private View.OnClickListener giveQuestionListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String question=questionToGiven.getText().toString();
            if(question.contains("%")||question.contains("-")){
                Toast.makeText(PKActivity.this, "Here are some illegal chars(% OR -),please do not use them", Toast.LENGTH_SHORT).show();
            }else{
                say("QUESTION"+"%"+question);
            }
        }
    };

    private View.OnClickListener giveAnswerBack=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String question=questionReceive.getText().toString();
            String answer=answerBack.getText().toString();
            if(question.contains("%")||question.contains("-")||answer.contains("%")||answer.contains("-")){
                Toast.makeText(PKActivity.this, "Here are some illegal chars(% OR -),please do not use them", Toast.LENGTH_SHORT).show();
            }else{
                say("ANSWER"+"%"+question+"-"+answer);
            }
        }
    };

    private View.OnClickListener giveFeedback=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(idCorrect==R.id.correct_answer){
                say("FEEDBACK"+"%"+"CORRECT");
            }else if(idCorrect==R.id.incorrect_answer){
                say("FEEDBACK"+"%"+"INCORRECT");
            }
            answerLayout.setVisibility(View.GONE);
            feedbackLayout.setVisibility(View.GONE);
            replyLayout.setVisibility(View.GONE);

            questionLayout.setVisibility(View.VISIBLE);
            questionToGiven.setText("");
            lvBlazeWood.startAnim();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Toast.makeText(this, "OK! Open Blue Tooth", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No!", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyHandler extends Handler {
        private boolean jumpOrNot=false;
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case Constant.MSG_GOT_DATA:
                    listen(String.valueOf(message.obj));
                    break;
                case Constant.MSG_ERROR:
                    if(!jumpOrNot){
                        jumpOrNot=true;
                        Intent intent=new Intent(PKActivity.this,ErrorBluetoothInformationActivity.class);
                        startActivity(intent);
                    }else{

                    }
                    break;
                case Constant.MSG_CONNECTED_TO_SERVER:
                    Toast.makeText(PKActivity.this, "Connect to One Server", Toast.LENGTH_SHORT).show();
                    if(asker&&!choosePartner){
                        listView.setVisibility(View.VISIBLE);
                    }
                    choosePartner=true;
                    findServer=true;
                    updateDisplayToGame();
                    break;
                case Constant.MSG_GOT_A_CLINET:
                    Toast.makeText(PKActivity.this, "Connect to One Client", Toast.LENGTH_SHORT).show();
                    if(asker&&!choosePartner){
                        listView.setVisibility(View.VISIBLE);
                    }
                    choosePartner=true;
                    findClient=true;
                    updateDisplayToGame();
                    break;
                default:
                    break;
            }
        }
    }

    private void say(String word) {
        if (mAcceptThread != null) {
            try {
                mAcceptThread.sendData(word.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else if( mConnectThread != null) {
            try {
                mConnectThread.sendData(word.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void listen(String string){
        String[] feedback=string.split("%");
        if(feedback.length==2){
            String type=feedback[0];
            String result=feedback[1];
            if("QUESTION".equals(type)){
               questionReceive.setText(result);
               answerLayout.setVisibility(View.VISIBLE);
               answerBack.setText("");
               lvGhost.setViewColor(Color.parseColor("#FF5722"));
               lvGhost.setHandColor(Color.parseColor("#00796B"));
               lvGhost.startAnim();

               questionLayout.setVisibility(View.GONE);
               lvBlazeWood.stopAnim();

               replyLayout.setVisibility(View.GONE);
               lvEatBeans.stopAnim();

               feedbackLayout.setVisibility(View.GONE);
               lvFunnyBar.stopAnim();

            }else if ("ANSWER".equals(type)){
                String[] turnBack=result.split("-");
                if(turnBack.length==2){
                    String question=turnBack[0];
                    String answer=turnBack[1];
                    replyLayout.setVisibility(View.VISIBLE);
                    lvEatBeans.setEyeColor(Color.parseColor("#FF5722"));
                    lvEatBeans.setViewColor(Color.parseColor("#00796B"));
                    lvEatBeans.startAnim(5000);

                    answerLayout.setVisibility(View.GONE);
                    lvGhost.stopAnim();
                    questionLayout.setVisibility(View.GONE);
                    lvBlazeWood.stopAnim();
                    feedbackLayout.setVisibility(View.GONE);
                    lvFunnyBar.stopAnim();

                    questionAsked.setText("QUESTION:    "+question);
                    answerReceived.setText("ANSWER:    "+answer);
                }else{
                    Toast.makeText(this, "SOME THING GOES WRONG", Toast.LENGTH_SHORT).show();
                }
            }else if("FEEDBACK".equals(type)){
                feedbackLayout.setVisibility(View.VISIBLE);
                lvFunnyBar.startAnim();

                questionLayout.setVisibility(View.GONE);
                lvBlazeWood.stopAnim();
                answerLayout.setVisibility(View.GONE);
                lvGhost.stopAnim();
                replyLayout.setVisibility(View.GONE);
                lvEatBeans.stopAnim();

                if("CORRECT".equals(result)){
                    feedbackTextView.setText("YOU ARE CORRECT");
                }else{
                    feedbackTextView.setText("YOU ARE WRONG");
                }
            }
        }else{
            Toast.makeText(this, "Some Thing Wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDisplayToGame(){
        if(findClient&&findServer){
            if(asker){
                questionLayout.setVisibility(View.VISIBLE);
                lvBlazeWood.startAnim();
            }else{

            }
        }else{

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
        }
        if (mConnectThread != null) {
            mConnectThread.cancel();
        }
        unregisterReceiver(receiver);
    }

}