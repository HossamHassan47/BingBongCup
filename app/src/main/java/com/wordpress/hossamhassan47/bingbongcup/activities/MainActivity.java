package com.wordpress.hossamhassan47.bingbongcup.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wordpress.hossamhassan47.bingbongcup.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // cups
        TextView family = (TextView) findViewById(R.id.cups);
        family.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CupsActivity.class);
                startActivity(i);
            }
        });

        // players
        TextView colors = (TextView) findViewById(R.id.players);
        colors.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PlayersActivity.class);
                startActivity(i);
            }
        });

//        // teams
//        TextView phrases = (TextView) findViewById(R.id.teams);
//        phrases.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(MainActivity.this, PlayersActivity.class);
//                startActivity(i);
//            }
//        });
    }
}
