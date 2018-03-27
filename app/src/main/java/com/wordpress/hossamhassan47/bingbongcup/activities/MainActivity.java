package com.wordpress.hossamhassan47.bingbongcup.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wordpress.hossamhassan47.bingbongcup.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // cups
        LinearLayout cups = findViewById(R.id.linear_layout_cups);
        cups.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CupsActivity.class);
                startActivity(i);
            }
        });

        // players
        LinearLayout players = findViewById(R.id.linear_layout_players);
        players.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PlayersActivity.class);
                startActivity(i);
            }
        });

        // Friendly Matches
        LinearLayout friendlyMatches = findViewById(R.id.linear_layout_friendly_matches);
        friendlyMatches.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, FriendlyMatchesActivity.class);
                startActivity(i);
            }
        });

    }
}
