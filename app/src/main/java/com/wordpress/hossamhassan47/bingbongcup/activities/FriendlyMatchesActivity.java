package com.wordpress.hossamhassan47.bingbongcup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.adapters.CupAdapter;
import com.wordpress.hossamhassan47.bingbongcup.adapters.RoundMatchAdapter;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Cup;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatch;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatchDetails;
import com.wordpress.hossamhassan47.bingbongcup.fragments.AddCupFragment;
import com.wordpress.hossamhassan47.bingbongcup.fragments.AddFriendlyMatchFragment;
import com.wordpress.hossamhassan47.bingbongcup.fragments.NoticeDialogListener;

import java.util.List;

public class FriendlyMatchesActivity extends AppCompatActivity implements NoticeDialogListener {

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendly_matches);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = AppDatabase.getAppDatabase(this);

        loadFriendlyMatches();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddFriendlyMatchFragment fragment = new AddFriendlyMatchFragment();
                fragment.show(getSupportFragmentManager(), "dialog_AddFriendlyMatch");
            }
        });
    }

    public void loadFriendlyMatches() {

        List<RoundMatchDetails> lstFriendlyMatches = db.roundMatchDao().loadFriendlyMatches();

        RoundMatchAdapter adapter = new RoundMatchAdapter(this, lstFriendlyMatches);

        ListView listView = findViewById(R.id.list_view_friendly_matches);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RoundMatchDetails roundMatchDetails = (RoundMatchDetails) parent.getItemAtPosition(position);

                Intent i = new Intent(getApplicationContext(), MatchDetailsActivity.class);

                i.putExtra("roundMatchId", roundMatchDetails.roundMatch.roundMatchId);
                i.putExtra("winnerId", roundMatchDetails.roundMatch.winnerId);

                startActivity(i);
            }
        });
    }

    @Override
    public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog) {
        loadFriendlyMatches();
    }

    @Override
    public void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog) {

    }

}
