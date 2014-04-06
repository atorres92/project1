package edu.msu.comfortablynumb.project1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Aaron on 3/31/14.
 */
public class WaitingActivity extends Activity {

    private String id;
    private String username;
    private String secondPlayerId;
    private String secondPlayerUsername;

    private volatile boolean stopThread = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        Intent intent = getIntent();
        id = intent.getStringExtra(MainActivity.USER_ID);
        username = intent.getStringExtra(MainActivity.USERNAME);

        checkForSecondPlayer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Called when options button selected
     * @param item a menu item to use
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.game_help:
                // The puzzle is done
                // Instantiate a dialog box builder
                AlertDialog.Builder builder = new AlertDialog.Builder(WaitingActivity.this);

                // Parameterize the builder
                builder.setTitle(R.string.howtoplay);
                builder.setMessage(R.string.howtotext);
                builder.setPositiveButton(android.R.string.ok, null);

                // Create the dialog box and show it
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;

            case R.id.exit_game:
                //Exit the game
                stopThread = true;
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkForSecondPlayer() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(!stopThread) {
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
                                secondPlayerId = xml.getAttributeValue(null, "secondplayerid");
                                if(id != null) {
                                	Log.i("ID", "" + secondPlayerId);
                                }
                                secondPlayerUsername = xml.getAttributeValue(null, "secondplayername");

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

                            stopThread = true;
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