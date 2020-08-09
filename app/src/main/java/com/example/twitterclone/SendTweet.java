package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendTweet extends AppCompatActivity implements View.OnClickListener{
    private EditText edtSendTweet;
    private Button btnSendTweet, btnReadTweet;
    List<Map<String, String>> data;
    private ListView listTweetView;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtSendTweet = findViewById(R.id.edtSendTweet);
        btnSendTweet = findViewById(R.id.btnSendTweet);
        btnSendTweet.setOnClickListener(this);
        btnReadTweet = findViewById(R.id.btnReadTweet);
        btnReadTweet.setOnClickListener(this);

        listTweetView = findViewById(R.id.listTweetView);
        data = new ArrayList<Map<String, String>>();
        simpleAdapter = new SimpleAdapter(this,
                data,
                android.R.layout.simple_list_item_2,
                new String[] {"First Line", "Second Line"},
                new int[] {android.R.id.text1, android.R.id.text2});
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSendTweet:
                if (edtSendTweet.getText().toString().equals("")) {
                    FancyToast.makeText(this, "Please write your tweet", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    return;
                }
                try {
                    ParseObject myTweet = new ParseObject("MyTweet");
                    myTweet.put("tweet", edtSendTweet.getText().toString());
                    myTweet.put("user", ParseUser.getCurrentUser().getUsername());
                    myTweet.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SendTweet.this, "Tweet uploaded", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                finish();
                            } else {
                                FancyToast.makeText(SendTweet.this, "Error\n" + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    FancyToast.makeText(this, "Error\n" + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
                break;
            case R.id.btnReadTweet:
                final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
                final SimpleAdapter adapter = new SimpleAdapter(SendTweet.this, tweetList, android.R.layout.simple_list_item_2,
                        new String[]{"tweetUserName", "tweetValue"}, new int[]{ android.R.id.text1, android.R.id.text2});
                try {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("MyTweet");
                    query.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));
                    query.orderByDescending("createdAt");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> tweets, ParseException e) {
                            if (e == null && tweets.size() > 0){
                                for (ParseObject tweet : tweets){
                                    HashMap<String,String> userTweet = new HashMap<>();
                                    userTweet.put("tweetUserName", tweet.get("user").toString());
                                    userTweet.put("tweetValue", tweet.get("tweet").toString());
                                    tweetList.add(userTweet);
                                }
                                listTweetView.setAdapter(adapter);
                            }
                        }
                    });
                } catch (Exception e){
                    FancyToast.makeText(SendTweet.this, "Error\n" + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
                break;
        }
    }
}