package edu.msu.comfortablynumb.project1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Aaron on 3/31/14.
 */
public class WaitingActivity extends Activity {

    private String id;
    private String username;
    private String secondPlayerId;
    private String secondPlayerUsername;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        Intent intent = getIntent();
        id = intent.getStringExtra(MainActivity.USER_ID);
        username = intent.getStringExtra(MainActivity.USERNAME);

        checkForSecondPlayer();
    }


    public void checkForSecondPlayer() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {

                        boolean secondPlayerConnected = false;


                        //SERVER CODE HERE:
                        //
                        // Run through the username table and set all waitings to false if the respective user hasn't polled in the past 60 seconds
                        //
                        //
                        //
                        // Check server if second player has polled the server within the last 60 seconds here.
                        // If second player has polled the server in the last 60 seconds => secondPlayerConnected = true, grab second player username to send to GameActivity
                        // else => secondPlayerConnected = false.
                        //

                        Log.i("Polling Server...", "Polling Server...");


                        if (secondPlayerConnected) {
                            Log.i("Connected", "Second Player Connected! Going to GameActivity...");

                            //Go to GameActivity
                            Intent intent = new Intent(WaitingActivity.this, GameActivity.class);
                            intent.putExtra(MainActivity.USERNAME, username);
                            intent.putExtra(MainActivity.USER_ID, id);
                            intent.putExtra(MainActivity.USERNAME_2, secondPlayerUsername);
                            intent.putExtra(MainActivity.USER_ID_2, secondPlayerId);
                            startActivity(intent);
                            finish();

                        } else {
                            Thread.sleep(3000);
                        }
                    }
                } catch( Exception e ) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}