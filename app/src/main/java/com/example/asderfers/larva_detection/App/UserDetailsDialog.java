package com.example.asderfers.larva_detection.App;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.asderfers.larva_detection.R;

import org.json.JSONObject;

/**
 * Created by akm on 09-Sep-17.
 */
public class UserDetailsDialog {
    private final Dialog dialog;
    private final TextView userId;
    private final TextView mname;
    private final TextView emailId;
    private final TextView phoneNo;
    private final Button mLogoutButton;
    private final LogoutHandler logoutHandler;

    public interface LogoutHandler {
        void onLogout(JSONObject logoutResponse);
        void onError(Exception exception);
    }

    //TODO handle the case when either field is empty
   private void setuserId()
   {
       String name = lardet.getUser().getuserId();
       if(name != null ) {
           userId.setText(name);
       }

   }
    private void setname() {
        String name = lardet.getUser().getname();
        if(name != null ) {
           mname.setText(name);
        }
    }
    private void setphoneNo() {
        String num = lardet.getUser().getphoneNo();
        if(num != null) {
            phoneNo.setText(num);
        }
    }
    private void setemailId() {
        String email = lardet.getUser().getemailId();
        if(email != null) {
            emailId.setText(email);
        }
    }

    private void setupLogoutButton() {
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  TODO
//                new LogoutHelper(new LogoutHelper.LogoutResponseHandler() {
//                    @Override
//                    public void onSuccess(JSONObject logoutResponse) {
//                        logoutHandler.onLogout(logoutResponse);
//                    }
//
//                    @Override
//                    public void onFailure() {
//                    }
//
//                    @Override
//                    public void onError(Exception exception) {
//                        exception.printStackTrace();
//                        logoutHandler.onError(exception);
//                    }
//                }).sendLogoutRequest();
            }
        });
    }


    public UserDetailsDialog(Dialog dialog, LogoutHandler logoutHandler) {
        this.dialog = dialog;
        this.logoutHandler = logoutHandler;
        userId = (TextView) dialog.findViewById(R.id.userId);
        mname = (TextView) dialog.findViewById(R.id.name);
        emailId = (TextView) dialog.findViewById(R.id.emailId);
        phoneNo = (TextView) dialog.findViewById(R.id.phoneNo);
        mLogoutButton = (Button) dialog.findViewById(R.id.logout_button);
    }

    public Dialog setUpDialog() {
        setuserId();
        setname();
        setemailId();
        setphoneNo();
        setupLogoutButton();
        return dialog;
    }

}
