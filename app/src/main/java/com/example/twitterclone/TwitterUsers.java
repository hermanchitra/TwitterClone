package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Toolbar myToolbar;
    private ListView listView;
    private ArrayList<String> tUsers;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);
        setTitle("AC-TwitterClone");

        listView = findViewById(R.id.listView);
        tUsers = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, tUsers);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        FancyToast.makeText(this, "Welcome " + ParseUser.getCurrentUser().getUsername(), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if (e == null && users.size() > 0) {
                        for (ParseUser user : users) {
                            tUsers.add(user.getUsername());
                        }
                        listView.setAdapter(adapter);
                        for (String tUser : tUsers) {
                            if (ParseUser.getCurrentUser().getList("fanOf") != null) {
                                if (ParseUser.getCurrentUser().getList("fanOf").contains(tUser)) {
                                    listView.setItemChecked(tUsers.indexOf(tUser), true);
                                }
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutUserItem:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(TwitterUsers.this, SignupActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            FancyToast.makeText(TwitterUsers.this, "Logging out failed\n" + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }
                });
                break;
            case R.id.sendTweetItem:
                Intent intent = new Intent(this, SendTweet.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if (checkedTextView.isChecked()){
            FancyToast.makeText(this, tUsers.get(i) + " is followed", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            ParseUser.getCurrentUser().addAllUnique("fanOf", Collections.singleton(tUsers.get(i)));
        } else {
            FancyToast.makeText(this, tUsers.get(i) + " is not followed", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            ParseUser.getCurrentUser().removeAll("fanOf", Collections.singleton(tUsers.get(i)));
//            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(i));
//            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
//            ParseUser.getCurrentUser().remove("fanOf");
//            ParseUser.getCurrentUser().put("fanOf", currentUserFanOfList);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    FancyToast.makeText(TwitterUsers.this, "Changes saved", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                }
            }
        });
    }
}