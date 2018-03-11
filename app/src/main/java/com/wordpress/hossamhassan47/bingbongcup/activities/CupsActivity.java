package com.wordpress.hossamhassan47.bingbongcup.activities;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.adapters.CupAdapter;
import com.wordpress.hossamhassan47.bingbongcup.adapters.PlayerAdapter;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Cup;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;
import com.wordpress.hossamhassan47.bingbongcup.fragments.AddCupFragment;
import com.wordpress.hossamhassan47.bingbongcup.fragments.AddPlayerFragment;
import com.wordpress.hossamhassan47.bingbongcup.fragments.NoticeDialogListener;

import java.util.List;

public class CupsActivity extends AppCompatActivity implements NoticeDialogListener {

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cups);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = AppDatabase.getAppDatabase(this);

        this.loadCups();

        // Add
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog_AddCup");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                Bundle bundle = new Bundle();
                bundle.putInt("id", -1);
                bundle.putString("cupName", "");
                bundle.putString("playersCount", "2");
                bundle.putString("gamesCount", "1");
                bundle.putInt("cupMode", 0);

                AddCupFragment fragment = new AddCupFragment();
                fragment.setArguments(bundle);

                fragment.show(ft, "dialog_AddCup");
            }
        });
    }

    public void loadCups() {
        List<Cup> cupList = db.cupDao().loadAllCups();

        CupAdapter itemsAdapter = new CupAdapter(this, cupList);

        ListView listView = findViewById(R.id.list_view_cups);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cup cup = (Cup) parent.getItemAtPosition(position);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog_AddCup");

                if (prev != null) {
                    ft.remove(prev);
                }

                ft.addToBackStack(null);

                // Create and show the dialog.
                Bundle bundle = new Bundle();
                bundle.putInt("id", cup.id);
                bundle.putString("cupName", cup.name);
                bundle.putString("playersCount", String.valueOf(cup.playersCount));
                bundle.putString("gamesCount", String.valueOf(cup.gamesCount));
                bundle.putInt("cupMode", cup.mode - 1);

                AddCupFragment fragment = new AddCupFragment();
                fragment.setArguments(bundle);

                fragment.show(ft, "dialog_AddCup");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cup cup = (Cup) parent.getItemAtPosition(position);
                Intent i = new Intent(CupsActivity.this, CupDetailsActivity.class);
                i.putExtra("numberOfPages", getNumberOfPages(cup.playersCount));
                i.putExtra("cupId", cup.id);

                startActivity(i);
                return true;
            }
        });
    }

    private int getNumberOfPages(int numberOfPlayers) {
        if (numberOfPlayers == 64)
            return 8;
        else if (numberOfPlayers == 32)
            return 7;
        else if (numberOfPlayers == 16)
            return 6;
        else if (numberOfPlayers == 8)
            return 5;
        else if (numberOfPlayers == 4)
            return 4;
        else
            return 2;
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        loadCups();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

}
