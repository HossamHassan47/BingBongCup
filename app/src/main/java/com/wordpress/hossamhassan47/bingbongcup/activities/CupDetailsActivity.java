package com.wordpress.hossamhassan47.bingbongcup.activities;

//import android.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.SparseArray;
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
import com.wordpress.hossamhassan47.bingbongcup.fragments.SetMatchDateFragment;

import java.util.List;

public class CupDetailsActivity extends AppCompatActivity implements NoticeDialogListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private int currentSelectedPage = 0;
    private ViewPager mViewPager;
    int cupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cup_details);

        cupId = getIntent().getIntExtra("fk_cupId", -1);

        SetViewPager();
    }

    private void SetViewPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this, cupId);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.pagerCupDetails);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentSelectedPage = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        SetViewPager();

        mViewPager.setCurrentItem(currentSelectedPage);
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


            AppDatabase db = AppDatabase.getAppDatabase(getActivity());
            List<RoundMatchDetails> lstRoundMatchDetails = db.roundMatchDao().loadRoundMatchesById(cupRoundId);

            RoundMatchAdapter adapter = new RoundMatchAdapter(getContext(), lstRoundMatchDetails);

            // View players in list view
            ListView listViewRoundMatches = rootView.findViewById(R.id.list_view_round_matches);
            listViewRoundMatches.setAdapter(adapter);
            listViewRoundMatches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RoundMatchDetails roundMatchDetails = (RoundMatchDetails) parent.getItemAtPosition(position);

                    // Create and show the dialog.
                    Bundle bundle = new Bundle();
                    bundle.putInt("roundMatchId", roundMatchDetails.roundMatch.roundMatchId);
                    if (roundMatchDetails.roundMatch.matchDate != null) {
                        bundle.putString("matchDate_Day", (String) DateFormat.format("dd", roundMatchDetails.roundMatch.matchDate));
                        bundle.putString("matchDate_Month", (String) DateFormat.format("MM", roundMatchDetails.roundMatch.matchDate));
                        bundle.putString("matchDate_Year", (String) DateFormat.format("yyyy", roundMatchDetails.roundMatch.matchDate));
                        bundle.putString("matchDate_Hours", (String) DateFormat.format("HH", roundMatchDetails.roundMatch.matchDate));
                        bundle.putString("matchDate_Minutes", (String) DateFormat.format("mm", roundMatchDetails.roundMatch.matchDate));
                    } else {
                        bundle.putString("matchDate_Day", "-1");
                    }

                    SetMatchDateFragment setMatchDateFragment = new SetMatchDateFragment();
                    setMatchDateFragment.setArguments(bundle);

                    setMatchDateFragment.show(getFragmentManager(), "dialog_SetRoundMatchDate");
                }
            });

            listViewRoundMatches.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    RoundMatchDetails roundMatchDetails = (RoundMatchDetails) parent.getItemAtPosition(position);
                    Intent i = new Intent(getContext(), MatchDetailsActivity.class);

                    i.putExtra("roundMatchId", roundMatchDetails.roundMatch.roundMatchId);

                    startActivity(i);
                    return true;
                }
            });

            return rootView;
        }
    }

}
