package edu.msu.comfortablynumb.project1;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

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

                        Cloud cloud = new Cloud();
                        InputStream stream = cloud.pollWaiting(username, id);
                        // Test for an error
                        boolean fail = stream == null;

                        if(!fail) {
                            try {
                                XmlPullParser xml = Xml.newPullParser();
                                xml.setInput(stream, "UTF-8");

                                xml.nextTag();      // Advance to first tag
                                xml.require(XmlPullParser.START_TAG, null, "brick");
                                String status = xml.getAttributeValue(null, "status");
                                secondPlayerId = xml.getAttributeValue(null, "id");
                                if(id != null)
                                	Log.i("ID", id);
                                secondPlayerUsername = xml.getAttributeValue(null, "secondplayer");

                                if(status.equalsIgnoreCase("yes")){
                                	secondPlayerConnected = true;
                                }



                            } catch(IOException ex) {
                                fail = true;
                            } catch(XmlPullParserException ex) {
                            	Log.i("ex:", ex.getMessage());
                                fail = true;
                            } finally {
                                try {
                                    stream.close();
                                } catch(IOException ex) {
                                	Log.i("Message",ex.getMessage());
                                }
                            }
                        }


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