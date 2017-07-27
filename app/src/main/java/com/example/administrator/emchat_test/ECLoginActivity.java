package com.example.administrator.emchat_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * @author yang.jianan 2017/07/27 11:41. Email:yang.jianan0926@gmail.com
 * @version 1.0.0
 */

public class ECLoginActivity extends AppCompatActivity {
    private static final String TAG = ECLoginActivity.class.getName();
    private AutoCompleteTextView editName;
    private EditText editPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ec_activity_login);

        editName = findViewById(R.id.name);
        editPwd = findViewById(R.id.password);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        findViewById(R.id.sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singUp();
            }
        });
    }

    /**
     * 注册
     */
    private void singUp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(editName.getText().toString().trim(), editPwd.getText().toString().trim());
                    toastOnUiThread("注册成功");
                } catch (HyphenateException e) {
                    Log.e(TAG, e.toString());
                    toastOnUiThread(e.getDescription());
                }
            }
        }).start();
    }

    /**
     * 登陆
     */
    private void login() {
        EMClient.getInstance().login(editName.getText().toString().trim(), editPwd.getText().toString().trim(), new EMCallBack() {
            @Override
            public void onSuccess() {
                toastOnUiThread("登陆成功");
                startActivity(new Intent(ECLoginActivity.this,ChatActivity.class));
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "login onError: " + i + " , " + s);
                toastOnUiThread(s);
            }

            @Override
            public void onProgress(int i, String s) {
                Log.i(TAG, "login onProgress: " + i + " , " + s);
            }
        });
    }

    private void toastOnUiThread(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ECLoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
