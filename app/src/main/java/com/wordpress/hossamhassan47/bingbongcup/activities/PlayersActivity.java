package com.wordpress.hossamhassan47.bingbongcup.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.adapters.PlayerAdapter;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;
import com.wordpress.hossamhassan47.bingbongcup.fragments.AddPlayerFragment;
import com.wordpress.hossamhassan47.bingbongcup.fragments.NoticeDialogListener;

import java.util.List;

public class PlayersActivity extends AppCompatActivity implements NoticeDialogListener {
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = AppDatabase.getAppDatabase(this);

        // Load Players
        //this.loadPlayers();

        // Add
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create and show the dialog.
                Bundle bundle = new Bundle();
                bundle.putString("fullName", "");
                bundle.putString("email", "");
                bundle.putString("mobileNo", "");
                bundle.putBoolean("isTeam", false);
                bundle.putInt("cupPlayerId", -1);

                AddPlayerFragment playerFragment = new AddPlayerFragment();
                playerFragment.setArguments(bundle);

                playerFragment.show(getSupportFragmentManager(), "dialog_AddPlayer");
            }
        });
    }

    public void loadPlayers()
    {
        List<Player> lstPlayers = db.playerDao().loadAllPlayers();

        PlayerAdapter itemsAdapter = new PlayerAdapter(this, lstPlayers);

        ListView listView = findViewById(R.id.lstPlayers);
        listView.setAdapter(itemsAdapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadPlayers();
    }

    @Override
    public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog) {
        loadPlayers();
    }

    @Override
    public void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog) {

    }
}
