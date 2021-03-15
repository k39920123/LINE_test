package com.example.line_test;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.webkit.ConsoleMessage;


import com.linecorp.linesdk.LineAccessToken;
import com.linecorp.linesdk.LineProfile;
import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;

public class AsyncTasktest extends Thread{
    private static LineApiClient lineApiClient;
    public int check;
    public void setLineApiClient(LineApiClient a)
    {
        lineApiClient=a;
    }
    public void checknum(int a)
    {
        check=a;
    }
    public void run()
    {
        try{
            if(check == 1) {
                LineProfile profile = lineApiClient.getProfile().getResponseData();
                String[] strings = new String[4];
                strings[0] = profile.getDisplayName();
                strings[1] = profile.getUserId();
                strings[2] = profile.getStatusMessage();
                strings[3] = profile.getPictureUrl().toString();
                Bundle carrier = new Bundle();
                carrier.putString("Name", strings[0]);
                carrier.putString("UserID", strings[1]);
                carrier.putString("StatusMessage", strings[2]);
                carrier.putString("Pic", strings[3]);
                Message msg = new Message();
                msg.setData(carrier);
                test.taskHandler.sendMessage(msg);
            }
            else if(check == 2) {
                lineApiClient.logout();
            }


        }
        catch(Exception e){
            e.printStackTrace();
        }
    }




}
