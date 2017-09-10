package com.example.asderfers.larva_detection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.asderfers.larva_detection.App.ParseResponse;
import com.example.asderfers.larva_detection.App.lardet;
import com.example.asderfers.larva_detection.Network.LoginHelper;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
private static final String TAG=MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
   performAutoLogin();
    }
    private void performAutoLogin()
    {
         String userName = lardet.App.getLoginUsername();
        String password = lardet.App.gettLoginPassword();
        if(!userName.isEmpty() && !password.isEmpty()) {
            performLogin(userName,password);
        }else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
    private void performLogin(String userName, String password) {
        LoginHelper.LoginResponseHandler loginResponseHandler = new LoginHelper.LoginResponseHandler() {
            @Override
            public void onSuccess(JSONObject userData) {
                MainActivity.this.onLoginSuccess(userData);
            }

            @Override
            public void onFailure() {
                MainActivity.this.onLoginFailure();
            }

            @Override
            public void onError(Exception exception) {
                MainActivity.this.onLoginError(exception);
            }
        };
        LoginHelper loginHelper = new LoginHelper(userName, password, loginResponseHandler);
        loginHelper.sendLoginRequest();
    }
    private void saveUserData(JSONObject userData) {
        try {
            lardet.setUser(ParseResponse.parseUserData(userData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void onLoginSuccess(JSONObject userData) {
        try {
            Log.d(TAG, userData.toString(4));
            saveUserData(userData);
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show();
        //TODO
    }

    private void onLoginFailure() {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Login failed");
        lardet.App.clearLoginCredentials();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //TODO
    }

    private void onLoginError(Exception exception) {
        Toast.makeText(this, "Error:"+exception, Toast.LENGTH_LONG).show();
        Log.e(TAG, "Error during login: " + exception);
        exception.printStackTrace();
        lardet.App.clearLoginCredentials();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //TODO
    }

}
