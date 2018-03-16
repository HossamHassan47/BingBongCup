package com.wordpress.hossamhassan47.bingbongcup.activities;

//import android.app.DialogFragment;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.adapters.CupPlayerAdapter;
import com.wordpress.hossamhassan47.bingbongcup.adapters.RoundMatchAdapter;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupPlayerDetails;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupRound;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatchDetails;
import com.wordpress.hossamhassan47.bingbongcup.fragments.NoticeDialogListener;
import com.wordpress.hossamhassan47.bingbongcup.fragments.SetCupPlayerFragment;

import java.util.List;

public class CupDetailsActivity extends AppCompatActivity implements NoticeDialogListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    int cupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cup_details);

        //numberOfPages = getIntent().getIntExtra("numberOfPages", 2);
        cupId = getIntent().getIntExtra("fk_cupId", -1);

        SetViewPager();
    }

    private void SetViewPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this, cupId);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.pagerCupDetails);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        SetViewPager();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    // Pages Adapter
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<CupRound> _cupRoundList;
        private final int _numberOfPages;
        private final int _cupId;

        public SectionsPagerAdapter(FragmentManager fm, Context c, int cupId) {

            super(fm);
            AppDatabase db = AppDatabase.getAppDatabase(c);
            _cupRoundList = db.cupRoundDao().loadAllCupRounds(cupId);

            _numberOfPages = _cupRoundList.size() + 1;
            _cupId = cupId;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                // Players
                return CupPlayersFragment.newInstance(_cupId);
            } else {
                // Rounds
                int roundId = _cupRoundList.get(position - 1).cupRoundId;

                return CupRoundFragment.newInstance(roundId);
            }
        }

        @Override
        public int getCount() {
            return _numberOfPages;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Players";
            } else {
                return _cupRoundList.get(position - 1).roundName;
            }
        }
    }

    // Cup Players Fragment
    public static class CupPlayersFragment extends Fragment {

        private static final String ARG_CUP_ID = "ARG_CUP_ID";

        ListView listViewPlayers;

        public CupPlayersFragment() {
        }

        public static CupPlayersFragment newInstance(int cupId) {
            CupPlayersFragment fragment = new CupPlayersFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_CUP_ID, cupId);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_cup_details_players, container, false);

            int cupId = getArguments().getInt(ARG_CUP_ID);

            // Load Players
            AppDatabase db = AppDatabase.getAppDatabase(getActivity());
            List<CupPlayerDetails> lstPlayers = db.cupPlayerDao().getPlayersByCupId(cupId);

            CupPlayerAdapter adapter = new CupPlayerAdapter(getContext(), lstPlayers);

            // View players in list view
            listViewPlayers = rootView.findViewById(R.id.list_view_cup_players);
            listViewPlayers.setAdapter(adapter);
            listViewPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CupPlayerDetails playerItem = (CupPlayerDetails) parent.getItemAtPosition(position);

                    // Create and show the dialog.
                    Bundle bundle = new Bundle();
                    bundle.putInt("cupPlayerId", playerItem.cupPlayer.cupPlayerId);
                    bundle.putInt("fk_cupId", playerItem.cupPlayer.fk_cupId);

                    if (playerItem.player != null) {
                        bundle.putInt("fk_playerId", playerItem.player.playerId);
                    } else {
                        bundle.putInt("fk_playerId", -1);
                    }


                    SetCupPlayerFragment setCupPlayerFragment = new SetCupPlayerFragment();
                    setCupPlayerFragment.setArguments(bundle);

                    setCupPlayerFragment.show(getFragmentManager(), "dialog_SetCupPlayer");
                }
            });

            return rootView;
        }

    }

    // Cup Round Fragment
    public static class CupRoundFragment extends Fragment {
        private static final String ARG_CUP_ROUND_ID = "ARG_CUP_ROUND_ID";

        ListView listViewRoundMatches;

        public CupRoundFragment() {
        }

        public static CupRoundFragment newInstance(int roundId) {
            CupRoundFragment fragment = new CupRoundFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_CUP_ROUND_ID, roundId);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_cup_details_round_matches, container, false);

            int cupRoundId = getArguments().getInt(ARG_CUP_ROUND_ID);

            // Load Round Matches
            AppDatabase db = AppDatabase.getAppDatabase(getActivity());
            List<RoundMatchDetails> lstRoundMatchDetails = db.roundMatchDao().loadRoundMatchesById(cupRoundId);

            RoundMatchAdapter adapter = new RoundMatchAdapter(getContext(), lstRoundMatchDetails);

            // View players in list view
            listViewRoundMatches = rootView.findViewById(R.id.list_view_round_matches);
            listViewRoundMatches.setAdapter(adapter);
            listViewRoundMatches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RoundMatchDetails roundMatchDetails = (RoundMatchDetails) parent.getItemAtPosition(position);

                    // Create and show the dialog.
                    Bundle bundle = new Bundle();
                    bundle.putInt("roundMatchId", roundMatchDetails.roundMatch.roundMatchId);

                    //SetRoundMatchFragment setRoundMatchFragment = new SetRoundMatchFragment();
                    //setRoundMatchFragment.setArguments(bundle);

                    //setRoundMatchFragment.show(getFragmentManager(), "dialog_SetRoundMatch");
                }
            });

            return rootView;
        }
    }
}
