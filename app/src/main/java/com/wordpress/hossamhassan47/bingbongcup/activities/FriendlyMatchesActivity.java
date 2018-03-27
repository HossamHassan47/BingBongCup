package com.wordpress.hossamhassan47.bingbongcup.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.fragments.AddCupFragment;

public class FriendlyMatchesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendly_matches);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and show the dialog.
                Bundle bundle = new Bundle();
                bundle.putInt("cupPlayerId", -1);
                bundle.putString("cupName", "");
                bundle.putString("playersCount", "2");
                bundle.putString("gamesCount", "1");
                bundle.putInt("cupMode", 0);

                AddCupFragment fragment = new AddCupFragment();
                fragment.setArguments(bundle);

                fragment.show(getSupportFragmentManager(), "dialog_AddFriendlyMatch");
            }
        });
    }

}
