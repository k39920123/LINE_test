package com.example.line_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.response.GetMessageEventResponse;
import com.linecorp.linesdk.LineAccessToken;
import com.linecorp.linesdk.LineIdToken;
import com.linecorp.linesdk.LineProfile;
import com.linecorp.linesdk.LoginDelegate;
import com.linecorp.linesdk.LoginListener;
import com.linecorp.linesdk.Scope;
import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;
import com.linecorp.linesdk.auth.LineAuthenticationParams;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;
import com.linecorp.linesdk.widget.LoginButton;

import static com.linecorp.linesdk.auth.LineLoginApi.getLoginIntentWithoutLineAppAuth;

public class MainActivity extends AppCompatActivity {
    private LoginDelegate loginDelegate = LoginDelegate.Factory.create();
    public LoginButton LINE_loginButton ;
    private static final int REQUEST_CODE = 1;
    private String channelid = "1655646776";
    public Button loginButton;
    private static LineApiClient lineApiClient;
    public TextView aaa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LINE_loginButton = findViewById(R.id.LINE_loginbtn);
        LINEAPP();
        loginButton = findViewById(R.id.loginbutton);
        LineApiClientBuilder apiClientBuilder = new LineApiClientBuilder(getApplicationContext(),channelid);
        lineApiClient = apiClientBuilder.build();

        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try{
                    Intent loginIntent =getLoginIntentWithoutLineAppAuth(
                            view.getContext(),
                            channelid,
                            new LineAuthenticationParams.Builder()
                                    .scopes(Arrays.asList(Scope.PROFILE,Scope.OPENID_CONNECT,Scope.OC_EMAIL))
                                    .nonce("<a randomly-generated string>") // nonce can be used to improve security
                                    .botPrompt(LineAuthenticationParams.BotPrompt.aggressive)
                                    .build());
                    startActivityForResult(loginIntent, REQUEST_CODE);

                }
                catch(Exception e) {
                    Log.e("ERROR", e.toString());
                }
            }
        });

        /*final LineMessagingClient client = LineMessagingClient
                .builder("sIAuBuNC5ZTHmE4No8Np/+PEqfPe2p2J+HgusnHYyViJ4klZYrwnpo5ScjukhRN6iIYjQ8KUKKlULaNSGqA4kHJ1XbHYWzPFnTLWn899+ojOd4S1Q6Z9YuLtCyExhYa/fh/idMatc8tZ0NfVgS3xWgdB04t89/1O/w1cDnyilFU=")
                .build();

        final GetMessageEventResponse messageContentResponse;
        try {
            messageContentResponse = client.getMessageEvent("1655748522").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }*/

    }

    public void LINEAPP(){
        LINE_loginButton.setChannelId(channelid);
        LINE_loginButton.setLoginDelegate(loginDelegate);
        LINE_loginButton.enableLineAppAuthentication(true);
        LINE_loginButton.setAuthenticationParams(new LineAuthenticationParams.Builder()
                .scopes(Arrays.asList(Scope.PROFILE))
                .nonce("<a randomly-generated string>") // nonce can be used to improve security
                .botPrompt(LineAuthenticationParams.BotPrompt.aggressive)
                .build()
        );
        LINE_loginButton.addLoginListener(new LoginListener() {
            @Override
            public void onLoginSuccess(@NonNull LineLoginResult result) {
                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoginFailure(@Nullable LineLoginResult result) {
                Toast.makeText(getApplicationContext(), "Login failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE) {
            Log.e("ERROR", "Unsupported Request");
            return;
        }

        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);

        switch (result.getResponseCode()) {

            case SUCCESS:
                // Login successful
                String accessToken = result.getLineCredential().getAccessToken().getTokenString();
                LineAccessToken accessToken_LINESDK = lineApiClient.getCurrentAccessToken().getResponseData();
                LineIdToken lineidtoken = result.getLineIdToken();

                Intent transitionIntent = new Intent(this, test.class);
                transitionIntent.putExtra("line_profile", result.getLineProfile());
                transitionIntent.putExtra("line_credential", result.getLineCredential());
                transitionIntent.putExtra("display_name", result.getLineProfile().getDisplayName());
                transitionIntent.putExtra("status_message", result.getLineProfile().getStatusMessage());
                transitionIntent.putExtra("user_id", result.getLineProfile().getUserId());
                transitionIntent.putExtra("picture_url", result.getLineProfile().getPictureUrl().toString());

                AsyncTasktest task = new AsyncTasktest();

                //Log.v("INFO",lineidtoken.getEmail()); thread
                Log.e("SUCCESS", "LINE Login SUCCESS.");

                startActivity(transitionIntent); //問題



                //lineApiClient.logout();
                break;

            case CANCEL:
                // Login canceled by user
                Log.e("ERROR", "LINE Login Canceled by user.");
                break;

            default:
                // Login canceled due to other error
                Log.e("ERROR", "Login FAILED!");
                Log.e("ERROR", result.getErrorData().toString());
        }
    }
}

//token profile api