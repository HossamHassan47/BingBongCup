package com.wordpress.hossamhassan47.bingbongcup.activities;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wordpress.hossamhassan47.bingbongcup.fragments.AddPlayerFragment;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;
import com.wordpress.hossamhassan47.bingbongcup.adapters.PlayerAdapter;
import com.wordpress.hossamhassan47.bingbongcup.R;

import java.util.List;

public class PlayersActivity extends AppCompatActivity implements AddPlayerFragment.NoticeDialogListener {
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = AppDatabase.getAppDatabase(this);

        // Load Players
        this.loadPlayers();

        // Add
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog_AddPlayer");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                Bundle bundle = new Bundle();
                bundle.putString("fullName", "");
                bundle.putString("email", "");
                bundle.putInt("id", -1);

                AddPlayerFragment playerFragment = new AddPlayerFragment();
                playerFragment.setArguments(bundle);

                playerFragment.show(ft, "dialog_AddPlayer");
            }
        });
    }

    public void loadPlayers()
    {
        List<Player> palyerList = db.playerDao().loadAllPlayers();

        PlayerAdapter itemsAdapter = new PlayerAdapter(this, palyerList);

        ListView listView = findViewById(R.id.lstPlayers);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Player playerItem = (Player) parent.getItemAtPosition(position);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog_AddPlayer");
                if (prev != null) {
                    ft.remove(prev);
                }

                ft.addToBackStack(null);

                // Create and show the dialog.
                Bundle bundle = new Bundle();
                bundle.putString("fullName", playerItem.fullName);
                bundle.putString("email", playerItem.email);
                bundle.putInt("id", playerItem.id);

                AddPlayerFragment playerFragment = new AddPlayerFragment();
                playerFragment.setArguments(bundle);

                playerFragment.show(ft, "dialog_AddPlayer");
            }
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        loadPlayers();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
