package com.wordpress.hossamhassan47.bingbongcup.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupRound;
import com.wordpress.hossamhassan47.bingbongcup.entities.MatchGame;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatch;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatchDetails;

import java.util.List;

public class MatchDetailsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    int roundMatchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);

        roundMatchId = getIntent().getIntExtra("roundMatchId", -1);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this, roundMatchId);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<MatchGame> _matchGameList;
        private final int _numberOfPages;
        private final int _roundMatchId;

        public SectionsPagerAdapter(FragmentManager fm, Context c, int roundMatchId) {
            super(fm);

            AppDatabase db = AppDatabase.getAppDatabase(c);
            _matchGameList = db.matchGameDao().loadAllMatchGames(roundMatchId);

            _numberOfPages = _matchGameList.size();
            _roundMatchId = roundMatchId;
        }

        @Override
        public Fragment getItem(int position) {
            int matchGameId = _matchGameList.get(position).matchGameId;

            return PlaceholderFragment.newInstance(matchGameId);
        }

        @Override
        public int getCount() {
            return _numberOfPages;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Game#" + _matchGameList.get(position).gameNo;
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private int gameId = 0;
        private int player1Id = 0;
        private int player2Id = 0;
        private int player1WinningGameCount = 5;
        private int player2WinningGameCount = 3;
        private int maximumScore = 21;
        private int player1Score = 0;
        private int player2Score = 0;

        TextView txtPlayer1Score;
        TextView txtPlayer2Score;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_match_details, container, false);

            // Winning Game Count
            LinearLayout linearLayoutPlayer1WinningGameCount = rootView.findViewById(R.id.linear_layout_player_1_winning_game_count);
            for (int i = 0; i < player1WinningGameCount; i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(R.drawable.ic_star_border_white_24dp);

                linearLayoutPlayer1WinningGameCount.addView(imageView);
            }

            LinearLayout linearLayoutPlayer2WinningGameCount = rootView.findViewById(R.id.linear_layout_player_2_winning_game_count);
            for (int i = 0; i < player2WinningGameCount; i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(R.drawable.ic_star_border_white_24dp);

                linearLayoutPlayer2WinningGameCount.addView(imageView);
            }

            // Score
            txtPlayer1Score = rootView.findViewById(R.id.text_view_player_1_score);
            txtPlayer1Score.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (player1Score < maximumScore) {
                        player1Score += 1;
                    }

                    txtPlayer1Score.setText(player1Score + "");
                }
            });

            txtPlayer2Score = rootView.findViewById(R.id.text_view_player_2_score);
            txtPlayer2Score.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (player2Score < maximumScore) {
                        player2Score += 1;
                    }

                    txtPlayer2Score.setText(player2Score + "");
                }
            });

            // Negative
            LinearLayout player1Neg = rootView.findViewById(R.id.linear_layout_player_1_neg);
            player1Neg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (player1Score > 0) {
                        player1Score -= 1;
                    }

                    txtPlayer1Score.setText(player1Score + "");
                }
            });

            LinearLayout player2Neg = rootView.findViewById(R.id.linear_layout_player_2_neg);
            player2Neg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (player2Score > 0) {
                        player2Score -= 1;
                    }

                    txtPlayer2Score.setText(player2Score + "");
                }
            });

            // Save
            LinearLayout save = rootView.findViewById(R.id.linear_layout_save_match_result);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Player1: " + player1Score + " Player2: " + player2Score, Toast.LENGTH_SHORT)
                            .show();
                }
            });

            return rootView;
        }
    }

}
