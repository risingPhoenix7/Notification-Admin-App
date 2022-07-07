package com.bits_pilani.notificationadmin;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextInputEditText title;
    TextInputEditText body;
    Button send;

    //replace with notification key
    String notificationKey = "key=AAAAJMWajdg:APA91bGvjZTEKxswGqb8a6fh0ZGl0U9Fg5Kbiz6tElB4QCzZJeaBFcMR7ug0K59PTM8WbruIBpdjTTihnFUQUFZbNWpawq4jOfITWlgxNUxFJPyrFzMtecvX4BQ9wdiwWZ9_iq9H54O4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.icon);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);


        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
        send = findViewById(R.id.send);
        progressBar= findViewById(R.id.progressBar);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String titleText = title.getText().toString();
                String bodyText = title.getText().toString();

                try {
                    progressBar.setVisibility(View.VISIBLE);

                    JSONObject body = new JSONObject();
                    body.put("to","/topics/all");

                    JSONObject message = new JSONObject();
                    message.put("body", bodyText);
                    message.put("title", titleText);

                    body.put("data",message);
                    notification(body);

                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void notification(JSONObject obj){

        AndroidNetworking.post("https://fcm.googleapis.com/fcm/send")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization", notificationKey)
                .addJSONObjectBody(obj).build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        title.setText("");
                        body.setText("");
                        Log.d("Notification Response",response.toString());
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            if (response.get("message_id")!=null){
                                Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("Notification Error",anError.getErrorBody());
                        Log.d("Notification Error",anError.getErrorDetail());
                        Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
