package com.example.asderfers.larva_detection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asderfers.larva_detection.App.ParseResponse;
import com.example.asderfers.larva_detection.App.lardet;
import com.example.asderfers.larva_detection.Network.LoginHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    EditText _emailText;
    EditText _passwordText;
    private static final String TAG = LoginActivity.class.getName();

    public String getFilledUsername() {
        return ((EditText) findViewById(R.id.username_entry)).getText().toString();
    }
    public String getFilledPassword() {
        return ((EditText) findViewById(R.id.password_entry)).getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dialog = new ProgressDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         _emailText=((EditText) findViewById(R.id.username_entry));
         _passwordText=(EditText) findViewById(R.id.password_entry);
        dialog.setCancelable(false);
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
            dialog.dismiss();
            Log.d(TAG, userData.toString(4));
            saveUserData(userData);
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show();
        //TODO
    }

    private void onLoginFailure() {
        dialog.dismiss();
        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Login failed");
        //TODO
    }

    private void onLoginError(Exception exception) {
        dialog.dismiss();
        Toast.makeText(this, "Error:"+exception, Toast.LENGTH_LONG).show();
        Log.e(TAG, "Error during login: " + exception);
        exception.printStackTrace();
        //TODO
    }

    private void performLogin(String userName, String password) throws JSONException {
        {
            LoginHelper.LoginResponseHandler loginResponseHandler = new LoginHelper.LoginResponseHandler() {
                @Override
                public void onSuccess(JSONObject userData) {
                    LoginActivity.this.onLoginSuccess(userData);
                }

                @Override
                public void onFailure() {
                    LoginActivity.this.onLoginFailure();
                }

                @Override
                public void onError(Exception exception) {
                    LoginActivity.this.onLoginError(exception);
                }
            };
            LoginHelper loginHelper = new LoginHelper(userName, password, loginResponseHandler);
            loginHelper.sendLoginRequest();
            //TODO cancel login requests on back button pressed.
        }
    }

    public void doLogin(View view) throws JSONException {
        if (validate())   {
            dialog.setMessage("Signing in...");
            dialog.show();
            lardet.App.saveLoginCredentials(getFilledUsername(), getFilledPassword());
            performLogin(getFilledUsername(), getFilledPassword());
        }
    }
    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() ) {
            _emailText.setError("userId can't be empty!");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
