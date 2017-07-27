package com.example.administrator.emchat_test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

/**
 * @author yang.jianan 2017/07/27 14:38. Email:yang.jianan0926@gmail.com
 * @version 1.0.0
 */

public class ChatActivity extends AppCompatActivity implements EMMessageListener {
    private TextView mContent;
    private EditText mInput;
    //不同手机就换不同的用户名， 比如A手机用"hei"登陆，那么这里就填写"heih(非hei用户就行)"，.
    //B手机用"heih"登陆，那么这里就填写"hei(非heih用户就行)"
    private String chatId = "heih";

    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        mContent = findViewById(R.id.ec_text_content);
        mInput = findViewById(R.id.ec_edit_message_input);

        findViewById(R.id.ec_btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = mInput.getText().toString();

                if (TextUtils.isEmpty(content)) return;

                EMMessage message = EMMessage.createTxtSendMessage(content, chatId);
                message.setChatType(EMMessage.ChatType.Chat);

                //发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
                mContent.setText(mContent.getText() + "\n" + "我：" + content);
                mInput.getText().clear();

                message.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.i("yjn_chat", "信息发送成功");
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.i("yjn_chat", "信息发送失败：" + i + " , " + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        Log.i("yjn_chat", "onProgress：" + i + " , " + s);
                    }
                });
            }
        });
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        for (final EMMessage message : list) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mContent.setText(mContent.getText() + "\n" + chatId + "：" + ((EMTextMessageBody) message.getBody()).getMessage());
                }
            });
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }
}
