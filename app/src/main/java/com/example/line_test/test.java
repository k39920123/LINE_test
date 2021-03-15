package com.example.line_test;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.linesdk.LineAccessToken;
import com.linecorp.linesdk.LineProfile;
import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;

import java.nio.file.Files;
import java.util.concurrent.ExecutionException;


public class test extends AppCompatActivity {

    private static LineApiClient lineApiClient;
    private String channelid = "1655646776";
    public TextView aaa;
    public Button logout;
    public AsyncTasktest task = new AsyncTasktest();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginsuccess);
        logout=findViewById(R.id.logoutbut);
        logout.setOnClickListener(thelogout);

        LineApiClientBuilder apiClientBuilder = new LineApiClientBuilder(
                getApplicationContext(),channelid);
        lineApiClient = apiClientBuilder.build();
        LineProfile profile = lineApiClient.getProfile().getResponseData();

        task.setLineApiClient(lineApiClient);
        task.check=1;
        task.start();
        Intent transitionIntent = this.getIntent();


    }
    public void aaaaaa()
    {
        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public View.OnClickListener thelogout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            task.check=2;
            task.start();
            aaaaaa();
        }
    };

    public static Handler taskHandler =new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle gotmsg = msg.getData();

        }

    };
}



